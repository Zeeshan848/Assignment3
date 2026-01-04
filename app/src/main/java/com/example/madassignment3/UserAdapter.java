// UserAdapter.java
package com.example.madassignment3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> users;
    private Context context;

    public UserAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);

        if (user.getName() != null) {
            holder.tvName.setText(user.getName());
        } else {
            holder.tvName.setText("No Name");
        }

        if (user.getEmail() != null) {
            holder.tvEmail.setText(user.getEmail());
        } else {
            holder.tvEmail.setText("No Email");
        }

        if (user.getPhone() != null) {
            holder.tvPhone.setText(user.getPhone());
        } else {
            holder.tvPhone.setText("No Phone");
        }

        // Item click
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "Selected: " + user.getName(), Toast.LENGTH_SHORT).show();
        });

        // Long click for context menu
        holder.itemView.setOnLongClickListener(v -> {
            showContextMenu(v, user);
            return true;
        });

        // Popup menu on button click
        holder.btnOptions.setOnClickListener(v -> {
            showPopupMenu(v, user);
        });
    }

    private void showContextMenu(View view, User user) {
        android.widget.PopupMenu popupMenu = new android.widget.PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.context_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_edit) {
                Toast.makeText(context, "Edit: " + user.getName(), Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.menu_delete) {
                Toast.makeText(context, "Delete: " + user.getName(), Toast.LENGTH_SHORT).show();
                // Implement delete functionality here
                return true;
            } else if (itemId == R.id.menu_share) {
                Toast.makeText(context, "Share: " + user.getName(), Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void showPopupMenu(View view, User user) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_view_details) {
                Toast.makeText(context, "View details: " + user.getName(), Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.menu_send_email) {
                Toast.makeText(context, "Send email to: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return users != null ? users.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvPhone;
        ImageButton btnOptions; // Changed from View to ImageButton

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            btnOptions = itemView.findViewById(R.id.btnOptions);
        }
    }
}