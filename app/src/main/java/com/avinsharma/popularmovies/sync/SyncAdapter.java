package com.avinsharma.popularmovies.sync;

/**
 * Created by Avin on 25-01-2017.
 */

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.avinsharma.popularmovies.BuildConfig;
import com.avinsharma.popularmovies.R;
import com.avinsharma.popularmovies.Utility;
import com.avinsharma.popularmovies.data.MovieColumns;
import com.avinsharma.popularmovies.data.MovieProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    private static final String LOG_TAG = SyncAdapter.class.getSimpleName();

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Log.v(LOG_TAG, "In Sync adapter");
        try {
            getMoviesDataFromJson(fetchNewMoviesJsonString(getContext().getString(R.string.settings_sort_order_popular_value)), (getContext().getString(R.string.settings_sort_order_popular_value)));
            getMoviesDataFromJson(fetchNewMoviesJsonString(getContext().getString(R.string.settings_sort_order_top_rated_value)), (getContext().getString(R.string.settings_sort_order_top_rated_value)));
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(getContext().getString(R.string.boolean_sort_traversed_key), false);
            editor.putString(getContext().getString(R.string.first_sort_order), Utility.getSortOrder(getContext()));
            editor.commit();
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error in parsing JSON: ", e);
        }
    }

    //HELPER METHODS FOR onPerform sync

    private String fetchNewMoviesJsonString(String sortOrder) {

        final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie";

        try {
            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendPath(sortOrder)
                    .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            OkHttpClient client = new OkHttpClient();

            URL url = new URL(builtUri.toString());

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();

            return response.body().string();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getMoviesDataFromJson(String jsonString, String sortOrder)
            throws JSONException {



        final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w342";
        final String BASE_BACKDROP_IMAGE_URL = "https://image.tmdb.org/t/p/w500";
        final String TMDB_RESULTS = "results";
        final String TMDB_POSTER_PATH = "poster_path";
        final String TMDB_BACKDROP_PATH = "backdrop_path";
        final String TMDB_SYNOPSIS = "overview";
        final String TMDB_RELEASE_DATE = "release_date";
        final String TMDB_TITLE = "original_title";
        final String TMDB_RATING = "vote_average";
        final String TMDB_ID = "id";

        JSONObject root = new JSONObject(jsonString);
        JSONArray results = root.getJSONArray(TMDB_RESULTS);
        JSONObject currentObject;
        String title;
        String synopsis;
        String releaseDate;
        String posterUrl;
        String backdropUrl;
        String rating;
        String movieId;

        if (results != null) {
            Vector<ContentValues> cVVector = new Vector<>(results.length());
            for (int i = 0; i < results.length(); i++) {
                ContentValues movie = new ContentValues();
                currentObject = results.getJSONObject(i);
                title = currentObject.getString(TMDB_TITLE);
                synopsis = currentObject.getString(TMDB_SYNOPSIS);
                releaseDate = currentObject.getString(TMDB_RELEASE_DATE);
                posterUrl = BASE_IMAGE_URL + currentObject.getString(TMDB_POSTER_PATH);
                backdropUrl = BASE_BACKDROP_IMAGE_URL + currentObject.getString(TMDB_BACKDROP_PATH);
                rating = currentObject.getString(TMDB_RATING);
                movieId = currentObject.getString(TMDB_ID);


                movie.put(MovieColumns.COLUMN_MOVIE_TITLE,title);
                movie.put(MovieColumns.COLUMN_SYNOPSIS, synopsis);
                movie.put(MovieColumns.COLUMN_MOVIE_RATING, rating);
                movie.put(MovieColumns.COLUMN_RELEASE_DATE, releaseDate);
                movie.put(MovieColumns.COLUMN_IMAGE_URL, posterUrl);
                movie.put(MovieColumns.COLUMN_BACKGROUND_IMAGE, backdropUrl);
                movie.put(MovieColumns.COLUMN_TYPE, sortOrder);
                movie.put(MovieColumns.COLUMN_MOVIE_ID, movieId);

                cVVector.add(movie);
            }

            int inserted = 0;
            // add to database
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = mContentResolver.bulkInsert(MovieProvider.Movies.CONTENT_URI, cvArray);
                Log.v(LOG_TAG, "---------------------------------inserted: " + inserted);
                Cursor cursor = mContentResolver.query(MovieProvider.Movies.CONTENT_URI, new String[]{MovieColumns.COLUMN_MOVIE_TITLE}, MovieColumns.COLUMN_TYPE + "=?", new String[]{sortOrder},null);
                while (cursor.moveToNext())
                    Log.v(LOG_TAG, cursor.getString(0));
            }
        }

    }




    //all the other helper methods for the sync account

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Log.v(LOG_TAG, "In syncImmediately");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        Log.v(LOG_TAG, "In getSyncAccount");
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

        }
        return newAccount;
    }

    public static void initializeSyncAdapter(Context context) {
        Log.v(LOG_TAG, "In initializeSyncAdapter");
        getSyncAccount(context);
    }
}