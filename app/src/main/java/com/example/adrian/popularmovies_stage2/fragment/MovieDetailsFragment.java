package com.example.adrian.popularmovies_stage2.fragment;

import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        mService = ApiUtils.getMovieService();
        movieId = getActivity().getIntent().getIntExtra("id",0);
        findViews(view);

        setBookmarkButton();
        setTrailerDialogButtonListener();

        parseIntent();
        loadTrailers(movieId);
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
                }else {
                    // handle error
                }
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Log.d("MainActivity", "error loading from API");
            }
        });
    }
}
