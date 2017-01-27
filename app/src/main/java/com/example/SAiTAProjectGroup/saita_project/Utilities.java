package com.example.SAiTAProjectGroup.saita_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Utilities {

    public static String currencySymbol;

    public static String getAirportCode (String airportString) {
        String airportCode = "";

        String[] airport = airportString.split("\\[");
        airportCode = airport[1].substring(0, airport.length + 1);

        return airportCode;
    }

    public static boolean isValidReturnDate (String departureDateString, String returnDateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date departureDate = format.parse(departureDateString);
            Date returnDate = format.parse(returnDateString);
            if (departureDate.equals(returnDate) || departureDate.before(returnDate)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getPassengersString(int adultsNumber, int childrenNumber, int infantsNumber) {
        String passengersString = "";

        if (adultsNumber != 0) {
            passengersString = passengersString.concat("Adults: " + adultsNumber + "  ");
        }
        if (childrenNumber != 0) {
            passengersString = passengersString.concat("Children: " + childrenNumber + "  ");
        }
        if (infantsNumber != 0) {
            passengersString = passengersString.concat("Infants: " + infantsNumber);
        }

        return passengersString;
    }

    public static String getAdultsNumberString(String passengersString) {
        String adultsString = "0";
        if (passengersString.contains("Adults")) {
            String[] passengers = passengersString.split(" ");
            adultsString = passengers[1];
        }
        return adultsString;
    }

    public static String getChildrenNumberString(String passengersString) {
        String childrenString = "0";
        if (passengersString.contains("Children")) {
            String[] passengers = passengersString.split(" ");
            for (int i=0 ; i<passengers.length ; i++) {
                if (passengers[i].contains("Children")) {
                    childrenString = passengers[i+1];
                }
            }
        }
        return childrenString;
    }

    public static String getInfantsNumberString(String passengersString) {
        String infantsString = "0";
        if (passengersString.contains("Infants")) {
            String[] passengers = passengersString.split(" ");
            for (int i=0 ; i<passengers.length ; i++) {
                if (passengers[i].contains("Infants")) {
                    infantsString = passengers[i+1];
                }
            }
        }
        return infantsString;
    }

    public static ArrayList<ItineraryObject> getFlightsDataFromJson(String flightsJsonStr)
            throws JSONException {
        ArrayList<ItineraryObject> parsedItinerariesData = null;

        // Names of the JSON objects that need to be extracted
        final String AS_STATUS = "status";
        final String AS_RESULTS = "results";
        final String AS_ITINERARIES = "itineraries";
        final String AS_OUTBOUND = "outbound";
        final String AS_INBOUND = "inbound";
        final String AS_FLIGHTS = "flights";
        final String AS_DEPARTS_AT = "departs_at";
        final String AS_ARRIVES_AT = "arrives_at";
        final String AS_ORIGIN = "origin";
        final String AS_DESTINATION = "destination";
        final String AS_AIRPORT = "airport";
        final String AS_OPERATING_AIRLINE = "operating_airline";
        final String AS_FLIGHT_NUMBER = "flight_number";
        final String AS_BOOKING_INFO = "booking_info";
        final String AS_TRAVEL_CLASS = "travel_class";
        final String AS_SEATS_REMAINING = "seats_remaining";
        final String AS_FARE = "fare";
        final String AS_TOTAL_PRICE = "total_price";

        JSONObject flightsJson = new JSONObject(flightsJsonStr);

        if (flightsJson.has(AS_STATUS)) {
            int errorCode = flightsJson.getInt(AS_STATUS);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_BAD_REQUEST:
                    //No results found
                    return null;
                default:
                    //Server probably down
                    return null;
            }
        }

        JSONArray flightsArray = flightsJson.getJSONArray(AS_RESULTS);
        parsedItinerariesData = new ArrayList<ItineraryObject>();

        for(int i=0 ; i<flightsArray.length() ; i++) {
            JSONObject flightResult = flightsArray.getJSONObject(i);

            if (flightResult.has(AS_ITINERARIES)) {

                JSONArray itineraries = flightResult.getJSONArray(AS_ITINERARIES);
                for (int j=0 ; j<itineraries.length() ; j++) {

                    //Total price
                    String totalPrice = flightResult.getJSONObject(AS_FARE).getString(AS_TOTAL_PRICE);

                    ArrayList<FlightObject> outboundFlightsList = new ArrayList<FlightObject>();
                    ArrayList<FlightObject> inboundFlightsList = new ArrayList<FlightObject>();

                    //Outbound Flights
                    JSONObject outbound = itineraries.getJSONObject(j).getJSONObject(AS_OUTBOUND);

                    if (outbound.has(AS_FLIGHTS)) {
                        JSONArray outboundFlights = outbound.getJSONArray(AS_FLIGHTS);

                        for (int k=0 ; k<outboundFlights.length() ; k++) {
                            String outboundAirline = outboundFlights.getJSONObject(k).getString(AS_OPERATING_AIRLINE);
                            String outboundOrigin = outboundFlights.getJSONObject(k).getJSONObject(AS_ORIGIN).getString(AS_AIRPORT);
                            String outboundDestination = outboundFlights.getJSONObject(k).getJSONObject(AS_DESTINATION).getString(AS_AIRPORT);
                            String outboundDeparture = outboundFlights.getJSONObject(k).getString(AS_DEPARTS_AT);
                            String outboundArrival = outboundFlights.getJSONObject(k).getString(AS_ARRIVES_AT);
                            String outboundSeatsRemaining = outboundFlights.getJSONObject(k).getJSONObject(AS_BOOKING_INFO).getString(AS_SEATS_REMAINING);
                            String outboundFlightNumber = outboundFlights.getJSONObject(k).getString(AS_FLIGHT_NUMBER);
                            String outboundTravelClass = outboundFlights.getJSONObject(k).getJSONObject(AS_BOOKING_INFO).getString(AS_TRAVEL_CLASS);

                            FlightObject outboundFlight = new FlightObject(outboundAirline,
                                    outboundOrigin,
                                    outboundDestination,
                                    outboundDeparture,
                                    outboundArrival,
                                    outboundSeatsRemaining,
                                    outboundFlightNumber,
                                    outboundTravelClass);
                            outboundFlightsList.add(outboundFlight);
                        }


                        //Inbound Flights
                        if (itineraries.getJSONObject(j).has(AS_INBOUND)) {
                            JSONObject inbound = itineraries.getJSONObject(j).getJSONObject(AS_INBOUND);

                            if (inbound.has(AS_FLIGHTS)) {
                                JSONArray inboundFlights = inbound.getJSONArray(AS_FLIGHTS);

                                for (int k=0 ; k<inboundFlights.length() ; k++) {
                                    String inboundAirline = inboundFlights.getJSONObject(k).getString(AS_OPERATING_AIRLINE);
                                    String inboundOrigin = inboundFlights.getJSONObject(k).getJSONObject(AS_ORIGIN).getString(AS_AIRPORT);
                                    String inboundDestination = inboundFlights.getJSONObject(k).getJSONObject(AS_DESTINATION).getString(AS_AIRPORT);
                                    String inboundDeparture = inboundFlights.getJSONObject(k).getString(AS_DEPARTS_AT);
                                    String inboundArrival = inboundFlights.getJSONObject(k).getString(AS_ARRIVES_AT);
                                    String inboundSeatsRemaining = inboundFlights.getJSONObject(k).getJSONObject(AS_BOOKING_INFO).getString(AS_SEATS_REMAINING);
                                    String inboundFlightNumber = inboundFlights.getJSONObject(k).getString(AS_FLIGHT_NUMBER);
                                    String inboundTravelClass = inboundFlights.getJSONObject(k).getJSONObject(AS_BOOKING_INFO).getString(AS_TRAVEL_CLASS);

                                    FlightObject inboundFlight = new FlightObject(inboundAirline,
                                            inboundOrigin,
                                            inboundDestination,
                                            inboundDeparture,
                                            inboundArrival,
                                            inboundSeatsRemaining,
                                            inboundFlightNumber,
                                            inboundTravelClass);
                                    inboundFlightsList.add(inboundFlight);
                                }

                            }
                        }
                    }
                    parsedItinerariesData.add(new ItineraryObject(outboundFlightsList, inboundFlightsList, totalPrice));
                }
            }
        }

        return parsedItinerariesData;
    }

    public static String getAirlineDataFromJson(String airlineJsonStr)
            throws JSONException {
        // Names of the JSON objects that need to be extracted
        final String IC_RESPONSE = "response";
        final String IC_NAME = "name";

        JSONObject airlineJson = new JSONObject(airlineJsonStr);
        JSONArray responseArray = airlineJson.getJSONArray(IC_RESPONSE);
        JSONObject responseObject = responseArray.getJSONObject(0);
        String airline = responseObject.getString(IC_NAME);

        return airline;
    }

    public static ArrayList<String> getAirportsDataFromJson(String airportsJsonStr)
            throws JSONException {
        ArrayList<String> airports = new ArrayList<String>();

        // Names of the JSON objects that need to be extracted
        final String ASA_LABEL = "label";

        JSONArray airportsJson = new JSONArray(airportsJsonStr);
        for (int i=0 ; i<airportsJson.length() ; i++) {
            JSONObject airportObject = airportsJson.getJSONObject(i);
            airports.add(airportObject.getString(ASA_LABEL));
        }

        return airports;
    }

    public static void setCurrencySymbol(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        // TODO (επιπλέον): Να χρησιμοποιηθούν μεταβλητές από το αρχείο string.xml
        String currency = prefs.getString("preferred_currency", "EUR");

        // TODO (επιπλέον): Να χρησιμοποιηθούν μεταβλητές από το αρχείο string.xml
        if (currency.equals("EUR")) {
            currencySymbol = " €";
        } else if (currency.equals("USD")) {
            currencySymbol = " $";
        }
    }

}
