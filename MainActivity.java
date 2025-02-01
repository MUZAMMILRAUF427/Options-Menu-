package com.example.assignment4;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView birthDateText, resultText, errortext,calculateButton;
    private Calendar selectedDate;
    private boolean isDateSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        birthDateText = findViewById(R.id.dobTextView);
        calculateButton = findViewById(R.id.calculateButton);

        errortext = findViewById(R.id.errorText);

        selectedDate = Calendar.getInstance();

        birthDateText.setOnClickListener(v -> {
            showDatePicker();
        });

        calculateButton.setOnClickListener(v -> {
            if (validatePreferences()) {
                if (validateDateSelection()) {
                    calculateAge();
                }
            }
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate.set(year, month, dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
                    birthDateText.setText(dateFormat.format(selectedDate.getTime()));
                    isDateSelected = true;
                    errortext.setVisibility(TextView.GONE);
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private boolean validatePreferences() {
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        String ageUnit = preferences.getString("ageUnit", null);
        String color = preferences.getString("color", null);

        if (ageUnit == null || color == null) {
            errortext.setText("Please set Unit and Color in Settings before calculating age.");
            errortext.setVisibility(TextView.VISIBLE);
            return false;
        }
        errortext.setVisibility(TextView.GONE);
        return true;
    }

    private boolean validateDateSelection() {
        if (!isDateSelected) {
            errortext.setText("Please select a date before calculating age.");
            errortext.setVisibility(TextView.VISIBLE);
            return false;
        }
        errortext.setVisibility(TextView.GONE);
        return true;
    }

    private void calculateAge() {
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        String ageUnit = preferences.getString("ageUnit", "Days");
        String color = preferences.getString("color", "#000000");

        try {
            long diff = new Date().getTime() - selectedDate.getTimeInMillis();
            long ageInDays = diff / (1000 * 60 * 60 * 24);

            if (ageUnit.equals("Months")) {
                ageInDays /= 30;
            } else if (ageUnit.equals("Years")) {
                ageInDays /= 365;
            }

            String resultText = ageUnit + ": " + ageInDays;


            ResultFragment resultFragment = ResultFragment.newInstance(resultText, color);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, resultFragment);
            transaction.commit();
            errortext.setVisibility(TextView.GONE);
        } catch (Exception e) {
            resultText.setText("Invalid Date");
            errortext.setVisibility(TextView.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about) {
            Toast.makeText(this, "About Clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.contact) {
            Toast.makeText(this, "Contact Clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.settings) {
            Intent intent = new Intent(this, activity_settings.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
