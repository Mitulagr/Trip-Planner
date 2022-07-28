package com.mitulagr.tripplanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter_Activity extends RecyclerView.Adapter<Adapter_Activity.ViewHolder> {

    List<Activity> localDataSet;
    private onRecyclerViewItemLongClickListener mItemLongClickListener;

    public void setOnItemLongClickListener(onRecyclerViewItemLongClickListener mItemLongClickListener) {
        this.mItemLongClickListener = mItemLongClickListener;
    }

    public interface onRecyclerViewItemLongClickListener {
        void onItemLongClickListener(View view, int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private final TextView title,desc;
        private final ImageView img;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            title = (TextView) view.findViewById(R.id.textView8);
            desc = (TextView) view.findViewById(R.id.textView24);
            img = (ImageView) view.findViewById(R.id.imageView8);

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

    public Adapter_Activity(Context context, int day) {
        DBHandler db = new DBHandler(context);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int srno = sp.getInt("Current Trip", 0);
        localDataSet = db.getAllActivities(srno,day);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.design_activity, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Activity act = localDataSet.get(position);
        if(act.title!="") viewHolder.title.setText(act.title);
        else viewHolder.title.setVisibility(View.GONE);
        if(act.desc!="") viewHolder.desc.setText(act.desc);
        else viewHolder.desc.setVisibility(View.GONE);
        viewHolder.img.setImageResource(act.img);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}