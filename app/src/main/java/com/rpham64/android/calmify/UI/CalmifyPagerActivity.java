package com.rpham64.android.calmify.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.orhanobut.logger.Logger;
import com.rpham64.android.calmify.R;
import com.rpham64.android.calmify.model.Image;
import com.rpham64.android.calmify.model.ImageManager;
import com.rpham64.android.calmify.model.Song;
import com.rpham64.android.calmify.model.SongsManager;
import com.rpham64.android.calmify.ui.playback.MusicService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalmifyPagerActivity extends AppCompatActivity {

    private static final String TAG = CalmifyPagerActivity.class.getName();

    @BindView(R.id.drawer_layout) DrawerLayout layoutDrawer;
    @BindView(R.id.left_drawer) ListView listSongs;
    @BindView(R.id.fragment_view_pager) ViewPager viewPager;
    @BindView(R.id.play_pause) ImageView btnPlayPause;

    private MusicService mMusicService;
    private Intent mPlayIntent;

    private ServiceConnection mMusicConnection;

    private SongsPagerAdapter mPagerAdapter;

    private SongsManager mSongsManager;
    private List<Song> mSongs;
    private List<String> mTitles;

    private ImageManager mImageManager;
    private List<Image> mImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calmify);
        ButterKnife.bind(this);

        Log.i(TAG, "onCreate");

        mSongsManager = new SongsManager(this);
        mSongs = mSongsManager.getSongs();

        mImageManager = new ImageManager();
        mImages = mImageManager.getImages();

        mTitles = new ArrayList<>(mSongs.size());

        for (Song song : mSongs) {
            mTitles.add(song.getTitle());
        }

        mPagerAdapter = new SongsPagerAdapter(getSupportFragmentManager(), mSongs, mImages);
        viewPager.setAdapter(mPagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeSong(position);
                play();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mMusicConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

                Logger.d("MusicService connected");

                MusicService.MusicBinder binder = (MusicService.MusicBinder) iBinder;

                // Get Service
                mMusicService = binder.getService();

                // Pass list of songs
                mMusicService.setSongs(mSongs);

                mMusicService.setSong(0);
                mMusicService.play();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Logger.d("MusicService disconnected");
            }
        };

        listSongs.setAdapter(new ArrayAdapter<>(this, R.layout.list_song_info, mTitles));
        listSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changeSong(position);
                play();
                layoutDrawer.closeDrawers();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

        if (mPlayIntent == null) {
            mPlayIntent = new Intent(this, MusicService.class);
            bindService(mPlayIntent, mMusicConnection, Context.BIND_AUTO_CREATE);
            startService(mPlayIntent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        stopService(mPlayIntent);
        mMusicService = null;
        super.onDestroy();
    }

    /**
     * Immersive Full-screen Mode
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        View decorView = getWindow().getDecorView();

        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }

    @OnClick(R.id.play_pause)
    public void onPlayPauseClicked() {
        if (isPlaying()) {
            setPlayButton();
            pause();
        } else {
            setPausedButton();
            start();
        }
    }

    @OnClick(R.id.prev)
    public void onPrevClicked() {

        int nextItem = viewPager.getCurrentItem() - 1;

        if (nextItem < 0) {
            nextItem = mSongs.size() - 1;
        }

        changeSong(nextItem);
        play();
    }

    @OnClick(R.id.next)
    public void onNextClicked() {

        int nextItem = viewPager.getCurrentItem() + 1;

        if (nextItem >= mSongs.size()) {
            nextItem = 0;
        }

        changeSong(nextItem);
        play();
    }

    private void changeSong(int position) {
        viewPager.setCurrentItem(position);
        mMusicService.setSong(position);
    }

    private void play() {
        mMusicService.play();
        setPausedButton();
    }

    private void start() {
        mMusicService.start();
    }

    private void pause() {
        mMusicService.pause();
        setPlayButton();
    }

    private void stop() {
        mMusicService.stop();
    }

    private boolean isPlaying() {
        return mMusicService.isPlaying();
    }

    private boolean isLooping() {
        return mMusicService.isLooping();
    }

    private void setPlayButton() {
        btnPlayPause.setImageResource(R.mipmap.ic_play_button);
    }

    private void setPausedButton() {
        btnPlayPause.setImageResource(R.mipmap.ic_pause_button);
    }
}
