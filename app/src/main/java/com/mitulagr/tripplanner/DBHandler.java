package com.mitulagr.tripplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME = "DBTrips";

    private static final String TABLE_Trip = "TT";
    private static final String TABLE_ExpCat = "TE";
    private static final String TABLE_Exp = "TEx";
    private static final String TABLE_Day = "TD";
    private static final String TABLE_Hotel = "TH";
    private static final String TABLE_Travel = "TTr";
    private static final String TABLE_Activity = "TA";
    private static final String TABLE_Notes = "TN";

    // Table Trip - Columns
    private static final String TT_ID = "TT_id";
    private static final String TT_place = "TT_place";
    private static final String TT_img = "TT_img";
    private static final String TT_bg = "TT_bg";
    private static final String TT_rate = "TT_rate";
    private static final String TT_depDate = "TT_depDate";
    private static final String TT_retDate = "TT_retDatee";
    private static final String TT_nights = "TT_nights";
    private static final String TT_exp = "TT_exp";
    private static final String TT_hexp = "TT_hexp";
    private static final String TT_dexp = "TT_dexp";
    private static final String TT_isHome = "TT_isHome";
    private static final String TT_hcur = "TT_hcur";
    private static final String TT_dcur = "TT_dcur";

    // Table ExpCat - Columns
    private static final String TE_ID = "TE_id";
    private static final String TE_FID = "TE_fid";
    private static final String TE_category = "TE_category";
    private static final String TE_img = "TE_img";
    private static final String TE_amt = "TE_amt";
    private static final String TE_homamt = "TE_homamt";
    private static final String TE_desamt = "TE_desamt";

    // Table Hotel - Columns
    private static final String TH_ID = "TH_id";
    private static final String TH_FID = "TH_fid";
    private static final String TH_city = "TH_category";
    private static final String TH_name = "TH_img";
    private static final String TH_nights = "TH_amt";

    // Table Travel - Columns
    private static final String TTr_ID = "TTr_id";
    private static final String TTr_FID = "TTr_fid";
    private static final String TTr_img = "TTr_img";
    private static final String TTr_type = "TTr_type";
    private static final String TTr_no = "TTr_no";
    private static final String TTr_from = "TTr_from";
    private static final String TTr_fromDate = "TTr_fromDate";
    private static final String TTr_fromTime = "TTr_fromTime";
    private static final String TTr_to = "TTr_to";
    private static final String TTr_toDate = "TTr_toDate";
    private static final String TTr_toTime = "TTr_toTime";

    // Table Day - Columns
    private static final String TD_ID = "TD_id";
    private static final String TD_FID = "TD_fid";
    private static final String TD_city = "TD_city";
    private static final String TD_date = "TD_date";
    private static final String TD_day = "TD_day";
    private static final String TD_des = "TD_des";

    // Table Activity - Columns
    private static final String TA_ID = "TA_id";
    private static final String TA_FID = "TA_fid";
    private static final String TA_title = "TA_title";
    private static final String TA_des = "TA_des";
    private static final String TA_img = "TA_img";

    // Table Exp - Columns
    private static final String TEx_ID = "TEx_id";
    private static final String TEx_FID = "TEx_fid";
    private static final String TEx_title = "TEx_title";
    private static final String TEx_amt = "TEx_amt";
    private static final String TEx_amtd = "TEx_amtd";
    private static final String TEx_isHome = "TEx_isHome";

    // Table Notes - Columns
    private static final String TN_ID = "TN_id";
    private static final String TN_FID = "TN_fid";
    private static final String TN_title = "TN_title";
    private static final String TN_des = "TN_des";

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
                +TT_bg+" INTEGER,"
                +TT_rate+" REAL,"
                +TT_depDate+" TEXT,"
                +TT_retDate+" TEXT,"
                +TT_nights+" INTEGER,"
                +TT_exp +" REAL,"
                +TT_hexp+" REAL,"
                +TT_dexp+" REAL,"
                +TT_isHome+" INTEGER,"
                +TT_hcur+" TEXT,"
                +TT_dcur+" TEXT"
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

        String CREATE_TH = "CREATE TABLE "+TABLE_Hotel+"("
                +TH_ID+" INTEGER PRIMARY KEY,"
                +TH_FID+" INTEGER,"
                +TH_city+" TEXT,"
                +TH_name+" TEXT,"
                +TH_nights+" INTEGER,"
                +"FOREIGN KEY ("+TH_FID+") REFERENCES "+TABLE_Trip+" ("+TT_ID+"))";
        db.execSQL(CREATE_TH);

        String CREATE_TTr = "CREATE TABLE "+TABLE_Travel+"("
                +TTr_ID+" INTEGER PRIMARY KEY,"
                +TTr_FID+" INTEGER,"
                +TTr_img+" INTEGER,"
                +TTr_type+" TEXT,"
                +TTr_no+" TEXT,"
                +TTr_from+" TEXT,"
                +TTr_fromDate+" TEXT,"
                +TTr_fromTime+" TEXT,"
                +TTr_to+" TEXT,"
                +TTr_toDate+" TEXT,"
                +TTr_toTime+" TEXT,"
                +"FOREIGN KEY ("+TTr_FID+") REFERENCES "+TABLE_Trip+" ("+TT_ID+"))";
        db.execSQL(CREATE_TTr);

        String CREATE_TD = "CREATE TABLE "+TABLE_Day+"("
                +TD_ID+" INTEGER PRIMARY KEY,"
                +TD_FID+" INTEGER,"
                +TD_city+" TEXT,"
                +TD_date+" TEXT,"
                +TD_day+" TEXT,"
                +TD_des+" TEXT,"
                +"FOREIGN KEY ("+TD_FID+") REFERENCES "+TABLE_Trip+" ("+TT_ID+"))";
        db.execSQL(CREATE_TD);

        String CREATE_TA = "CREATE TABLE "+TABLE_Activity+"("
                +TA_ID+" INTEGER PRIMARY KEY,"
                +TA_FID+" INTEGER,"
                +TA_title+" TEXT,"
                +TA_des+" TEXT,"
                +TA_img+" INTEGER,"
                +"FOREIGN KEY ("+TA_FID+") REFERENCES "+TABLE_Day+" ("+TD_ID+"))";
        db.execSQL(CREATE_TA);

        String CREATE_TEx = "CREATE TABLE "+TABLE_Exp+"("
                +TEx_ID+" INTEGER PRIMARY KEY,"
                +TEx_FID+" INTEGER,"
                +TEx_title+" TEXT,"
                +TEx_amt+" REAL,"
                +TEx_amtd+" REAL,"
                +TEx_isHome+" INTEGER,"
                +"FOREIGN KEY ("+TEx_FID+") REFERENCES "+TABLE_Exp+" ("+TEx_ID+"))";
        db.execSQL(CREATE_TEx);

        String CREATE_TN = "CREATE TABLE "+TABLE_Notes+"("
                +TN_ID+" INTEGER PRIMARY KEY,"
                +TN_FID+" INTEGER,"
                +TN_title+" TEXT,"
                +TN_des+" TEXT,"
                +"FOREIGN KEY ("+TN_FID+") REFERENCES "+TABLE_Trip+" ("+TT_ID+"))";
        db.execSQL(CREATE_TN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_Trip);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ExpCat);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_Hotel);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_Travel);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_Day);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_Activity);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_Exp);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_Notes);
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
        values.put(TT_rate, trip.rate);
        values.put(TT_depDate, trip.depDate);
        values.put(TT_retDate, trip.retDate);
        values.put(TT_nights, trip.nights);
        values.put(TT_exp, trip.exp);
        values.put(TT_hexp, trip.Hexp);
        values.put(TT_dexp, trip.Dexp);
        values.put(TT_isHome, trip.isHom);
        values.put(TT_hcur, trip.Hcur);
        values.put(TT_dcur, trip.Dcur);

        db.insert(TABLE_Trip, null, values);
        db.close();

        ExpOnCreate(trip.srno);
        DayOnCreate(trip);
    }

    public Trip getTrip(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_Trip, new String[]{
                TT_ID,TT_place,TT_img,TT_bg,TT_rate,TT_depDate,TT_retDate,
                TT_nights,TT_exp,TT_hexp,TT_dexp,TT_isHome,TT_hcur,TT_dcur},
                TT_ID+"=?", new String[]{String.valueOf(id)},
                null,null,null,null);

        if (c!=null) c.moveToFirst();

        Trip trip = new Trip(
                c.getInt(c.getColumnIndexOrThrow(TT_ID)),
                c.getString(c.getColumnIndexOrThrow(TT_place)),
                c.getInt(c.getColumnIndexOrThrow(TT_img)));

        trip.bg = c.getInt(c.getColumnIndexOrThrow(TT_bg));
        trip.rate = c.getFloat(c.getColumnIndexOrThrow(TT_rate));
        trip.depDate = c.getString(c.getColumnIndexOrThrow(TT_depDate));
        trip.retDate = c.getString(c.getColumnIndexOrThrow(TT_retDate));
        trip.nights = c.getInt(c.getColumnIndexOrThrow(TT_nights));
        trip.exp = c.getFloat(c.getColumnIndexOrThrow(TT_exp));
        trip.Hexp = c.getFloat(c.getColumnIndexOrThrow(TT_hexp));
        trip.Dexp = c.getFloat(c.getColumnIndexOrThrow(TT_dexp));
        trip.isHom = c.getInt(c.getColumnIndexOrThrow(TT_isHome));
        trip.Hcur = c.getString(c.getColumnIndexOrThrow(TT_hcur));
        trip.Dcur = c.getString(c.getColumnIndexOrThrow(TT_dcur));

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
                trip.rate = c.getFloat(c.getColumnIndexOrThrow(TT_rate));
                trip.depDate = c.getString(c.getColumnIndexOrThrow(TT_depDate));
                trip.retDate = c.getString(c.getColumnIndexOrThrow(TT_retDate));
                trip.nights = c.getInt(c.getColumnIndexOrThrow(TT_nights));
                trip.exp = c.getFloat(c.getColumnIndexOrThrow(TT_exp));
                trip.Hexp = c.getFloat(c.getColumnIndexOrThrow(TT_hexp));
                trip.Dexp = c.getFloat(c.getColumnIndexOrThrow(TT_dexp));
                trip.isHom = c.getInt(c.getColumnIndexOrThrow(TT_isHome));
                trip.Hcur = c.getString(c.getColumnIndexOrThrow(TT_hcur));
                trip.Dcur = c.getString(c.getColumnIndexOrThrow(TT_dcur));
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

    public int getTripsNewId(){
        String selectQuery = "SELECT * FROM "+TABLE_Trip;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        int nid = -1;
        if (c!=null && c.moveToFirst()){
            do {
                nid = c.getInt(c.getColumnIndexOrThrow(TT_ID));
            } while (c.moveToNext());
        }
        return nid+1;
    }

    public int updateTrip(Trip trip){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TT_ID, trip.srno);
        values.put(TT_place, trip.place);
        values.put(TT_img, trip.imageId);
        values.put(TT_bg, trip.bg);
        values.put(TT_rate, trip.rate);
        values.put(TT_depDate, trip.depDate);
        values.put(TT_retDate, trip.retDate);
        values.put(TT_nights, trip.nights);
        values.put(TT_exp, trip.exp);
        values.put(TT_hexp, trip.Hexp);
        values.put(TT_dexp, trip.Dexp);
        values.put(TT_isHome, trip.isHom);
        values.put(TT_hcur, trip.Hcur);
        values.put(TT_dcur, trip.Dcur);

        return db.update(TABLE_Trip,values,TT_ID+"=?",new String[]{String.valueOf(trip.srno)});
    }

    public void deleteTrip(Trip trip){
        SQLiteDatabase db = this.getWritableDatabase();
        List<ExpCat> ExpCatList = getAllExpCats(trip.srno);
        for(int i=0;i<ExpCatList.size();i++){
            db.delete(TABLE_Exp, TEx_FID+"=?",new String[]{String.valueOf(ExpCatList.get(i).id)});
        }
        List<Day> DayList = getAllDays(trip.srno);
        for(int i=0;i<DayList.size();i++){
            db.delete(TABLE_Activity, TA_FID+"=?",new String[]{String.valueOf(DayList.get(i).id)});
        }
        db.delete(TABLE_ExpCat, TE_FID+"=?",new String[]{String.valueOf(trip.srno)});
        db.delete(TABLE_Day, TD_FID+"=?",new String[]{String.valueOf(trip.srno)});
        db.delete(TABLE_Hotel, TH_FID+"=?",new String[]{String.valueOf(trip.srno)});
        db.delete(TABLE_Travel, TTr_FID+"=?",new String[]{String.valueOf(trip.srno)});
        db.delete(TABLE_Trip, TT_ID+"=?",new String[]{String.valueOf(trip.srno)});
        db.close();
    }

