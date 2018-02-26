package com.example.adrian.popularmovies_stage2.data.rest;

import android.net.Uri;

/**
 * Created by adrian on 17.02.2018.
 */

public class NetworkUtils {
    private static final String SCHEME = "https";
    private static final String BASE_API_URL = "api.themoviedb.org";
    private static final String BASE_IMAGE_URL = "image.tmdb.org";
    private static final String BASE_TRAILER_URL = "www.youtube.com";
    private static final String IMAGE_SIZE = "w500";
    private static final String API_VERSION = "3";

    public static String buildApiUrl() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME)
                .authority(BASE_API_URL)
                .appendPath(API_VERSION)
                .appendPath("");
        return builder.build().toString();
    }

    public static String buildPosterUrl(String posterQuery){
        // Remove trailing slash
        posterQuery = posterQuery.substring(1);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME)
                .authority(BASE_IMAGE_URL)
                .appendPath("t")
                .appendPath("p")
                .appendPath(IMAGE_SIZE)
                .appendPath(posterQuery);
        return builder.build().toString();
    }

    public static String buildTrailerUrl(String videoKey){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME)
                .authority(BASE_TRAILER_URL)
                .appendPath("watch")
                .appendQueryParameter("v", videoKey);
        return builder.build().toString();
    }
}
