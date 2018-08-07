package com.haibeey.android.naijaapps.models;

import android.os.Parcel;
import android.os.Parcelable;

public class app {

    private String id;
    private String name;
    private long timeOfUpload;
    private long views;
    private String developer_id;
    private long no_of_download;
    private String type;
    private String uploaded_name;

    public app(){

    }
    public  app(String id,String name,long timeOfUpload,long no_of_download,long views,
                String developer_id,String uploaded_name,String type){
        this.id=id;
        this.name=name;
        this.timeOfUpload=timeOfUpload;
        this.no_of_download=no_of_download;
        this.views=views;
        this.developer_id=developer_id;
        this.uploaded_name=uploaded_name;
        this.type=type;

    }

    public app(Parcel in) {
        this.id=in.readString();
        this.name=in.readString();
        this.timeOfUpload=in.readLong();
        this.no_of_download=in.readLong();
        this.views=in.readLong();
        this.developer_id=in.readString();
        this.uploaded_name=in.readString();
        this.type=in.readString();
    }


    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDeveloper_id() {
        return developer_id;
    }

    public long getNo_of_download() {
        return no_of_download;
    }

    public long getTimeOfUpload() {
        return timeOfUpload;
    }

    public long getViews() {
        return views;
    }

    public String getType() {
        return type;
    }

    public String getUploaded_name() {
        return uploaded_name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDeveloper_id(String developer_id) {
        this.developer_id = developer_id;
    }

    public void setTimeOfUpload(long timeOfUpload) {
        this.timeOfUpload = timeOfUpload;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setNo_of_download(long no_of_download) {
        this.no_of_download = no_of_download;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public void setUploaded_name(String uploaded_name) {
        this.uploaded_name = uploaded_name;
    }

}
