package com.rpham64.android.calmify.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Keeps track of song title and any other info related to the song
 *
 */
public class Song implements Parcelable {

    private static final String TAG = Song.class.getName();

    String mAssetPath;
    String mFileName;

    public Song(String assetPath) {
        mAssetPath = assetPath;

        // Extract file name from asset path
        String[] components = assetPath.split("/");
        mFileName = components[components.length - 1];         // "## Title.ogg"

        Log.i(TAG, "Filename: " + mFileName);
    }

    public String getFileName() {
        return mFileName;
    }

    /**
     * Returns the title portion of the song filename
     *
     * "## title.ogg" -> "title"
     */
    public String getTitle() {
        return mFileName.substring(3, mFileName.length() - 4);
    }

    protected Song(Parcel in) {
        mAssetPath = in.readString();
        mFileName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAssetPath);
        dest.writeString(mFileName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
}