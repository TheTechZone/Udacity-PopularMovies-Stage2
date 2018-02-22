package com.example.adrian.popularmovies_stage2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.adrian.popularmovies_stage2.fragment.PopularMoviesFragment;
import com.example.adrian.popularmovies_stage2.fragment.TabFragment1;
import com.example.adrian.popularmovies_stage2.fragment.TopRatedMoviesFragment;

/**
 * Created by adrian on 18.02.2018.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int tabNumber;

    public PagerAdapter(FragmentManager fm, int tabNumber){
        super(fm);
        this.tabNumber = tabNumber;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PopularMoviesFragment tab1 = new PopularMoviesFragment();
                return tab1;
            case 1:
                TopRatedMoviesFragment tab2 = new TopRatedMoviesFragment();
                return tab2;
            case 2:
                TabFragment1 tab3 = new TabFragment1();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabNumber;
    }
}