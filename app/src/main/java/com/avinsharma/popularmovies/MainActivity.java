package com.avinsharma.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.avinsharma.popularmovies.adapter.MovieCursorAdapter;
import com.avinsharma.popularmovies.data.MovieProvider;
import com.avinsharma.popularmovies.sync.SyncAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieCursorAdapter.Callback{

    static final String LOG_TAG = MainActivity.class.getSimpleName();

    GridView gridView;
    ArrayList movies;
    MovieGridAdapter adapter;
    TextView empty;
    ProgressBar progressBar;

    boolean hasDataSaved = false;
    static boolean hasPreferenceChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*if (!(savedInstanceState == null || !savedInstanceState.containsKey("movies"))){
            movies = savedInstanceState.getParcelableArrayList("movies");
            hasDataSaved = true;
        }

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        empty = (TextView) findViewById(R.id.empty_textview);
        gridView = (GridView) findViewById(R.id.grid_view);

        gridView.setEmptyView(empty);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("movie", adapter.getItem(i));
                startActivity(intent);
            }
        });*/

        getContentResolver().delete(MovieProvider.Movies.CONTENT_URI, null, null);
        // Initialize sync adapter
        SyncAdapter.initializeSyncAdapter(this);
        SyncAdapter.syncImmediately(this);
    }

    @Override
    protected void onStart() {
        if (hasPreferenceChanged){
            GridFragment gridFragment = (GridFragment) getSupportFragmentManager().findFragmentById(R.id.grid_fragment);
            gridFragment.onPreferenceChanged();
            hasPreferenceChanged = false;
        }
        /*if (!hasDataSaved || hasPreferenceChanged){
            movies = new ArrayList();
            adapter = new MovieGridAdapter(this, movies);
            Log.v(LOG_TAG, "Making a new network call!!, hasDataChanged: " + hasPreferenceChanged
            + " ,has saved data :" + hasDataSaved);
            updateUi();
        }else {
            progressBar.setVisibility(View.GONE);
            adapter = new MovieGridAdapter(this, movies);
            Log.v(LOG_TAG, "Using saved data!!, hasDataChanged: " + hasPreferenceChanged);
        }
        gridView.setAdapter(adapter);*/

        super.onStart();
    }

    @Override
    protected void onStop() {
        /*empty.setText("");
        progressBar.setVisibility(View.VISIBLE);*/
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        /*hasDataSaved = true;
        outState.putParcelableArrayList("movies", movies);*/
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemSelected(Uri targetUri) {
        //TODO: start detail fragment by passing uri in bundle or data in fragment or intent respectively
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.setData(targetUri);
        startActivity(intent);
        Toast.makeText(this, "Callback called: onItemSelected, URI: " + targetUri.toString(), Toast.LENGTH_SHORT).show();
    }
}
