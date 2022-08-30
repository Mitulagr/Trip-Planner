package com.mitulagr.tripplanner;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class Adapter_TripNav extends RecyclerView.Adapter<Adapter_TripNav.ViewHolder> {

    int n;
    int selected = -1;

    public interface LongClickListener{
        public void onItemLongClickListener(View view, int position);
    }

    LongClickListener mItemLongClickListener;
    public void setOnItemLongClickListener(Adapter_TripNav.LongClickListener longClickListener){
        mItemLongClickListener = longClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private final TextView day;
        private final View div;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            day = (TextView) view.findViewById(R.id.textViewDayNav);
            div = (View) view.findViewById(R.id.divider6);
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

    public Adapter_TripNav(int num) {
        n = num;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.design_tripnav, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.day.setText("DAY "+(position+1));
        if(position==selected){
            viewHolder.day.setTextColor(Color.parseColor("#11E334"));
            viewHolder.div.setVisibility(View.VISIBLE);
        }
        else{
            viewHolder.day.setTextColor(Color.parseColor("#DBDBDB"));
            viewHolder.div.setVisibility(View.INVISIBLE);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return n;
    }

    public void updateColor(int clr) {
        selected = clr;
        notifyDataSetChanged();
    }

}