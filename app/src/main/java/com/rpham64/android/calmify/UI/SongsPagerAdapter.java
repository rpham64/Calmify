package com.rpham64.android.calmify.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.rpham64.android.calmify.model.Image;
import com.rpham64.android.calmify.model.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rudolf on 2/3/2017.
 */

public class SongsPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = SongsPagerAdapter.class.getName();

    private List<Song> mSongs;
    private List<Image> mImages;

    public SongsPagerAdapter(FragmentManager fm, List<Song> songs, List<Image> images) {
        super(fm);
        mSongs = new ArrayList<>(songs);
        mImages = new ArrayList<>(images);
    }

    @Override
    public Fragment getItem(int position) {

        Log.i(TAG, "Position: " + position);

        return CalmifyFragment.newInstance(mSongs.get(position), mImages.get(position));
    }

    @Override
    public int getCount() {
        return mSongs.size();
    }
}
