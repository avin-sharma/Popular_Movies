package com.avinsharma.popularmovies;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avinsharma.popularmovies.adapter.MovieCursorAdapter;
import com.avinsharma.popularmovies.data.FavouriteMovieColumns;
import com.avinsharma.popularmovies.data.MovieColumns;
import com.avinsharma.popularmovies.data.MovieProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class GridFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    String[] projection = {
            MovieColumns._ID,
            MovieColumns.COLUMN_MOVIE_TITLE,
            MovieColumns.COLUMN_IMAGE_URL,
            MovieColumns.COLUMN_MOVIE_ID
    };

    public static final int _ID = 0;
    public static final int COLUMN_MOVIE_TITLE = 1;
    public static final int COLUMN_IMAGE_URL = 2;
    public static final int COLUMN_MOVIE_ID = 3;


    private RecyclerView mGridView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MovieCursorAdapter mAdapter;
    private ProgressBar progressBar;
    private TextView empty;

    public static final String LOG_TAG = GridFragment.class.getSimpleName();
    public static final int MOVIE_LOADER_ID = 0;
    private String mSortOrder;

    public GridFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grid, container, false);
        mGridView = (RecyclerView) view.findViewById(R.id.grid_recycler_view);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        empty = (TextView) view.findViewById(R.id.empty_textview);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mGridView.setLayoutManager(mLayoutManager);
        mAdapter = new MovieCursorAdapter(getContext(), null);
        mGridView.setAdapter(mAdapter);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String firstSort = sharedPreferences.getString(getString(R.string.first_sort_order), getString(R.string.settings_sort_order_default_value));
        mSortOrder = firstSort;
        Log.v(LOG_TAG, "onCreate");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null)
        getLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        else
            getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
        Log.v(LOG_TAG, "onActivityCreated");
    }

    public void onPreferenceChanged() {
        getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
        if (Utility.isOnline(getContext()) && !Utility.getSortOrder(getContext()).equals(mSortOrder)){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(getString(R.string.boolean_sort_traversed_key), true);
            editor.commit();
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean sortTraversed = sharedPreferences.getBoolean(getString(R.string.boolean_sort_traversed_key), false);
        Log.v(LOG_TAG, "Both sorts traversed? " + sortTraversed);
        if (!Utility.isOnline(getContext()) && !Utility.getSortOrder(getContext()).equals(mSortOrder) && !sortTraversed) {
            empty.setVisibility(View.VISIBLE);
            empty.setText(getString(R.string.no_internet_connection));
            Log.v(LOG_TAG, "Offline message should be displayed");
        }
        else {
            mSortOrder = Utility.getSortOrder(getContext());
        }
    }

    // LOADER CALLBACKS
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortOrder = sharedPreferences.getString(getString(R.string.settings_sort_order_key), getString(R.string.settings_sort_order_default_value));
        Log.v(LOG_TAG, "Sort order is : " + sortOrder);

        if (sortOrder.equals("favourite") ) {
            String[] projection = {
                    FavouriteMovieColumns._ID,
                    FavouriteMovieColumns.COLUMN_MOVIE_TITLE,
                    FavouriteMovieColumns.COLUMN_IMAGE_URL,
                    FavouriteMovieColumns.COLUMN_MOVIE_ID
            };
            return new CursorLoader(getActivity(),
                    MovieProvider.FavouriteMovies.CONTENT_URI,
                    projection,
                    null,
                    null,
                    null);
        } else
            return new CursorLoader(getActivity(),
                    MovieProvider.Movies.CONTENT_URI,
                    projection,
                    MovieColumns.COLUMN_TYPE + "=?",
                    new String[]{sortOrder},
                    null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        progressBar.setVisibility(View.GONE);
        if (data.moveToNext())
        {
            mAdapter.swapCursor(data);
            empty.setVisibility(View.GONE);
        }
        else if (!Utility.isOnline(getContext()))
            empty.setText(getString(R.string.no_internet_connection));
        else
            empty.setText(getString(R.string.empty_text_view_no_data));


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
