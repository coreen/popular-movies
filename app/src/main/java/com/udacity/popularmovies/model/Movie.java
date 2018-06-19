package com.udacity.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public final class Movie implements Parcelable {
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_DEFAULT_POSTER_SIZE = "w185";

    private String title;
    private String backdropPath;
    private String posterPath;
    private String summary;
    private String releaseDate;
    private String voteAvg;

    public Movie(String title, String backdropPath, String posterPath, String summary,
                 String releaseDate, String voteAvg) {
        this.title = title;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.summary = summary;
        this.releaseDate = releaseDate;
        this.voteAvg = voteAvg;
    }

    // Note: Must read from parcel in same order contents were added
    public Movie(Parcel source) {
        title = source.readString();
        backdropPath = source.readString();
        posterPath = source.readString();
        summary = source.readString();
        releaseDate = source.readString();
        voteAvg = source.readString();
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

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeString(posterPath);
        dest.writeString(summary);
        dest.writeString(releaseDate);
        dest.writeString(voteAvg);
    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
