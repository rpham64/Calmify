package com.rpham64.android.calmify.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rpham64.android.calmify.R;
import com.rpham64.android.calmify.controller.PlaybackController;
import com.rpham64.android.calmify.model.ImageManager;
import com.rpham64.android.calmify.model.Song;
import com.rpham64.android.calmify.model.SongsManager;

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

    private PlaybackController mPlayback;

    private SongsManager mSongsManager;
    private List<Song> mSongs;

    private ImageManager imageManager;
    private List<Integer> mImages;

    private int songIndex = 0;

    public static CalmifyFragment newInstance() {
        return new CalmifyFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        
        mPlayback = new PlaybackController(getActivity());

        mSongsManager = new SongsManager(getActivity());
        mSongs = mSongsManager.getSongs();

        imageManager = new ImageManager();
        mImages = imageManager.getImages();

        play();
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

        updateUI();

        mPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                songIndex--;

                if (songIndex < 0) songIndex += mSongs.size();

                play();
                updateUI();
            }
        });

        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isPlaying()) {
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

                play();
                updateUI();
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

        // 1) Display song title
        String songTitle = mSongs.get(songIndex).getTitle();

        // "## song.ogg" -> "song"
        mSongTitle.setText(songTitle
                        .substring(songTitle.indexOf(' ') + 1)      // Remove song #
                        .replace(".ogg", "")                        // Remove file extension
        );

        // 2) Display live wallpaper (gif)

        // Image
        mBackgroundImage.setImageResource(mImages.get(songIndex));

        // Gif
        GifDrawable gifDrawable = (GifDrawable) mBackgroundImage.getDrawable();
        gifDrawable.start();

        // 3) Update play/pause button
        if (!isPlaying()) {
            setPlayButton();
        } else {
            setPausedButton();
        }

    }

    private void play() {
        mPlayback.play(songIndex);
    }

    private void start() {
        mPlayback.start();
    }

    private void pause() {
        mPlayback.pause();
    }

    private void stop() {
        mPlayback.stop();
    }

    private boolean isPlaying() {
        return mPlayback.isPlaying();
    }

    private boolean isLooping() {
        return mPlayback.isLooping();
    }
}
