package com.haibeey.android.naijaapps.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {
    private String id;
    private String user_id;
    private String developer_id;
    private String replying_to;
    private long likes;
    private  String text;

    public Review() {

    }    @Override
    public int describeContents() {
        return 0;
    }
    public Review(String id,String user_id,String developer_id,String replying_to,long likes,String  text){
        this.id=id;
        this.user_id=user_id;
        this.developer_id=developer_id;
        this.replying_to=replying_to;
        this.likes=likes;
        this.text=text;
    }
    public Review(Parcel parcel){
        id=parcel.readString();
        user_id=parcel.readString();
        developer_id=parcel.readString();
        replying_to=parcel.readString();
        likes=parcel.readLong();
        text=parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
    public  static Creator<Review> CREATOR=new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int i) {
            return new Review[0];
        }
    };

    public void setId(String id) {
        this.id = id;
    }

    public void setDeveloper_id(String developer_id) {
        this.developer_id = developer_id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setReplying_to(String replying_to) {
        this.replying_to = replying_to;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public String getDeveloper_id() {
        return developer_id;
    }

    public String getText() {
        return text;
    }

    public String getReplying_to() {
        return replying_to;
    }

    public long getLikes() {
        return likes;
    }

    public String getUser_id() {
        return user_id;
    }


}
