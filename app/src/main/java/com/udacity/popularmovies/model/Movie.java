package com.udacity.popularmovies.model;

public final class Movie {
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
}
