package com.rpham64.android.calmify.ui.playback;

/**
 * Interface that deals with MediaPlayer controls, such as play, pause, stop, etc.
 */
public interface Playback {

    void play();

    void start();

    void pause();

    void stop();

    void release();

    boolean isPlaying();

    boolean isLooping();
}
