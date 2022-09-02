package com.mitulagr.tripplanner;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private Adapter_ViewPager pagerAdapter;

    private int id;
    private DBHandler db;
    int days;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_trip, container, false);

        navDay = (RecyclerView) rootView.findViewById(R.id.tripday);
        navHome = (ImageView) rootView.findViewById(R.id.triphome);
        navAdd = (TextView) rootView.findViewById(R.id.tripadd);
        navDiv = (View) rootView.findViewById(R.id.divider7);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        id = sp.getInt("Current Trip", 0);
        db = new DBHandler(getContext());

        days = db.getDaysCount(id);

        /*
        =============================================================================
        Days Adapter
        =============================================================================
         */

//        TripHomeFragment frag = new TripHomeFragment();
//        androidx.fragment.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.trip_container, frag);
//        transaction.commit();

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new TripHomeFragment());
        TextView TvCity, TvDate, TvDay, TvDes;
        Day day;
        for(int i=1; i<=days; i++){
            fragmentList.add(TripDayFragment.newInstance(i));
//            TvCity = fragmentList.get(i).getView().findViewById(R.id.textView35);
//            TvDate = fragmentList.get(i).getView().findViewById(R.id.textView36);
//            TvDay = fragmentList.get(i).getView().findViewById(R.id.textView37);
//            TvDes = fragmentList.get(i).getView().findViewById(R.id.textView23);
//            day = db.getDay(id,i-1);
//            TvCity.setText(day.city);
//            TvDate.setText(dispDate(day.date));
//            TvDay.setText(day.day);
//            TvDes.setText(day.des);
        }

        pager = rootView.findViewById(R.id.trip_container);
        pagerAdapter = new Adapter_ViewPager(getActivity().getSupportFragmentManager(), getLifecycle(), fragmentList);
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

        adn.setOnItemLongClickListener(new Adapter_TripNav.LongClickListener() {
            @Override
            public void onItemLongClickListener(View view, int position) {
                pager.setCurrentItem(position+1, true);
                showEdit(view,position);
            }
        });

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

        navAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Day day = new Day();
                if(days>0){
                    Day prev = db.getDay(id,days-1);
                    day.city = prev.city;
                    day.date = getNextDate(prev.date,1);
                }
                else{
                    Trip t = db.getTrip(id);
                    day.city = t.place;
                    day.date = getNextDate(t.depDate,0);
                }
                day.day = getCurDay(day.date);
                day.des = "";
                day.fid = id;
                day.id = db.getDayNewId();
                db.addDay(day);
                days++;
                adn.n = days;
                pagerAdapter.fragmentList.add(TripDayFragment.newInstance(days));
                pagerAdapter.notifyDataSetChanged();
                pager.setAdapter(pagerAdapter);
                pager.setCurrentItem(days,true);
                adn.notifyDataSetChanged();
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

    void showEdit(View view, int pos) {
        PopupMenu popup = new PopupMenu(getActivity(), view);
        popup.getMenuInflater()
                .inflate(R.menu.edit3, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.e1:
                        Day Dpos = db.getDay(id,pos);
                        List<Activity> AList = db.getAllActivities(Dpos.id);
                        db.deleteDay(Dpos);
                        for(int i=0;i<AList.size();i++) db.deleteActivity(AList.get(i));
                        days--;
                        adn.n = days;
                        List<Day> DList= db.getAllDays(id);
                        for(int i = DList.size()-1;i>pos;i--){
                            DList.get(i).date = DList.get(i-1).date;
                            DList.get(i).day = DList.get(i-1).day;
                            db.updateDay(DList.get(i));
                        }
                        if(DList.size()>0 && pos<DList.size()){
                            DList.get(pos).date = Dpos.date;
                            DList.get(pos).day = Dpos.day;
                            db.updateDay(DList.get(pos));
                        }
                        List<Fragment> FList = new ArrayList<>();
                        FList.add(new TripHomeFragment());
                        for(int i=1; i<=days; i++) {
                            FList.add(TripDayFragment.newInstance(i));
                        }
                        pagerAdapter.fragmentList = FList;
                        pagerAdapter.notifyDataSetChanged();
                        pager.setAdapter(pagerAdapter);
                        adn.notifyDataSetChanged();
                        pager.setCurrentItem(pos,true);
                        break;
                    case R.id.e2:
                        if (pos == 0) break;
                        Day d1 = db.getDay(id, pos - 1);
                        Day d2 = db.getDay(id, pos);
                        List<Activity> ActList1 = db.getAllActivities(d1.id);
                        List<Activity> ActList2 = db.getAllActivities(d2.id);
                        for (int i = 0; i < ActList1.size(); i++) {
                            ActList1.get(i).fid = d2.id;
                            db.updateActivity(ActList1.get(i));
                        }
                        for (int i = 0; i < ActList2.size(); i++) {
                            ActList2.get(i).fid = d1.id;
                            db.updateActivity(ActList2.get(i));
                        }
                        String st = d1.date;
                        d1.date = d2.date;
                        d2.date = st;
                        st = d1.day;
                        d1.day = d2.day;
                        d2.day = st;
                        int temp = d1.id;
                        d1.id = d2.id;
                        d2.id = temp;
                        db.updateDay(d1);
                        db.updateDay(d2);
                        TripDayFragment F1 = (TripDayFragment) pagerAdapter.fragmentList.get(pos);
                        if(F1.db!=null) F1.refresh();
                        TripDayFragment F2 = (TripDayFragment) pagerAdapter.fragmentList.get(pos+1);
                        if(F2.db!=null) F2.refresh();
                        pager.setCurrentItem(pos, true);
                        adn.notifyDataSetChanged();
                        break;
                    case R.id.e3:
                        if (pos == adn.getItemCount() - 1) break;
                        Day d3 = db.getDay(id, pos);
                        Day d4 = db.getDay(id, pos + 1);
                        List<Activity> ActList3 = db.getAllActivities(d3.id);
                        List<Activity> ActList4 = db.getAllActivities(d4.id);
                        for (int i = 0; i < ActList3.size(); i++) {
                            ActList3.get(i).fid = d4.id;
                            db.updateActivity(ActList3.get(i));
                        }
                        for (int i = 0; i < ActList4.size(); i++) {
                            ActList4.get(i).fid = d3.id;
                            db.updateActivity(ActList4.get(i));
                        }
                        String st2 = d3.date;
                        d3.date = d4.date;
                        d4.date = st2;
                        st2 = d3.day;
                        d3.day = d4.day;
                        d4.day = st2;
                        int temp2 = d3.id;
                        d3.id = d4.id;
                        d4.id = temp2;
                        db.updateDay(d3);
                        db.updateDay(d4);
                        TripDayFragment F3 = (TripDayFragment) pagerAdapter.fragmentList.get(pos+1);
                        if(F3.db!=null) F3.refresh();
                        TripDayFragment F4 = (TripDayFragment) pagerAdapter.fragmentList.get(pos+2);
                        if(F4.db!=null) F4.refresh();
                        pager.setCurrentItem(pos+2, true);
                        adn.notifyDataSetChanged();
                        break;
                }

                return true;
            }
        });

        popup.show();

    }

}