package com.haibeey.android.naijaapps.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class developer implements Parcelable {
    private String id;
    private String email;
    private String full_name;
    private String password;
    private ArrayList<String> key_to_apps;//apps made by the developer

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(email);
        parcel.writeString(full_name);
        parcel.writeArray(key_to_apps.toArray());
    }


    public  developer(String id,String email,String full_name,String password){
        this.id=id;
        this.email=email;
        this.full_name=full_name;
        this.password=password;
        this.key_to_apps=new ArrayList<>();
    }

    public developer(Parcel in) {
        id = in.readString();
        email = in.readString();
        full_name = in.readString();
        password=in.readString();
        Object[] list=in.readArray(String.class.getClassLoader());
        key_to_apps=new ArrayList<>(list.length);
        for(Object object:list)
            key_to_apps.add((String) object);
    }

    public static final Creator<developer> CREATOR = new Creator<developer>() {
        @Override
        public developer createFromParcel(Parcel in) {
            return new developer(in);
        }

        @Override
        public developer[] newArray(int size) {
            return new developer[size];
        }
    };

    public developer(){

    }
    public String getId() {
        return id;
    }

    public ArrayList<String> getKey_to_apps() {
        return key_to_apps;
    }

    public String getEmail() {
        return email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public void setKey_to_apps(ArrayList<String> key_to_apps) {
        this.key_to_apps = key_to_apps;
    }

}
