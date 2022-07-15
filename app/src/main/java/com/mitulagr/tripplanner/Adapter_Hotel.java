package com.mitulagr.tripplanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class Adapter_Hotel extends RecyclerView.Adapter<Adapter_Hotel.ViewHolder> {

    private Hotel[] localDataSet;

    private Adapter_Hotel.onRecyclerViewItemLongClickListener mItemLongClickListener;

    public void setOnItemLongClickListener(Adapter_Hotel.onRecyclerViewItemLongClickListener mItemLongClickListener) {
        this.mItemLongClickListener = mItemLongClickListener;
    }

    public interface onRecyclerViewItemLongClickListener {
        void onItemLongClickListener(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private final TextView city,name,nights;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            city = (TextView) view.findViewById(R.id.hotCity);
            name = (TextView) view.findViewById(R.id.hotName);
            nights = (TextView) view.findViewById(R.id.hotNights);

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

    public Adapter_Hotel(Hotel[] hots) {
        localDataSet = hots;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.design_hotel, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.city.setText(localDataSet[position].city);
        viewHolder.name.setText(localDataSet[position].name);
        viewHolder.nights.setText(String.valueOf(localDataSet[position].nights));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }

}