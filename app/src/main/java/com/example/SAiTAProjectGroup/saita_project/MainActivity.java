/*

Εργασία στο πλαίσιο του μαθήματος:

"Ανάπτυξη εφαρμογών για κινητές συσκευές"

-------------------------------------

Φοιτητές/τριες:

Βροχίδης Αλέξανδρος (ΑΜ 169/14)
Χαραλαμπίδου Πολυξένη (ΑΜ 148/14)
Χατζάκη Εμμανουέλα (ΑΜ 166/14)

-------------------------------------

Περιγραφή εφαρμογής:

Σκοπός της εφαρμογής είναι η εύρεση πτήσεων (με τη  βοήθεια
του API της AMADEUS), ανάλογα με τα δεδομένα που εισάγει ο χρήστης,
και η εμφάνιση των λεπτομερειών τους.

-------------------------------------

Απαιτήσεις για τη σωστή λειτουργία της εφαρμογής:

1) Μέσα στο φάκελο .../User/.gradle πρέπει να υπάρχει το αρχείο
   gradle.properties, το οποίο περιέχει τα κλειδιά των API
   που χρησιμοποιούνται στη μορφή:
   MyAmadeusSandboxApiKey="abcdef0123456789"
   MyIATACodesApiKey="abcdef0123456789"

2) Η ελάχιστη έκδοση Android που υποστηρίζεται είναι:
   API 16 : Android 4.1 (Jelly Bean)

 */

