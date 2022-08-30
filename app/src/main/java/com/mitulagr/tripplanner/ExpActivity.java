package com.mitulagr.tripplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    ImageButton delete, modify;
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
        delete = findViewById(R.id.deletetrip2);
        modify = findViewById(R.id.modifydayplace2);

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
        mAdView.setVisibility(View.GONE);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ExpActivity.this);
                alert.setTitle("Delete Category "+expCat.category);
                alert.setMessage("All Expenses of Category - "+expCat.category+" will be lost. Are you sure you want to delete it?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteExpCat(expCat);
                        finish();
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCategory();
            }
        });

    }

    void editCategory(){
        Dialog curd = new Dialog(this);
        curd.setContentView(R.layout.editdaycity);
        curd.getWindow();
        curd.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        curd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        curd.show();

        TextView Title = (TextView) curd.findViewById(R.id.textViewed);
        EditText Place = (EditText) curd.findViewById(R.id.editTexted);
        Button Ok = (Button) curd.findViewById(R.id.buttoned);

        Title.setText("Category Name");

        Place.setText(expCat.category);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        AdView mAdEd = curd.findViewById(R.id.adEd);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdEd.loadAd(adRequest);

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expCat.category = Place.getText().toString();
                db.updateExpCat(expCat);
                cat.setText("  "+expCat.category);
                curd.dismiss();
            }
        });
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

    void showEdit(View view, int pos){
        PopupMenu popup = new PopupMenu(ExpActivity.this,view);
        popup.getMenuInflater()
                .inflate(R.menu.edit2, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.e1:
                        Exp exp = db.getExp(id,expcat,pos);
                        expCat.Amt = expCat.Amt - exp.Amt;
                        trip.exp = trip.exp - exp.Amt;
                        if(exp.isHome==0){
                            expCat.DesAmt = expCat.DesAmt - exp.AmtD;
                            trip.Dexp = trip.Dexp - exp.AmtD;
                        }
                        else{
                            expCat.homAmt = expCat.homAmt - exp.Amt;
                            trip.Hexp = trip.Hexp - exp.Amt;
                        }
                        db.deleteExp(exp);
                        db.updateExpCat(expCat);
                        db.updateTrip(trip);
                        ade.localDataSet = db.getAllExps(id,expcat);
                        ade.notifyDataSetChanged();
                        amt.setText(trip.Hcur.substring(6)+" "+getAmt(expCat.Amt));
                        amth.setText(trip.Hcur.substring(0,3)+" : "+trip.Hcur.substring(6)+" "+getAmt(expCat.homAmt));
                        amtd.setText(trip.Dcur.substring(0,3)+" : "+trip.Dcur.substring(6)+" "+getAmt(expCat.DesAmt));
                        break;
                    case R.id.e2:
                        Adapter_ExpenseMain adem = new Adapter_ExpenseMain(ExpActivity.this,trip);
                        adem.showExpense(expCat.imageId,expcat,db.getExp(id,expcat,pos),false);
                        adem.setOnDataChangeListener(new Adapter_ExpenseMain.OnDataChangeListener() {
                            @Override
                            public void onDataChanged() {
                                setResult(1,new Intent());
                                ade.localDataSet = db.getAllExps(id,expcat);
                                ade.notifyDataSetChanged();
                                expCat = db.getExpCat(id,expcat);
                                amt.setText(trip.Hcur.substring(6)+" "+getAmt(expCat.Amt));
                                amth.setText(trip.Hcur.substring(0,3)+" : "+trip.Hcur.substring(6)+" "+getAmt(expCat.homAmt));
                                amtd.setText(trip.Dcur.substring(0,3)+" : "+trip.Dcur.substring(6)+" "+getAmt(expCat.DesAmt));
                            }
                        });
                        break;
                    case R.id.e3:
                        if(pos==0) break;
                        Exp e1 = db.getExp(id,expcat,pos-1);
                        Exp e2 = db.getExp(id,expcat,pos);
                        int temp = e1.id;
                        e1.id = e2.id;
                        e2.id = temp;
                        db.updateExp(e1);
                        db.updateExp(e2);
                        ade.localDataSet = db.getAllExps(id,expcat);
                        ade.notifyDataSetChanged();
                        break;
                    case R.id.e4:
                        if(pos== ade.getItemCount()-1) break;
                        Exp e3 = db.getExp(id,expcat,pos);
                        Exp e4 = db.getExp(id,expcat,pos+1);
                        int temp1 = e3.id;
                        e3.id = e4.id;
                        e4.id = temp1;
                        db.updateExp(e3);
                        db.updateExp(e4);
                        ade.localDataSet = db.getAllExps(id,expcat);
                        ade.notifyDataSetChanged();
                        break;
                }

                return true;
            }
        });

        popup.show();
    }

}