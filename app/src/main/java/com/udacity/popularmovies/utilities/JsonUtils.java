package com.udacity.popularmovies.utilities;

import com.udacity.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility to handle themoviedb.org JSON data.
 */
public class JsonUtils {

    private static final String RESULTS = "results";
    private static final String TITLE = "title";
    private static final String BACKDROP_IMAGE_PATH = "backdrop_path";
    private static final String POSTER_IMAGE_PATH = "poster_path";
    private static final String SUMMARY = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String VOTE_AVG = "vote_average";

    public static Movie[] getMoviesFromJsonString(String json) throws JSONException {
        JSONObject response = new JSONObject(json);
        JSONArray movies = response.getJSONArray(RESULTS);
        Movie[] result = new Movie[movies.length()];
        for (int i = 0; i < movies.length(); i++) {
            JSONObject movie = (JSONObject) movies.get(i);
            result[i] = parseMovieJson(movie);
        }
        return result;
    }

    public static Movie parseMovieJson(JSONObject json) {
        try {
            String title = json.getString(TITLE);
            String backdropImagePath = json.getString(BACKDROP_IMAGE_PATH);
            String posterImagePath = json.getString(POSTER_IMAGE_PATH);
            String summary = json.getString(SUMMARY);
            String releaseDate = json.getString(RELEASE_DATE);
            String voteAvg = json.getString(VOTE_AVG);

            return new Movie(title, backdropImagePath, posterImagePath,
                    summary, releaseDate, voteAvg);
        } catch (JSONException e) {
            return null;
        }
    }
}
