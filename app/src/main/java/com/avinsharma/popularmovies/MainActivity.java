package com.avinsharma.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final String LOG_TAG = MainActivity.class.getSimpleName();

    GridView gridView;
    MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.grid_view);
        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("YoloTitle", "YoloSynopsis", "4.3", "24//5", "http://i.imgur.com/DvpvklR.png"));
        Log.v(LOG_TAG, movies.get(0).toString());
        adapter = new MovieAdapter(this, movies);
        gridView.setAdapter(adapter);
    }


}
