package com.haibeey.android.naijaapps.models;

import android.os.Parcel;

public class MobileApp extends  app {
    public MobileApp(String id, String name, long timeOfUpload, long no_of_download, long views, String developer_id, String uploaded_name, String type) {
        super(id, name, timeOfUpload, no_of_download, views, developer_id, uploaded_name, type);
    }

    public MobileApp(app App){
        super(App.getId(),App.getName(),App.getTimeOfUpload(),App.getNo_of_download(),
                App.getViews(),App.getDeveloper_id(),App.getUploaded_name(),App.getType());

    }

    public MobileApp(){

    }

    public MobileApp(Parcel in) {
        super(in);
    }

    @Override
    public void setDeveloper_id(String developer_id) {
        super.setDeveloper_id(developer_id);
    }

    @Override
    public void setId(String id) {
        super.setId(id);
    }


}
