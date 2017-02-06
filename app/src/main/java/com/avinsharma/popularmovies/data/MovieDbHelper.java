package com.avinsharma.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.avinsharma.popularmovies.data.MovieContract.MovieColumns;
import com.avinsharma.popularmovies.data.MovieContract.FavouriteMovieColumns;

/**
 * Created by Avin on 06-02-2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";
    public static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieColumns.TABLE_NAME + " ("
                + MovieColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieColumns.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                + MovieColumns.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, "
                + MovieColumns.COLUMN_MOVIE_RATING + " TEXT NOT NULL, "
                + MovieColumns.COLUMN_SYNOPSIS + " TEXT NOT NULL, "
                + MovieColumns.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
                + MovieColumns.COLUMN_IMAGE_URL + " TEXT NOT NULL, "
                + MovieColumns.COLUMN_BACKGROUND_IMAGE + " TEXT NOT NULL, "
                + MovieColumns.COLUMN_TYPE + " TEXT NOT NULL);";

        String SQL_CREATE_FAVOURITE_MOVIES_TABLE = "CREATE TABLE " + FavouriteMovieColumns.TABLE_NAME + " ("
                + FavouriteMovieColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FavouriteMovieColumns.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                + FavouriteMovieColumns.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, "
                + FavouriteMovieColumns.COLUMN_MOVIE_RATING + " TEXT NOT NULL, "
                + FavouriteMovieColumns.COLUMN_SYNOPSIS + " TEXT NOT NULL, "
                + FavouriteMovieColumns.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
                + FavouriteMovieColumns.COLUMN_IMAGE_URL + " TEXT NOT NULL, "
                + FavouriteMovieColumns.COLUMN_BACKGROUND_IMAGE + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITE_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieColumns.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavouriteMovieColumns.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
