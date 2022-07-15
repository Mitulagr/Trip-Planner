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
        Hotel Overview (with Cities & Nights)
        =============================================================================
         */

        addHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHotel();
            }
        });

        // TODO: if >0 hotels then change text to - "* Long Press to Add Expense, Delete, Modify, or Move";
        // Check every time Hotel Added
        // Also set back if all hotels deleted

        Hotel h1 = new Hotel("New York", "Hilton", 3);
        Hotel h2 = new Hotel("New York", "Marriot", 5);
        Hotel h3 = new Hotel("New York", "Holiday Inn", 2);
        Hotel[] hotelList = {h1,h2,h3,h2,h1};
        //Hotel[] hotelList = {};

        hotels.setLayoutManager(new LinearLayoutManager(getActivity()));

        Adapter_Hotel adh = new Adapter_Hotel(hotelList);

        hotels.setAdapter(adh);

        adh.setOnItemLongClickListener(new Adapter_Hotel.onRecyclerViewItemLongClickListener() {
            @Override
            public void onItemLongClickListener(View view, int position) {
                showEdit(view);
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
                showTravel();
            }
        });

        // TODO: if >0 travels then change text to - Long Press to Modify or Delete
        // Check every time Travel Added
        // Also set back if all travels deleted

        Travel t1 = new Travel(R.drawable.ic_baseline_flight_24, "AI 452", "Mumbai",
                "16/06/2022", "8:45 PM", "Jaipur",
                "14/05/2022", "10:30 PM");
        Travel t2 = new Travel(R.drawable.ic_baseline_flight_24, "AI 452", "Mumbai",
                "15/06/2022", null, "Jaipur",
                null, null);
        Travel t3 = new Travel(R.drawable.ic_baseline_flight_24, "AI 452", "Mumbai",
                null, "8:45 PM", "Jaipur",
                "14/06/2022", null);
        Travel t4 = new Travel(R.drawable.ic_baseline_flight_24, null, "Mumbai",
                null, "8:45 PM", "Jaipur",
                "14/05/2022", "10:30 PM");
        Travel t5 = new Travel(R.drawable.ic_baseline_flight_24, "AI 452", "Mumbai",
                "17/06/2022", "8:45 PM", "Jaipur",
                null, "10:30 PM");

        Travel[] travelList = {t1,t2,t3,t4,t5};

        travels.setLayoutManager(new LinearLayoutManager(getActivity()));

        Adapter_Travel adt = new Adapter_Travel(travelList);

        travels.setAdapter(adt);

        adt.setOnItemLongClickListener(new Adapter_Travel.onRecyclerViewItemLongClickListener() {
            @Override
            public void onItemLongClickListener(View view, int position) {
                showEdit(view);
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

    void showHotel(){
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

        final Boolean [] isdec = {true};

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
                curd.dismiss();
            }
        });
    }


    void showTravel(){
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

                        return true;
                    }
                });

                popup.show();
            }
        });

        TravelDate td = new TravelDate(tFromDate,tToDate,getActivity(),tDepR,tArrR);

        clock Cfrom = new clock(tFromTime,tFromShowTime);
        clock Cto = new clock(tToTime,tToShowTime);

        tAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
                curd.dismiss();
            }
        });

    }

    void showEdit(View view){
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

                        break;
                    case R.id.e2:

                        break;
                    case R.id.e3:

                        break;
                    case R.id.e4:

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