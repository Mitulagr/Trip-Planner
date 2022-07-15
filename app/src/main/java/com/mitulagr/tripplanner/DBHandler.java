package com.mitulagr.tripplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME = "DBTrips";

    private static final String TABLE_Trip = "TT";
    private static final String TABLE_ExpCat = "TE";
    private static final String TABLE_Day = "TD";
    private static final String TABLE_Hotel = "TH";
    private static final String TABLE_Travel = "TTr";
    private static final String TABLE_Activity = "TA";

    // Table Trip - Columns
    private static final String TT_ID = "TT_id";
    private static final String TT_place = "TT_place";
    private static final String TT_img = "TT_img";
    private static final String TT_bg = "TT_bg";

    // Table ExpCat - Columns
    private static final String TE_ID = "TE_id";
    private static final String TE_FID = "TE_fid";
    private static final String TE_category = "TE_category";
    private static final String TE_img = "TE_img";
    private static final String TE_amt = "TE_amt";
    private static final String TE_homamt = "TE_homamt";
    private static final String TE_desamt = "TE_desamt";

    DBHandler(Context context){
        super(context,DATABASE_NAME,null,1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TT = "CREATE TABLE "+TABLE_Trip+"("
                +TT_ID+" INTEGER PRIMARY KEY,"
                +TT_place+" TEXT,"
                +TT_img+" INTEGER,"
                +TT_bg+" INTEGER"
                +")";
        db.execSQL(CREATE_TT);

        String CREATE_TE = "CREATE TABLE "+TABLE_ExpCat+"("
                +TE_ID+" INTEGER PRIMARY KEY,"
                +TE_FID+" INTEGER,"
                +TE_category+" TEXT,"
                +TE_img+" INTEGER,"
                +TE_amt+" REAL,"
                +TE_homamt+" REAL,"
                +TE_desamt+" REAL,"
                +"FOREIGN KEY ("+TE_FID+") REFERENCES "+TABLE_Trip+" ("+TT_ID+"))";
        db.execSQL(CREATE_TE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_Trip);
        onCreate(db);
    }

    /*
    =============================================================================
    Table Trip
    =============================================================================
     */

    public void addTrip(Trip trip){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TT_ID, trip.srno);
        values.put(TT_place, trip.place);
        values.put(TT_img, trip.imageId);
        values.put(TT_bg, trip.bg);

        db.insert(TABLE_Trip, null, values);
        db.close();

        ExpOnCreate(trip.srno);
    }

    public Trip getTrip(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_Trip, new String[]{
                TT_ID,TT_place,TT_img,TT_bg},
                TT_ID+"=?", new String[]{String.valueOf(id)},
                null,null,null,null);

        if (c!=null) c.moveToFirst();

        Trip trip = new Trip(
                c.getInt(c.getColumnIndexOrThrow(TT_ID)),
                c.getString(c.getColumnIndexOrThrow(TT_place)),
                c.getInt(c.getColumnIndexOrThrow(TT_img)));

        return trip;
    }

    public List<Trip> getAllTrips(){
        List<Trip> tripList = new ArrayList<Trip>();

        String selectQuery = "SELECT * FROM "+TABLE_Trip;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);

        if (c!=null && c.moveToFirst()){
            do {
                Trip trip = new Trip(
                        c.getInt(c.getColumnIndexOrThrow(TT_ID)),
                        c.getString(c.getColumnIndexOrThrow(TT_place)),
                        c.getInt(c.getColumnIndexOrThrow(TT_img)));
                tripList.add(trip);
            } while (c.moveToNext());
        }

        return tripList;
    }

    public int getTripsCount(){
        String countQuery = "SELECT * FROM "+TABLE_Trip;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery,null);

        return c.getCount();
    }

    public int updateTrip(Trip trip){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TT_ID, trip.srno);
        values.put(TT_place, trip.place);
        values.put(TT_img, trip.imageId);
        values.put(TT_bg, trip.bg);

        return db.update(TABLE_Trip,values,TT_ID+"=?",new String[]{String.valueOf(trip.srno)});
    }

    public void deleteTrip(Trip trip){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Trip, TT_ID+"=?",new String[]{String.valueOf(trip.srno)});
        db.close();
    }


    /*
    =============================================================================
    Table ExpCat
    =============================================================================
     */

    public void ExpOnCreate(int fid){
        int n = getExpCatsCount();
        int im[] = new int [8];
        im[0] = context.getResources().getIdentifier("ic_baseline_flight_24", "drawable", "com.mitulagr.tripplanner");
        im[1] = context.getResources().getIdentifier("ic_baseline_local_police_24", "drawable", "com.mitulagr.tripplanner");
        im[2] = context.getResources().getIdentifier("ic_baseline_hotel_24", "drawable", "com.mitulagr.tripplanner");
        im[3] = context.getResources().getIdentifier("ic_baseline_restaurant_24", "drawable", "com.mitulagr.tripplanner");
        im[4] = context.getResources().getIdentifier("ic_baseline_local_taxi_24", "drawable", "com.mitulagr.tripplanner");
        im[5] = context.getResources().getIdentifier("ic_baseline_hiking_24", "drawable", "com.mitulagr.tripplanner");
        im[6] = context.getResources().getIdentifier("ic_baseline_shopping_bag_24", "drawable", "com.mitulagr.tripplanner");
        im[7] = context.getResources().getIdentifier("ic_baseline_more_horiz_24", "drawable", "com.mitulagr.tripplanner");
        String ic[] = {"Travel","Visa","Hotel","Food","Transport","Activities","Shopping","Other"};
        for(int i=0; i<8; i++){
            ExpCat expcat = new ExpCat(ic[i],im[i]);
            expcat.id = n+i;
            expcat.fid = fid;
            addExpCat(expcat);
        }
    }

    public void addExpCat(ExpCat expcat){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TE_ID, expcat.id);
        values.put(TE_FID, expcat.fid);
        values.put(TE_category, expcat.category);
        values.put(TE_img, expcat.imageId);
        values.put(TE_amt, expcat.Amt);
        values.put(TE_homamt, expcat.homAmt);
        values.put(TE_desamt, expcat.DesAmt);

        db.insert(TABLE_ExpCat, null, values);
        db.close();
    }

    public ExpCat getExpCat(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_ExpCat, new String[]{
                        TE_ID,TE_FID,TE_category,TE_img,TE_amt,TE_homamt,TE_desamt},
                TE_ID+"=?", new String[]{String.valueOf(id)},
                null,null,null,null);

        if (c!=null) c.moveToFirst();

        ExpCat expcat = new ExpCat(
                c.getString(c.getColumnIndexOrThrow(TE_category)),
                c.getInt(c.getColumnIndexOrThrow(TE_img)));

        expcat.id = c.getInt(c.getColumnIndexOrThrow(TE_ID));
        expcat.fid = c.getInt(c.getColumnIndexOrThrow(TE_FID));
        expcat.Amt = c.getFloat(c.getColumnIndexOrThrow(TE_amt));
        expcat.homAmt = c.getFloat(c.getColumnIndexOrThrow(TE_homamt));
        expcat.DesAmt = c.getFloat(c.getColumnIndexOrThrow(TE_desamt));

        return expcat;
    }

    public List<ExpCat> getAllExpCats(int fid){
        List<ExpCat> expcatList = new ArrayList<ExpCat>();

        String selectQuery = "SELECT * FROM "+TABLE_ExpCat+" WHERE "+TE_FID+" = "+fid;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);

        if (c!=null && c.moveToFirst()){
            do {
                ExpCat expcat = new ExpCat(
                        c.getString(c.getColumnIndexOrThrow(TE_category)),
                        c.getInt(c.getColumnIndexOrThrow(TE_img)));
                expcat.id = c.getInt(c.getColumnIndexOrThrow(TE_ID));
                expcat.fid = c.getInt(c.getColumnIndexOrThrow(TE_FID));
                expcat.Amt = c.getFloat(c.getColumnIndexOrThrow(TE_amt));
                expcat.homAmt = c.getFloat(c.getColumnIndexOrThrow(TE_homamt));
                expcat.DesAmt = c.getFloat(c.getColumnIndexOrThrow(TE_desamt));
                expcatList.add(expcat);
            } while (c.moveToNext());
        }

        return expcatList;
    }

    public ExpCat getExpCat(int fid, int pos) {
        List<ExpCat> expcatList = getAllExpCats(fid);
        return expcatList.get(pos);
    }

    public int getExpCatsCount(int fid){
        String countQuery = "SELECT * FROM "+TABLE_ExpCat+" WHERE "+TE_FID+" = "+fid;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery,null);

        return c.getCount();
    }

    public int getExpCatsCount(){
        String countQuery = "SELECT * FROM "+TABLE_ExpCat;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery,null);

        return c.getCount();
    }

    public int updateExpCat(ExpCat expcat){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TE_ID, expcat.id);
        values.put(TE_FID, expcat.fid);
        values.put(TE_category, expcat.category);
        values.put(TE_img, expcat.imageId);
        values.put(TE_amt, expcat.Amt);
        values.put(TE_homamt, expcat.homAmt);
        values.put(TE_desamt, expcat.DesAmt);

        return db.update(TABLE_ExpCat,values,TE_ID+"=?",new String[]{String.valueOf(expcat.id)});
    }

    public void deleteExpCat(ExpCat expcat){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ExpCat, TE_ID+"=?",new String[]{String.valueOf(expcat.id)});
        db.close();
    }



}