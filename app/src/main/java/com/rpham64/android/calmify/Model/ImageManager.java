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

        mImages.add(R.drawable.open_book_fall);
        mImages.add(R.drawable.flower);
        mImages.add(R.drawable.misty_plant);
        mImages.add(R.drawable.fall_leaves);
        mImages.add(R.drawable.sunrise_forest);
        mImages.add(R.drawable.snow);
        mImages.add(R.drawable.sunset2);
        mImages.add(R.drawable.milky_way);
        mImages.add(R.drawable.snow_in_dark);
        mImages.add(R.drawable.foggy_forest);
        mImages.add(R.drawable.calm_flowing_water);
    }

    public List<Integer> getImages() {
        return mImages;
    }
}
