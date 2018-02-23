package com.popularmovies.presentation;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;

import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;
import com.popularmovies.R;
import com.popularmovies.databinding.ActivityMovieDetailBinding;
import com.popularmovies.entities.MovieItem;
import com.popularmovies.presentation.movies.MovieCollectionActivity;

/**
 * An activity representing a single Movie detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MovieCollectionActivity}.
 */
public class MovieDetailActivity extends AppCompatActivity {
    ActivityMovieDetailBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);

        Uri uri = Uri.parse(getIntent().getStringExtra(MovieItem.MOVIE_DATA));
        mBinding.ivThumbMovie.setImageURI(uri);
//
//        ImageDecodeOptions decodeOptions = ImageDecodeOptions.newBuilder()
//                .build();
//
//
//        ImageRequest request = ImageRequestBuilder
//                .newBuilderWithSource(uri)
//                .setImageDecodeOptions(decodeOptions)
//                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
//                .setResizeOptions(new ResizeOptions(400,200))
//                .build();
//
//
//        DraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setImageRequest(request)
//                .setControllerListener(mImageListener)
//                .build();
//
//        mBinding.ivThumbMovie.setController(controller);
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

    /** https://guides.codepath.com/android/shared-element-activity-transition
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