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
import com.rpham64.android.calmify.model.ImageManager;
import com.rpham64.android.calmify.model.Song;
import com.rpham64.android.calmify.model.SongsManager;
import com.rpham64.android.calmify.ui.playback.PlaybackController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Home fragment that houses the MediaPlayer
 *
 * Created by Rudolf on 5/24/2016.
 */
public class CalmifyFragment extends Fragment {

    private static final String TAG = CalmifyFragment.class.getName();

    @BindView(R.id.drawer_layout) DrawerLayout layoutDrawer;
    @BindView(R.id.left_drawer) ListView listSongs;
    @BindView(R.id.background_gif) GifImageView gifBackground;
    @BindView(R.id.song_title) TextView txtSong;
    @BindView(R.id.play_pause) ImageView btnPlay;
    @BindView(R.id.prev) ImageView btnPrev;
    @BindView(R.id.next) ImageView btnNext;

    private PlaybackController mPlayback;

    private SongsManager mSongsManager;
    private List<Song> mSongs;
    private List<String> mTitles;

    private ImageManager imageManager;
    private List<Integer> mImages;

    // Current song's position in playlist
    private int currentIndex = 0;

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

        // Song Titles
        for (Song song : mSongs) {

            // "## song.ogg" -> "song"
            String title = song.getTitle().substring(3, song.getTitle().length() - 4);

            mTitles.add(title);
        }

        // Gif Images
        imageManager = new ImageManager();
        mImages = imageManager.getImages();

        play();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_full_screen_media_player, container, false);
        ButterKnife.bind(this, view);

        listSongs.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.list_song_info, mTitles));
        listSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentIndex = position;
                layoutDrawer.closeDrawers();
            }
        });

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 19) hideStatusAndNavigationBars();
    }

    @OnClick(R.id.play_pause)
    public void onPlayPauseClicked() {
        if (isPlaying()) {
            pause();
            setPlayButton();
        } else {
            start();
            setPausedButton();
        }
    }

    @OnClick(R.id.prev)
    public void onPrevClicked() {
        if (currentIndex == 0) {
            currentIndex = mSongs.size() - 1;
        } else {
            --currentIndex;
        }

        play();
        updateUI();
    }

    @OnClick(R.id.next)
    public void onNextClicked() {
        currentIndex = (currentIndex + 1) % mSongs.size();

        play();
        updateUI();
    }

    private void hideStatusAndNavigationBars() {
        View decorView = getActivity().getWindow().getDecorView();

        // Hide the status and navigation bars.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /**
     * Changes title, artist, and background image to current song's
     */
    private void updateUI() {

        // 1) Display song title
        txtSong.setText(mTitles.get(currentIndex));

        // 2) Display live wallpaper (gif)

        // Image
        gifBackground.setImageResource(mImages.get(currentIndex));

        // Gif
        GifDrawable gifDrawable = (GifDrawable) gifBackground.getDrawable();
        gifDrawable.start();

        // 3) Update play/pause button
        if (!isPlaying()) {
            setPlayButton();
        } else {
            setPausedButton();
        }

    }

    private void play() {
        mPlayback.play(currentIndex);
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

    private void setPlayButton() {
        btnPlay.setImageResource(R.drawable.ic_play_button);
    }

    private void setPausedButton() {
        btnPlay.setImageResource(R.drawable.ic_pause_button);
    }
}
