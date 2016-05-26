package com.rpham64.android.calmify.controller;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rpham64.android.calmify.R;

import java.io.IOException;
import java.util.ArrayList;
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

    private MediaPlayer mMediaPlayer;

    private List<Integer> songs;
    private int songIndex = 0;

    public static CalmifyFragment newInstance() {
        return new CalmifyFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        songs = new ArrayList<>();

        songs.add(R.raw.ffx_to_zanarkand);
        songs.add(R.raw.ffx_suteki_da_ne);
        songs.add(R.raw.ffx2_memory_of_lightwaves);

        mMediaPlayer = MediaPlayer.create(getActivity(), songs.get(songIndex));
        mMediaPlayer.setLooping(true);

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                songIndex = (songIndex + 1) % songs.size();
                play(songIndex);

            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_full_screen_media_player, container, false);

        mBackgroundImage = (ImageView) view.findViewById(R.id.background_image);
        mTimer = (TextView) view.findViewById(R.id.sleep_timer);
        mSongTitle = (TextView) view.findViewById(R.id.song_title);
        mSongArtist = (TextView) view.findViewById(R.id.song_title);
        mPrev = (ImageView) view.findViewById(R.id.prev);
        mPlay = (ImageView) view.findViewById(R.id.play_pause);
        mNext = (ImageView) view.findViewById(R.id.next);

        mPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (songIndex == 0) {
                    songIndex = songs.size() - 1;
                } else {
                    songIndex--;
                }

                play(songIndex);

            }
        });

        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                } else {
                    mMediaPlayer.pause();
                }
            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                songIndex = (songIndex + 1) % songs.size();
                play(songIndex);

            }
        });

        return view;
    }

    private void play(int songIndex) {

        mMediaPlayer.reset();

        if (songIndex < songs.size()) {

            try {

                AssetFileDescriptor afd =
                        getActivity().getResources().openRawResourceFd(songs.get(songIndex));

                if (afd != null) {

                    mMediaPlayer.setDataSource(
                            afd.getFileDescriptor(),
                            afd.getStartOffset(),
                            afd.getLength());

                    mMediaPlayer.prepare();
                    afd.close();

                    mMediaPlayer.start();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

}
