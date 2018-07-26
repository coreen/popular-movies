package com.udacity.popularmovies.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.adapter.ReviewAdapter;
import com.udacity.popularmovies.model.Review;
import com.udacity.popularmovies.utilities.JsonUtils;
import com.udacity.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.net.URL;

public class ReviewFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {
    private static final String TAG = ReviewFragment.class.getSimpleName();

    private static final String MOVIE_ID_EXTRA = "movieId";

    private static final int MOVIE_REVIEW_LOADER = 10;

    private RecyclerView mRecyclerView;
    private ReviewAdapter mReviewAdapter;
    private Context mContext;

    private TextView mNoReviewsMessage;
    private ProgressBar mLoadingIndicator;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        // Inflate layout for fragment
        return inflater.inflate(R.layout.review_fragment, container,  false);
    }

    // Called immediately after "onCreateView", guarantees view was successfully created
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recyclerview_review);
        mNoReviewsMessage = view.findViewById(R.id.tv_no_reviews);
        mLoadingIndicator = view.findViewById(R.id.pb_review_loading_indicator);
    }

    // Called once the Activity this Fragment is loading in has finished creation
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);

        mReviewAdapter = new ReviewAdapter();
        mRecyclerView.setAdapter(mReviewAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            loadReviewData(bundle);
        }

        getLoaderManager().initLoader(MOVIE_REVIEW_LOADER, null, this);
    }

    private void showReviewDataView() {
        mNoReviewsMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showNoReviewsMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mNoReviewsMessage.setVisibility(View.VISIBLE);
    }

    private void loadReviewData(Bundle bundle) {
        // Check if have internet connection before kicking off AsyncTask
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            // NOTICE: This is different than the getSupportLoaderManager() for Activity class
            // https://medium.com/google-developers/making-loading-data-on-android-lifecycle-aware-897e12760832
            LoaderManager loaderManager = getLoaderManager();
            Loader<String> movieReviewLoader = loaderManager.getLoader(MOVIE_REVIEW_LOADER);
            if (movieReviewLoader == null) {
                loaderManager.initLoader(MOVIE_REVIEW_LOADER, bundle, this);
            } else {
                loaderManager.restartLoader(MOVIE_REVIEW_LOADER, bundle, this);
            }
        } else {
            showNoReviewsMessage();
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable final Bundle args) {
        Log.d(TAG, "onCreateLoader called with id: " + id + " and args: " + args);
        return new AsyncTaskLoader<String>(mContext) {
            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }
                mLoadingIndicator.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Nullable
            @Override
            public String loadInBackground() {
                int movieId = args.getInt(MOVIE_ID_EXTRA);
                URL reviewRequestUrl = NetworkUtils.buildReviewUrl(movieId);
                try {
                    String jsonReviewResponse = NetworkUtils.getResponseFromHttpUrl(reviewRequestUrl);
                    return jsonReviewResponse;
                } catch (Exception e) {
                    // error
                    Log.e(TAG, "Failed to retrieve reviews for movieId: " + movieId);
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data != null) {
            try {
                showReviewDataView();
                Log.d(TAG, "ReviewFragment onLoadFinished data: " + data);
                Review[] reviews = JsonUtils.parseReviewsFromJsonString(data);

                if (reviews.length == 0) {
                    showNoReviewsMessage();
                }

                mReviewAdapter.setReviewData(reviews);
            } catch (JSONException e) {
                e.printStackTrace();
                showNoReviewsMessage();
            }
        } else {
            showNoReviewsMessage();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {}
}
