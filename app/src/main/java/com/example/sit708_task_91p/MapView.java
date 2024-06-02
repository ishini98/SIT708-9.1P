package com.example.sit708_task_91p;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MapView extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    // GoogleMap instance for interacting with the map
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);

        // Initialize the map fragment and set the callback for map readiness
        initMapFragment();
    }

    // Initialize the map fragment and set the callback for map readiness
    private void initMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Enable user interaction settings on the map
        enableMapSettings();

        // Load items from storage and add markers to the map
        loadItemsAndAddMarkers();
    }

    // Enable user interaction settings on the map
    private void enableMapSettings() {
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.setOnMarkerClickListener(this);
    }

    // Load items from storage and add markers to the map
    private void loadItemsAndAddMarkers() {
        SharedPreferences sharedPreferences = getSharedPreferences("LostFoundPrefs", MODE_PRIVATE);
        String itemsJson = sharedPreferences.getString("items", "[]");
        Type type = new TypeToken<ArrayList<LostFoundItem>>() {}.getType();
        ArrayList<LostFoundItem> itemList = new Gson().fromJson(itemsJson, type);

        if (!itemList.isEmpty()) {
            addMarkersForItems(itemList);
            moveCameraToFirstItem(itemList.get(0));
        }
    }

    // Move the camera to the location of the first item in the list
    private void moveCameraToFirstItem(LostFoundItem firstItem) {
        LatLng location = new LatLng(firstItem.getLatitude(), firstItem.getLongitude());
        float zoomLevel = 10.0f; // Set zoom level
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));
    }

    // Add markers to the map for each item in the list
    private void addMarkersForItems(ArrayList<LostFoundItem> itemList) {
        for (LostFoundItem item : itemList) {
            LatLng location = new LatLng(item.getLatitude(), item.getLongitude());
            Marker marker = googleMap.addMarker(new MarkerOptions().position(location).title(item.getTitle()));
            marker.setTag(item); // Associate item with the marker
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Display details of the item associated with the selected marker
        displayItemDetails(marker);
        return true;
    }

    // Display details of the item associated with the selected marker
    private void displayItemDetails(Marker marker) {
        LostFoundItem item = (LostFoundItem) marker.getTag();
        if (item != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(item.getTitle());
            builder.setMessage("Description: " + item.getDescription() +
                    "\nPhone: " + item.getPhone() +
                    "\nDate: " + item.getDate() +
                    "\nLocation: " + item.getLocation());
            builder.setPositiveButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Toast.makeText(this, "Details unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
