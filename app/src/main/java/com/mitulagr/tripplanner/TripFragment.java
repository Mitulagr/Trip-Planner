package com.mitulagr.tripplanner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TripFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TripFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TripFragment newInstance(String param1, String param2) {
        TripFragment fragment = new TripFragment();
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

    private RecyclerView navDay;
    private ImageView navHome;
    private TextView navAdd;
    private View navDiv;
    private ViewPager2 pager;
    private Adapter_TripNav adn;

    private int days=5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_trip, container, false);

        navDay = (RecyclerView) rootView.findViewById(R.id.tripday);
        navHome = (ImageView) rootView.findViewById(R.id.triphome);
        navAdd = (TextView) rootView.findViewById(R.id.tripadd);
        navDiv = (View) rootView.findViewById(R.id.divider7);


//        TripHomeFragment frag = new TripHomeFragment();
//        androidx.fragment.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.trip_container, frag);
//        transaction.commit();

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new TripHomeFragment());
        for(int i=1; i<=days; i++){
            fragmentList.add(TripDayFragment.newInstance(i));
        }

        pager = rootView.findViewById(R.id.trip_container);
        Adapter_ViewPager pagerAdapter = new Adapter_ViewPager(getActivity().getSupportFragmentManager(), getLifecycle(), fragmentList);
        pager.setAdapter(pagerAdapter);

        if (pager.getChildAt(0) instanceof RecyclerView) pager.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        /*
        =============================================================================
        Day-Wise Navigation
        =============================================================================
         */

        navDay.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        adn = new Adapter_TripNav(days);

        navDay.setAdapter(adn);

        navDay.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), navDay, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //onDayClick(position);
                pager.setCurrentItem(position+1,true);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onHomeClick();
                pager.setCurrentItem(0,true);
            }
        });


        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position==0) onHomeClick();
                else onDayClick(position-1);
            }
        });

        return rootView;
    }


    void onDayClick(int position){
        if(adn.selected==-1){
            navHome.setImageResource(R.drawable.ic_baseline_home_24);
            navDiv.setVisibility(View.INVISIBLE);
        }
        if(position>adn.selected && position!=adn.getItemCount()-1) navDay.scrollToPosition(position+1);
        else if(position<adn.selected && position!=0) navDay.scrollToPosition(position-1);
        else navDay.scrollToPosition(position);
        adn.updateColor(position);
    }

    void onHomeClick(){
        navHome.setImageResource(R.drawable.ic_baseline_home_24_3);
        navDiv.setVisibility(View.VISIBLE);
        adn.updateColor(-1);
        navDay.scrollToPosition(0);
    }


}