package com.popularmovies.presentation.movies;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.popularmovies.R;
import com.popularmovies.databinding.MovieListContentBinding;
import com.popularmovies.entities.MovieItem;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by emil.ivanov on 2/18/18.
 *
 * Infinite scroll solution is based on this stack post
 * https://stackoverflow.com/questions/35673854/how-to-implement-infinite-scroll-in-gridlayout-recylcerview
 */
public class AdapterMovieCollection extends RecyclerView.Adapter<AdapterMovieCollection.ViewHolder> {


    private final List<MovieItem> mData;
    private final ICollectionInteraction mListenerMovieInteraction;
    private final String mImageBaseUrl;
    private final Context mContext;

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
            Picasso.with(mContext).load(uri).into(mBinding.ivThumbMovie);

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
    }
}
