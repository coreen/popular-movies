package com.udacity.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.model.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {
    private static final String TAG = ReviewAdapter.class.getSimpleName();

    private Review[] reviews;

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder holder, int position) {
        // Resource: https://medium.com/@haydar_ai/better-way-to-get-the-item-position-in-androids-recyclerview-820667d435d4
        holder.bind(holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        if (null == reviews) {
            return 0;
        }
        return reviews.length;
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView mContent;
        private TextView mAuthor;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            mContent = (TextView) itemView.findViewById(R.id.tv_review_content);
            mAuthor = (TextView) itemView.findViewById(R.id.tv_review_author);
        }

        public void bind(int position) {
            Review individualReview = reviews[position];
            mContent.setText(individualReview.getContent());
            mAuthor.setText(individualReview.getAuthor());
        }
    }

    public void setReviewData(Review[] reviewData) {
        reviews = reviewData;
        notifyDataSetChanged();
    }
}
