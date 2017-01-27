package com.example.SAiTAProjectGroup.saita_project;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class FlightsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<ItineraryObject> itinerariesData = new ArrayList<ItineraryObject>();
    private final FlightsAdapterOnClickHandler clickHandler;

    public interface FlightsAdapterOnClickHandler {
        void onClick(ItineraryObject flight);
    }

    public FlightsAdapter(FlightsAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    public class FlightsAdapterViewHolderWithoutReturn extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final TextView airlineView;
        public final TextView priceView;
        public final TextView outboundOriginView;
        public final TextView outboundDepartureView;
        public final TextView outboundDestinationView;
        public final TextView outboundArrivalView;
        public final TextView outboundStopsView;

        public FlightsAdapterViewHolderWithoutReturn(View view) {
            super(view);

            airlineView = (TextView) view.findViewById(R.id.airline);
            priceView = (TextView) view.findViewById(R.id.price);

            outboundOriginView = (TextView) view.findViewById(R.id.outbound_origin);
            outboundDepartureView = (TextView) view.findViewById(R.id.outbound_departure);
            outboundDestinationView = (TextView) view.findViewById(R.id.outbound_destination);
            outboundArrivalView = (TextView) view.findViewById(R.id.outbound_arrival);
            outboundStopsView = (TextView) view.findViewById(R.id.outbound_stops);

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            ItineraryObject itinerary = itinerariesData.get(adapterPosition);
            clickHandler.onClick(itinerary);
        }

    }

    public class FlightsAdapterViewHolderWithReturn extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final TextView airlineView;
        public final TextView priceView;
        public final TextView outboundOriginView;
        public final TextView outboundDepartureView;
        public final TextView outboundDestinationView;
        public final TextView outboundArrivalView;
        public final TextView outboundStopsView;

        public final TextView inboundOriginView;
        public final TextView inboundDepartureView;
        public final TextView inboundDestinationView;
        public final TextView inboundArrivalView;
        public final TextView inboundStopsView;


        public FlightsAdapterViewHolderWithReturn(View view) {
            super(view);

            airlineView = (TextView) view.findViewById(R.id.airline);
            priceView = (TextView) view.findViewById(R.id.price);

            outboundOriginView = (TextView) view.findViewById(R.id.outbound_origin);
            outboundDepartureView = (TextView) view.findViewById(R.id.outbound_departure);
            outboundDestinationView = (TextView) view.findViewById(R.id.outbound_destination);
            outboundArrivalView = (TextView) view.findViewById(R.id.outbound_arrival);
            outboundStopsView = (TextView) view.findViewById(R.id.outbound_stops);

            inboundOriginView = (TextView) view.findViewById(R.id.inbound_origin);
            inboundDepartureView = (TextView) view.findViewById(R.id.inbound_departure);
            inboundDestinationView = (TextView) view.findViewById(R.id.inbound_destination);
            inboundArrivalView = (TextView) view.findViewById(R.id.inbound_arrival);
            inboundStopsView = (TextView) view.findViewById(R.id.inbound_stops);

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            ItineraryObject itinerary = itinerariesData.get(adapterPosition);
            clickHandler.onClick(itinerary);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (itinerariesData.get(position).getInboundFlights().isEmpty()){
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        if (viewType == 0) {
            int layoutIdForListItem = R.layout.flights_list_item_without_return;
            View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
            return new FlightsAdapterViewHolderWithoutReturn(view);
        } else if (viewType == 1) {
            int layoutIdForListItem = R.layout.flights_list_item_with_return;
            View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
            return new FlightsAdapterViewHolderWithReturn(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        ItineraryObject itinerary = itinerariesData.get(position);

        if (holder.getItemViewType() == 0) {
            FlightsAdapterViewHolderWithoutReturn viewHolder0 = (FlightsAdapterViewHolderWithoutReturn)holder;

            String airlineString = itinerary.getAllAirlinesString();
            viewHolder0.airlineView.setText(airlineString);

            String priceString = itinerary.getTotalPrice() + Utilities.currencySymbol; //TODO
            viewHolder0.priceView.setText(priceString);

            // Outbound Flights
            String originString = itinerary.getOutboundFlights().get(0).getOrigin();
            viewHolder0.outboundOriginView.setText(originString);

            String departureString = itinerary.getOutboundFlights().get(0).getDepartureDate() + ", " + itinerary.getOutboundFlights().get(0).getDepartureHour();
            viewHolder0.outboundDepartureView.setText(departureString);

            String destinationString = itinerary.getOutboundFlights().get(itinerary.getOutboundFlights().size()-1).getDestination();
            viewHolder0.outboundDestinationView.setText(destinationString);

            String arrivalString = itinerary.getOutboundFlights().get(itinerary.getOutboundFlights().size()-1).getArrivalDate() + ", " + itinerary.getOutboundFlights().get(itinerary.getOutboundFlights().size()-1).getArrivalHour();
            viewHolder0.outboundArrivalView.setText(arrivalString);

            // TODO (επιπλέον): Να χρησιμοποιηθούν μεταβλητές από το αρχείο string.xml
            String stopsString = "Stops: ";
            int stops = itinerary.getOutboundStops();
            if (stops == 0) {
                stopsString = stopsString.concat("Direct");
            } else {
                stopsString = stopsString.concat(Integer.toString(stops));
            }
            viewHolder0.outboundStopsView.setText(stopsString);

        } else {
            FlightsAdapterViewHolderWithReturn viewHolder1 = (FlightsAdapterViewHolderWithReturn)holder;

            String airlineString = itinerary.getAllAirlinesString();
            viewHolder1.airlineView.setText(airlineString);

            String priceString = itinerary.getTotalPrice() + Utilities.currencySymbol; //TODO
            viewHolder1.priceView.setText(priceString);

            // Outbound Flights
            String originString = itinerary.getOutboundFlights().get(0).getOrigin();
            viewHolder1.outboundOriginView.setText(originString);

            String departureString = itinerary.getOutboundFlights().get(0).getDepartureDate() + ", " + itinerary.getOutboundFlights().get(0).getDepartureHour();
            viewHolder1.outboundDepartureView.setText(departureString);

            String destinationString = itinerary.getOutboundFlights().get(itinerary.getOutboundFlights().size()-1).getDestination();
            viewHolder1.outboundDestinationView.setText(destinationString);

            String arrivalString = itinerary.getOutboundFlights().get(itinerary.getOutboundFlights().size()-1).getArrivalDate() + ", " + itinerary.getOutboundFlights().get(itinerary.getOutboundFlights().size()-1).getArrivalHour();
            viewHolder1.outboundArrivalView.setText(arrivalString);

            // TODO (επιπλέον): Να χρησιμοποιηθούν μεταβλητές από το αρχείο string.xml
            String stopsString = "Stops: ";
            int stops = itinerary.getOutboundStops();
            if (stops == 0) {
                stopsString = stopsString.concat("Direct");
            } else {
                stopsString = stopsString.concat(Integer.toString(stops));
            }
            viewHolder1.outboundStopsView.setText(stopsString);

            // Inbound Flights
            String inboundOriginString = itinerary.getInboundFlights().get(0).getOrigin();
            viewHolder1.inboundOriginView.setText(inboundOriginString);

            String inboundDepartureString = itinerary.getInboundFlights().get(0).getDepartureDate() + ", " + itinerary.getInboundFlights().get(0).getDepartureHour();
            viewHolder1.inboundDepartureView.setText(inboundDepartureString);

            String inboundDestinationString = itinerary.getInboundFlights().get(itinerary.getInboundFlights().size()-1).getDestination();
            viewHolder1.inboundDestinationView.setText(inboundDestinationString);

            String inboundArrivalString = itinerary.getInboundFlights().get(itinerary.getInboundFlights().size()-1).getArrivalDate() + ", " + itinerary.getInboundFlights().get(itinerary.getInboundFlights().size()-1).getArrivalHour();
            viewHolder1.inboundArrivalView.setText(inboundArrivalString);

            // TODO (επιπλέον): Να χρησιμοποιηθούν μεταβλητές από το αρχείο string.xml
            String inboundStopsString = "Stops: ";
            int inboundStops = itinerary.getInboundStops();
            if (inboundStops == 0) {
                inboundStopsString = inboundStopsString.concat("Direct");
            } else {
                inboundStopsString = inboundStopsString.concat(Integer.toString(inboundStops));
            }
            viewHolder1.inboundStopsView.setText(inboundStopsString);

        }

    }

    @Override
    public int getItemCount() {
        if (null == itinerariesData) return 0;
        return itinerariesData.size();
    }

    public void setItinerariesData(ArrayList<ItineraryObject> itinerariesData) {
        this.itinerariesData = itinerariesData;
        notifyDataSetChanged();
    }

}
