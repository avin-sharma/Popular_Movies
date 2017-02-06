package com.avinsharma.popularmovies.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.avinsharma.popularmovies.GridFragment;
import com.avinsharma.popularmovies.R;
import com.avinsharma.popularmovies.Utility;
import com.avinsharma.popularmovies.data.MovieContract.FavouriteMovieColumns;
import com.avinsharma.popularmovies.data.MovieContract.MovieColumns;
import com.squareup.picasso.Picasso;

/**
 * Created by Avin on 27-01-2017.
 */

public class MovieCursorAdapter
        extends CursorRecyclerViewAdapter<MovieCursorAdapter.ViewHolder> {

    Context mContext;
    ViewHolder mVH;
    int movieId;
    long id;

    public interface Callback {
        void onItemSelected(Uri targetUri, String movieId);
    }

    public MovieCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView poster;
        public View parent;

        public ViewHolder(ImageView poster, View parent) {
            super(poster);
            this.poster = poster;
            this.parent = parent;
        }
    }

    @Override
    public MovieCursorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView poster = (ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(poster, parent);
    }

    @Override
    public void onBindViewHolder(MovieCursorAdapter.ViewHolder viewHolder, Cursor cursor) {
        Uri uri = null;
        final String title = cursor.getString(GridFragment.COLUMN_MOVIE_TITLE);
        final long id = cursor.getLong(GridFragment._ID);
        final int movieId = cursor.getInt(GridFragment.COLUMN_MOVIE_ID);
        Picasso.with(mContext).load(cursor.getString(GridFragment.COLUMN_IMAGE_URL)).into(viewHolder.poster);
        if (Utility.getSortOrder(mContext).equals("favourite"))
            uri = ContentUris.withAppendedId(FavouriteMovieColumns.CONTENT_URI, id);
        else
            uri = ContentUris.withAppendedId(MovieColumns.CONTENT_URI, id);

        final Uri finalUri = uri;
        viewHolder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Callback) mContext).onItemSelected(finalUri, String.valueOf(movieId));
            }
        });
    }
}
