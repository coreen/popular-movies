package com.udacity.popularmovies.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.udacity.popularmovies.model.Movie;

import static com.udacity.popularmovies.data.FavoriteContract.FavoriteEntry;

/**
 * Utility that wraps all data operations in single file.
 */
public class DataUtils {
    private static final String TAG = DataUtils.class.getSimpleName();

    public static boolean toggleIsFavorite(Context context, Movie movie) {
        boolean isFavorite = movie.getIsFavorite(context);
        if (isFavorite) {
            removeFavorite(context, movie.getMovieId());
        } else {
            addFavorite(context, movie);
        }
        return !isFavorite;
    }

    private static Uri addFavorite(Context context, Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteEntry.COLUMN_MOVIE_ID, movie.getMovieId());
        contentValues.put(FavoriteEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(FavoriteEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        contentValues.put(FavoriteEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        contentValues.put(FavoriteEntry.COLUMN_SUMMARY, movie.getSummary());
        contentValues.put(FavoriteEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(FavoriteEntry.COLUMN_VOTE_AVG, movie.getVoteAvg());
        contentValues.put(FavoriteEntry.COLUMN_IS_FAVORITE, 1);

        Uri uri = context.getContentResolver().insert(FavoriteEntry.CONTENT_URI, contentValues);
        Log.d(TAG, "Added uri " + uri + " to favorites database");

        return uri;
    }

    private static void removeFavorite(Context context, int movieId) {
        final String selection = FavoriteEntry.COLUMN_MOVIE_ID + " = ?";
        final String[] selectionArgs = { Integer.toString(movieId) };
        final int removedEntryCount = context.getContentResolver()
                .delete(FavoriteEntry.CONTENT_URI, selection, selectionArgs);
        // removedEntryCount should always equal 1 since movieId should be unique
        Log.d(TAG, "Removed " + removedEntryCount +
                " entry with movieId " + movieId + " from favorites database");
    }

    public static boolean getFavorite(Context context, int movieId) {
        // Resource: https://stackoverflow.com/questions/10507005/using-string-selectionargs-in-sqlitedatabase-query
        final String[] projection = { FavoriteEntry.COLUMN_IS_FAVORITE };
        final String selection = FavoriteEntry.COLUMN_MOVIE_ID + " = ?";
        final String[] selectionArgs = { Integer.toString(movieId) };
        final String sortBy = null;
        Cursor cursor = context.getContentResolver().query(
                FavoriteEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortBy);
        // returns true only when have entries returned
        final boolean hasEntry = cursor.moveToFirst();
        if (!hasEntry) {
            return false;
        }
        final int isFavorite = cursor.getInt(
                cursor.getColumnIndex(FavoriteEntry.COLUMN_IS_FAVORITE));
        return isFavorite == 1;
    }
}