//    public void deleteTrip(int srno){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_Trip, TT_ID+"=?",new String[]{String.valueOf(srno)});
//        db.close();
//    }


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

    public ExpCat getExpCat(int fid, String cat) {
        List<ExpCat> expcatList = getAllExpCats(fid);
        for(int i=0;i<expcatList.size();i++){
            if(expcatList.get(i).category.equals(cat)) return expcatList.get(i);
        }
        if(cat.equals("Other")) return getExpCat(fid,0);
        return getExpCat(fid,"Other");
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

    public int getExpCatNewId(){
            String selectQuery = "SELECT * FROM "+TABLE_ExpCat;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery(selectQuery,null);
            int nid = -1;
            if (c!=null && c.moveToFirst()){
                do {
                    nid = c.getInt(c.getColumnIndexOrThrow(TE_ID));
                } while (c.moveToNext());
            }
            return nid+1;
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

    //TODO: also delete related exps. For Level 2+ deletions confirmation dialog
    public void deleteExpCat(ExpCat expcat){
        SQLiteDatabase db = this.getWritableDatabase();
        Trip trip = getTrip(expcat.fid);
        trip.exp = trip.exp - expcat.Amt;
        if(trip.isHom==0) {
            trip.Dexp = trip.Dexp - expcat.DesAmt;
            trip.Hexp = trip.Hexp - expcat.homAmt;
        }
        updateTrip(trip);
        db.delete(TABLE_Exp, TEx_FID+"=?",new String[]{String.valueOf(expcat.id)});
        db.delete(TABLE_ExpCat, TE_ID+"=?",new String[]{String.valueOf(expcat.id)});
        db.close();
    }

    public int getExpCatPos(ExpCat expCat){
        List<ExpCat> expCatList = getAllExpCats(expCat.fid);
        for(int i=0;i<expCatList.size();i++) {
            if (expCatList.get(i).id == expCat.id) return i;
        }
        return 0;
    }


    /*
    =============================================================================
    Table Hotel
    =============================================================================
     */

    public void addHotel(Hotel hotel){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TH_ID, hotel.id);
        values.put(TH_FID, hotel.fid);
        values.put(TH_city, hotel.city);
        values.put(TH_name, hotel.name);
        values.put(TH_nights, hotel.nights);

        db.insert(TABLE_Hotel, null, values);
        db.close();
    }

    public Hotel getHotel(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_Hotel, new String[]{
                        TH_ID,TH_FID,TH_city,TH_name,TH_nights},
                TH_ID+"=?", new String[]{String.valueOf(id)},
                null,null,null,null);

        if (c!=null) c.moveToFirst();

        Hotel hotel = new Hotel(
                c.getString(c.getColumnIndexOrThrow(TH_city)),
                c.getString(c.getColumnIndexOrThrow(TH_name)),
                c.getInt(c.getColumnIndexOrThrow(TH_nights)));

        hotel.id = c.getInt(c.getColumnIndexOrThrow(TH_ID));
        hotel.fid = c.getInt(c.getColumnIndexOrThrow(TH_FID));

        return hotel;
    }

    public List<Hotel> getAllHotels(int fid){
        List<Hotel> hotelList = new ArrayList<Hotel>();

        String selectQuery = "SELECT * FROM "+TABLE_Hotel
                +" WHERE "+TH_FID+" = "+fid
                +" ORDER BY "+TH_ID+" ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);

        if (c!=null && c.moveToFirst()){
            do {
                Hotel hotel = new Hotel(
                        c.getString(c.getColumnIndexOrThrow(TH_city)),
                        c.getString(c.getColumnIndexOrThrow(TH_name)),
                        c.getInt(c.getColumnIndexOrThrow(TH_nights)));
                hotel.id = c.getInt(c.getColumnIndexOrThrow(TH_ID));
                hotel.fid = c.getInt(c.getColumnIndexOrThrow(TH_FID));
                hotelList.add(hotel);
            } while (c.moveToNext());
        }

        return hotelList;
    }

    public Hotel getHotel(int fid, int pos) {
        List<Hotel> hotelList = getAllHotels(fid);
        return hotelList.get(pos);
    }

    public int getHotelsCount(int fid){
        String countQuery = "SELECT * FROM "+TABLE_Hotel+" WHERE "+TH_FID+" = "+fid;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery,null);

        return c.getCount();
    }

    public int getHotelsCount(){
        String countQuery = "SELECT * FROM "+TABLE_Hotel;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery,null);

        return c.getCount();
    }

    public int getHotelNewId(){
        String selectQuery = "SELECT * FROM "+TABLE_Hotel;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        int nid = -1;
        if (c!=null && c.moveToFirst()){
            do {
                nid = c.getInt(c.getColumnIndexOrThrow(TH_ID));
            } while (c.moveToNext());
        }
        return nid+1;
    }

    public int updateHotel(Hotel hotel){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TH_ID, hotel.id);
        values.put(TH_FID, hotel.fid);
        values.put(TH_city, hotel.city);
        values.put(TH_name, hotel.name);
        values.put(TH_nights, hotel.nights);

        return db.update(TABLE_Hotel,values,TH_ID+"=?",new String[]{String.valueOf(hotel.id)});
    }

    public void deleteHotel(Hotel hotel){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Hotel, TH_ID+"=?",new String[]{String.valueOf(hotel.id)});
        db.close();
    }


    /*
    =============================================================================
    Table Travel
    =============================================================================
     */

    public void addTravel(Travel travel){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TTr_ID, travel.id);
        values.put(TTr_FID, travel.fid);
        values.put(TTr_img, travel.img);
        values.put(TTr_type, travel.type);
        values.put(TTr_no, travel.no);
        values.put(TTr_from, travel.from);
        values.put(TTr_fromDate, travel.from_date);
        values.put(TTr_fromTime, travel.from_time);
        values.put(TTr_to, travel.to);
        values.put(TTr_toDate, travel.to_date);
        values.put(TTr_toTime, travel.to_time);

        db.insert(TABLE_Travel, null, values);
        db.close();
    }

    public Travel getTravel(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_Travel, new String[]{
                        TTr_ID,TTr_FID,TTr_img,TTr_type,TTr_no,TTr_from,TTr_fromDate,TTr_fromTime,TTr_to,TTr_toDate,TTr_toTime},
                TTr_ID+"=?", new String[]{String.valueOf(id)},
                null,null,null,null);

        if (c!=null) c.moveToFirst();

        Travel travel = new Travel(
                c.getInt(c.getColumnIndexOrThrow(TTr_img)),
                c.getString(c.getColumnIndexOrThrow(TTr_no)),
                c.getString(c.getColumnIndexOrThrow(TTr_from)),
                c.getString(c.getColumnIndexOrThrow(TTr_fromDate)),
                c.getString(c.getColumnIndexOrThrow(TTr_fromTime)),
                c.getString(c.getColumnIndexOrThrow(TTr_to)),
                c.getString(c.getColumnIndexOrThrow(TTr_toDate)),
                c.getString(c.getColumnIndexOrThrow(TTr_toTime)));

        travel.type = c.getString(c.getColumnIndexOrThrow(TTr_type));
        travel.id = c.getInt(c.getColumnIndexOrThrow(TTr_ID));
        travel.fid = c.getInt(c.getColumnIndexOrThrow(TTr_FID));

        return travel;
    }

    public List<Travel> getAllTravels(int fid){
        List<Travel> travelList = new ArrayList<Travel>();

        String selectQuery = "SELECT * FROM "+TABLE_Travel
                +" WHERE "+TTr_FID+" = "+fid
                +" ORDER BY "+TTr_ID+" ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);

        if (c!=null && c.moveToFirst()){
            do {
                Travel travel = new Travel(
                        c.getInt(c.getColumnIndexOrThrow(TTr_img)),
                        c.getString(c.getColumnIndexOrThrow(TTr_no)),
                        c.getString(c.getColumnIndexOrThrow(TTr_from)),
                        c.getString(c.getColumnIndexOrThrow(TTr_fromDate)),
                        c.getString(c.getColumnIndexOrThrow(TTr_fromTime)),
                        c.getString(c.getColumnIndexOrThrow(TTr_to)),
                        c.getString(c.getColumnIndexOrThrow(TTr_toDate)),
                        c.getString(c.getColumnIndexOrThrow(TTr_toTime)));
                travel.type = c.getString(c.getColumnIndexOrThrow(TTr_type));
                travel.id = c.getInt(c.getColumnIndexOrThrow(TTr_ID));
                travel.fid = c.getInt(c.getColumnIndexOrThrow(TTr_FID));
                travelList.add(travel);
            } while (c.moveToNext());
        }

        return travelList;
    }

    public Travel getTravel(int fid, int pos) {
        List<Travel> travelList = getAllTravels(fid);
        return travelList.get(pos);
    }

    public int getTravelsCount(int fid){
        String countQuery = "SELECT * FROM "+TABLE_Travel+" WHERE "+TTr_FID+" = "+fid;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery,null);

        return c.getCount();
    }

    public int getTravelsCount(){
        String countQuery = "SELECT * FROM "+TABLE_Travel;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery,null);

        return c.getCount();
    }

    public int getTravelNewId(){
        String selectQuery = "SELECT * FROM "+TABLE_Travel;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        int nid = -1;
        if (c!=null && c.moveToFirst()){
            do {
                nid = c.getInt(c.getColumnIndexOrThrow(TTr_ID));
            } while (c.moveToNext());
        }
        return nid+1;
    }

    public int updateTravel(Travel travel){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TTr_ID, travel.id);
        values.put(TTr_FID, travel.fid);
        values.put(TTr_img, travel.img);
        values.put(TTr_type, travel.type);
        values.put(TTr_no, travel.no);
        values.put(TTr_from, travel.from);
        values.put(TTr_fromDate, travel.from_date);
        values.put(TTr_fromTime, travel.from_time);
        values.put(TTr_to, travel.to);
        values.put(TTr_toDate, travel.to_date);
        values.put(TTr_toTime, travel.to_time);

        return db.update(TABLE_Travel,values,TTr_ID+"=?",new String[]{String.valueOf(travel.id)});
    }

    public void deleteTravel(Travel travel){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Travel, TTr_ID+"=?",new String[]{String.valueOf(travel.id)});
        db.close();
    }


    /*
    =============================================================================
    Table Day
    =============================================================================
     */

    public static String getNextDate(String curDate, int inc) {
        if(curDate.length()<1) return "";
        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        final Date date;
        try {
            date = format.parse(curDate);
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, inc);
            return format.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getCurDay(String curDate) {
        if(curDate.length()<1) return "";
        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        final Date date;
        try {
            date = format.parse(curDate);
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if(dayOfWeek==Calendar.SUNDAY) return "Sunday";
            if(dayOfWeek==Calendar.MONDAY) return "Monday";
            if(dayOfWeek==Calendar.TUESDAY) return "Tuesday";
            if(dayOfWeek==Calendar.WEDNESDAY) return "Wednesday";
            if(dayOfWeek==Calendar.THURSDAY) return "Thursday";
            if(dayOfWeek==Calendar.FRIDAY) return "Friday";
            return "Saturday";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void DayOnCreate(Trip trip){
        int d = trip.nights+1;
        if(d>10) d=10;
        if(trip.depDate.equals(trip.retDate) && trip.depDate.length()<2) d=5;
        Day day = new Day();
        day.des = "";
        day.fid = trip.srno;
        for(int i=0;i<d;i++){
            day.city = trip.place;
            day.date = getNextDate(trip.depDate,i);
            day.day = getCurDay(day.date);
            day.id = getDayNewId();
            addDay(day);
        }
    }

    public void addDay(Day day){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TD_ID, day.id);
        values.put(TD_FID, day.fid);
        values.put(TD_city, day.city);
        values.put(TD_date, day.date);
        values.put(TD_day, day.day);
        values.put(TD_des, day.des);

        db.insert(TABLE_Day, null, values);
        db.close();
    }

    public Day getDay(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_Day, new String[]{
                        TD_ID,TD_FID,TD_city,TD_date,TD_day,TD_des},
                TD_ID+"=?", new String[]{String.valueOf(id)},
                null,null,null,null);

        if (c!=null) c.moveToFirst();

        Day day = new Day();

        day.id = c.getInt(c.getColumnIndexOrThrow(TD_ID));
        day.fid = c.getInt(c.getColumnIndexOrThrow(TD_FID));
        day.city = c.getString(c.getColumnIndexOrThrow(TD_city));
        day.date = c.getString(c.getColumnIndexOrThrow(TD_date));
        day.day = c.getString(c.getColumnIndexOrThrow(TD_day));
        day.des = c.getString(c.getColumnIndexOrThrow(TD_des));

        return day;
    }

    public List<Day> getAllDays(int fid){
        List<Day> dayList = new ArrayList<Day>();

        String selectQuery = "SELECT * FROM "+TABLE_Day
                +" WHERE "+TD_FID+" = "+fid
                +" ORDER BY "+TD_ID+" ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);

        if (c!=null && c.moveToFirst()){
            do {
                Day day = new Day();

                day.id = c.getInt(c.getColumnIndexOrThrow(TD_ID));
                day.fid = c.getInt(c.getColumnIndexOrThrow(TD_FID));
                day.city = c.getString(c.getColumnIndexOrThrow(TD_city));
                day.date = c.getString(c.getColumnIndexOrThrow(TD_date));
                day.day = c.getString(c.getColumnIndexOrThrow(TD_day));
                day.des = c.getString(c.getColumnIndexOrThrow(TD_des));
                dayList.add(day);
            } while (c.moveToNext());
        }

        return dayList;
    }

    public Day getDay(int fid, int pos) {
        List<Day> dayList = getAllDays(fid);
        return dayList.get(pos);
    }

    public int getDaysCount(int fid){
        String countQuery = "SELECT * FROM "+TABLE_Day+" WHERE "+TD_FID+" = "+fid;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery,null);

        return c.getCount();
    }

    public int getDaysCount(){
        String countQuery = "SELECT * FROM "+TABLE_Day;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery,null);

        return c.getCount();
    }

    public int getDayNewId(){
        String selectQuery = "SELECT * FROM "+TABLE_Day;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        int nid = -1;
        if (c!=null && c.moveToFirst()){
            do {
                nid = c.getInt(c.getColumnIndexOrThrow(TD_ID));
            } while (c.moveToNext());
        }
        return nid+1;
    }

    public int updateDay(Day day){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TD_ID, day.id);
        values.put(TD_FID, day.fid);
        values.put(TD_city, day.city);
        values.put(TD_date, day.date);
        values.put(TD_day, day.day);
        values.put(TD_des, day.des);

        return db.update(TABLE_Day,values,TD_ID+"=?",new String[]{String.valueOf(day.id)});
    }

    //TODO: Delete Day

    public void deleteDay(Day day){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Day, TD_ID+"=?",new String[]{String.valueOf(day.id)});
        db.close();
    }


    /*
    =============================================================================
    Table Activity
    =============================================================================
     */

    public void addActivity(Activity act){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TA_ID, act.id);
        values.put(TA_FID, act.fid);
        values.put(TA_title, act.title);
        values.put(TA_des, act.desc);
        values.put(TA_img, act.img);

        db.insert(TABLE_Activity, null, values);
        db.close();
    }

    public Activity getActivity(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_Activity, new String[]{
                        TA_ID,TA_FID,TA_title,TA_des,TA_img},
                TA_ID+"=?", new String[]{String.valueOf(id)},
                null,null,null,null);

        if (c!=null) c.moveToFirst();

        Activity act = new Activity(c.getString(c.getColumnIndexOrThrow(TA_title)),
                c.getString(c.getColumnIndexOrThrow(TA_des)),
                c.getInt(c.getColumnIndexOrThrow(TA_img)));

        act.id = c.getInt(c.getColumnIndexOrThrow(TA_ID));
        act.fid = c.getInt(c.getColumnIndexOrThrow(TA_FID));

        return act;
    }

    public List<Activity> getAllActivities(int fid){
        List<Activity> actList = new ArrayList<Activity>();

        String selectQuery = "SELECT * FROM "+TABLE_Activity
                +" WHERE "+TA_FID+" = "+fid
                +" ORDER BY "+TA_ID+" ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);

        if (c!=null && c.moveToFirst()){
            do {
                Activity act = new Activity(c.getString(c.getColumnIndexOrThrow(TA_title)),
                        c.getString(c.getColumnIndexOrThrow(TA_des)),
                        c.getInt(c.getColumnIndexOrThrow(TA_img)));

                act.id = c.getInt(c.getColumnIndexOrThrow(TA_ID));
                act.fid = c.getInt(c.getColumnIndexOrThrow(TA_FID));
                actList.add(act);
            } while (c.moveToNext());
        }

        return actList;
    }

    public List<Activity> getAllActivities(int srno, int day){
        List<Day> dayList = getAllDays(srno);
        return getAllActivities(dayList.get(day-1).id);
    }

    public Activity getActivity(int fid, int pos) {
        List<Activity> actList = getAllActivities(fid);
        return actList.get(pos);
    }

    public Activity getActivity(int srno, int day, int pos) {
        List<Activity> actList = getAllActivities(srno,day);
        return actList.get(pos);
    }

    public int getActivitiesCount(int fid){
        String countQuery = "SELECT * FROM "+TABLE_Activity+" WHERE "+TA_FID+" = "+fid;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery,null);

        return c.getCount();
    }

    public int getActivitiesCount(){
        String countQuery = "SELECT * FROM "+TABLE_Activity;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery,null);

        return c.getCount();
    }

    public int getActivityNewId(){
        String selectQuery = "SELECT * FROM "+TABLE_Activity;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        int nid = -1;
        if (c!=null && c.moveToFirst()){
            do {
                nid = c.getInt(c.getColumnIndexOrThrow(TA_ID));
            } while (c.moveToNext());
        }
        return nid+1;
    }

    public int updateActivity(Activity act){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TA_ID, act.id);
        values.put(TA_FID, act.fid);
        values.put(TA_title, act.title);
        values.put(TA_des, act.desc);
        values.put(TA_img, act.img);

        return db.update(TABLE_Activity,values,TA_ID+"=?",new String[]{String.valueOf(act.id)});
    }

    public void deleteActivity(Activity act){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Activity, TA_ID+"=?",new String[]{String.valueOf(act.id)});
        db.close();
    }

    public int getActivityFid(int srno, int day){
        List<Day> dayList = getAllDays(srno);
        return dayList.get(day-1).id;
    }


    /*
    =============================================================================
    Table Exp
    =============================================================================
     */

    public void addExp(Exp exp){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TEx_ID, exp.id);
        values.put(TEx_FID, exp.fid);
        values.put(TEx_title, exp.title);
        values.put(TEx_amt, exp.Amt);
        values.put(TEx_amtd, exp.AmtD);
        values.put(TEx_isHome, exp.isHome);

        db.insert(TABLE_Exp, null, values);
        db.close();
    }

    public Exp getExp(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_Exp, new String[]{
                        TEx_ID,TEx_FID,TEx_title,TEx_amt,TEx_amtd,TEx_isHome},
                TEx_ID+"=?", new String[]{String.valueOf(id)},
                null,null,null,null);

        if (c!=null) c.moveToFirst();

        Exp exp = new Exp(c.getString(c.getColumnIndexOrThrow(TEx_title)),
                c.getFloat(c.getColumnIndexOrThrow(TEx_amt)),
                c.getInt(c.getColumnIndexOrThrow(TEx_isHome)));

        exp.AmtD = c.getFloat(c.getColumnIndexOrThrow(TEx_amtd));
        exp.id = c.getInt(c.getColumnIndexOrThrow(TEx_ID));
        exp.fid = c.getInt(c.getColumnIndexOrThrow(TEx_FID));

        return exp;
    }

    public List<Exp> getAllExps(int fid){
        List<Exp> expList = new ArrayList<Exp>();

        String selectQuery = "SELECT * FROM "+TABLE_Exp
                +" WHERE "+TEx_FID+" = "+fid
                +" ORDER BY "+TEx_ID+" ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);

        if (c!=null && c.moveToFirst()){
            do {
                Exp exp = new Exp(c.getString(c.getColumnIndexOrThrow(TEx_title)),
                        c.getFloat(c.getColumnIndexOrThrow(TEx_amt)),
                        c.getInt(c.getColumnIndexOrThrow(TEx_isHome)));

                exp.AmtD = c.getFloat(c.getColumnIndexOrThrow(TEx_amtd));
                exp.id = c.getInt(c.getColumnIndexOrThrow(TEx_ID));
                exp.fid = c.getInt(c.getColumnIndexOrThrow(TEx_FID));
                expList.add(exp);
            } while (c.moveToNext());
        }

        return expList;
    }

    public List<Exp> getAllExps(int srno, int expcat){
        List<ExpCat> expCatList = getAllExpCats(srno);
        return getAllExps(expCatList.get(expcat).id);
    }

    public Exp getExp(int fid, int pos) {
        List<Exp> expList = getAllExps(fid);
        return expList.get(pos);
    }

    public Exp getExp(int srno, int expcat, int pos) {
        List<Exp> expList = getAllExps(srno,expcat);
        return expList.get(pos);
    }

    public int getExpsCount(int fid){
        String countQuery = "SELECT * FROM "+TABLE_Exp+" WHERE "+TEx_FID+" = "+fid;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery,null);

        return c.getCount();
    }

    public int getExpsCount(){
        String countQuery = "SELECT * FROM "+TABLE_Exp;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery,null);

        return c.getCount();
    }

    public int getExpNewId(){
        String selectQuery = "SELECT * FROM "+TABLE_Exp;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        int nid = -1;
        if (c!=null && c.moveToFirst()){
            do {
                nid = c.getInt(c.getColumnIndexOrThrow(TEx_ID));
            } while (c.moveToNext());
        }
        return nid+1;
    }

    public int updateExp(Exp exp){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TEx_ID, exp.id);
        values.put(TEx_FID, exp.fid);
        values.put(TEx_title, exp.title);
        values.put(TEx_amt, exp.Amt);
        values.put(TEx_amtd, exp.AmtD);
        values.put(TEx_isHome, exp.isHome);

        return db.update(TABLE_Exp,values,TEx_ID+"=?",new String[]{String.valueOf(exp.id)});
    }

    public void deleteExp(Exp exp){
        SQLiteDatabase db = this.getWritableDatabase();
        ExpCat expcat = getExpCat(exp.fid);
        Trip trip = getTrip(expcat.fid);
        expcat.Amt = expcat.Amt - exp.Amt;
        if(exp.isHome==0) expcat.DesAmt = expcat.DesAmt - exp.AmtD;
        else expcat.homAmt = expcat.homAmt - exp.Amt;
        trip.exp = trip.exp - expcat.Amt;
        trip.Hexp = trip.Hexp - expcat.homAmt;
        trip.Dexp = trip.Dexp - expcat.DesAmt;
        updateExpCat(expcat);
        updateTrip(trip);
        db.delete(TABLE_Exp, TEx_ID+"=?",new String[]{String.valueOf(exp.id)});
        db.close();
    }

    public int getExpFid(int srno, int expcat){
        List<ExpCat> expCatList = getAllExpCats(srno);
        return expCatList.get(expcat).id;
    }


    /*
    =============================================================================
    Table Notes
    =============================================================================
     */

    public void addNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TN_ID, note.id);
        values.put(TN_FID, note.fid);
        values.put(TN_title, note.title);
        values.put(TN_des, note.desc);

        db.insert(TABLE_Notes, null, values);
        db.close();
    }

    public Note getNote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_Notes, new String[]{
                        TN_ID,TN_FID,TN_title,TN_des},
                TN_ID+"=?", new String[]{String.valueOf(id)},
                null,null,null,null);

        if (c!=null) c.moveToFirst();

        Note note = new Note();

        note.id = c.getInt(c.getColumnIndexOrThrow(TN_ID));
        note.fid = c.getInt(c.getColumnIndexOrThrow(TN_FID));
        note.title = c.getString(c.getColumnIndexOrThrow(TN_title));
        note.desc = c.getString(c.getColumnIndexOrThrow(TN_des));

        return note;
    }

    public List<Note> getAllNotes(int fid){
        List<Note> noteList = new ArrayList<Note>();

        String selectQuery = "SELECT * FROM "+TABLE_Notes
                +" WHERE "+TN_FID+" = "+fid
                +" ORDER BY "+TN_ID+" ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);

        if (c!=null && c.moveToFirst()){
            do {
                Note note = new Note();

                note.id = c.getInt(c.getColumnIndexOrThrow(TN_ID));
                note.fid = c.getInt(c.getColumnIndexOrThrow(TN_FID));
                note.title = c.getString(c.getColumnIndexOrThrow(TN_title));
                note.desc = c.getString(c.getColumnIndexOrThrow(TN_des));
                noteList.add(note);
            } while (c.moveToNext());
        }

        return noteList;
    }

    public int getNotesCount(int fid){
        String countQuery = "SELECT * FROM "+TABLE_Notes+" WHERE "+TN_FID+" = "+fid;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery,null);

        return c.getCount();
    }

    public int getNotesCount(){
        String countQuery = "SELECT * FROM "+TABLE_Notes;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery,null);

        return c.getCount();
    }

    public int getNoteNewId(){
        String selectQuery = "SELECT * FROM "+TABLE_Notes;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        int nid = -1;
        if (c!=null && c.moveToFirst()){
            do {
                nid = c.getInt(c.getColumnIndexOrThrow(TN_ID));
            } while (c.moveToNext());
        }
        return nid+1;
    }

    public int updateNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TN_ID, note.id);
        values.put(TN_FID, note.fid);
        values.put(TN_title, note.title);
        values.put(TN_des, note.desc);

        return db.update(TABLE_Notes,values,TN_ID+"=?",new String[]{String.valueOf(note.id)});
    }

    public void deleteNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Notes, TN_ID+"=?",new String[]{String.valueOf(note.id)});
        db.close();
    }

}