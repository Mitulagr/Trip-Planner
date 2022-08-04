package com.mitulagr.tripplanner;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class Adapter_ExpenseMain extends RecyclerView.Adapter<Adapter_ExpenseMain.ViewHolder> {

    List<ExpCat> localDataSet;
    Context context;
    Trip trip;
    int fid;
    DBHandler db;
    String qry = "";

    public interface OnDataChangeListener{
        public void onDataChanged();
    }

    OnDataChangeListener mOnDataChangeListener;
    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener){
        mOnDataChangeListener = onDataChangeListener;
    }

    public interface OnMainClickListener{
        public void onMainClicked(int position);
    }

    OnMainClickListener mOnMainClickListener;
    public void setOnMainClickListener(OnMainClickListener onMainClickListener){
        mOnMainClickListener = onMainClickListener;
    }

    public interface LongClickListener{
        public void onItemLongClickListener(View view, int position);
    }

    LongClickListener mItemLongClickListener;
    public void setOnItemLongClickListener(LongClickListener longClickListener){
        mItemLongClickListener = longClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        private final TextView CatName;
        private final TextView CatExpense;
        private final FloatingActionButton CatAdd;
        private final View CatMain;
        private final View CatPar;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            CatName = (TextView) view.findViewById(R.id.textCatName);
            CatExpense = (TextView) view.findViewById(R.id.textCatExpense);
            CatAdd = (FloatingActionButton) view.findViewById(R.id.fabAdd);
            CatMain = (View) view.findViewById(R.id.mainParent);
            CatPar = (View) view.findViewById(R.id.fabParent);

            //view.setOnLongClickListener(this);
        }

//        @Override
//        public boolean onLongClick(View view) {
//            if(mItemLongClickListener!=null){
//                mItemLongClickListener.onItemLongClickListener(view,getAdapterPosition());
//            }
//            return true;
//        }

//        @Override
//        public boolean onLongClick(View view) {
////            if (mItemLongClickListener != null) {
////                mItemLongClickListener.onItemLongClickListener(view, getAdapterPosition());
////            }
//            editExpense(view,getAdapterPosition());
//            return true;
//        }
    }


    public Adapter_ExpenseMain(Context context, Trip trip) {

        //this.localDataSet = dataSet;
        //disp = Arrays.asList(dataSet);
        this.trip = trip;
        db = new DBHandler(context);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        fid = sp.getInt("Current Trip", 0);
        localDataSet = db.getAllExpCats(fid);
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
        viewHolder.CatName.setText("  "+localDataSet.get(position).category);
        viewHolder.CatName.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(localDataSet.get(position).imageId),null,null,null);
        viewHolder.CatExpense.setText(trip.Hcur.substring(6)+" "+getAmt(localDataSet.get(position).Amt));

        viewHolder.CatPar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExpense(localDataSet.get(position).imageId,
                        db.getExpCatPos(localDataSet.get(position)),
                        new Exp("",0.0f,1),
                        true);
            }
        });

        viewHolder.CatMain.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(mItemLongClickListener!=null){
                    mItemLongClickListener.onItemLongClickListener(view,position);
                }
                return true;
            }
        });

        viewHolder.CatMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnMainClickListener!=null) mOnMainClickListener.onMainClicked(db.getExpCatPos(localDataSet.get(position)));
