package com.mitulagr.tripplanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class Adapter_Notes extends RecyclerView.Adapter<Adapter_Notes.ViewHolder> {

    List<Note> localDataSet;
    Context context;
    int fid;
    DBHandler db;

    public interface LongClickListener{
        public void onItemLongClickListener(View view, int position);
    }

    LongClickListener mItemLongClickListener;
    public void setOnItemLongClickListener(LongClickListener longClickListener){
        mItemLongClickListener = longClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private final TextView title,desc;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.dnT);
            desc = (TextView) view.findViewById(R.id.dnD);
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


    public Adapter_Notes(Context context) {
        db = new DBHandler(context);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        fid = sp.getInt("Current Trip", 0);
        localDataSet = db.getAllNotes(fid);
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.design_notes, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        Note note = localDataSet.get(position);
        if(note.title!="") viewHolder.title.setText(note.title);
        else viewHolder.title.setVisibility(View.GONE);
        if(note.desc!="") viewHolder.desc.setText(note.desc);
        else viewHolder.desc.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}