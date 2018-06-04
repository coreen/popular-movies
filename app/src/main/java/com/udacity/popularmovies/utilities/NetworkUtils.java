package com.udacity.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Utility to communicate with themoviedb.org API.
 */
public final class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY";
    private static final String POPULAR_MOVIES_PATH = "movie/popular";
    private static final String TOP_RATED_MOVIES_PATH = "movie/top_rated";
    private static final String API_KEY_QUERY = "api_key";

    private enum PATH_TYPE {
        POPULAR_MOVIES,
        TOP_RATED_MOVIES
    }

    public static URL buildPopularMoviesUrl() {
        return buildUrl(PATH_TYPE.POPULAR_MOVIES);
    }

    public static URL buildTopRatedMoviesUrl() {
        return buildUrl(PATH_TYPE.TOP_RATED_MOVIES);
    }

    /**
     * Builds the URL used to talk to the movie db server.
     *
     * @resource https://medium.com/@geocohn/keeping-your-android-projects-secrets-secret-393b8855765d
     *
     * @return The URL to use to query the movie db server.
     */
    private static URL buildUrl(PATH_TYPE pathType) {
        String path = "";
        if (pathType == PATH_TYPE.POPULAR_MOVIES) {
            path = POPULAR_MOVIES_PATH;
        } else if (pathType == PATH_TYPE.TOP_RATED_MOVIES) {
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
}
