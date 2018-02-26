package com.example.adrian.popularmovies_stage2.data.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by adrian on 24.02.2018.
 */

public class MoviesProvider extends ContentProvider {
    private static final String DEBUG_TAG = MoviesProvider.class.getSimpleName();
    private static final UriMatcher uriMatcher = buildUriMatcher();
    private MoviesDBHelper mHelper;
    // UriMatcher Codes
    private static final int MOVIE = 100;
    private static final int MOVIE_WITH_ID = 101;

    @Override
    public boolean onCreate() {
        mHelper = new MoviesDBHelper(getContext());
        return true;
    }

    private static UriMatcher buildUriMatcher(){
        // Build a UriMatcher by adding a specific code to return based on a match
        // It's common to use NO_MATCH as the code for this case.
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, MoviesContract.MovieEntry.TABLE_MOVIES, MOVIE);
        uriMatcher.addURI(authority, MoviesContract.MovieEntry.TABLE_MOVIES + "/#", MOVIE_WITH_ID);
        return uriMatcher;
    }

    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match){
            case MOVIE:
                return MoviesContract.MovieEntry.CONTENT_DIR_TYPE;
            case MOVIE_WITH_ID:
                return MoviesContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor dataCursor;
        switch (uriMatcher.match(uri)){
            case MOVIE:{
                dataCursor = mHelper.getReadableDatabase().query(
                        MoviesContract.MovieEntry.TABLE_MOVIES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return dataCursor;
            }
            case MOVIE_WITH_ID:{
                dataCursor = mHelper.getReadableDatabase().query(
                        MoviesContract.MovieEntry.TABLE_MOVIES,
                        projection,
                        MoviesContract.MovieEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                return dataCursor;
            }
            default:{
                // Assume a bad URI
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        Uri returnUri;
        final int match = uriMatcher.match(uri);
        switch (match){
            case MOVIE:{
                long _id = db.insert(MoviesContract.MovieEntry.TABLE_MOVIES, null, values);
                if(_id > 0){
                    returnUri = MoviesContract.MovieEntry.buildMoviesUri(_id);
                }else {
                    throw new SQLException("Failed to insert data into row: " + uri);
                }
                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri,String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int countDeleted;
        switch (match){
            case MOVIE:{
                countDeleted = db.delete(MoviesContract.MovieEntry.TABLE_MOVIES,
                        selection, selectionArgs);
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MoviesContract.MovieEntry.TABLE_MOVIES + "'");
                break;
            }
            case MOVIE_WITH_ID:{
                countDeleted = db.delete(MoviesContract.MovieEntry.TABLE_MOVIES,
                        MoviesContract.MovieEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MoviesContract.MovieEntry.TABLE_MOVIES + "'");
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return countDeleted;
    }

    @Override
    public int bulkInsert(Uri uri,ContentValues[] values) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        switch (match){
            case MOVIE:{
                db.beginTransaction();
                int countInserted = 0;
                try {
                    for (ContentValues value : values){
                        if (value == null){
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long _id = -1;
                        try{
                            _id = db.insertOrThrow(MoviesContract.MovieEntry.TABLE_MOVIES,
                                    null, value);
                        }catch(SQLiteConstraintException e) {
                            Log.w(DEBUG_TAG, "Attempting to insert " +
                                    value.getAsString(
                                            MoviesContract.MovieEntry.COLUMN_MOVIEDB_ID)
                                    + " but value is already in database.");
                        }
                        if (_id != -1){
                            countInserted++;
                        }
                    }
                } finally {
                    db.endTransaction();
                }
                if (countInserted > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return countInserted;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        int numUpdated = 0;

        if (contentValues == null){
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch(uriMatcher.match(uri)){
            case MOVIE:{
                numUpdated = db.update(MoviesContract.MovieEntry.TABLE_MOVIES,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            case MOVIE_WITH_ID: {
                numUpdated = db.update(MoviesContract.MovieEntry.TABLE_MOVIES,
                        contentValues,
                        MoviesContract.MovieEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numUpdated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }
}
