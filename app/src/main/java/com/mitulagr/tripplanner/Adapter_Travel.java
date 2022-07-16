package com.mitulagr.tripplanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Adapter_Travel extends RecyclerView.Adapter<Adapter_Travel.ViewHolder> {

    List<Travel> localDataSet;

    private Adapter_Travel.onRecyclerViewItemLongClickListener mItemLongClickListener;

    public void setOnItemLongClickListener(Adapter_Travel.onRecyclerViewItemLongClickListener mItemLongClickListener) {
        this.mItemLongClickListener = mItemLongClickListener;
    }

    public interface onRecyclerViewItemLongClickListener {
        void onItemLongClickListener(View view, int position);
    }

    String dateNow = getDate();


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private final ImageView icon;
        private final TextView from,fromDate,fromTime,to,toDate,toTime,no;

        public ViewHolder(View view) {
            super(view);

            icon = (ImageView) view.findViewById(R.id.traIcon);
            from = (TextView) view.findViewById(R.id.traFrom);
            fromDate = (TextView) view.findViewById(R.id.traFromDate);
            fromTime = (TextView) view.findViewById(R.id.traFromTime);
            to = (TextView) view.findViewById(R.id.traTo);
            toDate = (TextView) view.findViewById(R.id.traToDate);
            toTime = (TextView) view.findViewById(R.id.traToTime);
            no = (TextView) view.findViewById(R.id.traNo);

            view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            if (mItemLongClickListener != null) {
                mItemLongClickListener.onItemLongClickListener(view, getAdapterPosition());
            }
            return true;
        }

    }

    public Adapter_Travel(Context context) {
        DBHandler db = new DBHandler(context);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int fid = sp.getInt("Current Trip", 0);
        localDataSet = db.getAllTravels(fid);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.design_travel, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        if(localDataSet.get(position).from_date.length()==0 && localDataSet.get(position).to_date.length()==0){
            viewHolder.fromDate.setVisibility(View.GONE);
            viewHolder.toDate.setVisibility(View.GONE);
        }

        if(localDataSet.get(position).from_time.length()==0 && localDataSet.get(position).to_time.length()==0){
            viewHolder.fromTime.setVisibility(View.GONE);
            viewHolder.toTime.setVisibility(View.GONE);
        }

        if(dateNow.equals(localDataSet.get(position).from_date)) {
            viewHolder.from.setTextColor(Color.parseColor("#FF0000"));
            viewHolder.fromDate.setTextColor(Color.parseColor("#FF0000"));
            viewHolder.fromTime.setTextColor(Color.parseColor("#FF0000"));
            viewHolder.to.setTextColor(Color.parseColor("#FF0000"));
            viewHolder.toDate.setTextColor(Color.parseColor("#FF0000"));
            viewHolder.toTime.setTextColor(Color.parseColor("#FF0000"));
            viewHolder.no.setTextColor(Color.parseColor("#FF0000"));
        }

        viewHolder.icon.setImageResource(localDataSet.get(position).img);
        viewHolder.from.setText(localDataSet.get(position).from);
        viewHolder.fromDate.setText(localDataSet.get(position).from_date);
        viewHolder.fromTime.setText(localDataSet.get(position).from_time);
        viewHolder.to.setText(localDataSet.get(position).to);
        viewHolder.toDate.setText(localDataSet.get(position).to_date);
        viewHolder.toTime.setText(localDataSet.get(position).to_time);
        viewHolder.no.setText(localDataSet.get(position).no);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public String getDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return df.format(c);
    }

}