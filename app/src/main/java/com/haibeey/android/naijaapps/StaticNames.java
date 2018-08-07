package com.haibeey.android.naijaapps;

import android.os.Environment;

public class StaticNames {
    public  final static String MobileAppDataBaseReference="MobileApps";
    public  final static String DeskTopAppDataBaseReference="DesktopApps";
    public  final static String DeveloperDataBaseReference="Developer";
    public  final static String UserDataBaseReference="User";
    public  final static String ReviewDataBaseReference="Review";
    public  final static String AnonymousReviewDataBaseReference="AnonymousReview";
    public final static  String AppImageReference="AppImageReference";
    public final static  String DownloadIntentName="Download";

    public final  static String ApplicationId="com.haibeey.android.naijaapps";
    public final  static String DeveloperId="Developer_ID";
    public final  static String UserId="User_ID";
    public  final static String BaseDirPath= Environment.getExternalStorageDirectory().getAbsolutePath();
    public  final static String ImageUri= "ImageUri";
    public  final static int PendingNotificationId= 89;
    public  final static int UploadNotificationId= 1;
    public  final static int UploadNotificationIdImage= 2;
    public final static int PendingDownloadTaskId=96;
    public final static String Anonymous="anonymous";


    //names to pass data to another activity
    public final  static  String id_="id";
    public final  static  String id_dev="id_dev";
    public final  static  String views_="views";
    public final  static  String time_upload_="time";
    public final static  String name_="name";
    public final static String name_uploaded_="name_uploaded";
    public final static String no_downloaded_="no_downloaded";
    public final static String type_="type";
}
