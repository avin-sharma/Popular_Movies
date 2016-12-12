package com.avinsharma.popularmovies;

/**
 * Created by Avin on 11-12-2016.
 */
public class Movie {
    private String mTitle;
    private String mSynopsis;
    private String mUserRating;
    private String mReleaseDate;
    private String mImageUrl;

    public Movie(String mTitle, String mSynopsis, String mUserRating, String mReleaseDate, String mImageUrl) {
        this.mTitle = mTitle;
        this.mSynopsis = mSynopsis;
        this.mUserRating = mUserRating;
        this.mReleaseDate = mReleaseDate;
        this.mImageUrl = mImageUrl;
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

    @Override
    public String toString() {
        return "Movie{" +
                "mTitle='" + mTitle + '\'' +
                ", mSynopsis='" + mSynopsis + '\'' +
                ", mUserRating='" + mUserRating + '\'' +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                ", mImageUrl='" + mImageUrl + '\'' +
                '}';
    }
}
