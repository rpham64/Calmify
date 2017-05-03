package com.rpham64.android.calmify.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.rpham64.android.calmify.R;
import com.rpham64.android.calmify.model.Image;
import com.rpham64.android.calmify.model.ImageManager;
import com.rpham64.android.calmify.model.Song;
import com.rpham64.android.calmify.model.SongsManager;
import com.rpham64.android.calmify.ui.playback.MusicService;
import com.rpham64.android.calmify.utils.PlaybackUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalmifyPagerActivity extends AppCompatActivity {

    private static final String TAG = CalmifyPagerActivity.class.getName();

    public interface SavedItems {
        String songIndex = "CalmifyPagerActivity.songIndex";
        String isPlaying = "CalmifyPagerActivity.isPlaying";
    }

    @BindView(R.id.drawer_layout) DrawerLayout layoutDrawer;
    @BindView(R.id.left_drawer) ListView listSongs;
    @BindView(R.id.fragment_view_pager) ViewPager viewPager;
    @BindView(R.id.play) ImageView btnPlay;
    @BindView(R.id.pause) ImageView btnPause;

    private SongsPagerAdapter mPagerAdapter;

    private List<Song> mSongs;
    private List<Image> mImages;
    private List<String> mTitles;

    private int mSongIndex = 0;
    private boolean isPlaying = false;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SavedItems.songIndex, mSongIndex);
        outState.putBoolean(SavedItems.isPlaying, isPlaying);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calmify);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            mSongIndex = savedInstanceState.getInt(SavedItems.songIndex);
            isPlaying = savedInstanceState.getBoolean(SavedItems.isPlaying);
        }

        // Set play/pause button on activity re-creation
        if (isPlaying) {
            setPausedButton();
        } else {
            setPlayButton();
        }

        mSongs = new SongsManager(this).getSongs();
        mImages = new ImageManager().getImages();

        mTitles = new ArrayList<>(mSongs.size());

        for (Song song : mSongs) {
            mTitles.add(song.getTitle());
        }

        mPagerAdapter = new SongsPagerAdapter(getSupportFragmentManager(), mSongs, mImages);
        viewPager.setAdapter(mPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // If page selected is a different page, change the song
                if (mSongIndex != position) changeSong(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        listSongs.setAdapter(new ArrayAdapter<>(this, R.layout.list_song_info, mTitles));
        listSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changeSong(position);
                layoutDrawer.closeDrawers();
            }
        });

        // Start MusicService if not currently active
        if (!isMyServiceRunning(MusicService.class)) {

            Log.i(TAG, "Running the service.");

            PlaybackUtils.play(this, mSongs, 0);
            isPlaying = true;
            setPausedButton();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    @OnClick(R.id.play)
    public void onPlayClicked() {
        PlaybackUtils.play(this, mSongIndex);
        isPlaying = true;
        setPausedButton();
    }

    @OnClick(R.id.pause)
    public void onPauseClicked() {
        PlaybackUtils.pause(this, mSongIndex);
        isPlaying = false;
        setPlayButton();
    }

    @OnClick(R.id.prev)
    public void onPrevClicked() {

        Log.i(TAG, "Prev clicked");

        mSongIndex = viewPager.getCurrentItem() - 1;

        if (mSongIndex < 0) mSongIndex = mSongs.size() - 1;

        changeSong(mSongIndex);
    }

    @OnClick(R.id.next)
    public void onNextClicked() {

        Log.i(TAG, "Next clicked");

        mSongIndex = viewPager.getCurrentItem() + 1;

        if (mSongIndex >= mSongs.size()) mSongIndex = 0;

        changeSong(mSongIndex);
    }

    public void changeSong(int position) {

        Log.i(TAG, "Changing song");

        mSongIndex = position;

        // 1) Change background UI
        viewPager.setCurrentItem(position);

        // 2) Change song on MusicService
        PlaybackUtils.skip(this, position);

        setPausedButton();
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void setToImmersiveMode() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    public void setPlayButton() {
        btnPlay.setVisibility(View.VISIBLE);
        btnPause.setVisibility(View.GONE);
    }

    public void setPausedButton() {
        btnPlay.setVisibility(View.GONE);
        btnPause.setVisibility(View.VISIBLE);
    }
}
