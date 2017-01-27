package com.example.SAiTAProjectGroup.saita_project;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DetailsAdapter extends ArrayAdapter<String>{

    private final Activity context;

    private final String[] originCityList;
    private final String[] destinationCityList;
    private final String[] airlineList;
    private final String[] flightCodeList;
    private final String[] seatTypeList;
    private final String[] remainingSeatsList;
    private final String[] departureDateList;
    private final String[] departureTimeList;
    private final String[] arrivalDateList;
    private final String[] arrivalTimeList;

    public DetailsAdapter(Activity context,
                          String[] originCityList,
                          String[] destinationCityList,
                          String[] airlineList,
                          String[] flightCodeList,
                          String[] seatTypeList,
                          String[] remainingSeatsList,
                          String[] departureDateList,
                          String[] departureTimeList,
                          String[] arrivalDateList,
                          String[] arrivalTimeList) {
        super(context, R.layout.details_list_item, originCityList);
        this.context = context;
        this.originCityList = originCityList;
        this.destinationCityList = destinationCityList;
        this.airlineList = airlineList;
        this.flightCodeList = flightCodeList;
        this.seatTypeList = seatTypeList;
        this.remainingSeatsList = remainingSeatsList;
        this.departureDateList = departureDateList;
        this.departureTimeList = departureTimeList;
        this.arrivalDateList = arrivalDateList;
        this.arrivalTimeList = arrivalTimeList;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.details_list_item, null, true);

        TextView originCityTextView = (TextView) rowView.findViewById(R.id.textViewOriginCity);
        originCityTextView.setText(originCityList[position]);

        TextView destinationCityTextView = (TextView) rowView.findViewById(R.id.textViewDestinationCity);
        destinationCityTextView.setText(destinationCityList[position]);

        TextView airlineTextView = (TextView) rowView.findViewById(R.id.textViewAirline);
        airlineTextView.setText(airlineList[position]);

        TextView flightCodeTextView = (TextView) rowView.findViewById(R.id.textViewFlightCode);
        flightCodeTextView.setText(flightCodeList[position]);

        TextView seatTypeTextView = (TextView) rowView.findViewById(R.id.textViewSeatType);
        seatTypeTextView.setText(seatTypeList[position]);

        TextView remainingSeatsTextView = (TextView) rowView.findViewById(R.id.textViewRemainingSeats);
        remainingSeatsTextView.setText(remainingSeatsList[position]);

        TextView departureDateTextView = (TextView) rowView.findViewById(R.id.textViewDepartureDate);
        departureDateTextView.setText(departureDateList[position]);

        TextView departureTimeTextView = (TextView) rowView.findViewById(R.id.textViewDepartureTime);
        departureTimeTextView.setText(departureTimeList[position]);

        TextView arrivalDateTextView = (TextView) rowView.findViewById(R.id.textViewArrivalDate);
        arrivalDateTextView.setText(arrivalDateList[position]);

        TextView arrivalTimeTextView = (TextView) rowView.findViewById(R.id.textViewArrivalTime);
        arrivalTimeTextView.setText(arrivalTimeList[position]);

        return rowView;
    }

}
