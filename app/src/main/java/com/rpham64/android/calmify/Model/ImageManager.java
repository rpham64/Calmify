package com.rpham64.android.calmify.model;

import com.rpham64.android.calmify.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds available images and maps them to the current song.
 */
public class ImageManager {

    private static final String TAG = "ImageManager";

    private List<Integer> mImages;

    public ImageManager() {
        mImages = new ArrayList<>();

        mImages.add(R.drawable.coffee);
        mImages.add(R.drawable.rain);
        mImages.add(R.drawable.fall);
        mImages.add(R.drawable.fall_leaves);
        mImages.add(R.drawable.snow);
        mImages.add(R.drawable.lake_nature);
        mImages.add(R.drawable.aurora);
        mImages.add(R.drawable.lake_sunrise);
        mImages.add(R.drawable.fireflies_fall);
        mImages.add(R.drawable.snow_in_dark);
        mImages.add(R.drawable.foggy_forest);
        mImages.add(R.drawable.windmill);
        mImages.add(R.drawable.record_player);
    }

    public List<Integer> getImages() {
        return mImages;
    }
}
