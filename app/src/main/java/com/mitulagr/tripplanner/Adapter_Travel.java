package com.mitulagr.tripplanner;

import android.graphics.Color;
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
import java.util.Locale;

public class Adapter_Travel extends RecyclerView.Adapter<Adapter_Travel.ViewHolder> {

    private Travel[] localDataSet;

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

    public Adapter_Travel(Travel[] tras) {
        localDataSet = tras;
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

        if(localDataSet[position].from_date==null && localDataSet[position].to_date==null){
            viewHolder.fromDate.setVisibility(View.GONE);
            viewHolder.toDate.setVisibility(View.GONE);
        }

        if(localDataSet[position].from_time==null && localDataSet[position].to_time==null){
            viewHolder.fromTime.setVisibility(View.GONE);
            viewHolder.toTime.setVisibility(View.GONE);
        }

        if(dateNow.equals(localDataSet[position].from_date)) {
            viewHolder.from.setTextColor(Color.parseColor("#FF0000"));
            viewHolder.fromDate.setTextColor(Color.parseColor("#FF0000"));
            viewHolder.fromTime.setTextColor(Color.parseColor("#FF0000"));
            viewHolder.to.setTextColor(Color.parseColor("#FF0000"));
            viewHolder.toDate.setTextColor(Color.parseColor("#FF0000"));
            viewHolder.toTime.setTextColor(Color.parseColor("#FF0000"));
            viewHolder.no.setTextColor(Color.parseColor("#FF0000"));
        }

        // TODO: call update adapter everywhere after any corresponding change (eg modify delete add)

        viewHolder.icon.setImageResource(localDataSet[position].img);
        viewHolder.from.setText(localDataSet[position].from);
        if(localDataSet[position].from_date!=null) viewHolder.fromDate.setText(localDataSet[position].from_date);
        if(localDataSet[position].from_time!=null) viewHolder.fromTime.setText(localDataSet[position].from_time);
        viewHolder.to.setText(localDataSet[position].to);
        if(localDataSet[position].to_date!=null) viewHolder.toDate.setText(localDataSet[position].to_date);
        if(localDataSet[position].to_time!=null) viewHolder.toTime.setText(localDataSet[position].to_time);
        if(localDataSet[position].no!=null) viewHolder.no.setText(localDataSet[position].no);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }

    public String getDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return df.format(c);
    }

}