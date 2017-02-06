package com.avinsharma.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import  com.avinsharma.popularmovies.data.MovieContract.MovieColumns;
import  com.avinsharma.popularmovies.data.MovieContract.FavouriteMovieColumns;

/**
 * Created by Avin on 09-01-2017.
 */

public class MovieProvider extends ContentProvider {

    public static final int MOVIES = 100;
    public static final int MOVIE_ID = 101;
    public static final int FAVOURITE_MOVIES = 102;
    public static final int FAVOURITE_MOVIES_ID = 103;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        sUriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIE_ID);
        sUriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_FAVOURITE_MOVIES, FAVOURITE_MOVIES);
        sUriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_FAVOURITE_MOVIES + "/#", FAVOURITE_MOVIES_ID);
    }

    private MovieDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match){
            case MOVIES:
                cursor = database.query(MovieColumns.TABLE_NAME, strings, s, strings1, null, null, s1);
                break;
            case MOVIE_ID:
                s = MovieColumns._ID + "=?";
                strings1 = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(MovieColumns.TABLE_NAME, strings, s, strings1, null, null, s1);
                break;
            case FAVOURITE_MOVIES:
                cursor = database.query(FavouriteMovieColumns.TABLE_NAME, strings, s, strings1, null, null, s1);
                break;
            case FAVOURITE_MOVIES_ID:
                s = MovieColumns._ID + "=?";
                strings1 = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(FavouriteMovieColumns.TABLE_NAME, strings, s, strings1, null, null, s1);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        switch (match){
            case FAVOURITE_MOVIES:
                long _id = database.insert(FavouriteMovieColumns.TABLE_NAME, null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, _id);
            default:
                throw new IllegalArgumentException("Cannot insert unknown URI " + uri);
        }

    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        switch (match){
            case MOVIES:
                database.beginTransaction();
                int returnCount = 0;
                try{
                    for (ContentValues contentValues : values){
                        long _id = database.insert(MovieColumns.TABLE_NAME, null, contentValues);
                        if (_id != -1)
                            returnCount ++;
                    }
                    database.setTransactionSuccessful();
                }finally {
                    database.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int rowsDeleted = 0;
        switch (match){
            case MOVIES:
                rowsDeleted = database.delete(MovieColumns.TABLE_NAME, s, strings);
                break;
            case FAVOURITE_MOVIES:
                rowsDeleted = database.delete(FavouriteMovieColumns.TABLE_NAME, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Cannot delete unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}

