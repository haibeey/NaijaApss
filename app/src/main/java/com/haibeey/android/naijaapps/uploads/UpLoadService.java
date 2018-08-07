package com.haibeey.android.naijaapps.uploads;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.haibeey.android.naijaapps.R;
import com.haibeey.android.naijaapps.StaticNames;
import com.haibeey.android.naijaapps.mainpage.DesktopAdapter;
import com.haibeey.android.naijaapps.models.DeskTopApp;
import com.haibeey.android.naijaapps.models.MobileApp;

import java.io.File;
import java.net.URI;

public class UpLoadService extends Service {
    private NotificationManager notificationManager;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseDatabase mFirebaseDataBase;
    private Uri ImageUri;
    private NotificationCompat.Builder builder;

    public UpLoadService() {

    }


    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.upload_channel_name);
            String description = getString(R.string.upload_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("88", name, importance);
            channel.setDescription(description);
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }else {
            notificationManager = (NotificationManager)
                    this.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Intent startActivityIntent = new Intent();
        PendingIntent intent=PendingIntent.getActivity(this,
                StaticNames.PendingNotificationId,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder = new NotificationCompat.Builder(this)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(this.getResources().getString(R.string.uploading))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(intent)
                .setVibrate(new long[]{0,0})
                .setAutoCancel(true).setProgress(100,5,false);
        builder.setPriority(Notification.PRIORITY_HIGH);

        mFirebaseDataBase=FirebaseDatabase.getInstance();
        mFirebaseStorage=FirebaseStorage.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Uri appUri=intent.getData();

        ImageUri=Uri.fromFile(new File(intent.getStringExtra(StaticNames.ImageUri)));
        String TypeSelected=intent.getStringExtra(UploadActivity.TYPE);

        //uploading the file
        Upload(appUri,TypeSelected);

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void UploadImage(String id){
        mFirebaseStorage.getReference().child(StaticNames.AppImageReference).child(id)
                .putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                builder.setContentTitle("Image upLoad SuccessFull");
                notificationManager.notify(StaticNames.UploadNotificationIdImage,builder.build());
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                builder.setProgress(100, (int) progress,false);
                builder.setContentTitle("Uploading Image");
                notificationManager.notify(StaticNames.UploadNotificationId,builder.build());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                builder.setContentTitle("Image upLoad Failed");
                notificationManager.notify(StaticNames.UploadNotificationIdImage,builder.build());
            }
        });
    }
    private void UpLoadTheFile(final Uri data, final StorageReference StorageRef, final String Type, final DatabaseReference databaseReference){
        StorageRef.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                builder.setContentTitle("App upLoad Successful");
                notificationManager.notify(StaticNames.UploadNotificationId,builder.build());
                HandleSuccess(data,Type,taskSnapshot,databaseReference);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                builder.setContentTitle("Image upLoad Failed");
                notificationManager.notify(StaticNames.UploadNotificationId,builder.build());
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                builder.setProgress(100, (int) progress,false);
                builder.setContentTitle("Uploading App");
                notificationManager.notify(StaticNames.UploadNotificationId,builder.build());
            }
        });
    }

    private void Upload(Uri Data,String type){
        switch (type){
            case StaticNames.MobileAppDataBaseReference:{
                StorageReference ref=mFirebaseStorage.getReference().child(StaticNames.MobileAppDataBaseReference).child(Data.getPath());
                DatabaseReference databaseReference=mFirebaseDataBase.getReference().child(StaticNames.MobileAppDataBaseReference);
                UpLoadTheFile(Data,ref,type,databaseReference);
                break;
            }

            case StaticNames.DeskTopAppDataBaseReference:{
                StorageReference ref=mFirebaseStorage.getReference().child(StaticNames.DeskTopAppDataBaseReference).child(Data.getPath());
                DatabaseReference databaseReference=mFirebaseDataBase.getReference().child(StaticNames.DeskTopAppDataBaseReference);
                UpLoadTheFile(Data,ref,type,databaseReference);
                break;
            }
        }

    }

    private void HandleSuccess(Uri data,String Type,UploadTask.TaskSnapshot taskSnapshot,final DatabaseReference Databaseref){
        switch (Type){
            case StaticNames.MobileAppDataBaseReference:{
                MobileApp mobileApp=new MobileApp("",data.getPath(),System.currentTimeMillis(),
                        0,0,getDeveloperId(),data.getLastPathSegment(),Type);
                String uploadId = Databaseref.push().getKey();
                mobileApp.setId(uploadId);
                Databaseref.child(uploadId).setValue(mobileApp);
                UploadImage(uploadId);
                break;
            }
            case StaticNames.DeskTopAppDataBaseReference:{
                DeskTopApp deskTopApp=new DeskTopApp("",data.getPath(),System.currentTimeMillis(),
                        0,0,getDeveloperId(),data.getLastPathSegment(),Type);
                String uploadId = Databaseref.push().getKey();
                deskTopApp.setId(uploadId);
                Databaseref.child(uploadId).setValue(deskTopApp);
                UploadImage(uploadId);
                break;
            }
        }
    }

    private String getDeveloperId(){
        SharedPreferences sharedPreferences=getSharedPreferences(StaticNames.ApplicationId,MODE_PRIVATE);
        return sharedPreferences.getString(StaticNames.DeveloperId,null);
    }

}
