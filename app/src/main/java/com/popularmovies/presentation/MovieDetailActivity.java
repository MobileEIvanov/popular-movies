package com.popularmovies.presentation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Animatable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.popularmovies.R;
import com.popularmovies.data.models.ImageConfiguration;
import com.popularmovies.databinding.ActivityMovieDetailBinding;
import com.popularmovies.entities.MovieItem;
import com.popularmovies.presentation.movies.MovieCollectionActivity;
import com.popularmovies.utils.UtilsConfiguration;

/**
 * An activity representing a single Movie detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MovieCollectionActivity}.
 */
public class MovieDetailActivity extends AppCompatActivity {
    ActivityMovieDetailBinding mBinding;

    private MovieItem mMovieItem;
    private UtilsConfiguration mImageConfig;

    private IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);


    BroadcastReceiver mConnectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final ConnectivityManager connMgr;
            connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            final android.net.NetworkInfo wifi = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            final android.net.NetworkInfo mobile = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


            if (wifi.isAvailable() || mobile.isAvailable()) {

                if (wifi.isConnected() || mobile.isConnected()) {

                    mBinding.layoutNoConnection.getRoot().setVisibility(View.INVISIBLE);
                    mBinding.contentDetails.setVisibility(View.VISIBLE);


                } else {
                    mBinding.layoutNoConnection.getRoot().setVisibility(View.VISIBLE);
                    mBinding.contentDetails.setVisibility(View.INVISIBLE);

                }
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        mImageConfig = UtilsConfiguration.getInstance(this);


        mMovieItem = getIntent().getParcelableExtra(MovieItem.MOVIE_DATA);

        populateData();

    }


    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(mConnectionReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unregisterReceiver(mConnectionReceiver);
    }


    private void populateData() {
        String imagePosterUrl = mImageConfig.getImageBaseURL() + mImageConfig.getImageMaxSize() + mMovieItem.getPosterPath();
        mBinding.ivThumbMovie.setImageURI(imagePosterUrl);

        mBinding.tvMovieTitle.setText(mMovieItem.getOriginalTitle());
        mBinding.tvPlotSynopsis.setText(mMovieItem.getOverview());
        mBinding.tvReleaseDate.setText(mMovieItem.getReleaseDate());
    }

    @Override
    public void onBackPressed() {

        supportFinishAfterTransition();
        super.onBackPressed();
    }

    BaseControllerListener<ImageInfo> mImageListener = new BaseControllerListener<ImageInfo>() {

        @Override
        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
            Log.i("DraweeUpdate", "Image is fully loaded!");

//            scheduleStartPostponedTransition(mBinding.ivThumbMovie);
        }

        @Override
        public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
            Log.i("DraweeUpdate", "Image is partly loaded! (maybe it's a progressive JPEG?)");
            if (imageInfo != null) {
                int quality = imageInfo.getQualityInfo().getQuality();
                Log.i("DraweeUpdate", "Image quality (number scans) is: " + quality);
            }
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
            Log.i("DraweeUpdate", "Image failed to load: " + throwable.getMessage());
        }
    };

    /**
     * https://guides.codepath.com/android/shared-element-activity-transition
     * Schedules the shared element transition to be started immediately
     * after the shared element has been measured and laid out within the
     * activity's view hierarchy. Some common places where it might make
     * sense to call this method are:
     * <p>
     * (1) Inside a Fragment's onCreateView() method (if the shared element
     * lives inside a Fragment hosted by the called Activity).
     * <p>
     * (2) Inside a Picasso Callback object (if you need to wait for Picasso to
     * asynchronously load/scale a bitmap before the transition can begin).
     **/
    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
//                        mBinding.headerImage.getViewTreeObserver().removeOnPreDrawListener(this);
//                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }
}
