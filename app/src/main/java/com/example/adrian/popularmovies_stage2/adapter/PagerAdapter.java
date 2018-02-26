package com.example.adrian.popularmovies_stage2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.adrian.popularmovies_stage2.fragment.PopularMoviesFragment;
import com.example.adrian.popularmovies_stage2.fragment.FavouriteMoviesFragment;
import com.example.adrian.popularmovies_stage2.fragment.TopRatedMoviesFragment;


public class PagerAdapter extends FragmentStatePagerAdapter {
    int tabNumber;

    public PagerAdapter(FragmentManager fm, int tabNumber) {
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
                FavouriteMoviesFragment tab3 = new FavouriteMoviesFragment();
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
