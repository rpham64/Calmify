package com.rpham64.android.calmify.model;

import android.util.Log;

/**
 * Keeps track of song title and any other info related to the song
 *
 */
public class Song {

    private final String TAG = "Song";

    private String mAssetPath;      // Uri: "music/## filename.ogg"
    private String mTitle;

    public Song(String assetPath) {
        mAssetPath = assetPath;

        // Extract file name from asset path
        String[] components = assetPath.split("/");
        mTitle = components[components.length - 1];         // "## Title.ogg"

        Log.i(TAG, "Title: " + mTitle);
    }

    public String getTitle() {
        return mTitle;
    }
}
