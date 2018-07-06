package com.udacity.popularmovies.fragment;

import android.content.Context;
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

    private RecyclerView mRecyclerView;
    private ReviewAdapter mReviewAdapter;
    private Context mContext;

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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_review);
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
    }

    // TODO(coreeny): temp testing string
    private final String jsonReviewResponse = "{\"id\":333339,\"page\":1,\"results\":[{\"author\":\"Columbusbuck\",\"content\":\"There's a moment in this movie when the central character says \\\"I love her\\\". His friend hastily replies, \\\"Slow down bro... she could be a 300 lb man living in his mom's basement.\\\" In the very brief beat between the two lines, I thought FINALLY a movie about falling in love completely independent of what a person's genetics may be. And then the crushing cynicism in a dystopian world where there is little to live for and even less to hope for: even in a nightmare, a fat man is undateable.\\r\\n\\r\\nDon't worry though. If you have the skills you may end up on top. Just like Ben Mendelsohn's slave-owning antagonist almost did. Because in this story, it's not a world where anyone feels compassion or empathy. It's a world where everyone only thinks of themselves. A sociopath's dream. And that's what earns the top prize.\\r\\n\\r\\nI love looking at Tye Sheridan. But not enough to sit through this again.\",\"id\":\"5ac333d092514126c302d333\",\"url\":\"https://www.themoviedb.org/review/5ac333d092514126c302d333\"},{\"author\":\"izgzhen\",\"content\":\"I watched this movie because I was once working in the VR industry and really curious what it could tell us (even though I am not really interested after watching the trailer).\\r\\n\\r\\nAnyway, the experience is not bad (in 4DX). But not recommended for a second watch since there is little to dig and feel except for the numerous, eye-dazzling amount of Easter eggs.\\r\\n\\r\\nSorry.\",\"id\":\"5ac8816fc3a36834d7040f13\",\"url\":\"https://www.themoviedb.org/review/5ac8816fc3a36834d7040f13\"},{\"author\":\"crackerboi\",\"content\":\"Another great movie from Steven Spielberg\",\"id\":\"5adc381c0e0a26144501a3c6\",\"url\":\"https://www.themoviedb.org/review/5adc381c0e0a26144501a3c6\"},{\"author\":\"lasse\",\"content\":\"Obviously everything from the book cannot go into the movie, however a lot of what got into the movie was not from the book. Too much focus on car chases and too little on solving a puzzle.\\r\\n\\r\\nIf you haven't read the book, read the book! it's much more awesome!\",\"id\":\"5ae455889251416ce400528b\",\"url\":\"https://www.themoviedb.org/review/5ae455889251416ce400528b\"}],\"total_pages\":1,\"total_results\":4}";

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<String>(mContext) {
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
        if (data != null) {
            try {
                Review[] reviews = JsonUtils.parseReviewsFromJsonString(jsonReviewResponse);
                mReviewAdapter.setReviewData(reviews);
            } catch (JSONException e) {
                e.printStackTrace();
                // show "No Reviews" text
            }
        } else {
            // show "No Reviews" text
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {}
}