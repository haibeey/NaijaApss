package com.haibeey.android.naijaapps.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StyleableRes;

public class User implements Parcelable {
    private Long id;
    private String full_name;
    private String screen_name;
    private String email;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(full_name);
        parcel.writeString(screen_name);
        parcel.writeString(email);

    }

    public User(){

    }

    public User(long id, String full_name, String screen_name , String email){
        this.id=id;
        this.full_name=full_name;
        this.screen_name=screen_name;
        this.email=email;
    }
    public User(Parcel parcel){

        id=parcel.readLong();
        full_name=parcel.readString();
        email=parcel.readString();
        screen_name=parcel.readString();
    }
    public  static Creator<User> CREATOR=new Creator<User>() {
        @Override
        public User createFromParcel(Parcel parcel) {
            return new User(parcel);
        }

        @Override
        public User[] newArray(int i) {
            return new User[0];
        }
    };

    public void setId(Long id) {
        this.id = id;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getEmail() {
        return email;
    }

    public String getFull_name() {
        return full_name;
    }

    public Long getId() {
        return id;
    }

    public String getScreen_name() {
        return screen_name;
    }

}
