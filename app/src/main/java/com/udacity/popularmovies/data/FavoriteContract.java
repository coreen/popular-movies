package com.udacity.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteContract {

    public static final String AUTHORITY = "com.udacity.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";

    /*
    favorites
     - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    | _id | movieId | title |  backdropPath | posterPath | summary | releaseDate | voteAvg | isFavorite |
     - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    |  1  |   567   |  It   | /B79KC89F.jpg | /IB6K3.jpg |  good   |  2018-07-24 |   4.2    |   true    |
     - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    |  .. |   ...   |  ...  |      ...      |     ...    |   ...   |     ...     |   ...   |     ...    |
     - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    */

    // automatically adds the _id column with autoincrement integer
    public static final class FavoriteEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();

        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BACKDROP_PATH = "backdropPath";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_SUMMARY = "summary";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_VOTE_AVG = "voteAvg";
        public static final String COLUMN_IS_FAVORITE = "isFavorite";
    }

}
