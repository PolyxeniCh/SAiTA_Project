package com.example.SAiTAProjectGroup.saita_project;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FlightsActivity extends AppCompatActivity implements FlightsAdapter.FlightsAdapterOnClickHandler {

    private static RecyclerView flightsRecyclerView;
    public static FlightsAdapter flightsAdapter;
    private static TextView flightErrorMessage;
    public static ProgressBar flightsLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flights);

        // ---- flightsRecyclerView ----
        flightsRecyclerView = (RecyclerView) findViewById(R.id.rv_flights_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        flightsRecyclerView.setLayoutManager(layoutManager);
        flightsRecyclerView.setHasFixedSize(true);

        flightsAdapter = new FlightsAdapter(this);
        flightsRecyclerView.setAdapter(flightsAdapter);

        // ---- flightsErrorMessage ----
        flightErrorMessage = (TextView) findViewById(R.id.tv_error_message_display);

        // ---- flightsLoadingIndicator ----
        flightsLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        // Load flights data
        loadFlightsData();
    }

    private void loadFlightsData() {
        showFlightsDataView();

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("ORIGIN")
                    && intent.hasExtra("DESTINATION")
                    && intent.hasExtra("DEPARTURE_DATE")) {
                Bundle extras = intent.getExtras();
                new FetchFlightsTask().execute(extras);
            }
        }
    }

    @Override
    public void onClick(ItineraryObject itinerary) {
        Context context = this;
        Class destinationClass = DetailsActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("ITINERARY", itinerary);
        startActivity(intentToStartDetailActivity);
    }

    public static void showFlightsDataView() {
        flightErrorMessage.setVisibility(View.INVISIBLE);
        flightsRecyclerView.setVisibility(View.VISIBLE);
    }

    public static void showFlightsErrorMessage() {
        flightsRecyclerView.setVisibility(View.INVISIBLE);
        flightErrorMessage.setVisibility(View.VISIBLE);
    }

}
