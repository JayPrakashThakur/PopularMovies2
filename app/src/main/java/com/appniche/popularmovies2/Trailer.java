package com.appniche.popularmovies2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JayPrakash on 27-03-2016.
 */
public class Trailer implements Parcelable{

    private String key;
    private String name;
    private String site;
    private String size;

    public Trailer(String key, String name, String site, String size) {
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Trailer(Parcel source) {
        key = source.readString();
        name = source.readString();
        site = source.readString();
        size = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeString(size);

    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[0];
        }
    };
}
