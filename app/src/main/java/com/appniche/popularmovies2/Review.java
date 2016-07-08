package com.appniche.popularmovies2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JayPrakash on 27-03-2016.
 */
public class Review implements Parcelable {

    private String content;
    private String author;

    public Review(String content, String author) {
        this.content = content;
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Review(Parcel source) {
        content = source.readString();
        author = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(content);
        dest.writeString(author);

    }

    public static final Parcelable.Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[0];
        }
    };
}
