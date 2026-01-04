package com.example.madassignment3;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private AppDatabase database;
    private SharedPrefManager prefManager;
    private Button btnFetch, btnWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefManager = SharedPrefManager.getInstance(this);
        database = AppDatabase.getInstance(this);

        setupUI();
        loadUsersFromDatabase();
    }

    private void setupUI() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnFetch = findViewById(R.id.btnFetch);
        btnWebView = findViewById(R.id.btnWebView);

        btnFetch.setOnClickListener(v -> fetchUsersFromApi());
        btnWebView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
            intent.putExtra("url", "https://jsonplaceholder.typicode.com");
            startActivity(intent);
        });
    }

    private void loadUsersFromDatabase() {
        new Thread(() -> {
            try {
                List<User> users = database.userDao().getAllUsers();
                runOnUiThread(() -> {
                    if (users.isEmpty()) {
                        Toast.makeText(this, "No data found. Fetch from API.", Toast.LENGTH_SHORT).show();
                    } else {
                        adapter = new UserAdapter(users, this);
                        recyclerView.setAdapter(adapter);
                        Toast.makeText(this, "Loaded " + users.size() + " users from database", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error loading database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void fetchUsersFromApi() {
        btnFetch.setEnabled(false);
        btnFetch.setText("Fetching...");

        ApiService apiService = RetrofitClient.getApiService();
        Call<List<User>> call = apiService.getUsers(); // Fixed: Changed Users to User

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> users = response.body(); // Fixed: Changed Users to User

                    // Save to database in background thread
                    new Thread(() -> {
                        try {
                            // Clear existing data
                            database.userDao().deleteAll();
                            // Insert new data
                            database.userDao().insertAll(users);

                            // Update UI on main thread
                            runOnUiThread(() -> {
                                adapter = new UserAdapter(users, MainActivity.this);
                                recyclerView.setAdapter(adapter);
                                Toast.makeText(MainActivity.this, "Fetched " + users.size() + " users from API", Toast.LENGTH_SHORT).show();
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(() -> {
                                Toast.makeText(MainActivity.this, "Error saving to database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        }
                    }).start();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch data: " + response.code(), Toast.LENGTH_SHORT).show();
                }

                btnFetch.setEnabled(true);
                btnFetch.setText("Fetch Users");
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                btnFetch.setEnabled(true);
                btnFetch.setText("Fetch Users");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_theme_light) {
            ThemeManager.setTheme(this, ThemeManager.THEME_LIGHT);
            recreate();
            return true;
        } else if (id == R.id.menu_theme_dark) {
            ThemeManager.setTheme(this, ThemeManager.THEME_DARK);
            recreate();
            return true;
        } else if (id == R.id.menu_theme_custom) {
            ThemeManager.setTheme(this, ThemeManager.THEME_CUSTOM);
            recreate();
            return true;
        } else if (id == R.id.menu_refresh) {
            fetchUsersFromApi();
            return true;
        } else if (id == R.id.menu_logout) {
            prefManager.setLoggedIn(false);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}