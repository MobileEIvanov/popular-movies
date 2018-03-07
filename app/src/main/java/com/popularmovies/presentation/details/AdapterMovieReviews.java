package com.popularmovies.presentation.details;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.popularmovies.R;
import com.popularmovies.databinding.ItemLayoutMovieReviewBinding;
import com.popularmovies.entities.MovieReview;

import java.util.List;


/**
 * Created by emil.ivanov on 2/18/18.
 *
 * Infinite scroll solution is based on this stack post
 * https://stackoverflow.com/questions/35673854/how-to-implement-infinite-scroll-in-gridlayout-recylcerview
 */
public class AdapterMovieReviews extends RecyclerView.Adapter<AdapterMovieReviews.ViewHolder> {


    private final List<MovieReview> mData;
    private final ICollectionReviewInteraction mListenerMovieInteraction;
    private final Context mContext;

    AdapterMovieReviews(Context context, List<MovieReview> movieItems,
                        ICollectionReviewInteraction listenerMovieInteraction) {
        this.mData = movieItems;
        this.mListenerMovieInteraction = listenerMovieInteraction;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_movie_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.bindData(mData.get(position));

        if ((position >= getItemCount() - 1)) {
            mListenerMovieInteraction.onLoadMore();
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final View.OnClickListener mClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                mListenerMovieInteraction.onReviewSelected(mData.get(position));
            }
        };

        private final ItemLayoutMovieReviewBinding mBinding;

        ViewHolder(View view) {
            super(view);
            mBinding = DataBindingUtil.bind(view);
            itemView.setOnClickListener(mClickListener);
        }

        private void bindData(MovieReview movieReview) {
            mBinding.tvAuthorName.setText(movieReview.getAuthor());
            mBinding.tvReviewSummary.setText(movieReview.getContent());
        }
    }

    void addItemsCollection(List<MovieReview> newList) {
        if (!mData.containsAll(newList)) {
            mData.addAll(newList);
            notifyDataSetChanged();
        }
    }

    void updateCollection(List<MovieReview> newList) {
        mData.clear();
        mData.addAll(newList);
        notifyDataSetChanged();
    }

    public interface ICollectionReviewInteraction {
        void onReviewSelected(MovieReview movieReviewItem);

        void onLoadMore();
    }
}
