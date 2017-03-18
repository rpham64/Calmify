package com.rpham64.android.calmify.ui.playback;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.rpham64.android.calmify.R;
import com.rpham64.android.calmify.model.Song;
import com.rpham64.android.calmify.ui.CalmifyPagerActivity;

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

    //notification id
    private static final int NOTIFY_ID=1;

    private final IBinder mMusicBinder = new MusicBinder();

    private AssetManager mAssets;
    private MediaPlayer mMediaPlayer;
    private List<Song> mSongs;
    private Song currentSong;
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

        // Make sure the media player will acquire a wake-lock while
        // playing. If we don't do that, the CPU might go to sleep while the
        // song is playing, causing playback to stop.
        mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

        Logger.d("MusicService: onPrepared");

        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();

        //notification
        Intent notIntent = new Intent(this, CalmifyPagerActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.mipmap.ic_play_button)
                .setTicker(currentSong.getTitle())
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(currentSong.getTitle());
        Notification not = builder.build();
        startForeground(NOTIFY_ID, not);
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

            currentSong = mSongs.get(mSongIndex);

            Log.i(TAG, "Now playing: " + mSongs.get(mSongIndex).getTitle());

            AssetFileDescriptor afd = mAssets.openFd("music/" + currentSong.getFileName());
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
