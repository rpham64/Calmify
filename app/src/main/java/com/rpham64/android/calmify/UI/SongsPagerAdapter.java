package com.rpham64.android.calmify.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.rpham64.android.calmify.model.Image;
import com.rpham64.android.calmify.model.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rudolf on 2/3/2017.
 */

public class SongsPagerAdapter extends FragmentStatePagerAdapter {

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

        Log.i(TAG, "Song: " + mSongs.get(position));
        Log.i(TAG, "Image: " + mImages.get(position));
        Log.i(TAG, "Is null: " + CalmifyFragment.newInstance(mSongs.get(position), mImages.get(position)));

        return CalmifyFragment.newInstance(mSongs.get(position), mImages.get(position));
    }

    @Override
    public int getCount() {
        return mSongs.size();
    }
}
