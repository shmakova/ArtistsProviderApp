package ru.shmakova.artistsapp.network.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shmakova on 15.04.16.
 */
public class Cover implements Parcelable {
    private String small;
    private String big;

    public Cover(String small, String big) {
        this.small = small;
        this.big = big;
    }

    public String getSmall() {
        return small;
    }

    public String getBig() {
        return big;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.small);
        dest.writeString(this.big);
    }

    protected Cover(Parcel in) {
        this.small = in.readString();
        this.big = in.readString();
    }

    public static final Parcelable.Creator<Cover> CREATOR = new Parcelable.Creator<Cover>() {
        @Override
        public Cover createFromParcel(Parcel source) {
            return new Cover(source);
        }

        @Override
        public Cover[] newArray(int size) {
            return new Cover[size];
        }
    };
}
