package com.mitulagr.tripplanner;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class TravelDate implements DatePickerDialog.OnDateSetListener{

    Context context;
    Button dep, arr;

    boolean isDep;
    int[] depret = new int[6];

    private DBHandler db;
    private Trip trip;

    TravelDate(Button dep, Button arr, Context context, TextView depR, TextView arrR){

        this.context = context;
        this.dep = dep;
        this.arr = arr;

        depret[0] = 0;
        depret[1] = 0;

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        db = new DBHandler(context);
        trip = db.getTrip(sp.getInt("Current Trip", 0));
        if(trip.depDate.length()>1){
            depret[0] = Integer.parseInt(trip.depDate.substring(0,2));
            depret[2] = Integer.parseInt(trip.depDate.substring(3,5));
            depret[4] = Integer.parseInt(trip.depDate.substring(6));
        }

        dep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDep = true;
                if(depret[0]==0 && depret[1]!=0) showDatePicker(depret[1],depret[3],depret[5]);
                else showDatePicker(depret[0],depret[2],depret[4]);
            }
        });

        arr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDep = false;
                if(depret[1]==0 && depret[0]!=0) showDatePicker(depret[0],depret[2],depret[4]);
                else showDatePicker(depret[1],depret[3],depret[5]);
            }
        });

        depR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                depret[0] = 0;
                dep.setText(null);
            }
        });

        arrR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                depret[1] = 0;
                arr.setText(null);
            }
        });

    }


    private void showDatePicker(int day, int month, int year) {
        if(day==0){
            day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            month = Calendar.getInstance().get(Calendar.MONTH);
            year = Calendar.getInstance().get(Calendar.YEAR);
        }
        else month = month-1;
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                this,
                year,
                month,
                day
        );
        datePickerDialog.show();
    }


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String date = String.format("%02d/%02d/%04d", i2,i1+1,i);
        if (isDep || depret[0]==0) {
            dep.setText(date);
            depret[0] = i2;
            depret[2] = i1 + 1;
            depret[4] = i;
        }
        if (!isDep || depret[1]==0) {
            arr.setText(date);
            depret[1] = i2;
            depret[3] = i1 + 1;
            depret[5] = i;
        }
    }
}
