package com.avinsharma.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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

        if (!(savedInstanceState == null || !savedInstanceState.containsKey("movies"))){
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
        });

    }

    @Override
    protected void onStart() {
        if (!hasDataSaved || hasPreferenceChanged){
            movies = new ArrayList();
            adapter = new MovieGridAdapter(this, movies);
            Log.v(LOG_TAG, "Making a new network call!!, hasDataChanged: " + hasPreferenceChanged);
            updateUi();
        }else {
            progressBar.setVisibility(View.GONE);
            adapter = new MovieGridAdapter(this, movies);
            Log.v(LOG_TAG, "Using saved data!!, hasDataChanged: " + hasPreferenceChanged);
        }
        gridView.setAdapter(adapter);

        super.onStart();
    }

    @Override
    protected void onStop() {
        empty.setText("");
        progressBar.setVisibility(View.VISIBLE);
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
        outState.putParcelableArrayList("movies", movies);
        super.onSaveInstanceState(outState);
    }

    private void updateUi (){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            FetchMoviesTask task = new FetchMoviesTask();
            task.execute();
        } else {
            empty.setText(R.string.empty_text_view_no_internet_connection);
            Toast.makeText(MainActivity.this, R.string.empty_text_view_no_internet_connection, Toast.LENGTH_SHORT).show();
        }
        hasPreferenceChanged = false;
    }

    private String fetchMovieJsonString() {

        final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie";

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrder = sharedPreferences.getString(getString(R.string.settings_sort_order_key),getString(R.string.settings_sort_order_default_value));
        String jsonString;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {

            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendPath(sortOrder)
                    .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                return null;
            }

            StringBuilder buffer = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            jsonString = buffer.toString();
            return jsonString;

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error fetching JSON from api ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error closing Buffered reader ", e);
                }
            }
        }
        return null;
    }

    private ArrayList<Movie> getMoviesDataFromJson(String jsonString)
            throws JSONException {

        ArrayList<Movie> movies = new ArrayList<>();

        final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w342";
        final String BASE_BACKDROP_IMAGE_URL = "https://image.tmdb.org/t/p/w500";
        final String TMDB_RESULTS = "results";
        final String TMDB_POSTER_PATH = "poster_path";
        final String TMDB_BACKDROP_PATH = "backdrop_path";
        final String TMDB_SYNOPSIS = "overview";
        final String TMDB_RELEASE_DATE = "release_date";
        final String TMDB_TITLE = "original_title";
        final String TMDB_RATING = "vote_average";

        JSONObject root = new JSONObject(jsonString);
        JSONArray results = root.getJSONArray(TMDB_RESULTS);
        JSONObject currentObject;
        String title;
        String synopsis;
        String releaseDate;
        String posterUrl;
        String backdropUrl;
        String rating;
        Movie currentMovie;

        if (results != null)
            for (int i = 0; i < results.length(); i++) {
                currentObject = results.getJSONObject(i);
                title = currentObject.getString(TMDB_TITLE);
                synopsis = currentObject.getString(TMDB_SYNOPSIS);
                releaseDate = currentObject.getString(TMDB_RELEASE_DATE);
                posterUrl = BASE_IMAGE_URL + currentObject.getString(TMDB_POSTER_PATH);
                backdropUrl = BASE_BACKDROP_IMAGE_URL + currentObject.getString(TMDB_BACKDROP_PATH);
                rating = currentObject.getString(TMDB_RATING);
                currentMovie = new Movie(title, synopsis, rating, releaseDate, posterUrl, backdropUrl);
                movies.add(currentMovie);
            }

        return movies;
    }

    public class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {
            try {
                return getMoviesDataFromJson(fetchMovieJsonString());
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error parsing JSON ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> m) {
            if (m != null) {
                movies = m;
                adapter.clear();
                adapter.addAll(m);
            }
            progressBar.setVisibility(View.GONE);
            empty.setText(R.string.empty_text_view_no_data);
        }
    }
}
