package com.avinsharma.popularmovies.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Avin on 09-01-2017.
 */

@Database(version = MovieDatabase.VERSION)
public final  class MovieDatabase {
    private MovieDatabase(){}

    public static final int VERSION = 1;

    @Table(MovieColumns.class) public static final String MOVIES = "movies";

    @Table(FavouriteMovieColumns.class) public static final String FAVOURITE_MOVIES = "favourites";

}
