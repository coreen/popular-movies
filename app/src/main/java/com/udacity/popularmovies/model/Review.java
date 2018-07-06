package com.udacity.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public final class Review implements Parcelable {
    private String content;
    private String author;

    public Review(String content, String author) {
        this.content = content;
        this.author = author;
    }

    public Review(Parcel source) {
        content = source.readString();
        author = source.readString();
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(author);
    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
