package com.mitulagr.tripplanner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class Adapter_ViewPager extends FragmentStateAdapter {

    List<Fragment> fragmentList;

    public Adapter_ViewPager(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Fragment> fragmentList) {
        super(fragmentManager, lifecycle);
        this.fragmentList = fragmentList;
    }


    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

}
