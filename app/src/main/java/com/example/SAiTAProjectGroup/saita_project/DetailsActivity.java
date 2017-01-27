package com.example.SAiTAProjectGroup.saita_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity{

    ItineraryObject itinerary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_details);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("ITINERARY")) {
                itinerary = (ItineraryObject) intent.getSerializableExtra("ITINERARY");

                TextView totalPrice = (TextView) findViewById(R.id.textViewFlightPrice);
                totalPrice.setText(itinerary.getTotalPrice() + Utilities.currencySymbol);

                FlightObject[] flights = new FlightObject[itinerary.getOutboundFlights().size() + itinerary.getInboundFlights().size()];

                for (int i=0 ; i<itinerary.getOutboundFlights().size() ; i++) {
                    flights[i] = itinerary.getOutboundFlights().get(i);
                }

                for (int i=0 ; i<itinerary.getInboundFlights().size() ; i++) {
                    flights[i + itinerary.getOutboundFlights().size()] = itinerary.getInboundFlights().get(i);
                }

                String[] originCityList = new String[flights.length];
                String[] destinationCityList = new String[flights.length];
                String[] airlineList = new String[flights.length];
                String[] flightCodeList = new String[flights.length];
                String[] seatTypeList = new String[flights.length];
                String[] remainingSeatsList = new String[flights.length];
                String[] departureDateList = new String[flights.length];
                String[] departureTimeList = new String[flights.length];
                String[] arrivalDateList = new String[flights.length];
                String[] arrivalTimeList = new String[flights.length];

                for (int i=0 ; i<flights.length ; i++) {
                    originCityList[i] = flights[i].getOrigin();
                    destinationCityList[i] = flights[i].getDestination();
                    airlineList[i] = flights[i].getAirline();
                    flightCodeList[i] = flights[i].getFlightNumber();
                    seatTypeList[i] = flights[i].getTravelClass();
                    remainingSeatsList[i] = flights[i].getSeatsRemaining();
                    departureDateList[i] = flights[i].getDepartureDate();
                    departureTimeList[i] = flights[i].getDepartureHour();
                    arrivalDateList[i] = flights[i].getArrivalDate();
                    arrivalTimeList[i] = flights[i].getArrivalHour();
                }

                DetailsAdapter adapter = new DetailsAdapter(DetailsActivity.this,
                                                            originCityList,
                                                            destinationCityList,
                                                            airlineList,
                                                            flightCodeList,
                                                            seatTypeList,
                                                            remainingSeatsList,
                                                            departureDateList,
                                                            departureTimeList,
                                                            arrivalDateList,
                                                            arrivalTimeList);
                ListView flightsListView= (ListView) findViewById(R.id.listview_flight);
                flightsListView.setAdapter(adapter);

                Button bookNowButton = (Button) findViewById(R.id.book_now_button);
                bookNowButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = DetailsActivity.this;
                        String message = "You selected to book this flight";
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                    }
                });

            }
        }

    }

}
