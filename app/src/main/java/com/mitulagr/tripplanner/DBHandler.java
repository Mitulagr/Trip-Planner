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
    private static final String TABLE_Exp = "TEx";
    private static final String TABLE_Day = "TD";
    private static final String TABLE_Hotel = "TH";
    private static final String TABLE_Travel = "TTr";
    private static final String TABLE_Activity = "TA";

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_Trip);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ExpCat);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_Hotel);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_Travel);
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
        db.delete(TABLE_Trip, TT_ID+"=?",new String[]{String.valueOf(trip.srno)});
        db.close();
    }

    public void deleteTrip(int srno){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Trip, TT_ID+"=?",new String[]{String.valueOf(srno)});
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

    //TODO: also delete related exps. For Level 2+ deletions confirmation dialog
    public void deleteExpCat(ExpCat expcat){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ExpCat, TE_ID+"=?",new String[]{String.valueOf(expcat.id)});
        db.close();
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

}