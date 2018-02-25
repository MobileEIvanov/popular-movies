package com.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.popularmovies.data.models.ImageConfiguration;

/**
 * Created by emil ivanov on 2/25/18.
 */

public class UtilsConfiguration {

    private static final String PREFERENCE_NAME = "image_config";
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private static final String IMAGE_BASE_URL = "image_base_url";
    private static final String IMAGE_MIN_SIZE = "image_min_size";
    private static final String IMAGE_MEDIUM_SIZE = "image_medium_size";
    private static final String IMAGE_MAX_SIZE = "image_max_size";
    private static final String IMAGE_ORIGINAL_SIZE = "image_original_size";

    private static final String CURRENT_MOVIE_CATEGORY = "movie_category";
    private static final String CURRENT_COLLECTION_PAGE = "collection_page";

    private static UtilsConfiguration mInstance;

    private UtilsConfiguration(Context context) {
        if (mPreferences == null) {
            mPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        mEditor = mPreferences.edit();

        mInstance = this;

    }

    public static UtilsConfiguration getInstance(Context context) {

        if (mInstance == null) {
            return new UtilsConfiguration(context);
        } else {
            return mInstance;
        }
    }

    public boolean storeImageBaseURL(String baseUrl) {
        mEditor.putString(IMAGE_BASE_URL, baseUrl);
        return mEditor.commit();
    }

    public String getImageBaseURL() {
        return mPreferences.getString(IMAGE_BASE_URL, null);
    }

    public void storeAllowedStillSize(int configurationItem, String imageSize) {
        switch (configurationItem) {
            case ImageConfiguration.STILL_SIZE_MIN_POSITION:
                storeMinImageSize(imageSize);
                break;
            case ImageConfiguration.STILL_SIZE_MEDIUM_POSITION:
                storeMediumImageSize(imageSize);
                break;
            case ImageConfiguration.STILL_SIZE_MAX_POSITION:
                storeMaxImageSize(imageSize);
                break;
            case ImageConfiguration.STILL_SIZE_ORIGINAL_POSITION:
                storeOriginalImageSize(imageSize);
                break;
        }
    }

    private void storeMinImageSize(String size) {
        mEditor.putString(IMAGE_MIN_SIZE, size);
        mEditor.apply();
    }

    private void storeMediumImageSize(String size) {
        mEditor.putString(IMAGE_MEDIUM_SIZE, size);
        mEditor.apply();
    }

    private void storeMaxImageSize(String size) {
        mEditor.putString(IMAGE_MAX_SIZE, size);
        mEditor.apply();
    }

    private void storeOriginalImageSize(String size) {
        mEditor.putString(IMAGE_ORIGINAL_SIZE, size);
        mEditor.apply();
    }

    public String getImageMinSize() {
        return mPreferences.getString(IMAGE_MIN_SIZE, null);
    }

    public String getImageMediumSize() {
        return mPreferences.getString(IMAGE_MEDIUM_SIZE, null);
    }

    public String getImageMaxSize() {
        return mPreferences.getString(IMAGE_MAX_SIZE, null);
    }

    public String getImageOriginalSize() {
        return mPreferences.getString(IMAGE_ORIGINAL_SIZE, null);
    }


    /**
     * Store user selected movie category
     * @param category
     */
    public void storeCurrentCategory(String category) {
        mEditor.putString(CURRENT_MOVIE_CATEGORY, category);
        mEditor.apply();
    }

    /**
     * Retrieve the last stored movie category or return empty string.
     * Empty string will trigger request for all movies
     * @return - movie category string or empty string.
     */
    public String getCurrentMovieCategory() {
        return mPreferences.getString(CURRENT_MOVIE_CATEGORY, "");
    }


    /**
     * Store user selected movie category
     * @param page - last request page for the collection
     */
    public void storeCurrentCollectionPage(long page) {
        mEditor.putLong(CURRENT_COLLECTION_PAGE, page);
        mEditor.apply();
    }

    /**
     * Retrieve the last stored page which was requested by the user or 0 for the initial page.
     * @return - long page id or 0
     */
    public long getCurrentCollectionPage() {
        return mPreferences.getLong(CURRENT_COLLECTION_PAGE, 0);
    }

}
