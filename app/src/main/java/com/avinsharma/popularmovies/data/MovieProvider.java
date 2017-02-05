package com.avinsharma.popularmovies.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by Avin on 09-01-2017.
 */

@ContentProvider(authority = MovieProvider.AUTHORITY, database = MovieDatabase.class)
public final class MovieProvider {
    public static final String AUTHORITY = "com.avinsharma.popularmovies";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String MOVIES = "movies";
        String FAVOURITE_MOVIES = "favourites";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = MovieDatabase.MOVIES)
    public static class Movies {

        @ContentUri(
                path = Path.MOVIES,
                type = "vnd.android.cursor.dir/movies")
        public static final Uri CONTENT_URI = buildUri(Path.MOVIES);


        @InexactContentUri(
                name = "MOVIE_SELECTED",
                path = Path.MOVIES + "/#",
                type = "vnd.android.cursor.item/movies",
                whereColumn = MovieColumns._ID,
                pathSegment = 1
        )
        public static Uri withId(long id){
            return buildUri(Path.MOVIES, String.valueOf(id));
        }
    }

    @TableEndpoint(table = MovieDatabase.FAVOURITE_MOVIES)
    public static class FavouriteMovies {

        @ContentUri(
                path = Path.FAVOURITE_MOVIES,
                type = "vnd.android.cursor.dir/favourites")
        public static final Uri CONTENT_URI = buildUri(Path.FAVOURITE_MOVIES);


        @InexactContentUri(
                name = "MOVIE_SELECTED",
                path = Path.FAVOURITE_MOVIES + "/#",
                type = "vnd.android.cursor.item/favourites",
                whereColumn = FavouriteMovieColumns._ID,
                pathSegment = 1
        )
        public static Uri withId(long id){
            return buildUri(Path.FAVOURITE_MOVIES, String.valueOf(id));
        }
    }
}


