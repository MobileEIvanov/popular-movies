package com.popularmovies.presentation.details;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.popularmovies.R;
import com.popularmovies.databinding.ItemLayoutMovieVideoBinding;
import com.popularmovies.entities.MovieVideo;

import java.util.List;

import javax.annotation.Nonnull;


/**
 * Created by emil.ivanov on 2/18/18.
 * <p>
 * Infinite scroll solution is based on this stack post
 * https://stackoverflow.com/questions/35673854/how-to-implement-infinite-scroll-in-gridlayout-recylcerview
 */
public class AdapterMovieVideos extends RecyclerView.Adapter<AdapterMovieVideos.ViewHolder> {


    private final List<MovieVideo> mData;
    private final ICollectionVideosInteraction mListenerMovieInteraction;


    AdapterMovieVideos(List<MovieVideo> movieItems,
                       ICollectionVideosInteraction listenerMovieInteraction) {
        this.mData = movieItems;
        this.mListenerMovieInteraction = listenerMovieInteraction;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@Nonnull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_movie_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@Nonnull final ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.bindData(mData.get(position));


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final View.OnClickListener mClickListenerPlay = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int position = (Integer) itemView.getTag();
                mListenerMovieInteraction.onVideoSelected(mData.get(position));
            }
        };
        private final View.OnClickListener mClickListenerShare = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int position = (Integer) itemView.getTag();
                mListenerMovieInteraction.onShareVideo(mData.get(position));
            }
        };

        private final ItemLayoutMovieVideoBinding mBinding;

        ViewHolder(View view) {
            super(view);
            mBinding = DataBindingUtil.bind(view);
            mBinding.flPlayIcon.setOnClickListener(mClickListenerPlay);
            mBinding.icShareVideo.setOnClickListener(mClickListenerShare);
        }

        private void bindData(MovieVideo movieVideo) {
            mBinding.tvTrailerTitle.setText(movieVideo.getName());
        }
    }



    public interface ICollectionVideosInteraction {
        void onVideoSelected(MovieVideo videoItem);

        void onShareVideo(MovieVideo videoItem);
    }
}
