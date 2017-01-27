package com.example.SAiTAProjectGroup.saita_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class PassengersActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passengers);

        String[] numberOfPassengers = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, numberOfPassengers);

        final Spinner adultsSpinner = (Spinner) findViewById(R.id.adults_spinner);
        adultsSpinner.setAdapter(adapter);

        final Spinner childrenSpinner = (Spinner) findViewById(R.id.children_spinner);
        childrenSpinner.setAdapter(adapter);

        final Spinner infantsSpinner = (Spinner) findViewById(R.id.infants_spinner);
        infantsSpinner.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("ADULTS")) {
                String adultsString = (String) intent.getStringExtra("ADULTS");
                adultsSpinner.setSelection(Integer.parseInt(adultsString));
            }
            if (intent.hasExtra("CHILDREN")) {
                String childrenString = (String) intent.getStringExtra("CHILDREN");
                childrenSpinner.setSelection(Integer.parseInt(childrenString));
            }
            if (intent.hasExtra("INFANTS")) {
                String infantsString = (String) intent.getStringExtra("INFANTS");
                infantsSpinner.setSelection(Integer.parseInt(infantsString));
            }
        }

        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = PassengersActivity.this;
                Intent intent = new Intent();
                boolean allDataAreValid = true;

                int adultsNumber = Integer.parseInt(adultsSpinner.getSelectedItem().toString());
                int childrenNumber = Integer.parseInt(childrenSpinner.getSelectedItem().toString());
                int infantsNumber = Integer.parseInt(infantsSpinner.getSelectedItem().toString());

                if (infantsNumber > adultsNumber) {
                    allDataAreValid = false;
                    Toast.makeText(context, getResources().getString(R.string.warning_invalid_infants), Toast.LENGTH_SHORT).show();
                }
                else if (adultsNumber == 0 && childrenNumber == 0) {
                    allDataAreValid = false;
                    Toast.makeText(context, getResources().getString(R.string.warning_no_passengers), Toast.LENGTH_SHORT).show();
                }

                if (allDataAreValid) {
                    String passengersString = Utilities.getPassengersString(adultsNumber, childrenNumber, infantsNumber);
                    intent.putExtra("PASSENGERS_STRING", passengersString);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

    }

}
