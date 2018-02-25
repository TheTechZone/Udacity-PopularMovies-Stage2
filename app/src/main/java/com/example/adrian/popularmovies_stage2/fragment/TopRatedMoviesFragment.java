package com.example.adrian.popularmovies_stage2.fragment;

import android.util.Log;

import com.example.adrian.popularmovies_stage2.BuildConfig;
import com.example.adrian.popularmovies_stage2.model.Movie;
import com.example.adrian.popularmovies_stage2.model.MovieResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by adrian on 18.02.2018.
 */

public class TopRatedMoviesFragment extends MovieFragment {

    public void loadMovies() {
        String api = BuildConfig.THE_MOVIE_DB_API_TOKEN;
        mService.getTopRatedMovies(api).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if(response.isSuccessful()) {
                    mAdapter.updateMovies(response.body().getResults());
                    setMovieList((ArrayList<Movie>) response.body().getResults());
                    Log.d("MainActivity", "loaded posts from api");
                    onRestoreInstanceState(instance);
                }else {
                    int statusCode = response.code();
                    // code to handle errors based on status codes
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                showErrorMessage();
                Log.d("MainActivity", "error loading from API");
            }
        });
    }

}
