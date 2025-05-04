package com.prac.lostfound;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.location.Location;

import com.google.android.gms.location.*;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.*;

public class CreateAdvertActivity extends AppCompatActivity {
    private static final int AUTOCOMPLETE_REQUEST_CODE = 100;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 101;

    DatabaseHelper db;
    EditText name, phone, description, date, location;
    RadioButton lost, found;
    Button btnGetCurrentLocation;

    FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        db = new DatabaseHelper(this);

        lost = findViewById(R.id.radioLost);
        found = findViewById(R.id.radioFound);
        name = findViewById(R.id.etName);
        phone = findViewById(R.id.etPhone);
        description = findViewById(R.id.etDescription);
        date = findViewById(R.id.etDate);
        location = findViewById(R.id.etLocation);
        btnGetCurrentLocation = findViewById(R.id.btnGetCurrentLocation);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize Places SDK
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyC3_HLYla5BTleM0Fu-vCeTrW8b_EY9_iE"); // Replace with your actual API key
        }

        // Date picker
        date.setFocusable(false);
        date.setOnClickListener(v -> showDatePicker());

        // Location Autocomplete
        location.setFocusable(false);
        location.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });

        // Current location button
        btnGetCurrentLocation.setOnClickListener(v -> getCurrentLocation());
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR), month = calendar.get(Calendar.MONTH), day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
            (view, year1, monthOfYear, dayOfMonth) -> {
                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                date.setText(selectedDate);
            }, year, month, day);
        datePickerDialog.show();
    }

    public void saveAdvert(View view) {
        if (!lost.isChecked() && !found.isChecked()) {
            Toast.makeText(this, "Please select Lost or Found.", Toast.LENGTH_SHORT).show();
            return;
        }

        String type = lost.isChecked() ? "Lost" : "Found";
        db.insertAdvert(
            type,
            name.getText().toString(),
            phone.getText().toString(),
            description.getText().toString(),
            date.getText().toString(),
            location.getText().toString()
        );
        Toast.makeText(this, "Advert Saved!", Toast.LENGTH_SHORT).show();

        // Start MapsActivity
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);  // Open the map activity
        finish();  // Optionally finish the current activity
    }


    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);  // 10 seconds
        locationRequest.setFastestInterval(5000);  // 5 seconds

        fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    Location location = locationResult.getLocations().get(0);
                    try {
                        Geocoder geocoder = new Geocoder(CreateAdvertActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (!addresses.isEmpty()) {
                            CreateAdvertActivity.this.location.setText(addresses.get(0).getAddressLine(0));
                        }
                    } catch (Exception e) {
                        Toast.makeText(CreateAdvertActivity.this, "Failed to get address from location.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, null);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            location.setText(place.getAddress());
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Toast.makeText(this, "Error in autocomplete", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
