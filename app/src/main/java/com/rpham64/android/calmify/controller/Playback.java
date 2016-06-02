package com.rpham64.android.calmify.controller;

/**
 * Interface that deals with MediaPlayer controls, such as play, pause, stop, etc.
 */
public interface Playback {

    /**
     * Plays song at current index in mSongs list
     */
    void play(int songIndex);

    void start();

    void pause();

    void stop();

    boolean isPlaying();

    boolean isLooping();
}
