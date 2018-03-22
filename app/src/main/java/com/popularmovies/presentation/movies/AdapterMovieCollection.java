package com.popularmovies.presentation.movies;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.popularmovies.R;
import com.popularmovies.databinding.MovieListContentBinding;
import com.popularmovies.entities.MovieItem;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by emil.ivanov on 2/18/18.
 * <p>
 * Infinite scroll solution is based on this stack post
 * https://stackoverflow.com/questions/35673854/how-to-implement-infinite-scroll-in-gridlayout-recylcerview
 */
public class AdapterMovieCollection extends RecyclerView.Adapter<AdapterMovieCollection.ViewHolder> {


    private final List<MovieItem> mData;
    private final ICollectionInteraction mListenerMovieInteraction;
    private final String mImageBaseUrl;
    private final Context mContext;
    private final static int ITEMS_BEFORE_LOAD = 3;

    AdapterMovieCollection(Context context, List<MovieItem> movieItems, ICollectionInteraction listenerMovieInteraction, String imageBaseUrl) {
        this.mData = movieItems;
        this.mListenerMovieInteraction = listenerMovieInteraction;
        this.mImageBaseUrl = imageBaseUrl;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads != null && payloads.size() > 0 && payloads.get(0) instanceof MovieItem) {
            mData.get(position).setFavorite((((MovieItem) payloads.get(0))).isFavorite());
            holder.bindData(mData.get(position));
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.bindData(mData.get(position));

        if ((position >= getItemCount() - ITEMS_BEFORE_LOAD)) {
            mListenerMovieInteraction.onLoadMore();
        }
    }

    /**
     * Update the adapter items if the favorite status has changed
     * This method is invoked  only when the current selected filter is {@link MovieCollectionActivity#MOVIE_CATEGORY_FAVORITES}
     *
     * @param movieItem
     */
    public void removeItemFromFavorite(MovieItem movieItem) {
        if (!movieItem.isFavorite()) {
            int position = mData.indexOf(movieItem);
            mData.remove(movieItem);
            notifyItemRemoved(position);
            if (mData.size() == 0) {
                mListenerMovieInteraction.showEmptyList();
            }
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
                int position = getAdapterPosition();
                mListenerMovieInteraction.onMovieSelected(mData.get(position), mBinding.ivThumbMovie);
            }
        };

        private final MovieListContentBinding mBinding;

        ViewHolder(View view) {
            super(view);
            mBinding = DataBindingUtil.bind(view);
            itemView.setOnClickListener(mClickListener);
        }

        private void bindData(MovieItem movieItem) {
            Uri uri = Uri.parse(mImageBaseUrl + movieItem.getPosterPath());
            Picasso.with(mContext)
                    .load(uri)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.color.colorPrimary)
                    .error(R.drawable.empty_image)
                    .into(mBinding.ivThumbMovie, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            Picasso.with(mContext)
                                    .load(uri)
                                    .error(android.R.drawable.stat_notify_error)
                                    .into(mBinding.ivThumbMovie);
                        }
                    });

        }
    }

    void addItemsCollection(List<MovieItem> newList) {
        if (!mData.containsAll(newList)) {
            mData.addAll(newList);
            notifyDataSetChanged();
        }
    }

    void updateCollection(List<MovieItem> newList) {
        mData.clear();
        mData.addAll(newList);
        notifyDataSetChanged();
    }

    public interface ICollectionInteraction {
        void onMovieSelected(MovieItem movieItem, View imageView);

        void onLoadMore();

        void showEmptyList();
    }
}
