package com.avinsharma.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Avin on 01-02-2017.
 */

public class DetailsLoader extends android.support.v4.content.AsyncTaskLoader<String[]> {

    public static final String LOG_TAG = DetailsLoader.class.getSimpleName();
    String movieId;

    public DetailsLoader(Context context, String movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Override
    public String[] loadInBackground() {
        if (movieId == null)
            return null;
        Log.v(LOG_TAG, "in loadInBackground");
        String[] reviews = getReviews(movieId);
        String[] trailers = getTrailers(movieId);
        String[] returnArray = concat(reviews, trailers);

        return returnArray;
    }

    private String[] getReviews(String movieId){
        final String BASE_REVIEW_URL = "https://api.themoviedb.org/3/movie";
        String jsonString;
        try {
            Uri builtUri = Uri.parse(BASE_REVIEW_URL).buildUpon()
                    .appendPath(movieId)
                    .appendPath("reviews")
                    .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY)
                    .appendQueryParameter("language","en-US")
                    .appendQueryParameter("page", "1")
                    .build();

            Log.v(LOG_TAG, builtUri.toString());

            OkHttpClient client = new OkHttpClient();

            URL url = new URL(builtUri.toString());

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            jsonString = response.body().string();

            JSONObject root = new JSONObject(jsonString);
            JSONArray results = root.getJSONArray("results");
            JSONObject review;
            String stringAuthor = "";
            String stringReviews = "";

            if (results != null) {
                for (int i = 0; i < results.length(); i++) {
                    review = results.getJSONObject(i);
                    stringReviews +="\n" + review.getString("content") + "\n\n" + "!~";
                    stringAuthor += review.getString("author") + "!~";
                }
                return new String[] {stringAuthor, stringReviews};
            }
        }  catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String[] getTrailers(String movieId){

        // https://api.themoviedb.org/3/movie/{movie_id}/videos?api_key=<<api_key>>&language=en-US

        final String BASE_TRAILER_URL = "https://api.themoviedb.org/3/movie";
        final String YOUTUBE_URL ="https://www.youtube.com/watch?v=";
        String jsonString;
        try {
            Uri builtUri = Uri.parse(BASE_TRAILER_URL).buildUpon()
                    .appendPath(movieId)
                    .appendPath("videos")
                    .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY)
                    .appendQueryParameter("language","en-US")
                    .build();
            Log.v(LOG_TAG, builtUri.toString());

            OkHttpClient client = new OkHttpClient();

            URL url = new URL(builtUri.toString());

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            jsonString = response.body().string();

            JSONObject root = new JSONObject(jsonString);
            JSONArray results = root.getJSONArray("results");
            JSONObject video;
            String stringTitle = "";
            String stringVideos = "";

            if (results != null) {
                for (int i = 0; i < results.length(); i++) {
                    video = results.getJSONObject(i);
                    stringVideos += YOUTUBE_URL + video.getString("key") + "!~";
                    stringTitle +="\n" + video.getString("name") + "\n" + "!~";
                }
                return new String[] {stringTitle, stringVideos};
            }
        }  catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] concat(String[] a, String[] b) {
        int aLen = a.length;
        int bLen = b.length;
        String[] c= new String[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }
}
