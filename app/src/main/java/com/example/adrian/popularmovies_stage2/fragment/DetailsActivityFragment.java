package com.example.adrian.popularmovies_stage2.fragment;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adrian.popularmovies_stage2.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    public ImageView backdropImageView;
    public ImageView posterImageView;
    public TextView movieTitleTextView;
    public TextView releaseDateTextView;
    public TextView descriptionTextView;
    public TextView ratingTextView;
    public TextView familyTextView;
    public ImageButton bookmarkImageButton;

    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        findViews(view);

        bookmarkImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Bookmarked", Snackbar.LENGTH_SHORT).show();
            }
        });
        parseIntent();

        return view;
    }

    public void findViews(View view){
        backdropImageView = view.findViewById(R.id.iv_backdrop);
        posterImageView = view.findViewById(R.id.iv_poster);
        movieTitleTextView = view.findViewById(R.id.tv_movie_title);
        releaseDateTextView = view.findViewById(R.id.tv_release_date);
        descriptionTextView = view.findViewById(R.id.tv_description);
        ratingTextView = view.findViewById(R.id.tv_rating_percent);
        familyTextView = view.findViewById(R.id.tv_family);
        bookmarkImageButton = view.findViewById(R.id.btn_bookmark);
    }

    public void parseIntent(){
        Intent intent = getActivity().getIntent();

        String title = intent.getStringExtra("title");
        String posterUrl = intent.getStringExtra("poster");
        String coverUrl = intent.getStringExtra("cover");
        String synopsis = intent.getStringExtra("synopsis");
        Boolean adult = intent.getBooleanExtra("adult", false);
        Float rating = intent.getFloatExtra("rating",0);
        String releaseDate = intent.getStringExtra("releaseDate");

        populateUI(title,posterUrl,coverUrl,synopsis,adult,rating,releaseDate);
    }

    private void populateUI(String title, String posterUrl, String coverUrl, String synopsis, Boolean adult, float rating, String releaseDate){
        Picasso.with(getContext()).load(coverUrl).into(backdropImageView);
        Picasso.with(getContext()).load(posterUrl).into(posterImageView);
        movieTitleTextView.setText(title);
        releaseDateTextView.setText("Released: " + getFormattedData(releaseDate));
        descriptionTextView.setText(synopsis);
        if(adult){
           familyTextView.setText("Adult movie");
        }else {
            familyTextView.setText("Family movie");
        }
        ratingTextView.setText(ratingToPercent(rating));
    }

    private String ratingToPercent(float rating){
        return Math.round(rating * 10) + "%";
    }

    private String getFormattedData(String dateString){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            // handle exception here !
        }

        return DateFormat.getDateInstance(DateFormat.LONG).format(date);
    }
}
