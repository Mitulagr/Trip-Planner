package com.mitulagr.tripplanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Adapter_Trips extends RecyclerView.Adapter<Adapter_Trips.ViewHolder> {

    List<Trip> localDataSet;
    DBHandler db;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView place;
        private final TextView date;
        private final TextView nights;
        private final TextView exp;
        private final ImageView img;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            place = (TextView) view.findViewById(R.id.textView4);
            date = (TextView) view.findViewById(R.id.textView5);
            nights = (TextView) view.findViewById(R.id.textView7);
            exp = (TextView) view.findViewById(R.id.textView6);
            img = (ImageView) view.findViewById(R.id.imageView2);
        }

    }

    public Adapter_Trips(Context context) {
        db = new DBHandler(context);
        localDataSet = db.getAllTrips();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        filter(sp.getInt("Sort", 2));
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

//        if(position==5) viewHolder.itemView.setBackgroundResource(R.drawable.rounded); //desert
//        if(position==3) viewHolder.itemView.setBackgroundResource(R.drawable.rounded2); //city
//        if(position==1) viewHolder.itemView.setBackgroundResource(R.drawable.rounded3); //lake
//        if(position==2) viewHolder.itemView.setBackgroundResource(R.drawable.rounded4); //beach
//        if(position==4) viewHolder.itemView.setBackgroundResource(R.drawable.rounded5); //snow
//        if(position==7) viewHolder.itemView.setBackgroundResource(R.drawable.rounded6);
//        if(position==0) viewHolder.itemView.setBackgroundResource(R.drawable.rounded7); //mountain
//        if(position==6) viewHolder.itemView.setBackgroundResource(R.drawable.rounded8);
        Trip trip = localDataSet.get(position);
        viewHolder.itemView.setBackgroundResource(trip.bg);
        viewHolder.place.setText(trip.place);
        viewHolder.date.setText(dispDate(trip.depDate));
        if(trip.nights>0) viewHolder.nights.setText(String.valueOf(trip.nights)+" Nights");
        viewHolder.exp.setText(trip.Hcur.substring(6)+" "+String.valueOf(Math.round(trip.exp)));
        viewHolder.img.setImageResource(trip.imageId);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return db.getTripsCount();
    }

    void filter(int t){
        Collections.sort(localDataSet, new Comparator<Trip>(){
            public int compare(Trip t1, Trip t2){
                if(t1.srno == t2.srno) return 0;

                if(t==2) return t1.srno<t2.srno ? -1 : 1;
                if(t==3) return t1.srno>t2.srno ? -1 : 1;

                int cmp;

                if(t==0) cmp = 1;
                else cmp = -1;

                if(t<2){
                    if(t1.depDate.length()<2 && t2.depDate.length()<2) return t1.srno<t2.srno ? -1*cmp : cmp;
                    if(t1.depDate.length()<2) return cmp;
                    if(t2.depDate.length()<2) return -1*cmp;

                    int y1 = Integer.parseInt(t1.depDate.substring(6));
                    int y2 = Integer.parseInt(t2.depDate.substring(6));

                    if(y1<y2) return -1*cmp;
                    if(y2<y1) return cmp;

                    y1 = Integer.parseInt(t1.depDate.substring(3,5));
                    y2 = Integer.parseInt(t2.depDate.substring(3,5));

                    if(y1<y2) return -1*cmp;
                    if(y2<y1) return cmp;

                    y1 = Integer.parseInt(t1.depDate.substring(0,2));
                    y2 = Integer.parseInt(t2.depDate.substring(0,2));

                    if(y1<y2) return -1*cmp;
                    else return cmp;
                }

                cmp = t1.place.compareToIgnoreCase(t2.place);
                if(t==4) return cmp<0 ? -1 : 1;
                else return cmp<0 ? 1 : -1;
            }
        });
        notifyDataSetChanged();
    }

    String dispDate(String d){
        if(d.length()<2) return "";
        int day = Integer.valueOf(d.substring(0,2));
        int mth = Integer.valueOf(d.substring(3,5));
        String month = " , ";
        if(mth==1) month = " January, ";
        if(mth==2) month = " February, ";
        if(mth==3) month = " March, ";
        if(mth==4) month = " April, ";
        if(mth==5) month = " May, ";
        if(mth==6) month = " June, ";
        if(mth==7) month = " July, ";
        if(mth==8) month = " August, ";
        if(mth==9) month = " September, ";
        if(mth==10) month = " October, ";
        if(mth==11) month = " November, ";
        if(mth==12) month = " December, ";
        return String.valueOf(day)+month+d.substring(6);
    }

}