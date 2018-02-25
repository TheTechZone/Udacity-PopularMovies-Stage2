package com.example.adrian.popularmovies_stage2.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adrian.popularmovies_stage2.BuildConfig;
import com.example.adrian.popularmovies_stage2.R;
import com.example.adrian.popularmovies_stage2.activity.DetailsActivity;
import com.example.adrian.popularmovies_stage2.adapter.ReviewAdapter;
import com.example.adrian.popularmovies_stage2.model.Review;
import com.example.adrian.popularmovies_stage2.model.ReviewResponse;
import com.example.adrian.popularmovies_stage2.model.Trailer;
import com.example.adrian.popularmovies_stage2.model.TrailerResponse;
import com.example.adrian.popularmovies_stage2.rest.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsFragment extends DetailsFragment {

    protected List<Trailer> trailers;
    protected ArrayList<String> trailerTitles;
    protected ArrayList<String> trailerUrls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        mService = ApiUtils.getMovieService();
        movieId = getActivity().getIntent().getIntExtra("id", 0);
        findViews(view);

        setBookmarkButton();
        setTrailerDialogButtonListener();

        mRecyclerView = view.findViewById(R.id.rv_reviews);
        mAdapter = new ReviewAdapter(getContext(), new ArrayList<Review>(0));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        parseIntent();
        loadTrailers(movieId);
        loadReviews(movieId);
        return view;
    }

    public void setTrailerDialogButtonListener() {
        trailerDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrailersDialogFragment dialogFragment = new TrailersDialogFragment();

                Bundle bundle = new Bundle();
                bundle.putStringArrayList("titles", (ArrayList<String>) trailerTitles);
                bundle.putStringArrayList("urls", (ArrayList<String>) trailerUrls);

                dialogFragment.setArguments(bundle);
                dialogFragment.show(getFragmentManager(), "trailers");
            }
        });
    }


    public void loadTrailers(int movieId) {
        String api = BuildConfig.THE_MOVIE_DB_API_TOKEN;
        mService.getMovieTrailers(movieId, api).enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                if (response.isSuccessful()) {
                    List<Trailer> temptrailers = response.body().getResults();
                    trailers = temptrailers;
                    trailerTitles = new ArrayList<String>();
                    trailerUrls = new ArrayList<String>();
                    if (temptrailers.size() > 0) {
                        for (Trailer trailer :
                                temptrailers) {
                            //descriptionTextView.append(trailer.getTrailerUrl());
                            trailerTitles.add(trailer.getName());
                            trailerUrls.add(trailer.getTrailerUrl());
                        }
                    }
                    // Send the trailer list to the parent activity
                    ((DetailsActivity) getActivity()).setTrailers(trailers);
                } else {
                    // handle error
                }
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Log.d("DetailsFragment", "error loading from API");
            }
        });
    }

    public void loadReviews(int movieId) {
        String api = BuildConfig.THE_MOVIE_DB_API_TOKEN;
        mService.getMovieReviews(movieId, api).enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.isSuccessful()) {
                    List<Review> reviews = response.body().getResults();
                    mAdapter.updateReviews(reviews);
                } else {
                    // handle error
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Log.d("DetailsFragment", "error loading from API");
            }
        });
    }
}
