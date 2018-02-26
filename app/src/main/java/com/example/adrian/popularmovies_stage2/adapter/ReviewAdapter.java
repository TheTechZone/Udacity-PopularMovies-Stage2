package com.example.adrian.popularmovies_stage2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adrian.popularmovies_stage2.R;
import com.example.adrian.popularmovies_stage2.data.model.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by adrian on 24.02.2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private List<Review> mReviews;
    private Context mContext;

    public class ViewHolder extends  RecyclerView.ViewHolder{

        @BindView(R.id.tv_review_author) TextView reviewAuthorTextView;
        @BindView(R.id.tv_review_body) TextView reviewContentTextView;

        public ViewHolder(View itemView) {
            super(itemView);
//            reviewAuthorTextView = itemView.findViewById(R.id.tv_review_author);
//            reviewContentTextView = itemView.findViewById(R.id.tv_review_body);
            ButterKnife.bind(this,itemView);
        }
    }

    public ReviewAdapter(Context context, List<Review> reviews){
        this.mContext = context;
        this.mReviews = reviews;
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View movieView = inflater.inflate(R.layout.review_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(movieView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder holder, int position) {
        Review review = mReviews.get(position);
        holder.reviewAuthorTextView.setText(review.getAuthor());
        holder.reviewContentTextView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public void updateReviews(List<Review> reviews){
        mReviews = reviews;
        notifyDataSetChanged();
    }
}
