package com.avinsharma.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Avin on 11-12-2016.
 */
public class Movie  implements Parcelable{
    private String mTitle;
    private String mSynopsis;
    private String mUserRating;
    private String mReleaseDate;
    private String mImageUrl;
    private String mBackgroundImageUrl;

    public Movie(String mTitle, String mSynopsis, String mUserRating, String mReleaseDate, String mImageUrl, String mBackgroundImageUrl) {
        this.mTitle = mTitle;
        this.mSynopsis = mSynopsis;
        this.mUserRating = mUserRating;
        this.mReleaseDate = mReleaseDate;
        this.mImageUrl = mImageUrl;
        this.mBackgroundImageUrl = mBackgroundImageUrl;
    }

    //this
    protected Movie(Parcel in) {
        mTitle = in.readString();
        mSynopsis = in.readString();
        mUserRating = in.readString();
        mReleaseDate = in.readString();
        mImageUrl = in.readString();
        mBackgroundImageUrl = in.readString();
    }


    public String getmTitle() {
        return mTitle;
    }

    public String getmSynopsis() {
        return mSynopsis;
    }

    public String getmUserRating() {
        return mUserRating;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public String getmBackgroundImageUrl() {
        return mBackgroundImageUrl;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "mTitle='" + mTitle + '\'' +
                ", mSynopsis='" + mSynopsis + '\'' +
                ", mUserRating='" + mUserRating + '\'' +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                ", mImageUrl='" + mImageUrl + '\'' +
                ", mBackgroundImageUrl='" + mBackgroundImageUrl + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //this
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mSynopsis);
        parcel.writeString(mUserRating);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mImageUrl);
        parcel.writeString(mBackgroundImageUrl);
    }

    //and this
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
