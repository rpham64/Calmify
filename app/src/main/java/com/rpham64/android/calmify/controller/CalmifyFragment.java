package com.rpham64.android.calmify.controller;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rpham64.android.calmify.R;
import com.rpham64.android.calmify.model.ImageManager;
import com.rpham64.android.calmify.model.Song;
import com.rpham64.android.calmify.model.SongsManager;

import java.io.IOException;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Home fragment that houses the MediaPlayer
 *
 * Created by Rudolf on 5/24/2016.
 */
public class CalmifyFragment extends Fragment {

    private static final String TAG = "CalmifyFragment";

    private GifImageView mBackgroundImage;
    private TextView mTimer;
    private TextView mSongTitle;
    private ImageView mPrev;
    private ImageView mPlay;
    private ImageView mNext;

    private MediaPlayer mMediaPlayer;

    private ImageManager imageManager;
    private SongsManager songsManager;

    private List<Integer> mImages;
    private List<Song> mSongs;
    private int songIndex = 0;

    public static CalmifyFragment newInstance() {
        return new CalmifyFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        imageManager = new ImageManager();
        songsManager = new SongsManager(getActivity());

        mImages = imageManager.getImages();
        mSongs = songsManager.getSongs();

        mMediaPlayer = new MediaPlayer();

        play();

        // TODO: Create a "repeat" button that turns setLooping on/off
        mMediaPlayer.setLooping(true);

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                play();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= 19) hideStatusAndNavigationBars();
    }

    private void hideStatusAndNavigationBars() {
        View decorView = getActivity().getWindow().getDecorView();

        // Hide the status and navigation bars.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_full_screen_media_player, container, false);

        mBackgroundImage = (GifImageView) view.findViewById(R.id.background_image);
        mTimer = (TextView) view.findViewById(R.id.sleep_timer);
        mSongTitle = (TextView) view.findViewById(R.id.song_title);
        mPrev = (ImageView) view.findViewById(R.id.prev);
        mPlay = (ImageView) view.findViewById(R.id.play_pause);
        mNext = (ImageView) view.findViewById(R.id.next);

        // MediaPlayer not playing => Set "play" button
        if (!mMediaPlayer.isPlaying()) {
            setPlayButton();
        }

        updateUI();

        mPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                songIndex--;

                if (songIndex < 0) songIndex += mSongs.size();

                updateUI();
                setPausedButton();

                play();
            }
        });

        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mMediaPlayer.isPlaying()) {
                    setPausedButton();
                    start();
                } else {
                    setPlayButton();
                    pause();
                }
            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                songIndex = (songIndex + 1) % mSongs.size();

                updateUI();
                setPausedButton();

                play();

            }
        });

        return view;
    }

    /**
     * Sets play/pause button to PLAY
     */
    private void setPlayButton() {
        mPlay.setImageResource(R.drawable.ic_play_button);
    }

    /**
     * Sets play/pause button to PAUSE
     */
    private void setPausedButton() {
        mPlay.setImageResource(R.drawable.ic_pause_button);
    }

    /**
     * Changes title, artist, and background image to current song's
     *
     */
    private void updateUI() {

        String songTitle = mSongs.get(songIndex).getTitle();

        // "## song.ogg" -> "song"
        mSongTitle.setText(songTitle
                        .substring(songTitle.indexOf(' '))      // Remove song #
                        .replace(".ogg", "")                    // Remove file extension
        );

        // Image
        mBackgroundImage.setImageResource(mImages.get(songIndex));

        // Gif
        GifDrawable gifDrawable = (GifDrawable) mBackgroundImage.getDrawable();
        gifDrawable.start();
    }

    /**
     * Plays song at current index in mSongs list
     */
    private void play() {

        try {

            String currentSong = mSongs.get(songIndex).getTitle();

            Log.i(TAG, "Now playing: " + currentSong.substring(3, currentSong.length() - 4));

            AssetFileDescriptor afd =
                    getActivity().getAssets().openFd("music/" + currentSong);
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mMediaPlayer.prepare();
            start();
            afd.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mMediaPlayer.isLooping()) {
            mMediaPlayer.setNextMediaPlayer(mMediaPlayer);
        }

    }

    private void start() {
        mMediaPlayer.start();
    }

    private void pause() {
        mMediaPlayer.pause();
    }

    private void stop() {
        mMediaPlayer.stop();
    }

}
