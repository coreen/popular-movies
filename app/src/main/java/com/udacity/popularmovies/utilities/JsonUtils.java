package com.udacity.popularmovies.utilities;

import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.model.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility to handle themoviedb.org JSON data.
 */
public class JsonUtils {

    private static final String RESULTS = "results";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String BACKDROP_IMAGE_PATH = "backdrop_path";
    private static final String POSTER_IMAGE_PATH = "poster_path";
    private static final String SUMMARY = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String VOTE_AVG = "vote_average";

    private static final String VIDEO_KEY = "key";
    private static final String REVIEW_CONTENT = "content";
    private static final String REVIEW_AUTHOR = "author";

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
            final int id = json.getInt(ID);
            final String title = json.getString(TITLE);
            final String backdropImagePath = json.getString(BACKDROP_IMAGE_PATH);
            final String posterImagePath = json.getString(POSTER_IMAGE_PATH);
            final String summary = json.getString(SUMMARY);
            final String releaseDate = json.getString(RELEASE_DATE);
            final String voteAvg = json.getString(VOTE_AVG);

            return new Movie(id, title, backdropImagePath, posterImagePath,
                    summary, releaseDate, voteAvg);
        } catch (JSONException e) {
            return null;
        }
    }

    public static String[] parseVideosFromJsonString(String json) throws JSONException {
        JSONObject response = new JSONObject(json);
        JSONArray videos = response.getJSONArray(RESULTS);
        String[] result = new String[videos.length()];
        for (int i = 0; i < videos.length(); i++) {
            JSONObject video = (JSONObject) videos.get(i);
            result[i] = video.getString(VIDEO_KEY);
        }
        return result;
    }

    public static Review[] parseReviewsFromJsonString(String json) throws JSONException {
        JSONObject response = new JSONObject(json);
        JSONArray reviews = response.getJSONArray(RESULTS);
        Review[] result = new Review[reviews.length()];
        for (int i = 0; i < reviews.length(); i++) {
            JSONObject review = (JSONObject) reviews.get(i);
            result[i] = parseReviewJson(review);
        }
        return result;
    }

    public static Review parseReviewJson(JSONObject json) {
        try {
            final String content = json.getString(REVIEW_CONTENT);
            final String author = json.getString(REVIEW_AUTHOR);

            return new Review(content, author);
        } catch (JSONException e) {
            return null;
        }
    }
}
