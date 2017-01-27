package com.example.SAiTAProjectGroup.saita_project;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.SAiTAProjectGroup.saita_project.FlightsActivity.flightsAdapter;
import static com.example.SAiTAProjectGroup.saita_project.FlightsActivity.flightsLoadingIndicator;
import static com.example.SAiTAProjectGroup.saita_project.FlightsActivity.showFlightsDataView;
import static com.example.SAiTAProjectGroup.saita_project.FlightsActivity.showFlightsErrorMessage;

public class FetchFlightsTask extends AsyncTask<Bundle, Void, ArrayList<ItineraryObject>> {

    private final String TAG = FetchFlightsTask.class.getSimpleName();
    private ArrayList<ItineraryObject> itineraries = new ArrayList<ItineraryObject>();
    private ArrayList<String> airlineCodes = new ArrayList<String>();
    private ArrayList<String> airlineNames = new ArrayList<String>();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        flightsLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<ItineraryObject> doInBackground(Bundle... bundles) {

        if (bundles.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String flightsJsonStr = null;

        try {
            //AMADEUS API
            final String baseUrl = "https://api.sandbox.amadeus.com/v1.2/flights/low-fare-search?";
            final String API_KEY_PARAM = "apikey"; //REQUIRED
            final String ORIGIN_PARAM = "origin"; //REQUIRED
            final String DESTINATION_PARAM = "destination"; //REQUIRED
            final String DEPARTURE_DATE_PARAM = "departure_date"; //REQUIRED
            final String RETURN_DATE_PARAM = "return_date";
            final String ADULTS_PARAM = "adults";
            final String CHILDREN_PARAM = "children";
            final String INFANTS_PARAM = "infants";
            final String NONSTOP_PARAM = "nonstop";
            final String CURRENCY_PARAM = "currency";
            final String TRAVEL_CLASS_PARAM = "travel_class";

            String originString = bundles[0].getString("ORIGIN");
            String destinationString = bundles[0].getString("DESTINATION");
            String departureDateString = bundles[0].getString("DEPARTURE_DATE");

            Uri builtUri = Uri.parse(baseUrl).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM,BuildConfig.MY_AMADEUS_SANDBOX_API_ΚΕΥ)
                    .appendQueryParameter(ORIGIN_PARAM, originString)
                    .appendQueryParameter(DESTINATION_PARAM, destinationString)
                    .appendQueryParameter(DEPARTURE_DATE_PARAM, departureDateString)
                    .build();

            if (bundles[0].containsKey("RETURN_DATE")) {
                String returnDateString = bundles[0].getString("RETURN_DATE");
                builtUri = builtUri.buildUpon().appendQueryParameter(RETURN_DATE_PARAM, returnDateString).build();
            }

            if (bundles[0].containsKey("ADULTS")) {
                String adultsString = bundles[0].getString("ADULTS");
                builtUri = builtUri.buildUpon().appendQueryParameter(ADULTS_PARAM, adultsString).build();
            }

            if (bundles[0].containsKey("CHILDREN")) {
                String childrenString = bundles[0].getString("CHILDREN");
                builtUri = builtUri.buildUpon().appendQueryParameter(CHILDREN_PARAM, childrenString).build();
            }

            if (bundles[0].containsKey("INFANTS")) {
                String infantsString = bundles[0].getString("INFANTS");
                builtUri = builtUri.buildUpon().appendQueryParameter(INFANTS_PARAM, infantsString).build();
            }

            if (bundles[0].containsKey("ONLY_DIRECT_FLIGHTS")) {
                String nonstopString = bundles[0].getString("ONLY_DIRECT_FLIGHTS");
                builtUri = builtUri.buildUpon().appendQueryParameter(NONSTOP_PARAM, nonstopString).build();
            }

            if (bundles[0].containsKey("CURRENCY")) {
                String currencyString = bundles[0].getString("CURRENCY");
                builtUri = builtUri.buildUpon().appendQueryParameter(CURRENCY_PARAM, currencyString).build();
            }

            if (bundles[0].containsKey("TRAVEL_CLASS")) {
                String travelClassString = bundles[0].getString("TRAVEL_CLASS");
                builtUri = builtUri.buildUpon().appendQueryParameter(TRAVEL_CLASS_PARAM, travelClassString).build();
            }

            // URL creation
            URL url = null;
            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            Log.v(TAG, "Built URI: " + url);

            // Create the request, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }

            flightsJsonStr = buffer.toString();
            Log.v(TAG, "Flights JSON String: " + flightsJsonStr);
            try {
                itineraries = Utilities.getFlightsDataFromJson(flightsJsonStr);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            //IATA CODES API
            String airlineJsonStr = null;
            if (!itineraries.isEmpty()) {

                //Find all different airlines in the results
                for (ItineraryObject i : itineraries) {
                    for (FlightObject f : i.getOutboundFlights()) {
                        if (!airlineCodes.contains(f.getAirline())) {
                            airlineCodes.add(f.getAirline());
                        }
                    }
                    if (!i.getInboundFlights().isEmpty()){
                        for (FlightObject f : i.getInboundFlights()) {
                            if (!airlineCodes.contains(f.getAirline())) {
                                airlineCodes.add(f.getAirline());
                            }
                        }
                    }
                }

                for (String s : airlineCodes) {
                    String baseUrlIATA = "https://iatacodes.org/api/v6/airlines?";
                    String API_KEY_PARAM_IATA = "api_key";
                    String CODE_PARAM_IATA = "code";

                    //URI creation
                    Uri builtUriIATA = Uri.parse(baseUrlIATA).buildUpon()
                            .appendQueryParameter(API_KEY_PARAM_IATA, BuildConfig.MY_IATA_CODES_API_ΚΕΥ)
                            .appendQueryParameter(CODE_PARAM_IATA, s)
                            .build();

                    // URL creation
                    URL urlIATA = null;
                    try {
                        urlIATA = new URL(builtUriIATA.toString());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    Log.v(TAG, "Built URI (IATA): " + urlIATA);

                    // Create the request, and open the connection
                    urlConnection = (HttpURLConnection) urlIATA.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    // Read the input stream into a String
                    inputStream = urlConnection.getInputStream();
                    buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String lineIATA;
                    while ((lineIATA = reader.readLine()) != null) {
                        buffer.append(lineIATA + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty. No point in parsing.
                        return null;
                    }

                    airlineJsonStr = buffer.toString();
                    Log.v(TAG, "Airline JSON String: " + airlineJsonStr);
                    try {
                        String airlineNameFromJson = Utilities.getAirlineDataFromJson(airlineJsonStr);
                        airlineNames.add(airlineNameFromJson);
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage(), e);
                        e.printStackTrace();
                    }

                }

                //Replace airline codes with their names
                for (ItineraryObject i : itineraries) {
                    //Outbound
                    for (FlightObject f : i.getOutboundFlights()) {
                        int position = airlineCodes.indexOf(f.getAirline());
                        f.setAirline(airlineNames.get(position));
                    }
                    //Inbound
                    if (!i.getInboundFlights().isEmpty()) {
                        for (FlightObject f : i.getInboundFlights()) {
                            int position = airlineCodes.indexOf(f.getAirline());
                            f.setAirline(airlineNames.get(position));
                        }
                    }
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error ", e);
            // If the code didn't successfully get the data, there's no point in attempting to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }

        return itineraries;

    }

    @Override
    protected void onPostExecute(ArrayList<ItineraryObject> itineraryData) {
        flightsLoadingIndicator.setVisibility(View.INVISIBLE);
        if (itineraryData != null) {
            showFlightsDataView();
            flightsAdapter.setItinerariesData(itineraryData);
        } else {
            showFlightsErrorMessage();
        }
    }

}