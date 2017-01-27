package com.example.SAiTAProjectGroup.saita_project;

import java.io.Serializable;

public class FlightObject implements Serializable{

    private String airline;
    private String origin;
    private String destination;
    private String departure;
    private String arrival;
    private String seatsRemaining;
    private String flightNumber;
    private String travelClass;

    public FlightObject (String airline,
                         String origin,
                         String destination,
                         String departure,
                         String arrival,
                         String seatsRemaining,
                         String flightNumber,
                         String travelClass) {

        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.departure = departure;
        this.arrival = arrival;
        this.seatsRemaining = seatsRemaining;
        this.flightNumber = flightNumber;
        this.travelClass = travelClass;

    }

    public String getDepartureDate(){
        String date =  departure.substring(0, departure.length()-6);
        return date;
    }

    public String getDepartureHour (){
        String hour =  departure.substring(11);
        return hour;
    }

    public String getArrivalDate(){
        String date =  arrival.substring(0, arrival.length()-6);
        return date;
    }

    public String getArrivalHour (){
        String hour =  arrival.substring(11);
        return hour;
    }

    // ---- GETTERS ----

    public String getAirline() {
        return airline;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getSeatsRemaining() {
        return seatsRemaining;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getTravelClass() {
        return travelClass;
    }

    // ---- SETTERS ----

    public void setAirline(String airline) {
        this.airline = airline;
    }

}
