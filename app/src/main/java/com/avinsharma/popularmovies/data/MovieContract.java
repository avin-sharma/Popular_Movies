package com.avinsharma.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Avin on 06-02-2017.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.avinsharma.popularmovies";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";
    public static final String PATH_FAVOURITE_MOVIES = "favourites";

    public static class MovieColumns implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIES);
        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_RATING = "rating";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_RELEASE_DATE = "date";
        public static final String COLUMN_IMAGE_URL = "image";
        public static final String COLUMN_BACKGROUND_IMAGE = "background";
        public static final String COLUMN_TYPE = "type";

    }

    public static class FavouriteMovieColumns implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FAVOURITE_MOVIES);
        public static final String TABLE_NAME = "favourites";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_RATING = "rating";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_RELEASE_DATE = "date";
        public static final String COLUMN_IMAGE_URL = "image";
        public static final String COLUMN_BACKGROUND_IMAGE = "background";

    }

}
