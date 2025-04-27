package com.prac.lostfound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnCreateAdvert, btnShowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        btnCreateAdvert = findViewById(R.id.btnCreateAdvert);
        btnShowItems = findViewById(R.id.btnShowItems);


        if (btnCreateAdvert == null || btnShowItems == null) {
            Toast.makeText(this, "Error: Buttons not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Button click listener to create a new advert
        btnCreateAdvert.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(MainActivity.this, CreateAdvertActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to open CreateAdvertActivity", Toast.LENGTH_SHORT).show();
            }
        });

        // Button click listener to show all lost and found items
        btnShowItems.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(MainActivity.this, ViewItemsActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to open ViewItemsActivity", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
