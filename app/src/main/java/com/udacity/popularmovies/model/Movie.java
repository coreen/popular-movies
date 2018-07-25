package com.udacity.popularmovies.model;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import static com.udacity.popularmovies.data.FavoriteContract.FavoriteEntry;

public final class Movie implements Parcelable {
    private static final String TAG = Movie.class.getSimpleName();

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_DEFAULT_POSTER_SIZE = "w185";

    private int id;
    private String title;
    private String backdropPath;
    private String posterPath;
    private String summary;
    private String releaseDate;
    private String voteAvg;
    private boolean isFavorite;

    public Movie(int id, String title, String backdropPath, String posterPath,
                 String summary, String releaseDate, String voteAvg) {
        this.id = id;
        this.title = title;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.summary = summary;
        this.releaseDate = releaseDate;
        this.voteAvg = voteAvg;
        // TODO(coreeny): make this talk to SQLite for movie favorite status instead of hardcode
        // Resource: https://stackoverflow.com/questions/10507005/using-string-selectionargs-in-sqlitedatabase-query
        String[] projection = { "isFavorite" };
        String selection = "movieId = ?";
        String[] selectionArgs = { Integer.toString(id) };
        String sortBy = "ASC";
//        context.getContentResolver().query(FavoriteEntry.CONTENT_URI, projection,
//                selection, selectionArgs, sortBy);
        this.isFavorite = false;
    }

    // Note: Must read from parcel in same order contents were added
    public Movie(Parcel source) {
        id = source.readInt();
        title = source.readString();
        backdropPath = source.readString();
        posterPath = source.readString();
        summary = source.readString();
        releaseDate = source.readString();
        voteAvg = source.readString();
        isFavorite = source.readByte() != 0;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdropImageUrl() {
        return IMAGE_BASE_URL + IMAGE_DEFAULT_POSTER_SIZE + "/" + backdropPath;
    }
    public String getPosterImageUrl() {
        return IMAGE_BASE_URL + IMAGE_DEFAULT_POSTER_SIZE + "/" + posterPath;
    }

    public String getSummary() {
        return summary;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getVoteAvg() {
        return voteAvg;
    }

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void toggleIsFavorite(Context context) {
        isFavorite = !isFavorite;
        if (isFavorite) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(FavoriteEntry.COLUMN_MOVIE_ID, id);
            contentValues.put(FavoriteEntry.COLUMN_TITLE, title);
            contentValues.put(FavoriteEntry.COLUMN_BACKDROP_PATH, backdropPath);
            contentValues.put(FavoriteEntry.COLUMN_POSTER_PATH, posterPath);
            contentValues.put(FavoriteEntry.COLUMN_SUMMARY, summary);
            contentValues.put(FavoriteEntry.COLUMN_RELEASE_DATE, releaseDate);
            contentValues.put(FavoriteEntry.COLUMN_VOTE_AVG, voteAvg);
            contentValues.put(FavoriteEntry.COLUMN_IS_FAVORITE, (isFavorite ? 1 : 0));

            Uri uri = context.getContentResolver().insert(FavoriteEntry.CONTENT_URI, contentValues);
            Log.d(TAG, "Added uri " + uri + " to favorites database");
        } else {
            // TODO(coreeny): delete the db entry
        }
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeString(posterPath);
        dest.writeString(summary);
        dest.writeString(releaseDate);
        dest.writeString(voteAvg);
        // Resource: https://stackoverflow.com/questions/6201311/how-to-read-write-a-boolean-when-implementing-the-parcelable-interface
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
