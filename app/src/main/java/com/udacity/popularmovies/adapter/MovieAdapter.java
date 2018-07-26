package com.udacity.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.utilities.DataUtils;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName();

    private final MovieAdapterOnClickHandler mClickHandler;
    private Context mContext;
    private Movie[] movies;

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie selectedMovie);
    }

    public MovieAdapter(Context mContext, MovieAdapterOnClickHandler mClickHandler) {
        this.mContext = mContext;
        this.mClickHandler = mClickHandler;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mMovieImageView;
        public final TextView mMovieTitle;
        public final ImageView mFavorite;

        public MovieAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = view.findViewById(R.id.iv_movie_thumbnail);
            mMovieTitle = view.findViewById(R.id.tv_movie_title);
            mFavorite = view.findViewById(R.id.iv_favorite);

            // Resource: https://stackoverflow.com/questions/1967039/onclicklistener-x-y-location-of-event
            mFavorite.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        final int adapterPosition = getAdapterPosition();
                        Movie selectedMovie = movies[adapterPosition];
                        DataUtils.toggleIsFavorite(mContext, selectedMovie);
                        Log.d(TAG, "Tagging movieId " + selectedMovie.getId() +
                                " as favorite: " + selectedMovie.getIsFavorite(mContext));
                        notifyItemChanged(adapterPosition);
                    }
                    return true;
                }
            });

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int adapterPosition = getAdapterPosition();
            Movie selectedMovie = movies[adapterPosition];
            mClickHandler.onClick(selectedMovie);
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        Movie selectedMovie = movies[position];
        Picasso.with(mContext)
                .load(selectedMovie.getPosterImageUrl())
                .placeholder(R.drawable.movie_placeholder)
                .error(R.drawable.movie_placeholder_error)
                .into(movieAdapterViewHolder.mMovieImageView);
        movieAdapterViewHolder.mMovieTitle.setText(selectedMovie.getTitle());
        movieAdapterViewHolder.mFavorite.setImageResource(selectedMovie.getIsFavorite(mContext) ?
                R.drawable.enabled_star :
                R.drawable.disabled_star);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == movies) {
            return 0;
        }
        return movies.length;
    }

    public void setMovieData(Movie[] movieData) {
        movies = movieData;
        notifyDataSetChanged();
    }
}
