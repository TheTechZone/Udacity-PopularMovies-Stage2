package com.example.adrian.popularmovies_stage2.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.example.adrian.popularmovies_stage2.data.provider.MoviesContract;
import com.example.adrian.popularmovies_stage2.event.UpdateAdapterEvent;
import com.example.adrian.popularmovies_stage2.data.model.Movie;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;


public class FavouriteMoviesFragment extends MovieFragment {

    public List<Movie> movieList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(UpdateAdapterEvent event) {
        Log.d("EVENTBUS", "EVENT FIRED! " + event.isShouldUpdate());
        if (event.isShouldUpdate()) {
            // reload the data inside the fragment
            movieList = null;
            invalidateCache = true;
            loadMovies();
        }
    }

    @Override
    public void loadMovies() {
        Cursor mCursor = getActivity().getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI,
                null, null, null,
                MoviesContract.MovieEntry._ID + " DESC");
        try {
            if (mCursor != null) {
                movieList = new ArrayList<>();
                // Getting the value for each column in order to reduce the necessary calls
                int titleColumn = mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_TITLE);
                int descriptionColumn = mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_DESCRIPTION);
                int releaseDateColumn = mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE);
                int posterColumn = mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POSTER_URL);
                int backdropColumn = mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_BACKDROP_URL);
                int movieDbColumn = mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIEDB_ID);
                int ratingColumn = mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RATING);
                while (mCursor.moveToNext()) {
                    Movie tempMovie = new Movie();
                    tempMovie.setTitle(mCursor.getString(titleColumn));
                    tempMovie.setOverview(mCursor.getString(descriptionColumn));
                    tempMovie.setReleaseDate(mCursor.getString(releaseDateColumn));
                    tempMovie.setPosterUrl(mCursor.getString(posterColumn));
                    tempMovie.setBackdropUrl(mCursor.getString(backdropColumn));
                    tempMovie.setId(Integer.valueOf(mCursor.getString(movieDbColumn)));
                    tempMovie.setVoteAverage(mCursor.getFloat(ratingColumn));
                    tempMovie.setAdult(false);
                    movieList.add(tempMovie);
                }
                mAdapter.updateMovies(movieList);
                setMovieList((ArrayList<Movie>) movieList);
                onRestoreInstanceState(instance);
            }
        } finally {
            mCursor.close();
        }
    }
}
