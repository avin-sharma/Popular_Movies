package com.avinsharma.popularmovies;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.avinsharma.popularmovies.data.MovieContract.FavouriteMovieColumns;
import com.avinsharma.popularmovies.data.MovieContract.MovieColumns;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    ImageView poster;
    ImageView backdrop;
    TextView title;
    TextView synopsis;
    TextView releaseDate;
    TextView rating;
    ListView reviewsListView;
    ListView trailerListView;
    ToggleButton favourite;

    private static final String LOG_TAG = DetailsFragment.class.getSimpleName();
    private static final int MOVIE_LOADER = 0;
    public static String DETAIL_URI = "detail_uri";
    private Cursor movieCursor = null;

    Uri mUri = null;
    ContentValues contentValues = null;
    String firstTrailer = null;

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
        if (arguments != null) {
            mUri = arguments.getParcelable(DETAIL_URI);
        }


        if (!Utility.isOnline(getContext())) {
            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.details_main_linear_layout);
            CardView videos =(CardView) view.findViewById(R.id.videos_card_view);
            CardView reviews =(CardView) view.findViewById(R.id.reviews_card_view);
            CardView noInternet = (CardView) view.findViewById(R.id.no_internet_card_view);
            linearLayout.removeView(videos);
            linearLayout.removeView(reviews);
            noInternet.setVisibility(View.VISIBLE);
        }

        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        favourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    favourite.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.star_yellow_on));
                else
                    favourite.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.star_grey_off));
            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.details_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.share_button:
                if (firstTrailer != null) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, firstTrailer);
                    sendIntent.setType("text/plain");
                    if (sendIntent.resolveActivity(getContext().getPackageManager()) != null) {
                        startActivity(sendIntent);
                    }
                    startActivity(sendIntent);
                }else
                    Toast.makeText(getContext(), "Try again with Internet connectivity", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViews(View view) {
        poster = (ImageView) view.findViewById(R.id.poster_imageview);
        backdrop = (ImageView) view.findViewById(R.id.backdrop_imageview);
        title = (TextView) view.findViewById(R.id.title_textview);
        synopsis = (TextView) view.findViewById(R.id.synopsis_textview);
        releaseDate = (TextView) view.findViewById(R.id.release_date_textview);
        rating = (TextView) view.findViewById(R.id.rating_textview);
        reviewsListView = (ListView) view.findViewById(R.id.reviews_listview);
        trailerListView = (ListView) view.findViewById(R.id.trailer_listview);
        favourite = (ToggleButton) view.findViewById(R.id.fav_toggle_button);
    }

    private void updateUI(Cursor data) {
        Picasso.with(getContext()).load(data.getString(COLUMN_IMAGE_URL)).into(poster);
        Picasso.with(getContext()).load(data.getString(COLUMN_BACKGROUND_IMAGE)).into(backdrop);
        title.setText(data.getString(COLUMN_MOVIE_TITLE));
        synopsis.setText(data.getString(COLUMN_SYNOPSIS));
        releaseDate.setText(data.getString(COLUMN_RELEASE_DATE));
        rating.setText(data.getString(COLUMN_MOVIE_RATING));
    }

    public void updateReviewsAndTrailers(String[] data) {

        if (!data[1].equals("") && data[1] != null) {
            String[] reviews = data[1].split("!~");
            String[] trailers = data[2].split("!~");
            final String[] links = data[3].split("!~");
            firstTrailer = links[0];
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, reviews);
            reviewsListView.setAdapter(adapter);
            Utility.setListViewHeightBasedOnChildren(reviewsListView);
            ArrayAdapter<String> adapterTrailer = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, trailers);
            trailerListView.setAdapter(adapterTrailer);
            Utility.setListViewHeightBasedOnChildren(trailerListView);
            trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.v(LOG_TAG, "link: " + links[i]);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(links[i])));

                }
            });
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), mUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToNext()) {
            movieCursor = data;
            cursorToContentValues();
            Log.v(LOG_TAG, "is the movie in fav database? " + Utility.isMovieFavourite(getContext(), data.getString(COLUMN_MOVIE_ID)));
            Log.v(LOG_TAG, data.getString(COLUMN_MOVIE_TITLE));
            updateUI(data);
            if (Utility.isMovieFavourite(getContext(), data.getString(COLUMN_MOVIE_ID))) {
                favourite.setChecked(true);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Do nothing
    }

    @Override
    public void onPause() {
        super.onPause();
        if (movieCursor != null) {
            boolean fav = Utility.isMovieFavourite(getContext(), movieCursor.getString(COLUMN_MOVIE_ID));
            Log.v(LOG_TAG, "isChecked: " + favourite.isChecked() + " is in fav: " + fav + " contentValues not null? " + String.valueOf(contentValues != null));
            if (favourite.isChecked() && !fav && contentValues != null) {
                getContext().getContentResolver().insert(FavouriteMovieColumns.CONTENT_URI, contentValues);
            }
            if (!favourite.isChecked() && fav && contentValues != null) {
                getContext().getContentResolver().delete(FavouriteMovieColumns.CONTENT_URI,
                        FavouriteMovieColumns.COLUMN_MOVIE_ID + "=?", new String[]{movieCursor.getString(COLUMN_MOVIE_ID)});
            }
        }
    }

    public void cursorToContentValues() {
        contentValues = new ContentValues();
        contentValues.put(FavouriteMovieColumns.COLUMN_MOVIE_ID, movieCursor.getInt(COLUMN_MOVIE_ID));
        contentValues.put(FavouriteMovieColumns.COLUMN_MOVIE_TITLE, movieCursor.getString(COLUMN_MOVIE_TITLE));
        contentValues.put(FavouriteMovieColumns.COLUMN_MOVIE_RATING, movieCursor.getString(COLUMN_MOVIE_RATING));
        contentValues.put(FavouriteMovieColumns.COLUMN_SYNOPSIS, movieCursor.getString(COLUMN_SYNOPSIS));
        contentValues.put(FavouriteMovieColumns.COLUMN_RELEASE_DATE, movieCursor.getString(COLUMN_RELEASE_DATE));
        contentValues.put(FavouriteMovieColumns.COLUMN_IMAGE_URL, movieCursor.getString(COLUMN_IMAGE_URL));
        contentValues.put(FavouriteMovieColumns.COLUMN_BACKGROUND_IMAGE, movieCursor.getString(COLUMN_BACKGROUND_IMAGE));
    }
}
