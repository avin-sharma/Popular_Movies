package com.avinsharma.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.avinsharma.popularmovies.data.FavouriteMovieColumns;
import com.avinsharma.popularmovies.data.MovieProvider;

/**
 * Created by Avin on 02-02-2017.
 */

public class Utility {

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static boolean isMovieFavourite(Context context, String movieId){
        Cursor cursor = context.getContentResolver().query(MovieProvider.FavouriteMovies.CONTENT_URI,
                null,
                FavouriteMovieColumns.COLUMN_MOVIE_ID + "=?",
                new String[]{movieId},
                null);
        if (cursor.moveToNext()) {
            cursor.close();
            return true;
        }
        else {
            cursor.close();
            return false;
        }
    }

    public static String getSortOrder(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sortOrder = sharedPreferences.getString(context.getString(R.string.settings_sort_order_key), context.getString(R.string.settings_sort_order_default_value));
        return sortOrder;
    }

    public static boolean isOnline(Context context){
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
