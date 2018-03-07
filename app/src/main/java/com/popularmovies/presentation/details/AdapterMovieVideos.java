package com.popularmovies.presentation.details;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.popularmovies.R;
import com.popularmovies.databinding.ItemLayoutMovieVideoBinding;
import com.popularmovies.databinding.MovieListContentBinding;
import com.popularmovies.entities.MovieItem;
import com.popularmovies.entities.MovieVideo;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by emil.ivanov on 2/18/18.
 *
 * Infinite scroll solution is based on this stack post
 * https://stackoverflow.com/questions/35673854/how-to-implement-infinite-scroll-in-gridlayout-recylcerview
 */
public class AdapterMovieVideos extends RecyclerView.Adapter<AdapterMovieVideos.ViewHolder> {


    private final List<MovieVideo> mData;
    private final ICollectionVideosInteraction mListenerMovieInteraction;
    private final Context mContext;

    AdapterMovieVideos(Context context, List<MovieVideo> movieItems,
                       ICollectionVideosInteraction listenerMovieInteraction) {
        this.mData = movieItems;
        this.mListenerMovieInteraction = listenerMovieInteraction;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_movie_video, parent, false);
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
                mListenerMovieInteraction.onVideoSelected(mData.get(position));
            }
        };

        private final ItemLayoutMovieVideoBinding mBinding;

        ViewHolder(View view) {
            super(view);
            mBinding = DataBindingUtil.bind(view);
            itemView.setOnClickListener(mClickListener);
        }

        private void bindData(MovieVideo movieVideo) {
            mBinding.tvTrailerTitle.setText(movieVideo.getName());
        }
    }

    void addItemsCollection(List<MovieVideo> newList) {
        if (!mData.containsAll(newList)) {
            mData.addAll(newList);
            notifyDataSetChanged();
        }
    }

    void updateCollection(List<MovieVideo> newList) {
        mData.clear();
        mData.addAll(newList);
        notifyDataSetChanged();
    }

    public interface ICollectionVideosInteraction {
        void onVideoSelected(MovieVideo videoItem);

        void onLoadMore();
    }
}
