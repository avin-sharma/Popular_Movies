package com.avinsharma.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.avinsharma.popularmovies.adapter.MovieCursorAdapter;
import com.avinsharma.popularmovies.data.MovieContract.MovieColumns;
import com.avinsharma.popularmovies.sync.SyncAdapter;

import static com.avinsharma.popularmovies.DetailsActivity.DETAILS_FRAGMENT_TAG;

public class MainActivity extends AppCompatActivity implements MovieCursorAdapter.Callback, LoaderManager.LoaderCallbacks<String[]> {

    static final String LOG_TAG = MainActivity.class.getSimpleName();

    static boolean hasPreferenceChanged = false;
    boolean mTwoPane = false;

    public static String movieId = null;
    public final int DETAILS_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.container) != null) {
            mTwoPane = true;
        }
        if (savedInstanceState == null) {
            if (Utility.isOnline(this))
                getContentResolver().delete(MovieColumns.CONTENT_URI, null, null);
            // Initialize sync adapter
            SyncAdapter.initializeSyncAdapter(this);
            SyncAdapter.syncImmediately(this);
        }
    }

    @Override
    protected void onStart() {
        if (hasPreferenceChanged) {
            GridFragment gridFragment = (GridFragment) getSupportFragmentManager().findFragmentById(R.id.grid_fragment);
            gridFragment.onPreferenceChanged();
            hasPreferenceChanged = false;
        }

        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular_movies_menu:
                onSortOrderChanged(getString(R.string.settings_sort_order_popular_value));
                return true;
            case R.id.top_rated_movies_menu:
                onSortOrderChanged(getString(R.string.settings_sort_order_top_rated_value));
                return true;
            case R.id.favourite_movies_menu:
                onSortOrderChanged(getString(R.string.settings_sort_order_favourite_value));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemSelected(Uri targetUri, String movieId) {
        MainActivity.movieId = movieId;
        if (!mTwoPane) {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.setData(targetUri);
            startActivity(intent);
        } else {
            DetailsFragment detailsFragment = new DetailsFragment();
            Bundle args = new Bundle();
            args.putParcelable(DetailsFragment.DETAIL_URI, targetUri);
            detailsFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, detailsFragment, DETAILS_FRAGMENT_TAG).commit();
            Loader loader = getSupportLoaderManager().getLoader(DETAILS_LOADER_ID);
            if (Utility.isOnline(this))
                if (loader != null)
                    getSupportLoaderManager().restartLoader(DETAILS_LOADER_ID, null, this).forceLoad();
                else
                    getSupportLoaderManager().initLoader(DETAILS_LOADER_ID, null, this).forceLoad();
        }
    }

    public void onSortOrderChanged(String sortKey){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.settings_sort_order_key), sortKey);
        editor.commit();
        GridFragment gridFragment = (GridFragment) getSupportFragmentManager().findFragmentById(R.id.grid_fragment);
        gridFragment.onPreferenceChanged();
    }

    @Override
    public Loader<String[]> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In on create loader, MovieId is: " + movieId);
        return new DetailsLoader(this, movieId);
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] data) {
        Log.v(LOG_TAG, "innOnasf------------------------------23495872930570298374502983750982374");
        if (data != null) {
            DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentByTag(DETAILS_FRAGMENT_TAG);
            detailsFragment.updateReviewsAndTrailers(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {

    }
}
