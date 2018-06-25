package com.udacity.popularmovies;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.model.Review;
import com.udacity.popularmovies.utilities.JsonUtils;
import com.udacity.popularmovies.utilities.NetworkUtils;

import java.net.URL;

public class DetailActivity extends AppCompatActivity {
    private static final String MOVIE = "movie";

    private ImageView mBackdrop;
    private TextView mTitle;
    private ImageView mPoster;
    private TextView mReleaseDate;
    private TextView mVoteAvg;
    private TextView mMovieSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // find all views of activity
        mBackdrop = (ImageView) findViewById(R.id.iv_backdrop);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mPoster = (ImageView) findViewById(R.id.iv_poster);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mVoteAvg = (TextView) findViewById(R.id.tv_vote_avg);
        mMovieSummary = (TextView) findViewById(R.id.tv_summary);

        Intent intent = getIntent();

        Movie selectedMovie = intent.getParcelableExtra(MOVIE);
        populateUI(selectedMovie);
        Picasso.with(this)
                .load(selectedMovie.getBackdropImageUrl())
                .placeholder(R.drawable.movie_placeholder)
                .error(R.drawable.movie_placeholder_error)
                .into(mBackdrop);
        Picasso.with(this)
                .load(selectedMovie.getPosterImageUrl())
                .placeholder(R.drawable.movie_placeholder)
                .error(R.drawable.movie_placeholder_error)
                .into(mPoster);

        // Grab associated trailer videos and reviews via movieId
        URL videoRequestUrl = NetworkUtils.buildVideoUrl(selectedMovie.getId());
        try {
            String jsonVideoResponse = NetworkUtils.getResponseFromHttpUrl(videoRequestUrl);
            String[] videoKeys = JsonUtils.parseVideosFromJsonString(jsonVideoResponse);
            // TODO(coreeny) append necessary backpath to launch in web browser vs YouTube app
            // Web Browser launch
            Intent videoIntent = new Intent(
                    Intent.ACTION_VIEW ,
                    NetworkUtils.buildDetailVideoUri(videoKeys[i]));
            // Youtube launch
            intent.setComponent(new ComponentName(
                    "com.google.android.youtube",
                    "com.google.android.youtube.PlayerActivity"));
            this.startActivity(videoIntent);
        } catch (Exception e) {
            // error
        }

        URL reviewRequestUrl = NetworkUtils.buildReviewUrl(selectedMovie.getId());
        try {
            String jsonReviewResponse = NetworkUtils.getResponseFromHttpUrl(reviewRequestUrl);
            Review[] reviews = JsonUtils.parseReviewsFromJsonString(jsonReviewResponse);
        } catch (Exception e) {
            // error
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Movie selectedMovie) {
        mTitle.setText(selectedMovie.getTitle());
        mReleaseDate.setText("Release Date: " + selectedMovie.getReleaseDate());
        mVoteAvg.setText(selectedMovie.getVoteAvg() + " / 10");
        mMovieSummary.setText(selectedMovie.getSummary());
    }
}
