package com.example.SAiTAProjectGroup.saita_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class TravelClassActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_class);

        final RadioGroup travelClassRadioGroup = (RadioGroup) findViewById(R.id.travel_clas_radio_group);
        final RadioButton economyClassRadioButton = (RadioButton) findViewById(R.id.rb_economy_travel_class);
        final RadioButton premiumEconomyClassRadioButton = (RadioButton) findViewById(R.id.rb_premium_economy_travel_class);
        final RadioButton businessClassRadioButton = (RadioButton) findViewById(R.id.rb_business_travel_class);
        final RadioButton firstClassRadioButton = (RadioButton) findViewById(R.id.rb_first_travel_class);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("TRAVEL_CLASS")) {
                String travelClassString = intent.getStringExtra("TRAVEL_CLASS");

                if (economyClassRadioButton.getText().equals(travelClassString)) {
                    economyClassRadioButton.setChecked(true);
                } else if (premiumEconomyClassRadioButton.getText().equals(travelClassString)) {
                    premiumEconomyClassRadioButton.setChecked(true);
                } else if (businessClassRadioButton.getText().equals(travelClassString)) {
                    businessClassRadioButton.setChecked(true);
                } else if (firstClassRadioButton.getText().equals(travelClassString)) {
                    firstClassRadioButton.setChecked(true);
                }
            }
        }

        Button saveButton = (Button) findViewById(R.id.travel_class_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                String travelClassString = "";

                if (travelClassRadioGroup.getCheckedRadioButtonId() == economyClassRadioButton.getId()) {
                    travelClassString = economyClassRadioButton.getText().toString();
                } else if (travelClassRadioGroup.getCheckedRadioButtonId() == premiumEconomyClassRadioButton.getId()) {
                    travelClassString = premiumEconomyClassRadioButton.getText().toString();
                } else if (travelClassRadioGroup.getCheckedRadioButtonId() == businessClassRadioButton.getId()) {
                    travelClassString = businessClassRadioButton.getText().toString();
                } else if (travelClassRadioGroup.getCheckedRadioButtonId() == firstClassRadioButton.getId()) {
                    travelClassString = firstClassRadioButton.getText().toString();
                }

                intent.putExtra("TRAVEL_CLASS", travelClassString);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}
