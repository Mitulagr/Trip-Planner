package com.mitulagr.tripplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class ExpActivity extends AppCompatActivity {

    ExpActivity.onDataChangeListener mDataChangeListener;

    public interface onDataChangeListener {
        void onItemDataChangeListener();
    }

    public void setOnDataChangeListener(ExpActivity.onDataChangeListener DataChange) {
        this.mDataChangeListener = DataChange;
    }

    private Button exit, add;
    private AdView mAdView;
    private Dialog curd;
    private RecyclerView Exps;
    private TextView cat, amt, amth, amtd;
    Adapter_ExpenseCat ade;
    Trip trip;
    ExpCat expCat;
    DBHandler db;
    int id;

    int expcat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp);

        Bundle extras = getIntent().getExtras();
        if (extras!=null) expcat = extras.getInt("pos");

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        id = sp.getInt("Current Trip", 0);
        db = new DBHandler(this);
        trip = db.getTrip(id);
        expCat = db.getExpCat(id,expcat);

        exit = findViewById(R.id.backtotrips2);
        add = findViewById(R.id.backtotrips3);
        mAdView = findViewById(R.id.adExp2);
        Exps = findViewById(R.id.CatExpenses);
        cat = findViewById(R.id.textCatName2);
        amt = findViewById(R.id.TotalExpense);
        amth = findViewById(R.id.textViewEh);
        amtd = findViewById(R.id.textViewEd);

        amt.setText(trip.Hcur.substring(6)+" "+String.valueOf(getAmt(expCat.Amt)));
        amth.setText(trip.Hcur.substring(0,3)+" : "+trip.Hcur.substring(6)+" "+String.valueOf(getAmt(expCat.homAmt)));
        amtd.setText(trip.Dcur.substring(0,3)+" : "+trip.Dcur.substring(6)+" "+String.valueOf(getAmt(expCat.DesAmt)));

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

        //TODO: Make trip with default trip and in exchange function update it if it works otherwise let default remain

        mAdView = findViewById(R.id.adExp2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        /*
        =============================================================================
        ExpenseCat Adapter
        =============================================================================
         */

        Exps.setLayoutManager(new LinearLayoutManager(this));

        ade = new Adapter_ExpenseCat(this,expcat,trip);

        Exps.setAdapter(ade);

        //activityHelpUpdate();

        ade.setOnItemLongClickListener(new Adapter_ExpenseCat.onRecyclerViewItemLongClickListener() {
            @Override
            public void onItemLongClickListener(View view, int position) {
                showEdit(view,position);
            }
        });

        Exps.addItemDecoration(new DividerItemDecoration(Exps.getContext(), DividerItemDecoration.VERTICAL));

        /*
        =============================================================================
        Basic
        =============================================================================
         */

        cat.setText("  "+expCat.category);
        cat.setCompoundDrawablesWithIntrinsicBounds(expCat.imageId,0,0,0);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Adapter_ExpenseMain adem = new Adapter_ExpenseMain(ExpActivity.this,trip);
                adem.showExpense(expCat.imageId,expcat,new Exp("",0.0f,1),true);
                adem.setOnDataChangeListener(new Adapter_ExpenseMain.OnDataChangeListener() {
                    @Override
                    public void onDataChanged() {
                        //if (mDataChangeListener != null) mDataChangeListener.onItemDataChangeListener();
                        //else Toast.makeText(ExpActivity.this, "fhd", Toast.LENGTH_SHORT).show();
                        setResult(1,new Intent());
                        ade.localDataSet = db.getAllExps(id,expcat);
                        ade.notifyDataSetChanged();
                        expCat = db.getExpCat(id,expcat);
                        amt.setText(trip.Hcur.substring(6)+" "+getAmt(expCat.Amt));
                        amth.setText(trip.Hcur.substring(0,3)+" : "+trip.Hcur.substring(6)+" "+getAmt(expCat.homAmt));
                        amtd.setText(trip.Dcur.substring(0,3)+" : "+trip.Dcur.substring(6)+" "+getAmt(expCat.DesAmt));
                    }
                });

            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        /*
        =============================================================================
        Amount
        =============================================================================
         */


    }

    String getAmt(float amt){
        String a;
        a = String.format("%.0f", amt);
        if(Float.parseFloat(a)==amt || amt>1000.0f) return a;
        a = String.format("%.1f", amt);
        if(Float.parseFloat(a)==amt || amt>100.0f) return a;
        return String.format("%.2f", amt);
    }

    void showEdit(View view, int pos){
        PopupMenu popup = new PopupMenu(ExpActivity.this,view);
        popup.getMenuInflater()
                .inflate(R.menu.edit1, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.e1:
                        Exp exp = db.getExp(id,expcat,pos);
                        expCat.Amt = expCat.Amt - exp.Amt;
                        if(exp.isHome==0){
                            expCat.DesAmt = expCat.DesAmt - exp.AmtD;
                            trip.Dexp = trip.Dexp - exp.AmtD;
                        }
                        else{
                            expCat.homAmt = expCat.homAmt - exp.Amt;
                            trip.Hexp = trip.Hexp - exp.Amt;
                        }
                        trip.exp = trip.exp - expCat.Amt;
                        db.deleteExp(db.getExp(id,expcat,pos));
                        ade.localDataSet = db.getAllExps(id,expcat);
                        ade.notifyDataSetChanged();
                        if (mDataChangeListener != null) {
                            mDataChangeListener.onItemDataChangeListener();
                        }
                        //activityHelpUpdate();
                        break;
                    case R.id.e2:
                        //showActivity(db.getActivity(id,day,pos),false);
                        break;
                    case R.id.e3:
//                        if(pos==0) break;
//                        Activity a1 = db.getActivity(id,day,pos-1);
//                        Activity a2 = db.getActivity(id,day,pos);
//                        int temp = a1.id;
//                        a1.id = a2.id;
//                        a2.id = temp;
//                        db.updateActivity(a1);
//                        db.updateActivity(a2);
//                        ada.localDataSet = db.getAllActivities(id,day);
//                        ada.notifyDataSetChanged();
                        break;
                    case R.id.e4:
//                        if(pos== ada.getItemCount()-1) break;
//                        Activity a3 = db.getActivity(id,day,pos);
//                        Activity a4 = db.getActivity(id,day,pos+1);
//                        int temp1 = a3.id;
//                        a3.id = a4.id;
//                        a4.id = temp1;
//                        db.updateActivity(a3);
//                        db.updateActivity(a4);
//                        ada.localDataSet = db.getAllActivities(id,day);
//                        ada.notifyDataSetChanged();
                        break;
                }

                return true;
            }
        });

        popup.show();
    }

}