package com.avinsharma.popularmovies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Avin on 09-01-2017.
 */

public interface FavouriteMovieColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.INTEGER) @NotNull
    public static final String COLUMN_MOVIE_ID = "movie_id";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String COLUMN_MOVIE_TITLE = "title";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String COLUMN_MOVIE_RATING = "rating";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String COLUMN_SYNOPSIS = "synopsis";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String COLUMN_RELEASE_DATE = "date";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String COLUMN_IMAGE_URL = "image";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String COLUMN_BACKGROUND_IMAGE = "background";

}
