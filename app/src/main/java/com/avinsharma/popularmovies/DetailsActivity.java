package com.avinsharma.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    final String LOG_TAG = DetailsActivity.class.getSimpleName();
    ImageView poster;
    ImageView backdrop;
    TextView title;
    TextView synopsis;
    TextView releaseDate;
    TextView rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Movie movie = getIntent().getParcelableExtra("movie");
        setTitle(movie.getmTitle());

        poster = (ImageView) findViewById(R.id.poster_imageview);
        backdrop = (ImageView) findViewById(R.id.backdrop_imageview);
        title = (TextView) findViewById(R.id.title_textview);
        synopsis = (TextView) findViewById(R.id.synopsis_textview);
        releaseDate = (TextView) findViewById(R.id.release_date_textview);
        rating = (TextView) findViewById(R.id.rating_textview);

        Picasso.with(DetailsActivity.this).load(movie.getmImageUrl()).into(poster);
        Picasso.with(DetailsActivity.this).load(movie.getmBackgroundImageUrl()).into(backdrop);
        title.setText(movie.getmTitle());
        synopsis.setText(movie.getmSynopsis());
        releaseDate.setText(movie.getmReleaseDate());
        rating.setText(movie.getmUserRating());

    }
}
