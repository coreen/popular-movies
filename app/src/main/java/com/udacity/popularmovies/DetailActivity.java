package com.udacity.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.utilities.JsonUtils;

public class DetailActivity extends AppCompatActivity {

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

        String selectedMovieString = intent.getStringExtra(Intent.EXTRA_TEXT);
        Movie selectedMovie = JsonUtils.parseMovieJson(selectedMovieString);
        Picasso.with(this)
                .load(selectedMovie.getBackdropImageUrl())
                .placeholder(R.drawable.movie_placeholder)
                .error(R.drawable.movie_placeholder_error)
                .into(mBackdrop);
        mTitle.setText(selectedMovie.getTitle());
        Picasso.with(this)
                .load(selectedMovie.getPosterImageUrl())
                .placeholder(R.drawable.movie_placeholder)
                .error(R.drawable.movie_placeholder_error)
                .into(mPoster);
        mReleaseDate.setText("Release Date: " + selectedMovie.getReleaseDate());
        mVoteAvg.setText(selectedMovie.getVoteAvg() + " / 10");
        mMovieSummary.setText(selectedMovie.getSummary());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
    }
}