//                ExpActivity EA = new ExpActivity();
//                ActivityResultLauncher<Intent> aa = context.register
//                ActivityResultLauncher<Intent> startActivityForResult = EA.registerForActivityResult(
//                        new ActivityResultContracts.StartActivityForResult(),
//                        new ActivityResultCallback<ActivityResult>() {
//                            @Override
//                            public void onActivityResult(ActivityResult result) {
//                                localDataSet = db.getAllExpCats(trip.srno);
//                                notifyDataSetChanged();
//                                if (mOnDataChangeListener != null)
//                                    mOnDataChangeListener.onDataChanged();
//                            }
//                        });
//                Intent i = new Intent(context, EA.getClass());
//                i.putExtra("pos",position);
//                context.startActivity(i);
////                ActivityResultLauncher<Intent> activityResultLauncher = registerAdapterDataObserver(
////                        new ActivityResultContracts.StartActivityForResult(),
////                        new ActivityResultCallback<ActivityResult>() {
////                            @Override
////                            public void onActivityResult(ActivityResult result) {
////
////                            }
////                        });
////            }
////        });
            }
        });
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

    void filter(String text){
        qry = text;
        List<ExpCat> temp = new ArrayList<ExpCat>();
        List<ExpCat> all = db.getAllExpCats(fid);
        if(text.length()==0){
            localDataSet = all;
            notifyDataSetChanged();
            return;
        }
        text = text.toLowerCase();
        for(int i=0; i<all.size(); i++){
            if(all.get(i).category.toLowerCase().contains(text)){
                temp.add(all.get(i));
            }
        }
        localDataSet = temp;
        notifyDataSetChanged();
    }

    void showExpense(int img, int pos, Exp exp, boolean isNew){
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

        eDone.setCompoundDrawablesWithIntrinsicBounds(img,0,0,0);

        final int [] dot = {-1};
        final String [] amount = {""};
        final Boolean [] isHom = {true};

        eTitle.setText(exp.title);

        final int [] pisHome = {exp.isHome};

        if(!isNew){
            eDone.setText("Set");
            float amnt;
            if(exp.isHome==0) {
                amnt = exp.AmtD;
                eCurDes.setBackground(context.getDrawable(R.drawable.cur1));
                eCurHom.setBackground(context.getDrawable(R.drawable.cur2));
                eCurDes.setTextColor(Color.BLACK);
                eCurHom.setTextColor(Color.DKGRAY);
                eSym.setText(trip.Dcur.substring(6));
                isHom[0] = false;
            }
            else amnt = exp.Amt;
            if(amnt<0.005f) amount[0] = "0";
            else{
                amount[0] = String.format("%.0f",amnt);
                if(Float.parseFloat(amount[0])==amnt || amnt>1000.0f) ;
                else{
                    amount[0] = String.format("%.1f",amnt);
                    dot[0] = 1;
                    if(Float.parseFloat(amount[0])==amnt || amnt>100.0f) ;
                    else {
                        amount[0] = String.format("%.2f",amnt);
                        dot[0] = 2;
                    }
                }
            }
            eAmt.setText(amount[0]);
        }

        eCurHom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eCurHom.setBackground(context.getDrawable(R.drawable.cur1));
                eCurDes.setBackground(context.getDrawable(R.drawable.cur2));
                eCurHom.setTextColor(Color.BLACK);
                eCurDes.setTextColor(Color.DKGRAY);
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
                eSym.setText(trip.Dcur.substring(6));
                isHom[0] = false;
            }
        });


        eDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(amount[0].length()==0) return;

                ExpCat expcat = db.getExpCat(fid,pos);

                float amt = Float.valueOf(amount[0]);

                //Exp exp = new Exp(eTitle.getText().toString(),amt,isHom[0]?1:0);
                exp.title = eTitle.getText().toString();

                if(!isNew && pisHome[0]==0){
                    trip.exp = trip.exp - exp.Amt;
                    trip.Dexp = trip.Dexp - exp.AmtD;
                    expcat.Amt = expcat.Amt - exp.Amt;
                    expcat.DesAmt = expcat.DesAmt - exp.AmtD;
                }
                if(!isNew && pisHome[0]==1){
                    trip.exp = trip.exp - exp.Amt;
                    trip.Hexp = trip.Hexp - exp.Amt;
                    expcat.Amt = expcat.Amt - exp.Amt;
                    expcat.homAmt = expcat.homAmt - exp.Amt;
                }

                if(isHom[0]) {
                    trip.exp = trip.exp + amt;
                    trip.Hexp = trip.Hexp + amt;
                    expcat.Amt = expcat.Amt + amt;
                    expcat.homAmt = expcat.homAmt + amt;
                    exp.Amt = amt;
                    exp.isHome = 1;
                }
                else {
                    float change = Math.round(amt*trip.rate*100.0f)/100.0f;
                    trip.exp = trip.exp + change;
                    trip.Dexp = trip.Dexp + amt;
                    expcat.Amt = expcat.Amt + change;
                    expcat.DesAmt = expcat.DesAmt + amt;
                    exp.AmtD = amt;
                    exp.Amt = change;
                    exp.isHome = 0;
                }
                db.updateExpCat(expcat);
                db.updateTrip(trip);
                if(isNew){
                    exp.id = db.getExpsCount();
                    exp.fid = expcat.id;
                    db.addExp(exp);
                }
                else db.updateExp(exp);
                if(mOnDataChangeListener != null) mOnDataChangeListener.onDataChanged();
                filter(qry);
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