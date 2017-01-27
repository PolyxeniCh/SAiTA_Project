package com.example.SAiTAProjectGroup.saita_project;

import java.io.Serializable;
import java.util.ArrayList;

public class ItineraryObject implements Serializable{

    private ArrayList<FlightObject> outboundFlights = new ArrayList<FlightObject>();
    private ArrayList<FlightObject> inboundFlights = new ArrayList<FlightObject>();
    private String totalPrice;

    public ItineraryObject(ArrayList<FlightObject> outboundFlights,
                           ArrayList<FlightObject> inboundFlights,
                           String totalPrice) {

        this.outboundFlights = outboundFlights;
        this.inboundFlights = inboundFlights;
        this.totalPrice = totalPrice;

    }

    public String getAllAirlinesString() {
        String airlines = outboundFlights.get(0).getAirline();

        //Outbound airlines
        for (int i=1 ; i<outboundFlights.size() ; i++) {
            if (!airlines.contains(outboundFlights.get(i).getAirline())) {
                airlines = airlines.concat(", " + outboundFlights.get(i).getAirline());
            }
        }

        //Inbound Airlines
        if (!inboundFlights.isEmpty()) {
            for (FlightObject f : inboundFlights) {
                if (!airlines.contains(f.getAirline())) {
                    airlines = airlines.concat(", " + f.getAirline());
                }
            }
        }

        return airlines;
    }

    public int getOutboundStops() {
        return outboundFlights.size() - 1;
    }

    public int getInboundStops() {
        return inboundFlights.size() - 1;
    }

    // ---- GETTERS ----

    public ArrayList<FlightObject> getOutboundFlights() {
        return outboundFlights;
    }

    public ArrayList<FlightObject> getInboundFlights() {
        return inboundFlights;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

}
