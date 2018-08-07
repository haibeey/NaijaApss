package com.haibeey.android.naijaapps.uploads;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.MessagePattern;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.haibeey.android.naijaapps.DeveloperPage.DeveloperRegistration;
import com.haibeey.android.naijaapps.R;
import com.haibeey.android.naijaapps.StaticNames;
import com.haibeey.android.naijaapps.filechooser.FileChooserActivity;
import com.haibeey.android.naijaapps.views.CustomConstraintLayout;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.security.AccessController.getContext;

public class UploadActivity extends AppCompatActivity {

    private Uri AppUri;
    protected static String TYPE = "TYPE";
    private String SelectedType = StaticNames.MobileAppDataBaseReference;//defaults to apk
    private RelativeLayout mCustomConstraintLayout;
    private Button UploadButton;
    private EditText AppNameInput;
    private CircleImageView circleImageView;
    private ImageButton SelectButton;
    private  String FilePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mCustomConstraintLayout = findViewById(R.id.upload_parent);
        UploadButton = findViewById(R.id.upload_button);
        circleImageView = findViewById(R.id.app_image);
        AppNameInput = findViewById(R.id.app_name_text_input);
        SelectButton = findViewById(R.id.select_app);


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromGallery();
            }
        });

        Spinner spinner = findViewById(R.id.select_type);
        setSpinner(spinner, R.array.upload_spinner);

        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getDeveloperId() != null) {
                    if (FilePath != null) {
                        if (AppUri != null){
                            UploadButton.setEnabled(true);
                            startUploadService();
                        }
                        else
                            Snackbar.make(mCustomConstraintLayout, R.string.null_app_path, Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(mCustomConstraintLayout, R.string.null_image_path, Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(mCustomConstraintLayout, R.string.register_warning, Snackbar.LENGTH_SHORT).show();
                    startActivity(new Intent(UploadActivity.this, DeveloperRegistration.class));
                }
            }
        });

        SelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getApp();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void startUploadService() {
        Intent intent = new Intent(this, UpLoadService.class);
        intent.setData(AppUri);
        intent.putExtra(StaticNames.ImageUri, FilePath);
        intent.putExtra(TYPE, SelectedType);
        Toast.makeText(this,"Uploading starting...",Toast.LENGTH_SHORT).show();
        startService(intent);
        finish();
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private String getDeveloperId() {
        SharedPreferences sharedPreferences = getSharedPreferences(StaticNames.ApplicationId, MODE_PRIVATE);
        return sharedPreferences.getString(StaticNames.DeveloperId, null);
    }

    private void setSpinner(Spinner spinner, int stringResource) {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, stringResource, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        SelectedType = StaticNames.MobileAppDataBaseReference;
                        break;
                    case 1:
                        SelectedType = StaticNames.DeskTopAppDataBaseReference;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getImageFromGallery() {
        Intent I = new Intent(Intent.CATEGORY_APP_GALLERY);
        I.setType("image/*");
        I.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(I, 1);
    }

    private void getApp() {
        Intent I = new Intent(this, FileChooserActivity.class);
        startActivityForResult(I, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data==null){
            Snackbar.make(mCustomConstraintLayout,"No Data Selected",Snackbar.LENGTH_SHORT).show();
            return;
        }
        switch (requestCode) {
            case 1:
                Uri ImageUri = data.getData();

                String[] location = {MediaStore.Images.Media.DATA};
                assert ImageUri != null;
                Cursor cursor = getContentResolver().query(ImageUri, location, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                FilePath = cursor.getString(cursor.getColumnIndex(location[0]));
                Bitmap bitmap = BitmapFactory.decodeFile(FilePath);
                circleImageView.setImageBitmap(bitmap);
                cursor.close();
                break;
            case 2:
                AppUri = data.getData();
                assert AppUri != null;
                AppNameInput.setText(AppUri.getLastPathSegment());
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
