package com.example.SAiTAProjectGroup.saita_project;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SearchAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private String[] airportList;

    public SearchAdapter(Activity context, String[] airportList) {
        super(context, R.layout.search_list_item, airportList);
        this.context = context;
        this.airportList = airportList;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.search_list_item, null, true);

        TextView airportTextView = (TextView) rowView.findViewById(R.id.airport_label);
        airportTextView.setText(airportList[position]);

        return rowView;
    }

}