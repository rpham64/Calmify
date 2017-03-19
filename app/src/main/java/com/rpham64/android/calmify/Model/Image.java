package com.rpham64.android.calmify.model;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.ParcelProperty;

/**
 * Created by Rudolf on 2/3/2017.
 */
@Parcel(Parcel.Serialization.BEAN)
public class Image {

    @ParcelProperty("image")
    Integer mImage;

    @ParcelConstructor
    public Image(@ParcelProperty("image") Integer image) {
        mImage = image;
    }

    public Integer getImage() {
        return mImage;
    }

    public void setImage(Integer image) {
        mImage = image;
    }
}
