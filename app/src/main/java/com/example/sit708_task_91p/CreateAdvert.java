package com.example.sit708_task_91p;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CreateAdvert extends Activity {
    // Request code for location permission to access GPS data
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1000;
    // Request code for handling responses from location autocomplete
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

    // UI elements for the advertisement creation form
    private RadioGroup adTypeRadioGroup;
    private RadioButton lostRadioButton, foundRadioButton;
    private EditText advertiserNameEditText, advertiserPhoneEditText, adDescriptionEditText, adDateEditText, adLocationEditText;
    private Button saveAdButton, getCurrentLocationButton;

    // Variables to handle location data for the advertisement
    private double Latitude;
    private double Longitude;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        // Initialize UI elements
        initUI();

        // Initialize the Places API
        initPlaces();

        // Initialize the FusedLocationProviderClient for location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Set up location autocomplete functionality
        setupLocationAutocomplete();

        // Set up the button to get the current location
        setupGetCurrentLocationButton();

        // Set up the save button to save the advertisement
        setupSaveButton();
    }

    // Initialize UI elements
    private void initUI() {
        adTypeRadioGroup = findViewById(R.id.adTypeRadioGroup);
        lostRadioButton = findViewById(R.id.lostRadioButton);
        foundRadioButton = findViewById(R.id.foundRadioButton);
        advertiserNameEditText = findViewById(R.id.advertiserNameEditText);
        advertiserPhoneEditText = findViewById(R.id.advertiserPhoneEditText);
        adDescriptionEditText = findViewById(R.id.adDescriptionEditText);
        adDateEditText = findViewById(R.id.adDateEditText);
        adLocationEditText = findViewById(R.id.adLocationEditText);
        saveAdButton = findViewById(R.id.saveAdButton);
        getCurrentLocationButton = findViewById(R.id.getCurrentLocationButton);
    }

    // Initialize the Places API
    private void initPlaces() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyBfIS87rJqPE2uWWYILLlxoCgu5sanMuPo");
        }
    }

    // Set up location autocomplete functionality
    private void setupLocationAutocomplete() {
        adLocationEditText.setFocusable(false);
        adLocationEditText.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(CreateAdvert.this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });
    }

    // Set up the button to get the current location
    private void setupGetCurrentLocationButton() {
        getCurrentLocationButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            } else {
                getLocation();
            }
        });
    }

    // Set up the save button to save the advertisement
    private void setupSaveButton() {
        saveAdButton.setOnClickListener(v -> {
            try {
                String postType = lostRadioButton.isChecked() ? "Lost" : "Found";
                String name = advertiserNameEditText.getText().toString();
                String phone = advertiserPhoneEditText.getText().toString();
                String description = adDescriptionEditText.getText().toString();
                String date = adDateEditText.getText().toString();
                String location = adLocationEditText.getText().toString();

                // Validate input fields
                if (name.isEmpty() || phone.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()) {
                    Toast.makeText(CreateAdvert.this, "Please fill out required fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new LostFoundItem object
                LostFoundItem item = new LostFoundItem(postType + " " + name, description, phone, date, location, Latitude, Longitude);

                // Save the item to SharedPreferences
                saveItem(item);

                // Clear input fields after saving
                clearFields();

                // Notify the user that the advertisement has been saved
                Toast.makeText(CreateAdvert.this, "Saved!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("CreateAdvertisement", "Error saving advertisement", e);
                Toast.makeText(CreateAdvert.this, "Error saving advertisement", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Save the LostFoundItem object to SharedPreferences
    private void saveItem(LostFoundItem item) {
        SharedPreferences sharedPreferences = getSharedPreferences("LostFoundPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Retrieve the existing items from SharedPreferences
        String itemsJson = sharedPreferences.getString("items", "[]");
        Type type = new TypeToken<ArrayList<LostFoundItem>>() {}.getType();
        ArrayList<LostFoundItem> itemList = new Gson().fromJson(itemsJson, type);

        // Add the new item to the list
        itemList.add(item);

        // Save the updated list back to SharedPreferences
        itemsJson = new Gson().toJson(itemList);
        editor.putString("items", itemsJson);
        editor.apply();
    }

    // Get the current location and update the UI
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show();
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                Log.d("Location", "Location: " + location.toString());
                Latitude = location.getLatitude();
                Longitude = location.getLongitude();
                Geocoder geocoder = new Geocoder(CreateAdvert.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        String addressFragments = address.getMaxAddressLineIndex() >= 0 ? address.getAddressLine(0) : "";
                        runOnUiThread(() -> adLocationEditText.setText(addressFragments));
                    } else {
                        Toast.makeText(CreateAdvert.this, "No address found", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException | IllegalArgumentException e) {
                    Toast.makeText(CreateAdvert.this, "Service unavailable or invalid data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CreateAdvert.this, "Location not detected", Toast.LENGTH_SHORT).show();
                Log.d("Location", "Location not detected");
            }
        });
    }

    // Handle the result from the location permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Clear input fields after saving an item
    private void clearFields() {
        advertiserNameEditText.setText("");
        advertiserPhoneEditText.setText("");
        adDescriptionEditText.setText("");
        adDateEditText.setText("");
        adLocationEditText.setText("");
        adTypeRadioGroup.clearCheck();
    }

    // Handle the result from the autocomplete activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                adLocationEditText.setText(place.getAddress());
                if (place.getLatLng() != null) {
                    Latitude = place.getLatLng().latitude;
                    Longitude = place.getLatLng().longitude;
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(this, "Error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Address selection canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
