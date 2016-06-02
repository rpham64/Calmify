package com.rpham64.android.calmify.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rpham64.android.calmify.R;
import com.rpham64.android.calmify.controller.PlaybackController;
import com.rpham64.android.calmify.model.ImageManager;
import com.rpham64.android.calmify.model.Song;
import com.rpham64.android.calmify.model.SongsManager;

import java.util.ArrayList;
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

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private PlaybackController mPlayback;

    private SongsManager mSongsManager;
    private List<Song> mSongs;
    private List<String> mTitles;

    private ImageManager imageManager;
    private List<Integer> mImages;

    // Current song's position in playlist
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
        mTitles = new ArrayList<>(mSongs.size());

        for (Song song : mSongs) {

            // "## song.ogg" -> "song"
            String title = song.getTitle()
                    .substring(song.getTitle().indexOf(' ') + 1,
                            song.getTitle().indexOf('.'));

            mTitles.add(title);
        }

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

        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) view.findViewById(R.id.left_drawer);

        mBackgroundImage = (GifImageView) view.findViewById(R.id.background_image);
        mTimer = (TextView) view.findViewById(R.id.sleep_timer);
        mSongTitle = (TextView) view.findViewById(R.id.song_title);
        mPrev = (ImageView) view.findViewById(R.id.prev);
        mPlay = (ImageView) view.findViewById(R.id.play_pause);
        mNext = (ImageView) view.findViewById(R.id.next);

        updateUI();

        /*mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });*/

        // Set adapter for list view
        mDrawerList.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.list_song_info, mTitles));

        // Set list's click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                songIndex = position;
                mDrawerLayout.closeDrawers();
                play();
                updateUI();
            }
        });

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
        mSongTitle.setText(mTitles.get(songIndex));

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
