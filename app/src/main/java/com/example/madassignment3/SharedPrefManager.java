// SharedPrefManager.java
package com.example.madassignment3;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String PREF_NAME = "assignment_prefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USERNAME = "username";

    private static SharedPrefManager instance;
    private SharedPreferences pref;

    private SharedPrefManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public void setLoggedIn(boolean isLoggedIn) {
        pref.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setUsername(String username) {
        pref.edit().putString(KEY_USERNAME, username).apply();
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }

    public void clear() {
        pref.edit().clear().apply();
    }
}