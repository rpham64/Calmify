package com.rpham64.android.calmify.model;

import android.util.Log;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.ParcelProperty;

/**
 * Keeps track of song title and any other info related to the song
 *
 */
@Parcel(Parcel.Serialization.FIELD)
public class Song {

    private static final String TAG = Song.class.getName();

    @ParcelProperty("assetPath")
    String mAssetPath;
    String mFileName;

    @ParcelConstructor
    public Song(@ParcelProperty("assetPath") String assetPath) {
        mAssetPath = assetPath;

        // Extract file name from asset path
        String[] components = assetPath.split("/");
        mFileName = components[components.length - 1];         // "## Title.ogg"

        Log.i(TAG, "Filename: " + mFileName);
    }

    @ParcelProperty("filename")
    public String getFileName() {
        return mFileName;
    }

    /**
     * Returns the title portion of the song filename
     *
     * "## title.ogg" -> "title"
     */
    @ParcelProperty("title")
    public String getTitle() {
        return mFileName.substring(3, mFileName.length() - 4);
    }
}
