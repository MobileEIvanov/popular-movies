package com.popularmovies.utils;

import android.animation.Animator;
import android.view.View;
import android.view.ViewAnimationUtils;

/**
 * Created by user on 2/27/18.
 */

public class UtilsAnimations {


    // TODO: 2/27/18 GIVE CREDIT FOR THIS IMPLEMENTATION
    public static void createCircularReveal(View container) {

        if (!container.isAttachedToWindow()) {
            return;
        }
        // get the center for the clipping circle
        int cx = container.getWidth() / 2;
        int cy = container.getHeight() / 2;

        // get the final radius for the clipping circle
        float finalRadius = (float) Math.hypot(cx, cy);

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(container, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        container.setVisibility(View.VISIBLE);
        anim.start();
    }


}
