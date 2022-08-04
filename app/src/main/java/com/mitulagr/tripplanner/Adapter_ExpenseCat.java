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

public class Adapter_ExpenseCat extends RecyclerView.Adapter<Adapter_ExpenseCat.ViewHolder> {

    List<Exp> localDataSet;
    Trip trip;
    private onRecyclerViewItemLongClickListener mItemLongClickListener;

    public void setOnItemLongClickListener(onRecyclerViewItemLongClickListener mItemLongClickListener) {
        this.mItemLongClickListener = mItemLongClickListener;
    }

    public interface onRecyclerViewItemLongClickListener {
        void onItemLongClickListener(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private final TextView sr,title,amth,amtd;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            sr = (TextView) view.findViewById(R.id.textView44);
            title = (TextView) view.findViewById(R.id.textCatName3);
            amth = (TextView) view.findViewById(R.id.textCatExpense2);
            amtd = (TextView) view.findViewById(R.id.textView46);
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

    public Adapter_ExpenseCat(Context context, int expcat, Trip trip) {
        DBHandler db = new DBHandler(context);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int srno = sp.getInt("Current Trip", 0);
        localDataSet = db.getAllExps(srno,expcat);
        this.trip = trip;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.design_exp, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Exp exp = localDataSet.get(position);
        viewHolder.sr.setText(String.valueOf(position+1)+".");
        viewHolder.title.setText(exp.title);
        viewHolder.amth.setText(trip.Hcur.substring(6)+" "+getAmt(exp.Amt));
        if(exp.isHome==0){
            viewHolder.amtd.setText(trip.Dcur.substring(6)+" "+getAmt(exp.AmtD));
            viewHolder.amtd.setVisibility(View.VISIBLE);
        }
        else{
            viewHolder.amtd.setVisibility(View.GONE);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    String getAmt(float amt){
        if(amt<0.005f) return "0";
        String a;
        a = String.format("%.0f", amt);
        if(Float.parseFloat(a)==amt || amt>1000.0f) return a;
        a = String.format("%.1f", amt);
        if(Float.parseFloat(a)==amt || amt>100.0f) return a;
        return String.format("%.2f", amt);
    }

}