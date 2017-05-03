package com.rpham64.android.calmify.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rudolf on 2/3/2017.
 */
public class Image implements Parcelable {

    int mImage;

    public Image(int image) {
        mImage = image;
    }

    public int getImage() {
        return mImage;
    }

    public void setImage(int image) {
        mImage = image;
    }

    protected Image(Parcel in) {
        mImage = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mImage);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}