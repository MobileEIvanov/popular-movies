package com.popularmovies.presentation;


import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.popularmovies.R;
import com.popularmovies.databinding.MovieListContentBinding;
import com.popularmovies.entities.MovieItem;

import java.util.List;


/**
 * Created by emil.ivanov on 2/18/18.
 */
public class AdapterMovieCollection extends RecyclerView.Adapter<AdapterMovieCollection.ViewHolder> {


    private List<MovieItem> mData;
    private final ICollectionInteraction mListenerMovieInteraction;
    private final String mImageBaseUrl;

    public AdapterMovieCollection(List<MovieItem> movieItems, ICollectionInteraction listenerMovieInteraction, String imageBaseUrl) {
        this.mData = movieItems;
        this.mListenerMovieInteraction = listenerMovieInteraction;
        this.mImageBaseUrl = imageBaseUrl;
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

        private MovieListContentBinding mBinding;

        ViewHolder(View view) {
            super(view);
            mBinding = DataBindingUtil.bind(view);
            itemView.setOnClickListener(mClickListener);
        }

        private void bindData(MovieItem movieItem) {
            Uri uri = Uri.parse(mImageBaseUrl + movieItem.getPosterPath());
            Log.d("Movies adapter", "bindData: " + uri);
            mBinding.ivThumbMovie.setImageURI(uri);
        }
    }


    public void updateCollection(List<MovieItem> newList) {
        mData.clear();
        mData.addAll(newList);
        notifyDataSetChanged();
    }

    public interface ICollectionInteraction {
        void onMovieSelected(MovieItem movieItem, View imageView);
    }
}
