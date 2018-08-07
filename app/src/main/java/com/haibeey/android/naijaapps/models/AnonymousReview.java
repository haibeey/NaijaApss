package com.haibeey.android.naijaapps.models;

import android.os.Parcel;
import android.os.Parcelable;

public class AnonymousReview implements Parcelable {
    private String id;
    private String name;
    private String replying_to;
    private long likes;
    private  String text;
    @Override
    public int describeContents() {
        return 0;
    }
    public AnonymousReview(){

    }
    public AnonymousReview(String id,String name,String replying_to,long likes,String  text){
        this.id=id;
        this.name=name;
        this.replying_to=replying_to;
        this.likes=likes;
        this.text=text;
    }
    public AnonymousReview(Parcel parcel){
        id=parcel.readString();
        name=parcel.readString();
        replying_to=parcel.readString();
        likes=parcel.readLong();
        text=parcel.readString();

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
    public  static Creator<AnonymousReview> CREATOR=new Creator<AnonymousReview>() {
        @Override
        public AnonymousReview createFromParcel(Parcel parcel) {
            return new AnonymousReview(parcel);
        }

        @Override
        public AnonymousReview[] newArray(int i) {
            return new AnonymousReview[0];
        }
    };

    public String  getId() {
        return id;
    }

    public long getLikes() {
        return likes;
    }

    public String getReplying_to() {
        return replying_to;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReplying_to(String replying_to) {
        this.replying_to = replying_to;
    }

    public void setText(String text) {
        this.text = text;
    }

}
