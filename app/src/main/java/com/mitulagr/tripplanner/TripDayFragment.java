package com.mitulagr.tripplanner;

import static android.content.Context.VIBRATOR_SERVICE;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.common.util.ArrayUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripDayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripDayFragment extends Fragment {

    private static final String DAY = "day";

    private int day;

    public TripDayFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static TripDayFragment newInstance(int day) {
        TripDayFragment fragment = new TripDayFragment();
        Bundle args = new Bundle();
        args.putInt(DAY, day);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            day = getArguments().getInt(DAY);
        }
    }

    private TextView placeName, dayDate, dayDay, description, activityHelp;
    private ImageButton modify;
    private RecyclerView activities;
    private Button addActivity;

    private int id;
    private DBHandler db;
    private Adapter_Activity ada;
    Day d;

    public void refresh(){
        d =  db.getDay(id,day-1);
        placeName.setText(d.city);
        dayDate.setText(dispDate(d.date));
        dayDay.setText(d.day);
        description.setText(d.des);
        ada.localDataSet = db.getAllActivities(id,day);
        ada.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_trip_day, container, false);

        placeName = (TextView) rootView.findViewById(R.id.textView35);
        dayDate = (TextView) rootView.findViewById(R.id.textView36);
        dayDay = (TextView) rootView.findViewById(R.id.textView37);
        description = (TextView) rootView.findViewById(R.id.textView23);
        activityHelp = (TextView) rootView.findViewById(R.id.ActivityHelp);

        modify = (ImageButton) rootView.findViewById(R.id.modifydayplace);

        activities = (RecyclerView) rootView.findViewById(R.id.activities);

        addActivity = (Button) rootView.findViewById(R.id.addActivity);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        id = sp.getInt("Current Trip", 0);
        db = new DBHandler(getContext());

        d = db.getDay(id,day-1);
        placeName.setText(d.city);
        dayDate.setText(dispDate(d.date));
        dayDay.setText(d.day);
        description.setText(d.des);

        //temp = (TextView) rootView.findViewById(R.id.textView25);
        //temp.setText("DAY "+String.valueOf(day));

        //TODO: Show travels of that day too

        /*
        =============================================================================
        Basic Overview
        =============================================================================
         */

        //TODO: Set Placename to main place if day 1 and prev day for rest

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDay(true);
            }
        });

        /*
        =============================================================================
        Description
        =============================================================================
         */

        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDay(false);
            }
        });

        /*
        =============================================================================
        Activities
        =============================================================================
         */


        addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActivity(new Activity("","",-1),true);
            }
        });

