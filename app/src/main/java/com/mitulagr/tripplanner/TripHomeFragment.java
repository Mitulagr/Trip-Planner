package com.mitulagr.tripplanner;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TripHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment trip_home.
     */
    // TODO: Rename and change types and number of parameters
    public static TripHomeFragment newInstance(String param1, String param2) {
        TripHomeFragment fragment = new TripHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private RecyclerView hotels, travels;
    private TextView hotelHelp, travelHelp;
    private ImageButton modifyTrip, deleteTrip;
    private Button backToTrips, addHotel, addTravel;
    private TextView bPlace, bNights, bFromTo, bCur, bExp;
    private int id;
    private DBHandler db;
    private Trip trip;
    Adapter_Travel adt;
    Adapter_Hotel adh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_trip_home, container, false);

        backToTrips = (Button) rootView.findViewById(R.id.backtotrips);
        modifyTrip = (ImageButton) rootView.findViewById(R.id.modifytrip);
        deleteTrip = (ImageButton) rootView.findViewById(R.id.deletetrip);

        addHotel = (Button) rootView.findViewById(R.id.addhotel);
        hotels = (RecyclerView) rootView.findViewById(R.id.hotels);
        hotelHelp = (TextView) rootView.findViewById(R.id.HotelHelp);

        addTravel = (Button) rootView.findViewById(R.id.addtravel);
        travels = (RecyclerView) rootView.findViewById(R.id.travels);
        travelHelp = (TextView) rootView.findViewById(R.id.TravelHelp);

        bPlace = (TextView) rootView.findViewById(R.id.tripplacename);
        bNights = (TextView) rootView.findViewById(R.id.textView22);
        bFromTo = (TextView) rootView.findViewById(R.id.textView17);
        bCur = (TextView) rootView.findViewById(R.id.textView39);
        bExp = (TextView) rootView.findViewById(R.id.textView21);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        id = sp.getInt("Current Trip", 0);

        /*
        =============================================================================
        Basic Overview
        =============================================================================
         */

        db = new DBHandler(getContext());
        trip = db.getTrip(id);

        bPlace.setText(trip.place);
        if(trip.nights>0){
            bNights.setText(String.valueOf(trip.nights)+" Nights");
            bNights.setVisibility(View.VISIBLE);
        }
        else bNights.setVisibility(View.INVISIBLE);
        if(trip.depDate.length()>1){
            if(trip.retDate.length()>1) bFromTo.setText(dispDate(trip.depDate)+"   -   "+dispDate(trip.retDate));
            else bFromTo.setText(dispDate(trip.depDate));
            bFromTo.setVisibility(View.VISIBLE);
        }
        else bFromTo.setVisibility(View.INVISIBLE);
        if(trip.isHom==1) bCur.setText(trip.Hcur);
        else bCur.setText(trip.Hcur+"  |  "+trip.Dcur);
        bExp.setText("Expense: "+trip.Hcur.substring(6)+" "+String.valueOf(Math.round(trip.exp)));
        rootView.findViewById(R.id.constraintLayoutTrip).setBackgroundResource(trip.imageId);

        backToTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        modifyTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), addtrip.class));
                //TODO
            }
        });


        /*
        =============================================================================
        Hotel Overview
        =============================================================================
         */

        addHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHotel(new Hotel("","",-1));
            }
        });

        hotels.setLayoutManager(new LinearLayoutManager(getActivity()));

        adh = new Adapter_Hotel(getContext());

        hotels.setAdapter(adh);

        hotelHelpUpdate();

        adh.setOnItemLongClickListener(new Adapter_Hotel.onRecyclerViewItemLongClickListener() {
            @Override
            public void onItemLongClickListener(View view, int position) {
                showEditHotel(view,position);
            }
        });


        /*
        =============================================================================
        Travel Overview
        =============================================================================
         */

        addTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTravel(new Travel(R.drawable.ic_baseline_flight_24,"","",
                        "","","","",""),true);
            }
        });

        travels.setLayoutManager(new LinearLayoutManager(getActivity()));

        adt = new Adapter_Travel(getContext());

        travels.setAdapter(adt);

        travelHelpUpdate();

        adt.setOnItemLongClickListener(new Adapter_Travel.onRecyclerViewItemLongClickListener() {
            @Override
            public void onItemLongClickListener(View view, int position) {
                showEditTravel(view,position);
            }
        });

        /*
        =============================================================================
        Banner Ad
        =============================================================================
         */


        /*
        =============================================================================
        Day-Wise Overview
        =============================================================================
         */


        Exchange(trip.Dcur.substring(0,3).toLowerCase(),trip.Hcur.substring(0,3).toLowerCase(),getContext());

        return rootView;
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

    void hotelHelpUpdate(){
        if(adh.getItemCount()>0) hotelHelp.setText("* Long Press to Add Expense, Delete, Modify, or Move");
        else hotelHelp.setText("* No Hotels Added. Click on 'Add Hotel' to Add");
    }

    void travelHelpUpdate(){
        if(adt.getItemCount()>0) travelHelp.setText("* Long Press to Add Expense, Delete, Modify, or Move");
        else travelHelp.setText("* No Travels Added. Click on 'Add Travel' to Add");
    }

    void showHotel(Hotel hotel){
        Dialog curd = new Dialog(getActivity());
        curd.setContentView(R.layout.addhotel);
        curd.getWindow();
        curd.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        curd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        curd.show();

        EditText HCity = (EditText) curd.findViewById(R.id.editTextTextPersonName2);
        EditText HName = (EditText) curd.findViewById(R.id.editTextTextPersonName3);
        EditText HNights = (EditText) curd.findViewById(R.id.editTextNumber);
        Button addHotel = (Button) curd.findViewById(R.id.button3);
        TextView Hdec = (TextView) curd.findViewById(R.id.textView30);
        TextView Hinc = (TextView) curd.findViewById(R.id.textView31);

        boolean [] isNew = {false};
        if(hotel.nights==-1){
            hotel.id = db.getHotelsCount();
            hotel.fid = id;
            isNew[0] = true;
        }
        else addHotel.setText("Update Hotel");

        final Boolean [] isdec = {true};

        HCity.setText(hotel.city);
        HName.setText(hotel.name);
        if(hotel.nights==0){
            HNights.setText("");
            HNights.setHint("Blank");
            isdec[0] = false;
        }
        if(hotel.nights>0) HNights.setText(String.valueOf(hotel.nights));

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        AdView mAdHot = curd.findViewById(R.id.adHot);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdHot.loadAd(adRequest);

        // TODO: if returned nights is 0 then show nothing

        Hdec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isdec[0]) return;
                int nights = Integer.valueOf(HNights.getText().toString());
                if(nights==0) return;
                nights--;
                if(nights==0){
                    HNights.setText("");
                    HNights.setHint("Blank");
                    isdec[0] = false;
                    return;
                }
                HNights.setText(String.valueOf(nights));
            }
        });

        Hinc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nights = 0;
                if(isdec[0]) nights = Integer.valueOf(HNights.getText().toString());
                else {
                    HNights.setHint("");
                    isdec[0] = true;
                }
                nights++;
                HNights.setText(String.valueOf(nights));
            }
        });

        addHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(HCity.getText().toString().length()==0){
                    HCity.requestFocus();
                    HCity.setError("City Name Mandatory");
                    return;
                }
                if(HName.getText().toString().length()==0){
                    HName.requestFocus();
                    HName.setError("Hotel Name Mandatory");
                    return;
                }
                int nights=0;
                if(HNights.getText().toString().length()>0) nights = Integer.parseInt(HNights.getText().toString());
                //Hotel hotel = new Hotel(HCity.getText().toString(), HName.getText().toString(), nights);
                hotel.nights = nights;
                hotel.city = HCity.getText().toString();
                hotel.name = HName.getText().toString();
                if(isNew[0]) db.addHotel(hotel);
                else db.updateHotel(hotel);

                adh.localDataSet = db.getAllHotels(id);
                adh.notifyDataSetChanged();
                hotelHelpUpdate();
                curd.dismiss();
            }
        });
    }


    void showTravel(Travel travel, boolean isNew){
        Dialog curd = new Dialog(getActivity());
        curd.setContentView(R.layout.addtravel);
        curd.getWindow();
        curd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        curd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        curd.show();

        Button tType = (Button) curd.findViewById(R.id.trType);
        EditText tNo = (EditText) curd.findViewById(R.id.editTextTextPersonName4);
        EditText tFromCity = (EditText) curd.findViewById(R.id.editTextTextPersonName5);
        EditText tToCity = (EditText) curd.findViewById(R.id.editTextTextPersonName6);
        Button tFromDate = (Button) curd.findViewById(R.id.depdate3);
        Button tToDate = (Button) curd.findViewById(R.id.depdate4);
        TextView tDepR = (TextView) curd.findViewById(R.id.depReset);
        TextView tArrR = (TextView) curd.findViewById(R.id.arrReset);
        View tFromTime = (View) curd.findViewById(R.id.fromClock);
        View tToTime = (View) curd.findViewById(R.id.toClock);
        TextView tFromShowTime = (TextView) curd.findViewById(R.id.textView33);
        TextView tToShowTime = (TextView) curd.findViewById(R.id.textView34);
        Button tAdd = (Button) curd.findViewById(R.id.trAdd);

        if(!isNew) tAdd.setText("Update "+travel.type);
        else tAdd.setText("Add "+travel.type);
        tType.setText(travel.type);
        tType.setCompoundDrawablesWithIntrinsicBounds(travel.img,0,R.drawable.ic_baseline_arrow_drop_down_24,0);
        tNo.setHint(travel.type+" Number");
        tNo.setText(travel.no);
        tFromCity.setText(travel.from);
        tToCity.setText(travel.to);
        tFromDate.setText(travel.from_date);
        tToDate.setText(travel.to_date);


        // TODO: Set From City and Depart Date as previous arrival ones (after checking if they are filled)

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        AdView mAdHot = curd.findViewById(R.id.adTra);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdHot.loadAd(adRequest);

        tType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getActivity(),tType);
                popup.getMenuInflater()
                        .inflate(R.menu.travel_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        String typename;
                        int typeimg;

                        switch (menuItem.getItemId()) {

                            case R.id.tt1:
                                typename = "Flight";
                                typeimg = R.drawable.ic_baseline_flight_24;
                                break;
                            case R.id.tt2:
                                typename = "Train";
                                typeimg = R.drawable.ic_baseline_train_24;
                                break;
                            case R.id.tt3:
                                typename = "Bus";
                                typeimg = R.drawable.ic_baseline_directions_bus_24;
                                break;
                            case R.id.tt4:
                                typename = "Car";
                                typeimg = R.drawable.ic_baseline_directions_car_24;
                                break;
                            case R.id.tt5:
                                typename = "Cruise";
                                typeimg = R.drawable.ic_baseline_directions_boat_24;
                                break;
                            case R.id.tt6:
                                typename = "Rocket";
                                typeimg = R.drawable.ic_baseline_rocket_launch_24;
                                break;
                            default:
                                typename = "Other";
                                typeimg = R.drawable.ic_baseline_more_horiz_24;
                                break;
                        }

                        tType.setText(typename);
                        tType.setCompoundDrawablesWithIntrinsicBounds(typeimg,0,R.drawable.ic_baseline_arrow_drop_down_24,0);
                        tNo.setHint(typename+" Number");
                        tAdd.setText("Add "+typename);

                        travel.img = typeimg;
                        travel.type = typename;

                        return true;
                    }
                });

                popup.show();
            }
        });

        TravelDate td = new TravelDate(tFromDate,tToDate,getActivity(),tDepR,tArrR);

        clock Cfrom = new clock(tFromTime,tFromShowTime,travel.from_time);
        clock Cto = new clock(tToTime,tToShowTime,travel.to_time);

        tAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                travel.no = tNo.getText().toString();
                travel.from = tFromCity.getText().toString();
                travel.to = tToCity.getText().toString();
                travel.from_date = tFromDate.getText().toString();
                travel.to_date = tToDate.getText().toString();
                travel.from_time = tFromShowTime.getText().toString();
                travel.to_time = tToShowTime.getText().toString();

                if(isNew){
                    travel.id = db.getTravelsCount();
                    travel.fid = id;
                    db.addTravel(travel);
                }
                else db.updateTravel(travel);

                adt.localDataSet = db.getAllTravels(id);
                adt.notifyDataSetChanged();
                travelHelpUpdate();
                curd.dismiss();
            }
        });

    }

    void showEditHotel(View view, int pos){
        PopupMenu popup = new PopupMenu(getActivity(),view);
        popup.getMenuInflater()
                .inflate(R.menu.edit1, popup.getMenu());

        //int [] done = {-1};

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.e0:
                        //Adapter_ExpenseMain adE = new Adapter_ExpenseMain(getContext(),trip);
                        //adE.showExpense(getContext().getDrawable(disp.get(position).imageId),position);
                        //TODO: if hotel category deleted, if this hotel deleted.
                        break;
                    case R.id.e1:
                        db.deleteHotel(db.getHotel(id,pos));
                        adh.localDataSet = db.getAllHotels(id);
                        adh.notifyDataSetChanged();
                        hotelHelpUpdate();
                        break;
                    case R.id.e2:
                        showHotel(db.getHotel(id,pos));
                        break;
                    case R.id.e3:
                        if(pos==0) break;
                        Hotel h1 = db.getHotel(id,pos-1);
                        Hotel h2 = db.getHotel(id,pos);
                        int temp = h1.id;
                        h1.id = h2.id;
                        h2.id = temp;
                        db.updateHotel(h1);
                        db.updateHotel(h2);
                        adh.localDataSet = db.getAllHotels(id);
                        adh.notifyDataSetChanged();
                        //done[0] = pos-1;
                        break;
                    case R.id.e4:
                        if(pos== adh.getItemCount()-1) break;
                        Hotel h3 = db.getHotel(id,pos);
                        Hotel h4 = db.getHotel(id,pos+1);
                        int temp1 = h3.id;
                        h3.id = h4.id;
                        h4.id = temp1;
                        db.updateHotel(h3);
                        db.updateHotel(h4);
                        adh.localDataSet = db.getAllHotels(id);
                        adh.notifyDataSetChanged();
                        //done[0] = pos+1;
                        break;
                }

                return true;
            }
        });

        popup.show();

    }

    void showEditTravel(View view, int pos){
        PopupMenu popup = new PopupMenu(getActivity(),view);
        popup.getMenuInflater()
                .inflate(R.menu.edit1, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.e0:

                        break;
                    case R.id.e1:
                        db.deleteTravel(db.getTravel(id,pos));
                        adt.localDataSet = db.getAllTravels(id);
                        adt.notifyDataSetChanged();
                        travelHelpUpdate();
                        break;
                    case R.id.e2:
                        showTravel(db.getTravel(id,pos),false);
                        break;
                    case R.id.e3:
                        if(pos==0) break;
                        Travel t1 = db.getTravel(id,pos-1);
                        Travel t2 = db.getTravel(id,pos);
                        int temp = t1.id;
                        t1.id = t2.id;
                        t2.id = temp;
                        db.updateTravel(t1);
                        db.updateTravel(t2);
                        adt.localDataSet = db.getAllTravels(id);
                        adt.notifyDataSetChanged();
                        break;
                    case R.id.e4:
                        if(pos== adt.getItemCount()-1) break;
                        Travel t3 = db.getTravel(id,pos);
                        Travel t4 = db.getTravel(id,pos+1);
                        int temp1 = t3.id;
                        t3.id = t4.id;
                        t4.id = temp1;
                        db.updateTravel(t3);
                        db.updateTravel(t4);
                        adt.localDataSet = db.getAllTravels(id);
                        adt.notifyDataSetChanged();
                        break;
                }

                return true;
            }
        });

        popup.show();
    }

    void onExchange(float rate){
        if(rate>0.0f) {
            trip.rate = rate;
            db.updateTrip(trip);
        }
    }

    void Exchange(String c1, String c2, Context context){

        String url = "https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/"+c1+"/"+c2+".json";

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                //rate[0] = parseJsonData(string,c2);
                parseJsonData(string,c2);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                String url2 = "https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/"+c1+"/"+c2+".min.json";

                StringRequest request2 = new StringRequest(url2, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String string) {
                        //rate[0] = parseJsonData(string,c2);
                        parseJsonData(string,c2);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });

                RequestQueue rQueue2 = Volley.newRequestQueue(context);
                rQueue2.add(request2);

            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);

        //return rate[0];

    }

    void parseJsonData(String jsonString, String c) {
        float rate = 0.0f;
        try {
            JSONObject object = new JSONObject(jsonString);
            rate = Float.parseFloat(object.getString(c));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        onExchange(rate);
    }

}