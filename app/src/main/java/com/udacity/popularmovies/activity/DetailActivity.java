package com.udacity.popularmovies.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.fragment.ReviewFragment;
import com.udacity.popularmovies.fragment.TrailerFragment;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.utilities.DataUtils;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();

    private static final String MOVIE = "movie";
    private static final String MOVIE_ID_EXTRA = "movieId";

    private ScrollView mScrollView;

    private ImageView mBackdrop;
    private TextView mTitle;
    private ImageView mPoster;
    private ImageView mFavoriteStar;
    private TextView mReleaseDate;
    private TextView mVoteAvg;
    private TextView mMovieSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mScrollView = findViewById(R.id.detail_activity_scrollview);
        mScrollView.smoothScrollTo(0, 0);
        Log.d(TAG, "Setting scrollview to top");

        // find all views of activity
        mBackdrop = findViewById(R.id.iv_backdrop);
        mTitle = findViewById(R.id.tv_title);
        mPoster = findViewById(R.id.iv_poster);
        mFavoriteStar = findViewById(R.id.iv_favorite_star);
        mReleaseDate = findViewById(R.id.tv_release_date);
        mVoteAvg = findViewById(R.id.tv_vote_avg);
        mMovieSummary = findViewById(R.id.tv_summary);

        final Intent intent = getIntent();

        final Movie selectedMovie = intent.getParcelableExtra(MOVIE);
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

        final Context context = getBaseContext();
        mFavoriteStar.setImageResource(selectedMovie.getIsFavorite(context) ?
                R.drawable.enabled_star :
                R.drawable.disabled_star);
        mFavoriteStar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    DataUtils.toggleIsFavorite(context, selectedMovie);
                    Log.d(TAG, "Tagging movieId " + selectedMovie.getMovieId() +
                            " as favorite: " + selectedMovie.getIsFavorite(context));
                    mFavoriteStar.setImageResource(selectedMovie.getIsFavorite(context) ?
                            R.drawable.enabled_star :
                            R.drawable.disabled_star);
                }
                return true;
            }
        });

        // Grab associated trailer videos and reviews via movieId
        int movieId = selectedMovie.getMovieId();
        Bundle bundle = new Bundle();
        bundle.putInt(MOVIE_ID_EXTRA, movieId);

        Log.d(TAG, "Passing bundle into fragments: " + bundle);

        // Pass in the movieId into Fragments for processing
        ReviewFragment reviewFragment = new ReviewFragment();
        reviewFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.review_fragment_placeholder, reviewFragment)
                .commit();

        TrailerFragment trailerFragment = new TrailerFragment();
        trailerFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.trailer_fragment_placeholder, trailerFragment)
                .commit();
    }

    private void populateUI(Movie selectedMovie) {
        mTitle.setText(selectedMovie.getTitle());
        mReleaseDate.setText("Release Date:\n" + selectedMovie.getReleaseDate());
        mVoteAvg.setText("Rating: " + selectedMovie.getVoteAvg() + " / 10");
        mMovieSummary.setText("Synopsis:\n" + selectedMovie.getSummary());
    }
}
