package com.prac.lostfound;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 101;
    private GoogleMap mMap;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        db = new DatabaseHelper(this);

        // Use FragmentManager to handle the SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map_container);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.map_container, mapFragment)
                .commit();
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            loadMarkersFromDatabase();
        } else {
            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void loadMarkersFromDatabase() {
        new Thread(() -> {
            Cursor cursor = db.getAllAdvertsCursor();
            if (cursor == null || cursor.getCount() == 0) return;

            AtomicBoolean firstMarker = new AtomicBoolean(true);

            while (cursor.moveToNext()) {
                String type = cursor.getString(cursor.getColumnIndexOrThrow("postType"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String locationText = cursor.getString(cursor.getColumnIndexOrThrow("location"));

                LatLng latLng = GeocoderUtils.getLatLngFromAddress(MapsActivity.this, locationText);
                if (latLng == null) continue;

                runOnUiThread(() -> {

                    mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(String.format(Locale.getDefault(), "%s: %s", type, name))
                        .icon(BitmapDescriptorFactory.defaultMarker(
                            type.equalsIgnoreCase("lost")
                                ? BitmapDescriptorFactory.HUE_RED  // Red for lost items
                                : BitmapDescriptorFactory.HUE_GREEN  // Green for found items
                        )));


                    if (firstMarker.getAndSet(false)) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f));
                    }
                });
            }
            cursor.close();
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    loadMarkersFromDatabase();
                }
            } else {
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
