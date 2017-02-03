package com.rpham64.android.calmify.ui.playback;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.rpham64.android.calmify.model.Song;
import com.rpham64.android.calmify.model.SongsManager;

import java.io.IOException;
import java.util.List;

/**
 * Main controller for the MediaPlayer
 */
public class PlaybackController implements Playback {

    private static final String TAG = "PlaybackController";

    private AssetManager mAssets;

    private MediaPlayer mMediaPlayer;
    private SongsManager mSongsManager;

    private List<Song> mSongs;

    public PlaybackController(Context context) {
        mAssets = context.getAssets();
        mMediaPlayer = new MediaPlayer();
        mSongsManager = new SongsManager(context);
        mSongs = mSongsManager.getSongs();
    }

    /**
     * Plays song at current position in playlist
     *
     * @param songIndex
     */
    @Override
    public void play(int songIndex) {

        try {

            String currentSong = mSongs.get(songIndex).getFileName();

            Log.i(TAG, "Now playing: " + mSongs.get(songIndex).getTitle());

            AssetFileDescriptor afd = mAssets.openFd("music/" + currentSong);
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepare();
            start();
            afd.close();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not play media player.");
        }
    }

    @Override
    public void start() {
        mMediaPlayer.start();
    }

    @Override
    public void pause() {
        mMediaPlayer.pause();
    }

    @Override
    public void stop() {
        mMediaPlayer.stop();
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }


    @Override
    public boolean isLooping() {
        return mMediaPlayer.isLooping();
    }
}
