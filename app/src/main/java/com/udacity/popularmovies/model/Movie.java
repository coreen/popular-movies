package com.udacity.popularmovies.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.udacity.popularmovies.utilities.DataUtils;

public final class Movie implements Parcelable {
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_DEFAULT_POSTER_SIZE = "w185";

    private int movieId;
    private String title;
    private String backdropPath;
    private String posterPath;
    private String summary;
    private String releaseDate;
    private String voteAvg;

    public Movie(int movieId, String title, String backdropPath, String posterPath,
                 String summary, String releaseDate, String voteAvg) {
        this.movieId = movieId;
        this.title = title;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.summary = summary;
        this.releaseDate = releaseDate;
        this.voteAvg = voteAvg;
    }

    // Note: Must read from parcel in same order contents were added
    public Movie(Parcel source) {
        movieId = source.readInt();
        title = source.readString();
        backdropPath = source.readString();
        posterPath = source.readString();
        summary = source.readString();
        releaseDate = source.readString();
        voteAvg = source.readString();
    }

    public int getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getBackdropImageUrl() {
        return IMAGE_BASE_URL + IMAGE_DEFAULT_POSTER_SIZE + "/" + backdropPath;
    }

    public String getPosterPath() {
        return posterPath;
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

    public boolean getIsFavorite(Context context) {
        return DataUtils.getFavorite(context, movieId);
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeString(posterPath);
        dest.writeString(summary);
        dest.writeString(releaseDate);
        dest.writeString(voteAvg);
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
