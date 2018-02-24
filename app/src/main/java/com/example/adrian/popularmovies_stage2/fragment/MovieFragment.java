package com.example.adrian.popularmovies_stage2.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.adrian.popularmovies_stage2.model.Movie;
import com.example.adrian.popularmovies_stage2.rest.ApiUtils;
import com.example.adrian.popularmovies_stage2.rest.MovieApiService;

import java.util.ArrayList;

/**
 * Created by adrian on 18.02.2018.
 */

public abstract class MovieFragment extends Fragment {
    protected RecyclerView mRecyclerView;
    protected MoviesAdapter mAdapter;
    protected MovieApiService mService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        mService = ApiUtils.getMovieService();
        View myFragmentView = inflater.inflate(R.layout.movies_fragment, container, false);
        mRecyclerView = myFragmentView.findViewById(R.id.rv_movies);

        mAdapter = new MoviesAdapter(container.getContext(), new ArrayList<Movie>(0), new MoviesAdapter.MovieItemListener() {
            @Override
            public void onMovieClick(Movie movie) {
//                Toast.makeText(container.getContext(), movie.getPopularity() + " /", Toast.LENGTH_LONG).show();
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

        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(container.getContext(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        loadMovies();

        return myFragmentView;
    }

    public abstract void loadMovies();

    public void showErrorMessage() {
        Toast.makeText(getContext(), "Error loading posts", Toast.LENGTH_SHORT).show();
    }
}
