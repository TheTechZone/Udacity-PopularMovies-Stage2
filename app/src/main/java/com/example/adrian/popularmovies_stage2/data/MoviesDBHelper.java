package com.example.adrian.popularmovies_stage2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by adrian on 24.02.2018.
 */

public class MoviesDBHelper extends SQLiteOpenHelper {
    private static final String DEBUG_TAG = MoviesDBHelper.class.getSimpleName();

    private static final String DB_NAME = "movies.db";
    private static final int DB_VERSION = 1;

    public MoviesDBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MoviesContract.MovieEntry.TABLE_MOVIES + "(" + MoviesContract.MovieEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_RATING + " REAL NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_POSTER_URL + " TEXT NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_BACKDROP_URL + " TEXT NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_MOVIEDB_ID + " TEXT NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(DEBUG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MovieEntry.TABLE_MOVIES);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                MoviesContract.MovieEntry.TABLE_MOVIES + "'");
        // re-create database
        onCreate(sqLiteDatabase);
    }
}
