package com.example.adrian.popularmovies_stage2.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adrian.popularmovies_stage2.R;
import com.example.adrian.popularmovies_stage2.adapter.ReviewAdapter;
import com.example.adrian.popularmovies_stage2.data.provider.MoviesContract;
import com.example.adrian.popularmovies_stage2.event.UpdateAdapterEvent;
import com.example.adrian.popularmovies_stage2.data.rest.MovieApiService;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsFragment extends Fragment {

    protected String TAG = "Details fragment";
    public int movieId;

    @BindView(R.id.iv_backdrop)
    ImageView backdropImageView;
    @BindView(R.id.iv_poster)
    ImageView posterImageView;
    @BindView(R.id.tv_movie_title)
    TextView movieTitleTextView;
    @BindView(R.id.tv_release_date)
    TextView releaseDateTextView;
    @BindView(R.id.tv_description)
    TextView descriptionTextView;
    @BindView(R.id.tv_rating_percent)
    TextView ratingTextView;
    @BindView(R.id.tv_family)
    TextView familyTextView;
    @BindView(R.id.btn_bookmark)
    ImageButton bookmarkImageButton;
    @BindView(R.id.btn_trailer_dialog)
    Button trailerDialogButton;

    protected MovieApiService mService;
    protected RecyclerView mRecyclerView;
    protected ReviewAdapter mAdapter;

    public String title;
    public String posterUrl;
    public String coverUrl;
    public String synopsis;
    public float rating;
    public boolean adult;
    public String releaseDate;

    public void findViews(View view) {
//        backdropImageView = view.findViewById(R.id.iv_backdrop);
//        posterImageView = view.findViewById(R.id.iv_poster);
//        movieTitleTextView = view.findViewById(R.id.tv_movie_title);
//        releaseDateTextView = view.findViewById(R.id.tv_release_date);
//        descriptionTextView = view.findViewById(R.id.tv_description);
//        ratingTextView = view.findViewById(R.id.tv_rating_percent);
//        familyTextView = view.findViewById(R.id.tv_family);
//        bookmarkImageButton = view.findViewById(R.id.btn_bookmark);
//        trailerDialogButton = view.findViewById(R.id.btn_trailer_dialog);
        ButterKnife.bind(this, view);
    }

    public void parseIntent() {
        Intent intent = getActivity().getIntent();

        title = intent.getStringExtra("title");
        posterUrl = intent.getStringExtra("poster");
        coverUrl = intent.getStringExtra("cover");
        synopsis = intent.getStringExtra("synopsis");
        adult = intent.getBooleanExtra("adult", false);
        rating = intent.getFloatExtra("rating", 0);
        releaseDate = intent.getStringExtra("releaseDate");

        populateUI(title, posterUrl, coverUrl, synopsis, adult, rating, releaseDate);
    }

    private void populateUI(String title, String posterUrl, String coverUrl, String synopsis, Boolean adult, float rating, String releaseDate) {
        Picasso.with(getContext()).load(coverUrl).into(backdropImageView);
        Picasso.with(getContext()).load(posterUrl).into(posterImageView);
        movieTitleTextView.setText(title);
        releaseDateTextView.setText(String.format(getString(R.string.date_released), getFormattedData(releaseDate)));
        descriptionTextView.setText(synopsis);
        if (isMovieInDatabase()) {
            bookmarkImageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmarked));
        }
        if (adult) {
            familyTextView.setText(R.string.adult_movie);
        } else {
            familyTextView.setText(R.string.non_adult_movie);
        }
        ratingTextView.setText(ratingToPercent(rating));
    }

    private String ratingToPercent(float rating) {
        return Math.round(rating * 10) + "%";
    }

    private String getFormattedData(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            // handle exception here !
        }

        return DateFormat.getDateInstance(DateFormat.LONG).format(date);
    }

    public void setBookmarkButton() {
        bookmarkImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isMovieInDatabase()) {
                    // Add to db
                    addMovieToDB();
                    bookmarkImageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmarked));
                    Snackbar.make(view, R.string.added_favorites, Snackbar.LENGTH_SHORT).show();
                } else {
                    // Remove from db
                    removeFromDB();
                    bookmarkImageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark));
                    Snackbar.make(view, R.string.removed_favorites, Snackbar.LENGTH_SHORT).show();
                }
                UpdateAdapterEvent event = new UpdateAdapterEvent();
                event.setShouldUpdate(true);
                org.greenrobot.eventbus.EventBus.getDefault().post(event);
            }
        });
    }

    protected boolean isMovieInDatabase() {
        Cursor c = getActivity().getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI,
                new String[]{MoviesContract.MovieEntry.COLUMN_MOVIEDB_ID},
                MoviesContract.MovieEntry.COLUMN_MOVIEDB_ID + " = " + movieId,
                null, null, null);
        if (c != null) {
            c.close();
        }
        int a = 0;
        if (c != null) {
            a = c.getCount();
        }
        return (a > 0);
    }

    protected void addMovieToDB() {
        ContentValues movie = new ContentValues(1);
        movie.put(MoviesContract.MovieEntry.COLUMN_TITLE, title);
        movie.put(MoviesContract.MovieEntry.COLUMN_DESCRIPTION, synopsis);
        movie.put(MoviesContract.MovieEntry.COLUMN_POSTER_URL, posterUrl);
        movie.put(MoviesContract.MovieEntry.COLUMN_BACKDROP_URL, coverUrl);
        movie.put(MoviesContract.MovieEntry.COLUMN_MOVIEDB_ID, movieId);
        movie.put(MoviesContract.MovieEntry.COLUMN_RATING, rating);
        movie.put(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
        getActivity().getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, movie);
    }

    protected void removeFromDB() {
        getActivity().getContentResolver().delete(MoviesContract.MovieEntry.CONTENT_URI,
                MoviesContract.MovieEntry.COLUMN_MOVIEDB_ID + " = " + movieId, null);
    }
}