package com.example.SAiTAProjectGroup.saita_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView fromAirportTextView;
    private TextView toAirportTextView;
    private TextView departureDateTextView;
    private TextView returnDateTextView;
    private Switch hasReturnSwitch;
    private TextView passengersTextView;
    private TextView cabinClassTextView;
    private CheckBox onlyDirectFlightsCheckBox;
    private Button searchFlightsButton;

    private Context context = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ---- fromAirportTextView ----
        fromAirportTextView = (TextView) findViewById(R.id.tv_from_airport);
        fromAirportTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class destinationActivity = SearchActivity.class;
                Intent startActivityIntent = new Intent(context, destinationActivity);
                startActivityForResult(startActivityIntent, 1);
            }
        });

        // ---- toAirportTextView ----
        toAirportTextView = (TextView) findViewById(R.id.tv_to_airport);
        toAirportTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class destinationActivity = SearchActivity.class;
                Intent startActivityIntent = new Intent(context, destinationActivity);
                startActivityForResult(startActivityIntent, 2);
            }
        });

        // ---- departureDateTextView ----
        departureDateTextView = (TextView) findViewById(R.id.tv_departure_date);
        departureDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class destinationActivity = CalendarActivity.class;
                Intent startActivityIntent = new Intent(context, destinationActivity);
                String departureDateString = departureDateTextView.getText().toString();
                if(!departureDateString.equals(getResources().getString(R.string.tv_departure_date_text))) {
                    startActivityIntent.putExtra("DEPARTURE_DATE", departureDateString);
                }
                startActivityForResult(startActivityIntent, 3);
            }
        });

        // ---- returnDateTextView ----
        returnDateTextView = (TextView) findViewById(R.id.tv_return_date);
        returnDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class destinationActivity = CalendarActivity.class;
                Intent startActivityIntent = new Intent(context, destinationActivity);
                String departureDateString = departureDateTextView.getText().toString();
                if(!departureDateString.equals(getResources().getString(R.string.tv_departure_date_text))) {
                    startActivityIntent.putExtra("DEPARTURE_DATE", departureDateString);
                }
                startActivityForResult(startActivityIntent, 4);
            }
        });

        // ---- hasReturnSwitch ----
        hasReturnSwitch = (Switch) findViewById(R.id.sw_has_return);
        hasReturnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    returnDateTextView.setEnabled(true);
                } else {
                    returnDateTextView.setEnabled(false);
                }
            }
        });

        // ---- passengersTextView ----
        passengersTextView = (TextView) findViewById(R.id.tv_passengers);
        passengersTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class destinationActivity = PassengersActivity.class;
                Intent startActivityIntent = new Intent(context, destinationActivity);
                String passengers = passengersTextView.getText().toString();
                Bundle extras = new Bundle();
                if (!Utilities.getAdultsNumberString(passengers).equals("0")) {
                    extras.putString("ADULTS", Utilities.getAdultsNumberString(passengers));
                }
                if (!Utilities.getChildrenNumberString(passengers).equals("0")) {
                    extras.putString("CHILDREN", Utilities.getChildrenNumberString(passengers));
                }
                if (!Utilities.getInfantsNumberString(passengers).equals("0")) {
                    extras.putString("INFANTS", Utilities.getInfantsNumberString(passengers));
                }
                startActivityIntent.putExtras(extras);
                startActivityForResult(startActivityIntent, 5);
            }
        });

        // ---- cabinClassTextView ----
        cabinClassTextView = (TextView) findViewById(R.id.tv_travel_class);
        cabinClassTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class destinationActivity = TravelClassActivity.class;
                Intent startActivityIntent = new Intent(context, destinationActivity);
                String cabinClass = cabinClassTextView.getText().toString();
                startActivityIntent.putExtra("TRAVEL_CLASS", cabinClass);
                startActivityForResult(startActivityIntent, 6);
            }
        });

        // ---- onlyDirectFlightsCheckBox ----
        onlyDirectFlightsCheckBox = (CheckBox) findViewById(R.id.cb_direct_flight);

        // ---- searchFlightButton ----
        searchFlightsButton = (Button) findViewById(R.id.search_button);
        searchFlightsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean allDataAreValid = true;
                boolean hasReturnDate = false;
                boolean hasAdults = false;
                boolean hasChildren = false;
                boolean hasInfants = false;
                boolean hasOnlyDirectFlights = false;

                if (fromAirportTextView.getText().toString().equals(getResources().getString(R.string.tv_from_airport_text))) {
                    allDataAreValid = false;
                    Toast.makeText(context, getResources().getString(R.string.warning_invalid_origin), Toast.LENGTH_SHORT).show();
                }

                else if (toAirportTextView.getText().toString().equals(getResources().getString(R.string.tv_to_airport_text))) {
                    allDataAreValid = false;
                    Toast.makeText(context, getResources().getString(R.string.warning_invalid_destination), Toast.LENGTH_SHORT).show();
                }

                else if (departureDateTextView.getText().toString().equals(getResources().getString(R.string.tv_departure_date_text))) {
                    allDataAreValid = false;
                    Toast.makeText(context, getResources().getString(R.string.warning_invalid_departure_date), Toast.LENGTH_SHORT).show();
                }

                else if (hasReturnSwitch.isChecked()) {
                    if (!returnDateTextView.getText().toString().equals(getResources().getString(R.string.tv_return_date_text))) {
                        String departureDate = departureDateTextView.getText().toString();
                        String returnDate = returnDateTextView.getText().toString();
                        if (Utilities.isValidReturnDate(departureDate, returnDate)) {
                            hasReturnDate = true;
                        } else {
                            allDataAreValid = false;
                            Toast.makeText(context, getResources().getString(R.string.warning_invalid_return_date), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        allDataAreValid = false;
                        Toast.makeText(context, getResources().getString(R.string.warning_no_return_date), Toast.LENGTH_SHORT).show();
                    }
                }

                String passengersString = passengersTextView.getText().toString();
                if (!Utilities.getAdultsNumberString(passengersString).equals("0")) {
                    hasAdults = true;
                }
                if (!Utilities.getChildrenNumberString(passengersString).equals("0")) {
                    hasChildren = true;
                }
                if (!Utilities.getInfantsNumberString(passengersString).equals("0")) {
                    hasInfants = true;
                }

                if (onlyDirectFlightsCheckBox.isChecked()) {
                    hasOnlyDirectFlights = true;
                }

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                String currency = prefs.getString(getString(R.string.general_preferences_currency_key), getString(R.string.preferred_currency_eur));
                Utilities.setCurrencySymbol(context);

                if (allDataAreValid) {
                    Class destinationActivity = FlightsActivity.class;
                    Intent startActivityIntent = new Intent(context, destinationActivity);
                    Bundle extras = new Bundle();

                    extras.putString("ORIGIN", fromAirportTextView.getText().toString());
                    extras.putString("DESTINATION", toAirportTextView.getText().toString());
                    extras.putString("DEPARTURE_DATE", departureDateTextView.getText().toString());
                    extras.putString("CURRENCY", currency);
                    extras.putString("TRAVEL_CLASS", cabinClassTextView.getText().toString());
                    if (hasReturnDate) extras.putString("RETURN_DATE", returnDateTextView.getText().toString());
                    if (hasAdults) extras.putString("ADULTS", Utilities.getAdultsNumberString(passengersString));
                    if (hasChildren) extras.putString("CHILDREN", Utilities.getChildrenNumberString(passengersString));
                    if (hasInfants) extras.putString("INFANTS", Utilities.getInfantsNumberString(passengersString));
                    if (hasOnlyDirectFlights) extras.putString("ONLY_DIRECT_FLIGHTS", "true");

                    startActivityIntent.putExtras(extras);
                    startActivity(startActivityIntent);
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String originAirportString = data.getStringExtra("AIRPORT_CODE");
                fromAirportTextView.setText(originAirportString);
            }
        }
        else if (requestCode == 2) {
            if(resultCode == RESULT_OK){
                String destinationAirportString = data.getStringExtra("AIRPORT_CODE");
                toAirportTextView.setText(destinationAirportString);
            }
        }
        else if (requestCode == 3) {
            if(resultCode == RESULT_OK){
                String departureDateString = data.getStringExtra("DATE");
                departureDateTextView.setText(departureDateString);
            }
        }
        else if (requestCode == 4) {
            if(resultCode == RESULT_OK){
                String returnDateString = data.getStringExtra("DATE");
                returnDateTextView.setText(returnDateString);
            }
        }
        else if (requestCode == 5) {
            if(resultCode == RESULT_OK){
                String passengersString = data.getStringExtra("PASSENGERS_STRING");
                passengersTextView.setText(passengersString);
            }
        }
        else if (requestCode == 6) {
            if(resultCode == RESULT_OK){
                String travelClassString = data.getStringExtra("TRAVEL_CLASS");
                cabinClassTextView.setText(travelClassString);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(context, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
