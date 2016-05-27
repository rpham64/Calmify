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

    private List<Song> songs;
    private int songIndex = 0;

    public static CalmifyFragment newInstance() {
        return new CalmifyFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        images.put(0, R.drawable.taipei101);
        images.put(1, R.drawable.el_nido_milky_way);
        images.put(2, R.drawable.ipw954av999);

        SongsManager songsManager = new SongsManager(getActivity());
        songs = songsManager.getSongs();

        mMediaPlayer = new MediaPlayer();

        play();

        // TODO: Create a "repeat" button that turns setLooping on/off
//        mMediaPlayer.setLooping(true);

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                Log.i(TAG, "Music finished. Playing next song.");

                songIndex = (songIndex + 1) % songs.size();

                updatePlayingInfo();
                play();
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
        mSongArtist = (TextView) view.findViewById(R.id.song_artist);
        mPrev = (ImageView) view.findViewById(R.id.prev);
        mPlay = (ImageView) view.findViewById(R.id.play_pause);
        mNext = (ImageView) view.findViewById(R.id.next);

        if (!mMediaPlayer.isPlaying()) {
            mPlay.setImageResource(R.drawable.ic_play_button);
        }

        updatePlayingInfo();
        updateBackground();

        mPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (songIndex) {

                    case 0:
                        songIndex = songs.size() - 1;
                        break;
                    default:
                        songIndex--;

                }

                updatePlayingInfo();
                updateBackground();
                play();
            }
        });

        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mMediaPlayer.isPlaying()) {
                    mPlay.setImageResource(R.drawable.ic_pause_button);
                    start();
                } else {
                    mPlay.setImageResource(R.drawable.ic_play_button);
                    pause();
                }
            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                songIndex = (songIndex + 1) % songs.size();

                updatePlayingInfo();
                updateBackground();
                play();

            }
        });

        return view;
    }

    private void updateBackground() {
        mBackgroundImage.setImageResource(images.get(songIndex));
    }

    /**
     * Changes UI title and artist to current song's
     *
     */
    private void updatePlayingInfo() {
        mSongTitle.setText(songs.get(songIndex).getTitle().replace(".mp3", ""));
        mSongArtist.setText(songs.get(songIndex).getArtist());
        mPlay.setImageResource(R.drawable.ic_pause_button);
    }

    /**
     * Plays song at current index in songs list
     *
     */
    private void play() {

        try {

            String currentSong =
                    songs.get(songIndex).getArtist() + '-' + songs.get(songIndex).getTitle();
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
