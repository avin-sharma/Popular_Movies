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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avinsharma.popularmovies.adapter.MovieCursorAdapter;
import com.avinsharma.popularmovies.data.MovieColumns;
import com.avinsharma.popularmovies.data.MovieProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class GridFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    String[] projection = {
            MovieColumns._ID,
            MovieColumns.COLUMN_MOVIE_TITLE,
            MovieColumns.COLUMN_IMAGE_URL,
    };

    public static final int _ID = 0;
    public static final int COLUMN_MOVIE_TITLE = 1;
    public static final int COLUMN_IMAGE_URL = 2;


    private RecyclerView mGridView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MovieCursorAdapter mAdapter;

    public static final int MOVIE_LOADER_ID = 0;

    public GridFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grid, container, false);
        mGridView = (RecyclerView) view.findViewById(R.id.grid_recycler_view);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mGridView.setLayoutManager(mLayoutManager);
        mAdapter = new MovieCursorAdapter(getContext(), null);
        mGridView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
    }

    public void onPreferenceChanged(){
        getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }

    // LOADER CALLBACKS
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortOrder = sharedPreferences.getString(getString(R.string.settings_sort_order_key),getString(R.string.settings_sort_order_default_value));

        return new CursorLoader(getActivity(),
                MovieProvider.Movies.CONTENT_URI,
                projection,
                MovieColumns.COLUMN_TYPE + "=?",
                new String[]{sortOrder},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
