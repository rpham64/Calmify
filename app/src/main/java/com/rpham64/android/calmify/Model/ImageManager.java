package com.rpham64.android.calmify.model;

import android.util.Log;

import com.rpham64.android.calmify.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds available images and maps them to the current song.
 */
public class ImageManager {

    private static final String TAG = ImageManager.class.getName();

    private List<Image> mImages = new ArrayList<>();

    public ImageManager() {

        List<Integer> mImageFiles = new ArrayList<>();

        mImageFiles.add(R.drawable.open_book_fall);
        mImageFiles.add(R.drawable.flower);
        mImageFiles.add(R.drawable.misty_plant);
        mImageFiles.add(R.drawable.fall_leaves);
        mImageFiles.add(R.drawable.sunrise_forest);
        mImageFiles.add(R.drawable.snow);
        mImageFiles.add(R.drawable.sunset2);
        mImageFiles.add(R.drawable.milky_way);
        mImageFiles.add(R.drawable.snow_in_dark);
        mImageFiles.add(R.drawable.foggy_forest);
        mImageFiles.add(R.drawable.calm_flowing_water);

        for (int i = 0; i < mImageFiles.size(); ++i) {
            Image image = new Image(mImageFiles.get(i));

            Log.i(TAG, "Image: " + image.getImage());

            mImages.add(image);
        }
    }

    public List<Image> getImages() {
        return mImages;
    }
}
