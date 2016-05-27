package com.rpham64.android.calmify.model;

import android.util.Log;

/**
 * Keeps track of song title and artist, the names the users see, and any other info
 * related to the song
 *
 */
public class Song {

    private final String TAG = "Song";

    private String mAssetPath;      // Uri: "music/filename.mp3"
    private String mTitle;
    private String mArtist;

    public Song(String assetPath) {
        mAssetPath = assetPath;

        // Extract file name from asset path
        String[] components = assetPath.split("/");
        String songInfo = components[components.length - 1];        // "Artist-Title.mp3"
        mArtist = songInfo.substring(0, songInfo.indexOf('-') - 1);
        mTitle = songInfo.substring(songInfo.indexOf('-') + 2);

        Log.i(TAG, "Artist: " + mArtist);
        Log.i(TAG, "Title: " + mTitle);
    }

    public String getAssetPath() {
        return mAssetPath;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getArtist() {
        return mArtist;
    }
}
