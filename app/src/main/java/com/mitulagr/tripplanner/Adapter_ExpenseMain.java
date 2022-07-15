package com.mitulagr.tripplanner;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Adapter_ExpenseMain extends RecyclerView.Adapter<Adapter_ExpenseMain.ViewHolder> {

    private List<ExpCat> localDataSet;
    private List<ExpCat> disp;
    Context context;
    Trip trip;
    int fid;
    DBHandler db;

    public interface OnDataChangeListener{
        public void onDataChanged();
    }

    OnDataChangeListener mOnDataChangeListener;
    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener){
        mOnDataChangeListener = onDataChangeListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView CatName;
        private final TextView CatExpense;
        private final FloatingActionButton CatAdd;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            CatName = (TextView) view.findViewById(R.id.textCatName);
            CatExpense = (TextView) view.findViewById(R.id.textCatExpense);
            CatAdd = (FloatingActionButton) view.findViewById(R.id.fabAdd);
        }

    }


    public Adapter_ExpenseMain(Context context, Trip trip) {

        //this.localDataSet = dataSet;
        //disp = Arrays.asList(dataSet);
        this.trip = trip;
        db = new DBHandler(context);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        fid = sp.getInt("Current Trip", 0);
        disp = db.getAllExpCats(fid);
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.design_expensemain, viewGroup, false);

        FloatingActionButton fab = view.findViewById(R.id.fabAdd);

        final View parent = (View) fab.getParent();
        parent.post(new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                fab.getHitRect(rect);
                rect.top -= 30;
                rect.left -= 20;
                rect.bottom += 30;
                rect.right += 20;
                parent.setTouchDelegate(new TouchDelegate(rect,fab));
            }
        });

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.CatName.setText("  "+disp.get(position).category);
        viewHolder.CatName.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(disp.get(position).imageId),null,null,null);
        viewHolder.CatExpense.setText(trip.Hcur.substring(6)+" "+getAmt(disp.get(position).Amt));

        viewHolder.CatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExpense(context.getDrawable(disp.get(position).imageId),position);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return disp.size();
    }

    String getAmt(float amt){
        String a;
        a = String.format("%.0f", amt);
        if(Float.parseFloat(a)==amt || Float.parseFloat(a)>1000) return a;
        a = String.format("%.1f", amt);
        if(Float.parseFloat(a)==amt) return a;
        return String.valueOf(amt);
    }

    void filter(String text){
        List<ExpCat> temp = new ArrayList<ExpCat>();
        localDataSet = db.getAllExpCats(fid);
        text = text.toLowerCase();
        for(int i=0; i<localDataSet.size(); i++){
            if(localDataSet.get(i).category.toLowerCase().contains(text)){
                temp.add(localDataSet.get(i));
            }
        }
        disp = temp;
        notifyDataSetChanged();
    }

    void showExpense(Drawable img, int pos){
        Dialog curd = new Dialog(context);
        curd.setContentView(R.layout.addexpense);
        curd.getWindow();
        curd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        curd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        curd.show();

        Button eCurHom = (Button) curd.findViewById(R.id.ecurhom);
        Button eCurDes = (Button) curd.findViewById(R.id.ecurdes);
        EditText eTitle = (EditText) curd.findViewById(R.id.etitle);
        TextView eSym = (TextView) curd.findViewById(R.id.esym);
        TextView eAmt = (TextView) curd.findViewById(R.id.eamt);
        Button eDone = (Button) curd.findViewById(R.id.eadd);
        Button [] e = new Button[10];
        e[0] = (Button) curd.findViewById(R.id.e0);
        e[1] = (Button) curd.findViewById(R.id.e1);
        e[2] = (Button) curd.findViewById(R.id.e2);
        e[3] = (Button) curd.findViewById(R.id.e3);
        e[4] = (Button) curd.findViewById(R.id.e4);
        e[5] = (Button) curd.findViewById(R.id.e5);
        e[6] = (Button) curd.findViewById(R.id.e6);
        e[7] = (Button) curd.findViewById(R.id.e7);
        e[8] = (Button) curd.findViewById(R.id.e8);
        e[9] = (Button) curd.findViewById(R.id.e9);
        Button eBack = (Button) curd.findViewById(R.id.eback);
        Button eDot = (Button) curd.findViewById(R.id.edot);

        eSym.setText(trip.Hcur.substring(6));

        if(trip.isHom==1){
            eCurDes.setVisibility(View.GONE);
            eCurHom.setVisibility(View.GONE);
        }
        else{
            eCurDes.setVisibility(View.VISIBLE);
            eCurHom.setVisibility(View.VISIBLE);
            eCurHom.setText(trip.Hcur.substring(0,3));
            eCurDes.setText(trip.Dcur.substring(0,3));
        }

        eDone.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);

        final int [] dot = {-1};
        final String [] amount = {""};
        final Boolean [] isHom = {true};

        eCurHom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eCurHom.setBackground(context.getDrawable(R.drawable.cur1));
                eCurDes.setBackground(context.getDrawable(R.drawable.cur2));
                eCurHom.setTextColor(Color.BLACK);
                eCurDes.setTextColor(Color.DKGRAY);
                // TODO: Set Symbol to Hom
                eSym.setText(trip.Hcur.substring(6));
                isHom[0] = true;
            }
        });

        eCurDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eCurDes.setBackground(context.getDrawable(R.drawable.cur1));
                eCurHom.setBackground(context.getDrawable(R.drawable.cur2));
                eCurDes.setTextColor(Color.BLACK);
                eCurHom.setTextColor(Color.DKGRAY);
                // TODO: Set Symbol to Des
                eSym.setText(trip.Dcur.substring(6));
                isHom[0] = false;
            }
        });


        eDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ExpCat expcat = db.getExpCat(fid,pos);
                //TODO: Convert if DesAmt
                float amt = Float.valueOf(amount[0]);
                //expcat.Amt = expcat.Amt + amt;
//                if(isHom[0]) expcat.homAmt = expcat.homAmt + amt;
//                else expcat.DesAmt = expcat.DesAmt + amt;
                if(isHom[0]) {
                    disp.get(pos).homAmt = disp.get(pos).homAmt + amt;
                    disp.get(pos).Amt = disp.get(pos).Amt + amt;
                    trip.exp = trip.exp + amt;
                    trip.Hexp = trip.Hexp + amt;
                }
                else {
                    disp.get(pos).DesAmt = disp.get(pos).DesAmt + amt;
                    float change = Math.round(amt*trip.rate*100.0f)/100.0f;
                    disp.get(pos).Amt = disp.get(pos).Amt + change;
                    trip.exp = trip.exp + change;
                    trip.Dexp = trip.Dexp + amt;
                }
                db.updateExpCat(disp.get(pos));
                db.updateTrip(trip);
                if(mOnDataChangeListener != null) mOnDataChangeListener.onDataChanged();
                notifyDataSetChanged();
                curd.dismiss();
            }
        });


        for (int i=0; i<10; i++) {
            final int I = i;
            e[I].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(dot[0]>=2) return;
                    amount[0] = amount[0] + String.valueOf(I);
                    eAmt.setText(amount[0]);
                    if(dot[0]>=0) dot[0]++;
                }
            });
        }

        eBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(amount[0].length()>0) amount[0] = amount[0].substring(0,amount[0].length()-1);
                eAmt.setText(amount[0]);
                if(dot[0]>=0) dot[0] = dot[0]-1;
            }
        });

        eDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dot[0]>=0) return;
                amount[0] = amount[0] + ".";
                eAmt.setText(amount[0]);
                dot[0]=0;
            }
        });

    }

}

