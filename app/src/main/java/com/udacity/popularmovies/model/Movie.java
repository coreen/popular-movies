package com.udacity.popularmovies.model;

public final class Movie {
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_DEFAULT_POSTER_SIZE = "w185";

    private String title;
    private String imagePath;
    private String summary;
    private String releaseDate;
    private String voteAvg;

    public Movie(String title, String imagePath, String summary,
                 String releaseDate, String voteAvg) {
        this.title = title;
        this.imagePath = imagePath;
        this.summary = summary;
        this.releaseDate = releaseDate;
        this.voteAvg = voteAvg;
    }

    public String getTitle() {
        return title;
    }

    public String getImagePath() {
        return IMAGE_BASE_URL + IMAGE_DEFAULT_POSTER_SIZE + "/" + imagePath;
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
