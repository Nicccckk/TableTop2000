package com.example.tabletop2000;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter{

    ViewPager2 viewPager2;
    ArrayList<Fragment> fragments;

//  Need to send through the List of fragment objects
    public ViewPagerAdapter(@NonNull @NotNull Fragment fragment, ViewPager2 viewPager2, ArrayList<Fragment> fragments) {
        super(fragment);
        this.viewPager2 = viewPager2;
        this.fragments = fragments;
    }

//  Create Fragments
    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

//  Determines how many fragments, and therefore how many fragments are created
    @Override
    public int getItemCount() {
        return fragments.size();
    }

    @NonNull public Fragment getItem(int position)
    {
        return fragments.get(position);
    }

}
