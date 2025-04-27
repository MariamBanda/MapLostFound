package com.prac.lostfound;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailActivity extends AppCompatActivity {

    DatabaseHelper db;
    int advertId;
    Advert advert;
    TextView titleTextView, dateTextView, locationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_item);

        db = new DatabaseHelper(this);
        advertId = getIntent().getIntExtra("id", -1);

        for (Advert a : db.getAllAdverts()) {
            if (a.getId() == advertId) {
                advert = a;
                break;
            }
        }

        titleTextView = findViewById(R.id.tvItemTitle);
        dateTextView = findViewById(R.id.tvItemDate);
        locationTextView = findViewById(R.id.tvItemLocation);

        titleTextView.setText(advert.getPostType() + " " + advert.getName());
        dateTextView.setText(advert.getDate());
        locationTextView.setText("At " + advert.getLocation());
    }

    public void removeAdvert(View view) {
        db.deleteAdvert(advertId);
        setResult(RESULT_OK);
        finish();
    }

}
