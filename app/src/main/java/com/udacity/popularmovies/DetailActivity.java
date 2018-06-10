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

import org.json.JSONException;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private TextView mMovieTitle;
    private ImageView mMovieThumbnail;
    private TextView mReleaseDate;
    private TextView mVoteAvg;
    private TextView mMovieSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // find all views of activity
        mMovieTitle = (TextView) findViewById(R.id.tv_movie_title);
        mMovieThumbnail = (ImageView) findViewById(R.id.iv_movie_thumbnail);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mVoteAvg = (TextView) findViewById(R.id.tv_vote_avg);
        mMovieSummary = (TextView) findViewById(R.id.tv_movie_summary);

        Intent intent = getIntent();
        final int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String selectedMovieString = intent.getStringExtra(Intent.EXTRA_TEXT);
        Movie selectedMovie = JsonUtils.parseMovieJson(selectedMovieString);
        mMovieTitle.setText(selectedMovie.getTitle());
        Picasso.with(this)
                .load(selectedMovie.getImageUrl())
                .placeholder(R.drawable.movie_placeholder)
                .error(R.drawable.movie_placeholder_error)
                .into(mMovieThumbnail);
        mReleaseDate.setText(selectedMovie.getReleaseDate());
        mVoteAvg.setText(selectedMovie.getVoteAvg());
        mMovieSummary.setText(selectedMovie.getSummary());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
    }
}
