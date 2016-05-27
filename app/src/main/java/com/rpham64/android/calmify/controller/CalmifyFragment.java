package com.rpham64.android.calmify.controller;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
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
import com.rpham64.android.calmify.model.Song;
import com.rpham64.android.calmify.model.SongsManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rudolf on 5/24/2016.
 */
public class CalmifyFragment extends Fragment {

    private static final String TAG = "CalmifyFragment";

    private ImageView mBackgroundImage;
    private TextView mTimer;
    private TextView mSongTitle;
    private TextView mSongArtist;
    private ImageView mPrev;
    private ImageView mPlay;
    private ImageView mNext;

    private HashMap<Integer, Integer> images = new HashMap<>();

    private MediaPlayer mMediaPlayer;

    private SongsManager mSongsManager;
    private List<Song> mSongs;
    private int songIndex = 0;

    public static CalmifyFragment newInstance() {
        return new CalmifyFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        // Map images to corresponding songIndex
        // Used for displaying a new image for each song
        images.put(0, R.drawable.new_york);
        images.put(1, R.drawable.san_francisco);
        images.put(2, R.drawable.hong_kong);
        images.put(3, R.drawable.taipei101);
        images.put(4, R.drawable.tokyo);

        mSongsManager = new SongsManager(getActivity());
        mSongs = mSongsManager.getSongs();

        mMediaPlayer = new MediaPlayer();

        play();

        // TODO: Create a "repeat" button that turns setLooping on/off
//        mMediaPlayer.setLooping(true);

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                Log.i(TAG, "Music finished. Playing next song.");

                songIndex = (songIndex + 1) % mSongs.size();

                updateUI();
                play();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        View decorView = getActivity().getWindow().getDecorView();

        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_full_screen_media_player, container, false);

        mBackgroundImage = (ImageView) view.findViewById(R.id.background_image);
        mTimer = (TextView) view.findViewById(R.id.sleep_timer);
        mSongTitle = (TextView) view.findViewById(R.id.song_title);
        mSongArtist = (TextView) view.findViewById(R.id.song_artist);
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
        mSongTitle.setText(mSongs.get(songIndex).getTitle().replace(".mp3", ""));
        mSongArtist.setText(mSongs.get(songIndex).getArtist());
        mBackgroundImage.setImageResource(images.get(songIndex));
    }

    /**
     * Plays song at current index in mSongs list
     *
     */
    private void play() {

        Log.i(TAG, "Now playing.");

        try {

            String currentSong =
                    mSongs.get(songIndex).getArtist() + " - " + mSongs.get(songIndex).getTitle();
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
