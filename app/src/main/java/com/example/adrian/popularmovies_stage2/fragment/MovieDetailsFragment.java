package com.example.adrian.popularmovies_stage2.fragment;

import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adrian.popularmovies_stage2.BuildConfig;
import com.example.adrian.popularmovies_stage2.R;
import com.example.adrian.popularmovies_stage2.activity.DetailsActivity;
import com.example.adrian.popularmovies_stage2.adapter.ReviewAdapter;
import com.example.adrian.popularmovies_stage2.model.Review;
import com.example.adrian.popularmovies_stage2.model.ReviewResponse;
import com.example.adrian.popularmovies_stage2.model.Trailer;
import com.example.adrian.popularmovies_stage2.model.TrailerResponse;
import com.example.adrian.popularmovies_stage2.rest.ApiUtils;
import com.example.adrian.popularmovies_stage2.rest.MovieApiService;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends DetailsFragment {

    protected List<Trailer> trailers;
    protected ArrayList<String> trailerTitles;
    protected ArrayList<String> trailerUrls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        mService = ApiUtils.getMovieService();
        movieId = getActivity().getIntent().getIntExtra("id",0);
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

    public void setTrailerDialogButtonListener(){
        trailerDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrailersDialogFragment dialogFragment = new TrailersDialogFragment();

                Bundle bundle = new Bundle();
                bundle.putStringArrayList("titles", (ArrayList<String>) trailerTitles);
                bundle.putStringArrayList("urls", (ArrayList<String>) trailerUrls);
                //bundle.putSerializable("trailers", (Serializable) trailers);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getFragmentManager(), "trailers");
            }
        });
    }


    public void loadTrailers(int movieId){
        String api = BuildConfig.THE_MOVIE_DB_API_TOKEN;
        mService.getMovieTrailers(movieId, api).enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                if(response.isSuccessful()){
                    List<Trailer> temptrailers = response.body().getResults();
                    trailers = temptrailers;
                    trailerTitles = new ArrayList<String>();
                    trailerUrls = new ArrayList<String>();
                    if(temptrailers.size() > 0 ) {
                        for (Trailer trailer :
                                temptrailers) {
                            //descriptionTextView.append(trailer.getTrailerUrl());
                            trailerTitles.add(trailer.getName());
                            trailerUrls.add(trailer.getTrailerUrl());
                        }
                    }
                    // Send the trailer list to the parent activity
                    ((DetailsActivity) getActivity()).setTrailers(trailers);
                }else {
                    // handle error
                }
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Log.d("DetailsFragment", "error loading from API");
            }
        });
    }

    public void loadReviews(int movieId){
        String api = BuildConfig.THE_MOVIE_DB_API_TOKEN;
        mService.getMovieReviews(movieId, api).enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if(response.isSuccessful()){
                    List<Review> reviews = response.body().getResults();
//                    if (reviews.size() > 0){
//                        for(Review rv : reviews){
//                            descriptionTextView.append("\n" + rv.getContent());
//                        }
//                    }
                    mAdapter.updateReviews(response.body().getResults());
                }else {
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
