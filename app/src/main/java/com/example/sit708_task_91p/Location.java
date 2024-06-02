package com.example.sit708_task_91p;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Location extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display for a modern UI experience
        enableEdgeToEdgeDisplay();

        // Set the content view to the layout for this activity
        initializeContentView();
    }

    // Enable edge-to-edge display for this activity
    private void enableEdgeToEdgeDisplay() {
        EdgeToEdge.enable(this);
    }

    // Set the content view to the layout resource defined in activity_location.xml
    private void initializeContentView() {
        setContentView(R.layout.activity_location);
    }
}
