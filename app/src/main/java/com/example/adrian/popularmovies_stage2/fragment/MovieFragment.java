package com.example.adrian.popularmovies_stage2.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.adrian.popularmovies_stage2.R;
import com.example.adrian.popularmovies_stage2.activity.DetailsActivity;
import com.example.adrian.popularmovies_stage2.adapter.MoviesAdapter;
import com.example.adrian.popularmovies_stage2.data.model.Movie;
import com.example.adrian.popularmovies_stage2.data.rest.ApiUtils;
import com.example.adrian.popularmovies_stage2.data.rest.MovieApiService;

import java.util.ArrayList;

/**
 * Created by adrian on 18.02.2018.
 */

public abstract class MovieFragment extends Fragment {
    protected RecyclerView mRecyclerView;
    protected MoviesAdapter mAdapter;
    protected MovieApiService mService;
    protected ArrayList<Movie> movieList;
    protected boolean invalidateCache = false;

    protected final String TAG = "Movie Fragment";
    private static final String RECYCLERVIEW_STATE = "recyclerview-state";
    private static final String MOVIE_LIST_STATE = "movielist-state";
    Bundle instance;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        mService = ApiUtils.getMovieService();
        View myFragmentView = inflater.inflate(R.layout.movies_fragment, container, false);
        mRecyclerView = myFragmentView.findViewById(R.id.rv_movies);

        mAdapter = new MoviesAdapter(container.getContext(), new ArrayList<Movie>(0), new MoviesAdapter.MovieItemListener() {
            @Override
            public void onMovieClick(Movie movie) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class)
                        .putExtra("id", movie.getId())
                        .putExtra("title", movie.getTitle())
                        .putExtra("poster", movie.getPosterUrl())
                        .putExtra("cover", movie.getBackdropUrl())
                        .putExtra("synopsis", movie.getOverview())
                        .putExtra("rating", movie.getVoteAverage())
                        .putExtra("adult", movie.getAdult())
                        .putExtra("releaseDate", movie.getReleaseDate());
                startActivity(intent);
            }
        });

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(container.getContext(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        if (savedInstanceState != null) {
            instance = savedInstanceState;
            movieList = savedInstanceState.getParcelableArrayList(MOVIE_LIST_STATE);
        }
        if (movieList != null) {
            mAdapter.updateMovies(movieList);
            Toast.makeText(getContext(), "Updated from bundle", Toast.LENGTH_LONG).show();
        } else {
            invalidateCache = false;
            loadMovies();
            Toast.makeText(getContext(), "Updated by asynctask", Toast.LENGTH_SHORT).show();
        }
        return myFragmentView;
    }

    public abstract void loadMovies();

    public void setMovieList(ArrayList<Movie> movieList) {
        this.movieList = movieList;
    }

    public void showErrorMessage() {
        Toast.makeText(getContext(), "Error loading posts", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        super.onSaveInstanceState(savedInstanceState);
        Parcelable listState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        savedInstanceState.putParcelable(RECYCLERVIEW_STATE, listState);
        savedInstanceState.putParcelableArrayList(MOVIE_LIST_STATE, movieList);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(RECYCLERVIEW_STATE);
            if (savedRecyclerLayoutState != null) {
                mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
            }
        }
    }
}
