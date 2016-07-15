package com.appniche.popularmovies2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JayPrakash on 25-03-2016.
 */
public class Movie implements Parcelable {

    private String mId;
    private String mTitle;
    private String mPosterUrl;
    private String mOverview;
    private String mReleaseDate;
    private String mRating;


    public Movie(String mId,String mTitle, String mPosterUrl, String mOverview, String mReleaseDate, String mRating) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mPosterUrl = mPosterUrl;
        this.mOverview = mOverview;
        this.mReleaseDate = mReleaseDate;
        this.mRating = mRating;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmPosterUrl() {
        return mPosterUrl;
    }

    public String getmOverview() {
        return mOverview;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public String getmRating() {
        return mRating;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmPosterUrl(String mPosterUrl) {
        this.mPosterUrl = mPosterUrl;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public void setmRating(String mRating) {
        this.mRating = mRating;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String id) {
        this.mId = id;
    }

    protected Movie(Parcel in){
        mId = in.readString();
        mTitle = in.readString();
        mPosterUrl = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mRating = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mTitle);
        dest.writeString(mPosterUrl);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
        dest.writeString(mRating);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }

    };

}
