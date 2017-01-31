package com.avinsharma.popularmovies.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.avinsharma.popularmovies.GridFragment;
import com.avinsharma.popularmovies.R;
import com.avinsharma.popularmovies.data.MovieProvider;
import com.squareup.picasso.Picasso;

/**
 * Created by Avin on 27-01-2017.
 */

public class MovieCursorAdapter
        extends CursorRecyclerViewAdapter<MovieCursorAdapter.ViewHolder> {

    Context mContext;
    ViewHolder mVH;

    public interface Callback {
        void onItemSelected(Uri targetUri);
    }

    public MovieCursorAdapter(Context context, Cursor cursor){
        super(context, cursor);
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

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
        ViewHolder viewHolder = new ViewHolder(poster, parent);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieCursorAdapter.ViewHolder viewHolder, Cursor cursor) {
        final String title = cursor.getString(GridFragment.COLUMN_MOVIE_TITLE);
        final long id = cursor.getLong(GridFragment._ID);
        Picasso.with(mContext).load(cursor.getString(GridFragment.COLUMN_IMAGE_URL)).into(viewHolder.poster);
        viewHolder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = MovieProvider.Movies.withId(id);
                Snackbar.make(view, title, Snackbar.LENGTH_SHORT).show();
                ((Callback) mContext).onItemSelected(uri);
            }
        });
    }
}
