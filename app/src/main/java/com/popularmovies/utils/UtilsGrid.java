package com.popularmovies.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by emil.ivanov on 2/27/18.
 * <p>
 * The solution is based on the following stack post
 * https://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns
 */

public class UtilsGrid {

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

}
