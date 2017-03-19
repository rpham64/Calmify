package com.rpham64.android.calmify.ui.playback;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.rpham64.android.calmify.R;
import com.rpham64.android.calmify.model.Song;
import com.rpham64.android.calmify.ui.CalmifyPagerActivity;

import org.parceler.Parcels;

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

    public interface Extras {
        String ACTION = "MusicService.Extras.ACTION";
        String LIST_SONGS = "MusicService.Extras.LIST_SONGS";
        String SONG_INDEX = "MusicService.Extras.SONG_INDEX";
    }

    public interface Actions {
        String PLAY = "MusicService.Actions.PLAY";
        String PAUSE = "MusicService.Actions.PAUSE";
        String SKIP = "MusicService.Actions.SKIP";
    }

    private static final int NOTIFICATION_ID = 1;

    private AssetManager mAssets;
    private MediaPlayer mMediaPlayer;

    // Notification
    private NotificationManager mNotificationManager;
    private Notification.Builder mNotificationBuilder = null;

    // Passed via intent from CalmifyPagerActivity
    private List<Song> mSongs;
    private Song mSong;
    private String mSongTitle;
    private int mSongIndex;

    @Override
    public void onCreate() {
        super.onCreate();
        mAssets = getApplicationContext().getAssets();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mSongIndex = 0;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // PLAY, PAUSE, PREV or NEXT
        String action = intent.getStringExtra(Extras.ACTION);

        if (intent.getExtras() != null) {

            // Wrap parcelable in activity using Parcels.wrap
            if (mSongs == null) mSongs = Parcels.unwrap(intent.getParcelableExtra(Extras.LIST_SONGS));

            mSongIndex = intent.getIntExtra(Extras.SONG_INDEX, 0);

            setupMediaPlayer();
        }

        switch (action) {

            case Actions.PLAY:
                play();
                break;
            case Actions.PAUSE:
                pause();
                break;
            case Actions.SKIP:
                skip();
                break;

        }

        return START_NOT_STICKY;
    }

    private void setupMediaPlayer() {

        createMediaPlayerIfNeeded();

        try {

            mSong = mSongs.get(mSongIndex);
            mSongTitle = mSong.getTitle();

            Log.i(TAG, "Now playing: " + mSongTitle);

            AssetFileDescriptor afd = mAssets.openFd("music/" + mSong.getFileName());
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mMediaPlayer.prepareAsync();
            afd.close();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in MusicService. Could not start media player.");
        }
    }

    /**
     * Creates media player if one does not exist. Else, resets the existing one.
     */
    private void createMediaPlayerIfNeeded() {

        if (mMediaPlayer == null) {

            mMediaPlayer = new MediaPlayer();

            // Make sure the media player will acquire a wake-lock while
            // playing. If we don't do that, the CPU might go to sleep while the
            // song is playing, causing playback to stop.
            mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnErrorListener(this);
        } else {
            mMediaPlayer.reset();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

        Logger.d("MusicService: onPrepared");

        mMediaPlayer.setLooping(true);
        updateNotification(mSongTitle + " (playing)");
        mMediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Logger.d("MusicService: onCompletion");
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Logger.e("MusicService: onError");
        Log.e(TAG, "Error: what=" + String.valueOf(i) + ", extra=" + String.valueOf(i1));

        Toast.makeText(getApplicationContext(), "MediaPlayer Error! Something broke!", Toast.LENGTH_SHORT).show();
        relaxResources(true);

        return true;    // True means we handled the error
    }

    @Override
    public void play() {
        if (!mMediaPlayer.isPlaying()) {
            setUpAsForeground(mSongTitle + " (playing)");
            mMediaPlayer.start();
        }
    }

    @Override
    public void pause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            relaxResources(false);
        }
    }

    @Override
    public void skip() {
        setupMediaPlayer();
        setUpAsForeground(mSongTitle + " (playing)");
        mMediaPlayer.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Configures service as a foreground service. A foreground service is a service that's doing
     * something the user is actively aware of (such as playing music), and must appear to the
     * user as a notification. That's why we create the notification here.
     */
    public void setUpAsForeground(String text) {
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), CalmifyPagerActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Build the notification object.
        mNotificationBuilder = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_play_button)
                .setTicker(text)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Calmify Notifications Test")
                .setContentText(text)
                .setContentIntent(pi)
                .setOngoing(true);

        startForeground(NOTIFICATION_ID, mNotificationBuilder.build());
    }

    /** Updates the notification. */
    void updateNotification(String text) {
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), CalmifyPagerActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        mNotificationBuilder.setContentText(text)
                .setContentIntent(pi);
        mNotificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build());
    }

    /**
     * Releases resources used by the service for playback. This includes the "foreground service"
     * status and notification, the wake locks and possibly the MediaPlayer.
     *
     * @param releaseMediaPlayer Indicates whether the Media Player should also be released or not
     */
    public void relaxResources(boolean releaseMediaPlayer) {
        // stop being a foreground service
        stopForeground(true);

        // stop and release the Media Player, if it's available
        if (releaseMediaPlayer && mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
