package com.prac.lostfound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnCreateAdvert, btnShowItems, btnShowOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreateAdvert = findViewById(R.id.btnCreateAdvert);
        btnShowItems = findViewById(R.id.btnShowItems);
        btnShowOnMap = findViewById(R.id.btnShowOnMap);

        if (btnCreateAdvert == null || btnShowItems == null || btnShowOnMap == null) {
            Toast.makeText(this, "Error: Buttons not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        btnCreateAdvert.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(MainActivity.this, CreateAdvertActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to open CreateAdvertActivity", Toast.LENGTH_SHORT).show();
            }
        });

        btnShowItems.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(MainActivity.this, ViewItemsActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to open ViewItemsActivity", Toast.LENGTH_SHORT).show();
            }
        });

        btnShowOnMap.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to open MapActivity", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
