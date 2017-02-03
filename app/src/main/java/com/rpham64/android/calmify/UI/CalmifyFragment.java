package com.rpham64.android.calmify.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rpham64.android.calmify.R;
import com.rpham64.android.calmify.model.Image;
import com.rpham64.android.calmify.model.Song;

import org.parceler.Parcels;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Home fragment that houses the MediaPlayer
 *
 * Created by Rudolf on 5/24/2016.
 */
public class CalmifyFragment extends Fragment {

    private static final String TAG = CalmifyFragment.class.getName();

    interface Extras {
        String song = "CalmifyFragment.song";
        String image = "CalmifyFragment.image";
    }

    @BindView(R.id.background_gif) GifImageView gifBackground;
    @BindView(R.id.song_title) TextView txtSong;

    private Song mSong;
    private Image mImage;

    public static CalmifyFragment newInstance(Song song, Image image) {

        Bundle args = new Bundle();
        args.putParcelable(Extras.song, Parcels.wrap(song));
        args.putParcelable(Extras.image, Parcels.wrap(image));

        CalmifyFragment fragment = new CalmifyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        Log.i(TAG, "onCreate");

        if (getArguments() != null) {
            mSong = Parcels.unwrap(getArguments().getParcelable(Extras.song));
            mImage = Parcels.unwrap(getArguments().getParcelable(Extras.image));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.fragment_calmify, container, false);
        ButterKnife.bind(this, view);

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 19) hideStatusAndNavigationBars();
    }

    /**
     * Changes title, artist, and background image to current song's
     */
    private void updateUI() {

        txtSong.setText(mSong.getTitle());

//        gifBackground.setImageResource(mImage.getImage());
//        GifDrawable gifDrawable = (GifDrawable) gifBackground.getDrawable();

        try {
            GifDrawable gifDrawable1 = new GifDrawable(getResources(), mImage.getImage());
            gifBackground.setBackground(gifDrawable1);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        gifDrawable.start();
    }

    private void hideStatusAndNavigationBars() {
        View decorView = getActivity().getWindow().getDecorView();

        // Hide the status and navigation bars.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
