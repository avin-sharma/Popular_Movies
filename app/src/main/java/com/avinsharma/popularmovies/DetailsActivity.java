package com.avinsharma.popularmovies;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import static com.avinsharma.popularmovies.MainActivity.movieId;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String[]> {

    final String LOG_TAG = DetailsActivity.class.getSimpleName();
    public static final String DETAILS_FRAGMENT_TAG = "DFTAG";
    public final int DETAILS_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Uri uri = getIntent().getData();
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(DetailsFragment.DETAIL_URI, uri);
        detailsFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, detailsFragment, DETAILS_FRAGMENT_TAG).commit();

        if (Utility.isOnline(this))
            getSupportLoaderManager().initLoader(DETAILS_LOADER_ID, null, this).forceLoad();

    }

    @Override
    public Loader<String[]> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In on create loader, MovieId is: " + movieId);
        return new DetailsLoader(this, movieId);
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] data) {

        if (data != null) {
            DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentByTag(DetailsActivity.DETAILS_FRAGMENT_TAG);
            detailsFragment.updateReviewsAndTrailers(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {

    }
}
