package com.rpham64.android.calmify.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Finds available songs and keeps track of them
 */
public class SongsManager {

    private static final String TAG = "SongsManager";
    private static final String SONGS_FOLDER = "music";

    private AssetManager mAssets;
    private List<Song> mSongs;

    public SongsManager(Context context) {
        mAssets = context.getAssets();
        loadSongs();
    }

    private void loadSongs() {

        mSongs = new ArrayList<>();
        String[] songNames;

        try {

            songNames = mAssets.list(SONGS_FOLDER);
            Log.i(TAG, "Found " + songNames.length + " songs");

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Add each sound in soundNames to mSounds
        for (String filename : songNames) {

            Log.i(TAG, "filename: " + filename);

            String assetPath = SONGS_FOLDER + "/" + filename;
            Song song = new Song(assetPath);
            mSongs.add(song);

        }

    }

    public List<Song> getSongs() {
        return mSongs;
    }
}
