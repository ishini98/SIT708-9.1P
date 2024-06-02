package com.example.sit708_task_91p;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    // Buttons to navigate to Create Advertisement, Show Items, and Map Activities
    private Button createAdButton;
    private Button showItemsButton;
    private Button showOnMapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the UI components
        initUI();

        // Set up event listeners for button clicks
        setupButtonListeners();
    }

    // Initialize the UI components
    private void initUI() {
        createAdButton = findViewById(R.id.createAdButton);
        showItemsButton = findViewById(R.id.showItemsButton);
        showOnMapButton = findViewById(R.id.showOnMapButton);
    }

    // Set up event listeners for button clicks
    private void setupButtonListeners() {
        createAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Create Advertisement Activity
                Intent intentCreateAdvert = new Intent(MainActivity.this, CreateAdvert.class);
                startActivity(intentCreateAdvert);
            }
        });

        showItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Lost and Found Items Activity
                Intent intentShowItems = new Intent(MainActivity.this, LostFound.class);
                startActivity(intentShowItems);
            }
        });

        showOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Map Activity displaying locations
                Intent intentShowOnMap = new Intent(MainActivity.this, MapView.class);
                startActivity(intentShowOnMap);
            }
        });
    }
}
