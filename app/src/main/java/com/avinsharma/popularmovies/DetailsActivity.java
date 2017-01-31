package com.avinsharma.popularmovies;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    final String LOG_TAG = DetailsActivity.class.getSimpleName();
    private String DETAILFRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

//        Movie movie = getIntent().getParcelableExtra("movie");
//        setTitle(movie.getmTitle());

        Uri uri = getIntent().getData();
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(DetailsFragment.DETAIL_URI, uri);
        detailsFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, detailsFragment, DETAILFRAGMENT_TAG).commit();

    }
}
