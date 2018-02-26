package com.example.adrian.popularmovies_stage2.data.rest;


public class ApiUtils {
    public static final String BASE_URL = NetworkUtils.buildApiUrl();

    public static MovieApiService getMovieService() {
        return RetrofitClient.getClient(BASE_URL).create(MovieApiService.class);
    }
}
