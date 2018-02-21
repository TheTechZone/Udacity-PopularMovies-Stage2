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
import com.example.adrian.popularmovies_stage2.activity.DetailsActivityFragment;
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
                Intent intent = new Intent(getActivity(), DetailsActivity.class)
                        .putExtra("title", movie.getTitle())
                        .putExtra("poster", movie.getPosterUrl())
                        .putExtra("synopsis", movie.getOverview())
                        .putExtra("userRating", movie.getVoteAverage())
                        .putExtra("releaseDate", movie.getReleaseDate());
                startActivity(intent);
                Toast.makeText(container.getContext(), "Movie desc: " + movie.getOverview() , Toast.LENGTH_LONG).show();
            }
//            @Override
//            public void onMovieClick(long id) {
//                Toast.makeText(container.getContext(), "Movie id is: " + id, Toast.LENGTH_LONG).show();
//            }
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
