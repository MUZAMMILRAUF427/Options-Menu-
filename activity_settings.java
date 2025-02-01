package com.example.assignment4;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class activity_settings extends AppCompatActivity {
    private Spinner colorSpinner, ageUnitSpinner;
   TextView saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        colorSpinner = findViewById(R.id.colorSpinner);
        ageUnitSpinner = findViewById(R.id.ageUnitSpinner);
        saveButton = findViewById(R.id.saveButton);


        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(this,
                R.array.color_names, android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(colorAdapter);


        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(this,
                R.array.age_units, android.R.layout.simple_spinner_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageUnitSpinner.setAdapter(unitAdapter);

        loadSavedPreferences();

        saveButton.setOnClickListener(v -> savePreferences());
    }

    private void loadSavedPreferences() {
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);

        String savedAgeUnit = preferences.getString("ageUnit", null);
        if (savedAgeUnit != null) {
            String[] ageUnits = getResources().getStringArray(R.array.age_units);
            for (int i = 0; i < ageUnits.length; i++) {
                if (ageUnits[i].equals(savedAgeUnit)) {
                    ageUnitSpinner.setSelection(i);
                    break;
                }
            }
        }

        String savedColor = preferences.getString("color", null);
        if (savedColor != null) {
            String[] colorValues = getResources().getStringArray(R.array.color_values);
            for (int i = 0; i < colorValues.length; i++) {
                if (colorValues[i].equals(savedColor)) {
                    colorSpinner.setSelection(i);
                    break;
                }
            }
        }
    }

    private void savePreferences() {
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String ageUnit = ageUnitSpinner.getSelectedItem().toString();
        editor.putString("ageUnit", ageUnit);
        String[] colorValues = getResources().getStringArray(R.array.color_values);
        int selectedColorPosition = colorSpinner.getSelectedItemPosition();
        String selectedColorHex = colorValues[selectedColorPosition];
        editor.putString("color", selectedColorHex);
        Toast.makeText(this, "Save Successfully", Toast.LENGTH_SHORT).show();
        editor.apply();
        finish();
    }
}
