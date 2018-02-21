package com.example.adrian.popularmovies_stage2.rest;

/**
 * Created by adrian on 17.02.2018.
 */

public class ApiUtils {
    public static final String BASE_URL = NetworkUtils.buildApiUrl();

    public static MovieApiService getMovieService() {
        return RetrofitClient.getClient(BASE_URL).create(MovieApiService.class);
    }
}
