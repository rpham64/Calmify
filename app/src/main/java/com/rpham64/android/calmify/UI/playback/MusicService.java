package com.rpham64.android.calmify.ui.playback;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.rpham64.android.calmify.model.Song;

import java.io.IOException;
import java.util.List;

/**
 * Main controller/service for the MediaPlayer
 *
 * https://code.tutsplus.com/tutorials/create-a-music-player-on-android-song-playback--mobile-22778
 */
public class MusicService extends Service implements Playback, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private static final String TAG = MusicService.class.getName();

    private final IBinder mMusicBinder = new MusicBinder();

    private AssetManager mAssets;
    private MediaPlayer mMediaPlayer;
    private List<Song> mSongs;
    private int mSongIndex;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMusicBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stop();
        release();
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mAssets = getApplicationContext().getAssets();
        mMediaPlayer = new MediaPlayer();
        mSongIndex = 0;

        initMusicPlayer();
    }

    public void initMusicPlayer() {
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

        Logger.d("MusicService: onPrepared");

        mMediaPlayer.setLooping(true);
        start();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Logger.d("MusicService: onCompletion");
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Logger.e("MusicService: onError");
        return false;
    }

    @Override
    public void play() {

        mMediaPlayer.reset();

        try {

            String currentSong = mSongs.get(mSongIndex).getFileName();

            Log.i(TAG, "Now playing: " + mSongs.get(mSongIndex).getTitle());

            AssetFileDescriptor afd = mAssets.openFd("music/" + currentSong);
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mMediaPlayer.prepareAsync();
            afd.close();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in MusicService. Could not start media player.");
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
    public void release() {
        mMediaPlayer.release();
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    @Override
    public boolean isLooping() {
        return mMediaPlayer.isLooping();
    }

    public void setSongs(List<Song> songs) {
        mSongs = songs;
    }

    public void setSong(int songIndex) {
        mSongIndex = songIndex;
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