//        Activity a1 = new Activity("Theme Park", "go to the theme park till evening", R.drawable.morning);
//        Activity a2 = new Activity("", "Night Animal Safari after theme parkNight Animal Safarier theme park Night Animal Safari after theme park", R.drawable.night);
//        Activity a3 = new Activity("City Tour", "", R.drawable.afternoon);
//        Activity[] activityList = {a1,a2,a3,a1,a2,a3};

        //TODO: Sort Activities based on time phase in database. keep in mind move up down option

        activities.setLayoutManager(new LinearLayoutManager(getActivity()));

        ada = new Adapter_Activity(getContext(),day);

        activities.setAdapter(ada);

        activityHelpUpdate();

        ada.setOnItemLongClickListener(new Adapter_Activity.onRecyclerViewItemLongClickListener() {
            @Override
            public void onItemLongClickListener(View view, int position) {
                showEdit(view,position);
            }
        });

        return rootView;
    }

    void activityHelpUpdate(){
        if(ada.getItemCount()>0) activityHelp.setText("* Long Press to Add Expense, Delete, Modify, or Move");
        else activityHelp.setText("* No Activities Added. Click on 'Add Activity' to Add");
    }

    void editDay(boolean isPlace){
        Dialog curd = new Dialog(getActivity());
        if(isPlace) curd.setContentView(R.layout.editdaycity);
        else curd.setContentView(R.layout.editday);
        curd.getWindow();
        curd.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        curd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        curd.show();

        TextView change;
        TextView Title = (TextView) curd.findViewById(R.id.textViewed);
        EditText Place = (EditText) curd.findViewById(R.id.editTexted);
        Button Ok = (Button) curd.findViewById(R.id.buttoned);

        if(isPlace) {
            Title.setText("Day "+day+" - City Name");
            change = placeName;
        }
        else {
            Title.setText("Day "+day+" - Description");
            change = description;
        }

        Place.setText(change.getText().toString());

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
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
                Day d = db.getDay(id,day-1);
                if(isPlace) d.city = Place.getText().toString();
                else d.des = Place.getText().toString();
                db.updateDay(d);
                change.setText(Place.getText().toString());
                curd.dismiss();
            }
        });
    }

    void showActivity(Activity act, boolean isNew){
        Dialog curd = new Dialog(getActivity());
        curd.setContentView(R.layout.addactivity);
        curd.getWindow();
        curd.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        curd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        curd.show();

        EditText ATitle = (EditText) curd.findViewById(R.id.editTextTextPersonName7);
        EditText ADesc = (EditText) curd.findViewById(R.id.editTextTextPersonName8);
        Button addActivity = (Button) curd.findViewById(R.id.buttonaddact);
        TextView Am = (TextView) curd.findViewById(R.id.textView26);
        TextView Aa = (TextView) curd.findViewById(R.id.textView27);
        TextView Ae = (TextView) curd.findViewById(R.id.textView41);
        TextView An = (TextView) curd.findViewById(R.id.textView43);

        int [] phaseImgs = {R.drawable.morning,R.drawable.afternoon,R.drawable.evening,R.drawable.night};

        final int [] phase = {0};
        TextView [] Ap = {Am,Aa,Ae,An};

        if(!isNew){
            addActivity.setText("Update");
            ATitle.setText(act.title);
            ADesc.setText(act.desc);
            for(int i=0;i<4;i++) {
                if(phaseImgs[i]==act.img){
                    Ap[i].setBackgroundColor(Color.parseColor("#8FE8E0"));
                    Ap[phase[0]].setBackgroundColor(Color.parseColor("#EFEFEF"));
                    phase[0] = i;
                }
            }
        }

        if(act.img==-1) act.img = phaseImgs[0];


        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        AdView mAdHot = curd.findViewById(R.id.adAct);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdHot.loadAd(adRequest);

        for(int i=0;i<4;i++){

            final int I = i;

            Ap[I].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Ap[I].setBackgroundColor(Color.parseColor("#8FE8E0"));
                    Ap[phase[0]].setBackgroundColor(Color.parseColor("#EFEFEF"));
                    phase[0] = I;
                }
            });
        }

        //TODO: If showing after edit button then display current contents

        addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ATitle.getText().toString().length()==0 && ADesc.getText().toString().length()==0){
                    ATitle.requestFocus();
                    ATitle.setError("Tite Mandatory");
                    return;
                }
                //TODO: If only desc enterend then change view of title to gone
                act.title = ATitle.getText().toString();
                act.desc = ADesc.getText().toString();
                act.img = phaseImgs[phase[0]];

                if(isNew){
                    act.id = db.getActivityNewId();
                    act.fid = db.getActivityFid(id,day);
                    db.addActivity(act);
                }
                else db.updateActivity(act);

                ada.localDataSet = db.getAllActivities(id,day);
                ada.notifyDataSetChanged();
                activityHelpUpdate();
                curd.dismiss();
            }
        });
    }

    void showEdit(View view, int pos){
        PopupMenu popup = new PopupMenu(getActivity(),view);
        popup.getMenuInflater()
                .inflate(R.menu.edit1, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.e0:
                        Adapter_ExpenseMain adem = new Adapter_ExpenseMain(getContext(),db.getTrip(id));
                        ExpCat expCat = db.getExpCat(id,"Activities");
                        adem.showExpense(expCat.imageId,
                                db.getExpCatPos(expCat),
                                new Exp(ada.localDataSet.get(pos).title,0.0f,1),
                                true);
                        break;
                    case R.id.e1:
                        db.deleteActivity(db.getActivity(id,day,pos));
                        ada.localDataSet = db.getAllActivities(id,day);
                        ada.notifyDataSetChanged();
                        activityHelpUpdate();
                        break;
                    case R.id.e2:
                        showActivity(db.getActivity(id,day,pos),false);
                        break;
                    case R.id.e3:
                        if(pos==0) break;
                        Activity a1 = db.getActivity(id,day,pos-1);
                        Activity a2 = db.getActivity(id,day,pos);
                        int temp = a1.id;
                        a1.id = a2.id;
                        a2.id = temp;
                        db.updateActivity(a1);
                        db.updateActivity(a2);
                        ada.localDataSet = db.getAllActivities(id,day);
                        ada.notifyDataSetChanged();
                        break;
                    case R.id.e4:
                        if(pos== ada.getItemCount()-1) break;
                        Activity a3 = db.getActivity(id,day,pos);
                        Activity a4 = db.getActivity(id,day,pos+1);
                        int temp1 = a3.id;
                        a3.id = a4.id;
                        a4.id = temp1;
                        db.updateActivity(a3);
                        db.updateActivity(a4);
                        ada.localDataSet = db.getAllActivities(id,day);
                        ada.notifyDataSetChanged();
                        break;
                }

                return true;
            }
        });

        popup.show();
    }

    String dispDate(String d){
        if(d.length()<2) return "";
        int day = Integer.valueOf(d.substring(0,2));
        int mth = Integer.valueOf(d.substring(3,5));
        String month = " , ";
        if(mth==1) month = " January, ";
        if(mth==2) month = " February, ";
        if(mth==3) month = " March, ";
        if(mth==4) month = " April, ";
        if(mth==5) month = " May, ";
        if(mth==6) month = " June, ";
        if(mth==7) month = " July, ";
        if(mth==8) month = " August, ";
        if(mth==9) month = " September, ";
        if(mth==10) month = " October, ";
        if(mth==11) month = " November, ";
        if(mth==12) month = " December, ";
        return String.valueOf(day)+month+d.substring(6);
    }

}

//TODO: In add day title add hints like it can be city name eg. "Mumbai" or city transition eg. "Mumbai - Jaipur" or anything which gives an overview of the day