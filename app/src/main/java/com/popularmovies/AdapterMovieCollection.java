package com.popularmovies;


import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.popularmovies.databinding.MovieListContentBinding;
import com.popularmovies.entities.MovieItem;

import java.util.List;

/**
 * Created by emil.ivanov on 2/18/18.
 */
public class AdapterMovieCollection extends RecyclerView.Adapter<AdapterMovieCollection.ViewHolder> {


    private List<MovieItem> mData;
    private ICollectionInteraction mListenerMovieIteraction;

    public AdapterMovieCollection(List<MovieItem> mData, ICollectionInteraction mListenerMovieIteraction) {
        this.mData = mData;
        this.mListenerMovieIteraction = mListenerMovieIteraction;
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
                mListenerMovieIteraction.onMovieSelected();
            }
        };

        private MovieListContentBinding mBinding;

        ViewHolder(View view) {
            super(view);
            mBinding = DataBindingUtil.bind(view);
            itemView.setOnClickListener(mClickListener);
        }

        public void bindData(MovieItem movieItem) {
            Uri uri = Uri.parse(movieItem.getImageUrl());

            mBinding.ivThumbMovie.setImageURI(uri);
        }
    }

    public interface ICollectionInteraction {
        void onMovieSelected();
    }
}
