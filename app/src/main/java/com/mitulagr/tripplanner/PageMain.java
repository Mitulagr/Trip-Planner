package com.mitulagr.tripplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class PageMain extends AppCompatActivity {

    private BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_main);

        TripFragment frag = new TripFragment();
        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, frag);
        transaction.commit();

        nav = findViewById(R.id.bottom_navigation);

        nav.setSelectedItemId(R.id.nav_icon2);

        // TODO: Save Fragment state when switching b/w fragments

        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_icon1:
                        selectedFragment = new ExpFragment();
                        break;
                    case R.id.nav_icon3:
                        selectedFragment = new NotesFragment();
                        break;
                    default:
                        selectedFragment = new TripFragment();
                        break;
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,selectedFragment)
                        .commit();

                return true;
            }
        });


    }
}