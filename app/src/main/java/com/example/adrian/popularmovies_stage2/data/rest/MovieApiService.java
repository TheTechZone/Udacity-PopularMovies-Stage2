package com.example.adrian.popularmovies_stage2.data.rest;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.Call;

import com.example.adrian.popularmovies_stage2.data.model.MovieResponse;
import com.example.adrian.popularmovies_stage2.data.model.ReviewResponse;
import com.example.adrian.popularmovies_stage2.data.model.TrailerResponse;

public interface MovieApiService {
    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MovieResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<TrailerResponse> getMovieTrailers(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewResponse> getMovieReviews(@Path("id") int id, @Query("api_key") String apiKey);
}
