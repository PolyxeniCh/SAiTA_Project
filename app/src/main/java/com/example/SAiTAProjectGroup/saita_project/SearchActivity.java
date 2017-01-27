package com.example.SAiTAProjectGroup.saita_project;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private Button searchButton;
    private SearchAdapter searchAdapter;
    private ListView airportsListView;
    private TextView airportsErrorMessage;
    private ProgressBar airportsLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        airportsErrorMessage = (TextView) findViewById(R.id.tv_error_message_airports);

        airportsLoadingIndicator = (ProgressBar)  findViewById(R.id.pb_loading_indicator_airports);

        searchView = (SearchView) findViewById(R.id.search_view);

        airportsListView= (ListView) findViewById(R.id.listview_search);
        airportsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String airportString = (String) airportsListView.getItemAtPosition(position);
                String airportCode = Utilities.getAirportCode(airportString);
                Intent intent = new Intent();
                intent.putExtra("AIRPORT_CODE", airportCode);
                setResult(RESULT_OK, intent);
                finish();
            }

        });

        searchButton = (Button) findViewById(R.id.general_search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchView.getQuery().length() != 0) {
                    String query = searchView.getQuery().toString();
                    new FetchAirportsTask().execute(query);
                } else {
                    Context context = SearchActivity.this;
                    Toast.makeText(context, getResources().getString(R.string.warning_no_airport_input), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showAirportsDataView() {
        airportsErrorMessage.setVisibility(View.INVISIBLE);
        airportsListView.setVisibility(View.VISIBLE);
    }

    public void showAirportsErrorMessage() {
        airportsListView.setVisibility(View.INVISIBLE);
        airportsErrorMessage.setVisibility(View.VISIBLE);
    }

    class FetchAirportsTask extends AsyncTask<String, Void, ArrayList<String>> {

        private final String TAG = FetchAirportsTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            airportsLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {

            if (strings.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String airportsJsonStr = null;

            try {
                //AMADEUS API AUTOCOMPLETE
                final String baseUrl = "https://api.sandbox.amadeus.com/v1.2/airports/autocomplete?";
                final String API_KEY_PARAM = "apikey"; //REQUIRED
                final String TERM_PARAM = "term";

                Uri builtUri = Uri.parse(baseUrl).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM,BuildConfig.MY_AMADEUS_SANDBOX_API_ΚΕΥ)
                        .appendQueryParameter(TERM_PARAM, strings[0])
                        .build();

                // URL creation
                URL url = null;
                try {
                    url = new URL(builtUri.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                Log.v(TAG, "Built URI Autocomplete: " + url);

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

                airportsJsonStr = buffer.toString();
                Log.v(TAG, "Airports JSON String: " + airportsJsonStr);

                try {
                    return Utilities.getAirportsDataFromJson(airportsJsonStr);
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage(), e);
                    e.printStackTrace();
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

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> airports) {
            airportsLoadingIndicator.setVisibility(View.INVISIBLE);
            if (airports != null) {
                if (!airports.isEmpty()) {
                    String[] airportsArray = new String[airports.size()];
                    for (int i=0 ; i<airports.size() ; i++) {
                        airportsArray[i] = airports.get(i);
                    }
                    searchAdapter = new SearchAdapter(SearchActivity.this, airportsArray);
                    airportsListView.setAdapter(searchAdapter);
                    showAirportsDataView();
                } else {
                    showAirportsErrorMessage();
                }
            } else {
                showAirportsErrorMessage();
            }
        }

    }

}
