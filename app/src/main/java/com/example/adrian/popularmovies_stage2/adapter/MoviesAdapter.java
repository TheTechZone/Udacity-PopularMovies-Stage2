package com.example.adrian.popularmovies_stage2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adrian.popularmovies_stage2.R;
import com.example.adrian.popularmovies_stage2.data.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by adrian on 17.02.2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private List<Movie> mMovies;
    private Context mContext;
    private MovieItemListener mMovieItemListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //public TextView titleTextView;
        @BindView(R.id.iv_poster) ImageView posterImageView;
        @BindView(R.id.tv_poster_title) TextView movieTitleTextView;
        @BindView(R.id.tv_poster_rating) TextView movieRatingTextView;

        MovieItemListener movieItemListener;

        public ViewHolder(Context context, View view, MovieItemListener movieItemListener){
            super(view);
            //titleTextView = view.findViewById(android.R.id.text1);
//            posterImageView = view.findViewById(R.id.iv_poster);
//            movieTitleTextView = view.findViewById(R.id.tv_poster_title);
//            movieRatingTextView = view.findViewById(R.id.tv_poster_rating);
            ButterKnife.bind(this, view);
            mContext = context;
            this.movieItemListener = movieItemListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Movie movie = getMovie(getAdapterPosition());
//            this.movieItemListener.onMovieClick(movie.getId());
            this.movieItemListener.onMovieClick(movie);
            notifyDataSetChanged();
        }
    }

    public MoviesAdapter(Context context, List<Movie> movies, MovieItemListener listener){
        this.mContext = context;
        this.mMovies = movies;
        this.mMovieItemListener = listener;
    }

    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View movieView = inflater.inflate(R.layout.poster, parent, false);

        ViewHolder viewHolder = new ViewHolder(context, movieView, this.mMovieItemListener);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MoviesAdapter.ViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        ImageView imageView = holder.posterImageView;
        Picasso.with(mContext)
                .load(movie.getPosterUrl())
                .into(imageView);
        holder.movieTitleTextView.setText(movie.getTitle());
        holder.movieRatingTextView.setText(String.format(mContext.getString(R.string.rating_out_of_ten), movie.getVoteAverage().toString()));

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public void updateMovies(List<Movie> movies){
        mMovies = movies;
        notifyDataSetChanged();
    }

    private Movie getMovie(int adapeterPos){
        return mMovies.get(adapeterPos);
    }

    public interface MovieItemListener {
        void onMovieClick(Movie movie);
    }
}
