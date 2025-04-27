package com.prac.lostfound;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
public class CreateAdvertActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText name, phone, description, date, location;
    RadioButton lost, found;

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

        date.setFocusable(false);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    private void showDatePicker() {
        final java.util.Calendar calendar = java.util.Calendar.getInstance();
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH);
        int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                    date.setText(selectedDate);
                }
            },
            year, month, day
        );
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
        finish();
    }
}
