package com.mitulagr.tripplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button add_trip;
    private RecyclerView trips;
    private ImageButton sort;
    private Adapter_Trips adt;

    // TODO: Save data onPause etc. also
    // TODO: Save updated sort trip list
    // TODO: Add del/archive menu for trips on long press or image button click
    // TODO: Make layout for landscape also and night also
    // TODO: Check if no. of nights > 10 ....make max 10 activity days initially, rest can be added using +
    // TODO: remove unnecessary imports everywhere in the end
    // TODO: mechanism to deal with currency exchange failure & to not allow currency that is not supported
    // TODO: multi-country visit currency support, more languages, import/export,
    // TODO: able to enter custom exchange rate
    // TODO: after completion do experiements like rotating layout, night mode, etc. like check if fragment disappears on rotation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_trip = (Button) findViewById(R.id.add_trip);
        trips = (RecyclerView) findViewById(R.id.trips);
        sort = (ImageButton) findViewById(R.id.sort);

        DBHandler db = new DBHandler(this);
        if(db.getTripsCount()>0) findViewById(R.id.textView25).setVisibility(View.GONE);
        else findViewById(R.id.textView25).setVisibility(View.VISIBLE);

        //TODO: open last opened automatically 2. if deleted
        //TODO: latest trip at top instead of end + sort logic (save sort preference)
        //TODO: prevent memory leak when screen rotate while adding anything like activity
        //TODO: In adding new travel default from name = prev to name and other such things like in day

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int id = sp.getInt("Current Trip", -1);
        if(id!=-1){
            Intent intent = new Intent(MainActivity.this, PageMain.class);
            startActivity(intent);
        }


        /*
        =============================================================================
        Trips Adapter
        =============================================================================
         */

        trips.setLayoutManager(new LinearLayoutManager(this));

        int im[] = new int [6];
        im[0] = getResources().getIdentifier("t1", "drawable", "com.mitulagr.tripplanner");
        im[1] = getResources().getIdentifier("t2", "drawable", "com.mitulagr.tripplanner");
        im[2] = getResources().getIdentifier("t3", "drawable", "com.mitulagr.tripplanner");
        im[3] = getResources().getIdentifier("t4", "drawable", "com.mitulagr.tripplanner");
        im[4] = getResources().getIdentifier("t5", "drawable", "com.mitulagr.tripplanner");
        im[5] = getResources().getIdentifier("t6", "drawable", "com.mitulagr.tripplanner");

//        Trip trip1 = new Trip(1,"Matheran",im[0]);
//        Trip trip2 = new Trip(2,"Leh Laddakh",im[1]);
//        Trip trip3 = new Trip(3,"Swizterland",im[2]);
//        Trip trip4 = new Trip(4,"Matheran",im[3]);
//        Trip trip5 = new Trip(5,"Leh Laddakh",im[4]);
//        Trip trip6 = new Trip(6,"Swizterland",im[5]);

        //Trip [] tripList = {trip1,trip2,trip3,trip4,trip5,trip6,trip1,trip2};
        //Trip [] tripList = {trip1};


        adt = new Adapter_Trips(this);

        trips.setAdapter(adt);

        //trips.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        trips.addItemDecoration(new VerticalSpaceItemDecoration(25));

        trips.addOnItemTouchListener(new RecyclerTouchListener(this, trips, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("Current Trip", adt.db.getTrip(position).srno);
                editor.commit();
                Intent intent = new Intent(MainActivity.this, PageMain.class);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(MainActivity.this, "Long Clicked", Toast.LENGTH_SHORT).show();
            }
        }));



        /*
        =============================================================================
        Add Trip
        =============================================================================
         */

        add_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Verify this activity starting method
                Intent intent = new Intent(MainActivity.this, addtrip.class);
                startActivity(intent);
                //finish();
            }
        });


        /*
        =============================================================================
        Sort Menu
        =============================================================================
         */

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainActivity.this,sort);
                popup.getMenuInflater()
                        .inflate(R.menu.sort_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {

                            case R.id.sort1:

                                break;


                        }

                        Toast.makeText(MainActivity.this, "You Clicked : " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();

                        return true;
                    }
                });

                popup.show();
            }
        });


        /*
        =============================================================================
        Search
        =============================================================================
         */

    }

    @Override
    protected void onResume() {
        super.onResume();
        adt.notifyDataSetChanged();
    }
}