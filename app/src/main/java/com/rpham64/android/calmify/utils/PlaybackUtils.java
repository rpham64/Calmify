package com.rpham64.android.calmify.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.rpham64.android.calmify.model.Song;
import com.rpham64.android.calmify.ui.playback.MusicService;

import java.util.ArrayList;
import java.util.List;

/**
 * Compilation of boilerplate code for starting MusicService or initiating a new playback function.
 *
 * Created by Rudolf on 3/19/2017.
 */

public class PlaybackUtils {

    public static void play(Context context) {
        Intent intent = new Intent(context, MusicService.class);
        intent.putExtra(MusicService.Extras.ACTION, MusicService.Actions.PLAY);
        context.startService(intent);
    }

    public static void play(Context context, int songIndex) {
        Intent intent = new Intent(context, MusicService.class);
        intent.putExtra(MusicService.Extras.ACTION, MusicService.Actions.PLAY);
        intent.putExtra(MusicService.Extras.SONG_INDEX, songIndex);
        context.startService(intent);
    }

    public static void play(Context context, List<Song> mSongs, int songIndex) {
        Intent intent = new Intent(context, MusicService.class);
        intent.putExtra(MusicService.Extras.ACTION, MusicService.Actions.PLAY);
        intent.putParcelableArrayListExtra(MusicService.Extras.LIST_SONGS, new ArrayList<Parcelable>(mSongs));
        intent.putExtra(MusicService.Extras.SONG_INDEX, songIndex);
        context.startService(intent);
    }

    public static void pause(Context context) {
        Intent intent = new Intent(context, MusicService.class);
        intent.putExtra(MusicService.Extras.ACTION, MusicService.Actions.PAUSE);
        context.startService(intent);
    }

    public static void pause(Context context, int songIndex) {
        Intent intent = new Intent(context, MusicService.class);
        intent.putExtra(MusicService.Extras.ACTION, MusicService.Actions.PAUSE);
        intent.putExtra(MusicService.Extras.SONG_INDEX, songIndex);
        context.startService(intent);
    }

    public static void skip(Context context, int songIndex) {
        Intent intent = new Intent(context, MusicService.class);
        intent.putExtra(MusicService.Extras.ACTION, MusicService.Actions.SKIP);
        intent.putExtra(MusicService.Extras.SONG_INDEX, songIndex);
        context.startService(intent);
    }
}
