package com.example.sit708_task_91p;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

public class LostFoundAdapter extends RecyclerView.Adapter<LostFoundAdapter.ViewHolder> {

    // List of lost and found items to be displayed
    private final List<LostFoundItem> lostFoundItems;
    // LayoutInflater to create item views
    private final LayoutInflater layoutInflater;
    // Listener for handling item click events
    private final OnItemClickListener itemClickListener;

    // Interface for defining item click behavior
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Constructor to initialize the adapter with context, items, and click listener
    public LostFoundAdapter(Context context, List<LostFoundItem> lostFoundItems, OnItemClickListener itemClickListener) {
        this.lostFoundItems = lostFoundItems;
        this.layoutInflater = LayoutInflater.from(context);
        this.itemClickListener = itemClickListener;
    }

    // Create new item views for the RecyclerView
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_lost_found, parent, false);
        return new ViewHolder(view);
    }

    // Bind item data to the view components
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LostFoundItem item = lostFoundItems.get(position);
        holder.titleTextView.setText(item.getTitle());
    }

    // Return the total number of items in the list
    @Override
    public int getItemCount() {
        return lostFoundItems.size();
    }

    // ViewHolder class to manage item views and handle item click events
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.itemTitleTextView);

            // Set the click listener for the item view
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(position);
                }
            });
        }
    }

    // Remove an item from the list and update the RecyclerView
    public void removeItem(int position) {
        lostFoundItems.remove(position);
        notifyItemRemoved(position);
        saveItemsToSharedPreferences(layoutInflater.getContext(), lostFoundItems);
    }

    // Save the current list of items to SharedPreferences
    private void saveItemsToSharedPreferences(Context context, List<LostFoundItem> lostFoundItemsList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LostFoundPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(lostFoundItemsList);
        editor.putString("lostFoundItems", json);
        editor.apply();
    }
}
