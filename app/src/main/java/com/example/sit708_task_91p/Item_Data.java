package com.example.sit708_task_91p;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Item_Data extends Activity {

    // UI elements to display item details and provide item removal functionality
    private TextView detailNameTextView;
    private TextView detailPhoneTextView;
    private TextView detailDescriptionTextView;
    private TextView detailDateTextView;
    private TextView detailLocationTextView;
    private Button removeItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        // Initialize UI elements
        initUI();

        // Load item data from the intent and display it
        populateDataFromIntent();

        // Set up the remove button to delete the item and return the result
        setupRemoveButton();
    }

    // Initialize UI elements for displaying item details
    private void initUI() {
        detailNameTextView = findViewById(R.id.detailNameTextView);
        detailPhoneTextView = findViewById(R.id.detailPhoneTextView);
        detailDescriptionTextView = findViewById(R.id.detailDescriptionTextView);
        detailDateTextView = findViewById(R.id.detailDateTextView);
        detailLocationTextView = findViewById(R.id.detailLocationTextView);
        removeItemButton = findViewById(R.id.removeItemButton);
    }

    // Load item data from the intent and set it to the respective UI elements
    private void populateDataFromIntent() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("TITLE");
        String description = intent.getStringExtra("DESCRIPTION");
        String phone = intent.getStringExtra("PHONE");
        String date = intent.getStringExtra("DATE");
        String location = intent.getStringExtra("LOCATION");

        detailNameTextView.setText(title);
        detailPhoneTextView.setText("Contact: " + phone);
        detailDescriptionTextView.setText("Description: " + description);
        detailDateTextView.setText("Date: " + date);
        detailLocationTextView.setText("Location: " + location);
    }

    // Set up the remove button to handle item removal and return the result to the calling activity
    private void setupRemoveButton() {
        int position = getIntent().getIntExtra("POSITION", -1);
        removeItemButton.setOnClickListener(v -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("ITEM_REMOVED", true);
            returnIntent.putExtra("POSITION", position);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
    }
}
