package com.avinsharma.popularmovies;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avinsharma.popularmovies.data.MovieColumns;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    ImageView poster;
    ImageView backdrop;
    TextView title;
    TextView synopsis;
    TextView releaseDate;
    TextView rating;

    private static final String LOG_TAG = DetailsFragment.class.getSimpleName();
    private static final int MOVIE_LOADER = 0;
    public static String DETAIL_URI = "detail_uri";

    Uri mUri = null;

    String[] projection = {
            MovieColumns._ID,
            MovieColumns.COLUMN_MOVIE_ID,
            MovieColumns.COLUMN_MOVIE_TITLE,
            MovieColumns.COLUMN_MOVIE_RATING,
            MovieColumns.COLUMN_SYNOPSIS,
            MovieColumns.COLUMN_RELEASE_DATE,
            MovieColumns.COLUMN_IMAGE_URL,
            MovieColumns.COLUMN_BACKGROUND_IMAGE,
    };

    public static final int _ID = 0;
    public static final int COLUMN_MOVIE_ID = 1;
    public static final int COLUMN_MOVIE_TITLE = 2;
    public static final int COLUMN_MOVIE_RATING = 3;
    public static final int COLUMN_SYNOPSIS = 4;
    public static final int COLUMN_RELEASE_DATE = 5;
    public static final int COLUMN_IMAGE_URL = 6;
    public static final int COLUMN_BACKGROUND_IMAGE = 7;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        setupViews(view);

        Bundle arguments = getArguments();
        if (arguments != null){
            mUri = arguments.getParcelable(DETAIL_URI);
        }
        Toast.makeText(getContext(), "Uri is: " + mUri, Toast.LENGTH_SHORT).show();
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);

        return view;
    }

    private void setupViews(View view){
        poster = (ImageView) view.findViewById(R.id.poster_imageview);
        backdrop = (ImageView) view.findViewById(R.id.backdrop_imageview);
        title = (TextView) view.findViewById(R.id.title_textview);
        synopsis = (TextView) view.findViewById(R.id.synopsis_textview);
        releaseDate = (TextView) view.findViewById(R.id.release_date_textview);
        rating = (TextView) view.findViewById(R.id.rating_textview);
    }

    private void updateUI(Cursor data){
        Picasso.with(getContext()).load(data.getString(COLUMN_IMAGE_URL)).into(poster);
        Picasso.with(getContext()).load(data.getString(COLUMN_BACKGROUND_IMAGE)).into(backdrop);
        title.setText(data.getString(COLUMN_MOVIE_TITLE));
        synopsis.setText(data.getString(COLUMN_SYNOPSIS));
        releaseDate.setText(data.getString(COLUMN_RELEASE_DATE));
        rating.setText(data.getString(COLUMN_MOVIE_RATING));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), mUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToNext()) {
            Log.v(LOG_TAG, data.getString(COLUMN_MOVIE_TITLE));
            updateUI(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Do nothing
    }

}
