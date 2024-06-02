package com.example.sit708_task_91p;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LostFound extends AppCompatActivity {

    // RecyclerView for displaying the list of lost and found items
    private RecyclerView lostFoundRecyclerView;
    // Adapter for managing the data and views in the RecyclerView
    private LostFoundAdapter lostFoundAdapter;
    // List to hold the data for lost and found items
    private List<LostFoundItem> lostFoundItems;

    // Launcher to handle results from the Item_Data activity for editing or removing items
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        boolean itemRemoved = data.getBooleanExtra("ITEM_REMOVED", false);
                        int position = data.getIntExtra("POSITION", -1);
                        if (itemRemoved && position != -1) {
                            lostFoundAdapter.removeItem(position);
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_found);

        // Initialize UI components
        initUI();

        // Load the lost and found items from SharedPreferences
        lostFoundItems = loadItems();

        // Set up the RecyclerView with its adapter
        setupRecyclerView();
    }

    // Initialize UI components
    private void initUI() {
        lostFoundRecyclerView = findViewById(R.id.lostFoundRecyclerView);
        lostFoundRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // Load lost and found items from SharedPreferences
    private List<LostFoundItem> loadItems() {
        SharedPreferences sharedPreferences = getSharedPreferences("LostFoundPrefs", MODE_PRIVATE);
        String itemsJson = sharedPreferences.getString("items", "[]");
        Type type = new TypeToken<ArrayList<LostFoundItem>>() {}.getType();
        return new Gson().fromJson(itemsJson, type);
    }

    // Set up the RecyclerView and its adapter
    private void setupRecyclerView() {
        lostFoundAdapter = new LostFoundAdapter(this, lostFoundItems, position -> {
            Intent intent = new Intent(LostFound.this, Item_Data.class);
            intent.putExtra("TITLE", lostFoundItems.get(position).getTitle());
            intent.putExtra("DESCRIPTION", lostFoundItems.get(position).getDescription());
            intent.putExtra("PHONE", lostFoundItems.get(position).getPhone());
            intent.putExtra("DATE", lostFoundItems.get(position).getDate());
            intent.putExtra("LOCATION", lostFoundItems.get(position).getLocation());
            intent.putExtra("POSITION", position);
            activityResultLauncher.launch(intent);
        });

        lostFoundRecyclerView.setAdapter(lostFoundAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload the items and update the adapter
        lostFoundItems.clear();
        lostFoundItems.addAll(loadItems());
        lostFoundAdapter.notifyDataSetChanged();
    }
}
