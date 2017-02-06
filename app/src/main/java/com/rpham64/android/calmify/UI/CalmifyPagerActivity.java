package com.rpham64.android.calmify.ui;

import android.os.Build;
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
import com.rpham64.android.calmify.ui.playback.PlaybackController;

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

    private PlaybackController mPlayback;
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

        mPlayback = new PlaybackController(this);

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
//                Log.i(TAG, "Page Position: " + position);
                stop();

                viewPager.setCurrentItem(position);
                play();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        listSongs.setAdapter(new ArrayAdapter<>(this, R.layout.list_song_info, mTitles));
        listSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewPager.setCurrentItem(position);
                layoutDrawer.closeDrawers();
            }
        });

        play();
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

        viewPager.setCurrentItem(nextItem);
        play();
    }

    @OnClick(R.id.next)
    public void onNextClicked() {

        int nextItem = viewPager.getCurrentItem() + 1;

        if (nextItem >= mSongs.size()) {
            nextItem = 0;
        }

        viewPager.setCurrentItem(nextItem);
        play();
    }

    private void play() {
        mPlayback.play(viewPager.getCurrentItem());
        setPausedButton();
    }

    private void start() {
        mPlayback.start();
    }

    private void pause() {
        mPlayback.pause();
        setPlayButton();
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
        btnPlayPause.setImageResource(R.mipmap.ic_play_button);
    }

    private void setPausedButton() {
        btnPlayPause.setImageResource(R.mipmap.ic_pause_button);
    }
}
