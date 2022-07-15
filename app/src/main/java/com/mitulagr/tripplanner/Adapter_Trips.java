package com.mitulagr.tripplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class Adapter_Trips extends RecyclerView.Adapter<Adapter_Trips.ViewHolder> {

    //private List<Trip> localDataSet;
    DBHandler db;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView place;
        private final ImageView img;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            place = (TextView) view.findViewById(R.id.textView4);
            img = (ImageView) view.findViewById(R.id.imageView2);
        }

    }

    public Adapter_Trips(Context context) {
        db = new DBHandler(context);
        //localDataSet = db.getAllTrips();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.design_trips, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
//        if(localDataSet[position].bg==0) viewHolder.itemView.setBackgroundResource(R.drawable.rounded);
//        if(localDataSet[position].bg==1) viewHolder.itemView.setBackgroundResource(R.drawable.rounded2);
//        if(localDataSet[position].bg==2) viewHolder.itemView.setBackgroundResource(R.drawable.rounded3);
//        if(localDataSet[position].bg==3) viewHolder.itemView.setBackgroundResource(R.drawable.rounded4);
//        if(localDataSet[position].bg==4) viewHolder.itemView.setBackgroundResource(R.drawable.rounded5);
//        if(localDataSet[position].bg==5) viewHolder.itemView.setBackgroundResource(R.drawable.rounded6);
//        if(localDataSet[position].bg==6) viewHolder.itemView.setBackgroundResource(R.drawable.rounded7);
//        if(localDataSet[position].bg==7) viewHolder.itemView.setBackgroundResource(R.drawable.rounded8);
        if(position==5) viewHolder.itemView.setBackgroundResource(R.drawable.rounded); //desert
        if(position==3) viewHolder.itemView.setBackgroundResource(R.drawable.rounded2); //city
        if(position==1) viewHolder.itemView.setBackgroundResource(R.drawable.rounded3); //lake
        if(position==2) viewHolder.itemView.setBackgroundResource(R.drawable.rounded4); //beach
        if(position==4) viewHolder.itemView.setBackgroundResource(R.drawable.rounded5); //snow
        if(position==7) viewHolder.itemView.setBackgroundResource(R.drawable.rounded6);
        if(position==0) viewHolder.itemView.setBackgroundResource(R.drawable.rounded7); //mountain
        if(position==6) viewHolder.itemView.setBackgroundResource(R.drawable.rounded8);
        Trip trip = db.getTrip(position);
        viewHolder.place.setText(trip.place);
        viewHolder.img.setImageResource(trip.imageId);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return db.getTripsCount();
    }

}