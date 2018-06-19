package com.udacity.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import com.udacity.popularmovies.model.SortBy;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Utility to communicate with themoviedb.org API.
 */
public final class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/movie";
    private static final String POPULAR_MOVIES_PATH = "popular";
    private static final String TOP_RATED_MOVIES_PATH = "top_rated";
    private static final String API_KEY_QUERY = "api_key";

    /**
     * Builds the URL used to talk to the movie db server.
     *
     * @resource https://medium.com/@geocohn/keeping-your-android-projects-secrets-secret-393b8855765d
     *
     * @return The URL to use to query the movie db server.
     */
    public static URL buildUrl(SortBy sort) {
        String path = "";
        if (sort == SortBy.MOST_POPULAR) {
            path = POPULAR_MOVIES_PATH;
        } else if (sort == SortBy.TOP_RATED) {
            path = TOP_RATED_MOVIES_PATH;
        }
        Uri builtUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(path)
                .appendQueryParameter(API_KEY_QUERY, com.udacity.popularmovies.BuildConfig.apikey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
