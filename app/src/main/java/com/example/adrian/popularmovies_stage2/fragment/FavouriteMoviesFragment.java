package com.example.adrian.popularmovies_stage2.fragment;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adrian.popularmovies_stage2.R;
import com.example.adrian.popularmovies_stage2.data.MoviesContract;
import com.example.adrian.popularmovies_stage2.model.Movie;

import java.util.ArrayList;
import java.util.List;


public class FavouriteMoviesFragment extends MovieFragment {

    public List<Movie> movieList = new ArrayList<>();

    @Override
    public void loadMovies() {
        Cursor mCursor = getActivity().getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI,
                null,null,null,null);
        try{
            if(mCursor != null) {
                while (mCursor.moveToNext()) {
                    Movie tempMovie = new Movie();
                    tempMovie.setTitle(mCursor.getString(mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_TITLE)));
                    tempMovie.setOverview(mCursor.getString(mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_DESCRIPTION)));
                    tempMovie.setReleaseDate(mCursor.getString(mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE)));
                    tempMovie.setPosterUrl(mCursor.getString(mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POSTER_URL)));
                    tempMovie.setBackdropUrl(mCursor.getString(mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_BACKDROP_URL)));
                    tempMovie.setId(Integer.valueOf(mCursor.getString(mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIEDB_ID))));
                    tempMovie.setVoteAverage(mCursor.getFloat(mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RATING)));
                    tempMovie.setAdult(false);
                    movieList.add(tempMovie);
                }
                mAdapter.updateMovies(movieList);
            }
        }finally {
            mCursor.close();
        }
    }
}
