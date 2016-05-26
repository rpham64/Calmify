package com.rpham64.android.calmify.ui;

/**
 * Keeps track of song title and artist, the names the users see, and any other info
 * related to the song
 *
 */
public class Song {

    private String mAssetPath;
    private String mTitle;
    private String mArtist;

    public Song(String assetPath) {
        mAssetPath = assetPath;

        // Extract file name from asset path
        String[] components = assetPath.split("/");
//        mArtist = components[0].trim();
        mTitle = components[components.length - 1];
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
