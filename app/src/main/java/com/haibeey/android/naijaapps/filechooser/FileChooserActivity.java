
package com.haibeey.android.naijaapps.filechooser;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haibeey.android.naijaapps.R;
import com.haibeey.android.naijaapps.StaticNames;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class FileChooserActivity extends AppCompatActivity implements FileChooserFragment.ChangeFragment{
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private File BaseDir;
    private File ChosenFile;
    private boolean PermissionToReadSDCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_chooser);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BaseDir=new File(StaticNames.BaseDirPath);
        ChosenFile=BaseDir;

        //show permission
        ShowPermission();
        //setupView
        setUpView();
    }

    private void ShowPermission(){
        if(PermissionToReadSDCard){

        }else{
            checkPermissionREAD_EXTERNAL_STORAGE(this);
        }
    }

    private void setUpView(){
        FileChooserFragment fileChooserFragment=
                FileChooserFragment.newInstance(BaseDir);
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_view,fileChooserFragment);
        fragmentTransaction.commit();

    }

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        PermissionToReadSDCard=true;
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onChange(File file) {
        if(file.isDirectory()){
            FileChooserFragment fileChooserFragment=
                    FileChooserFragment.newInstance(file);
            ChosenFile=file;
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_view,fileChooserFragment);
            fragmentTransaction.commit();
        }else{
            Intent intent=new Intent();
            intent.setData(Uri.fromFile(file));
            setResult(RESULT_OK,intent);
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        if(BaseDir.equals(ChosenFile)){
            super.onBackPressed();
        }else{
            onChange(new File(ChosenFile.getParent()));
        }
    }
}
