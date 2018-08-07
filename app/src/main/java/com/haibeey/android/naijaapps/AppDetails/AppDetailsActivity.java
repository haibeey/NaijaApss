package com.haibeey.android.naijaapps.AppDetails;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.haibeey.android.naijaapps.R;
import com.haibeey.android.naijaapps.StaticNames;
import com.haibeey.android.naijaapps.models.AnonymousReview;
import com.haibeey.android.naijaapps.models.DeskTopApp;
import com.haibeey.android.naijaapps.models.MobileApp;
import com.haibeey.android.naijaapps.models.Review;
import com.haibeey.android.naijaapps.models.app;
import com.haibeey.android.naijaapps.views.CircleTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AppDetailsActivity extends AppCompatActivity {

    private DeskTopApp deskTopApp;
    private MobileApp mobileApp;
    private FirebaseDatabase mFirebaseDatabase;
    protected static DatabaseReference AnonymousReviewDatabaseReference;
    protected static DatabaseReference ReviewDatabaseReference;
    private DatabaseReference AppDataBaseReference;
    private StorageReference storageReference;
    private ReviewsAdapter reviewsAdapter;
    private static SharedPreferences sharedPreferences;
    public static ArrayList<Long> DownloadReference=new ArrayList<>();
    private static DownloadReceiver downloadReceiver=new DownloadReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Firebase inits
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        AppDataBaseReference=mFirebaseDatabase.getReference();
        storageReference= FirebaseStorage.getInstance().getReference();

        final Intent intent=getIntent();
        final app App=new app(intent.getStringExtra(StaticNames.id_),intent.getStringExtra(StaticNames.name_),
                intent.getLongExtra(StaticNames.time_upload_,0),intent.getLongExtra(StaticNames.no_downloaded_,0),
                intent.getLongExtra(StaticNames.views_,0),intent.getStringExtra(StaticNames.id_dev),intent.getStringExtra(StaticNames.name_uploaded_),
                intent.getStringExtra(StaticNames.type_));


        if(App.getType().equals(StaticNames.MobileAppDataBaseReference)){
            mobileApp=new MobileApp(App);
        }else{
            deskTopApp=new DeskTopApp((app) intent.getParcelableExtra(StaticNames.DeskTopAppDataBaseReference));
        }
        AnonymousReviewDatabaseReference=mFirebaseDatabase.getReference().
                child(StaticNames.AnonymousReviewDataBaseReference).child(App.getId());
        ReviewDatabaseReference=mFirebaseDatabase.getReference()
                .child(StaticNames.ReviewDataBaseReference).child(App.getId());

        FloatingActionButton floatingActionButton=findViewById(R.id.fab_comment);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReviewDialog reviewDialog=ReviewDialog.newInstance(App);
                reviewDialog.show(getFragmentManager(),"reviews");
            }
        });

        //Views
        TextView textView=findViewById(R.id.app_name_text_view);
        textView.setText(App.getUploaded_name());
        CircleTextView circleTextView=findViewById(R.id.no_of_view);
        circleTextView.setText(String.valueOf(App.getNo_of_download()));
        final ImageView imageView=findViewById(R.id.app_image);
        storageReference.child(StaticNames.AppImageReference).child(
                App.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().into(imageView);
            }
        });

        final Button button=findViewById(R.id.app_download_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AppDetailsActivity.this,"Download Starting...",Toast.LENGTH_SHORT).show();
                storageReference.child(App.getType()).child(App.getName()).
                        getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        button.setEnabled(true);
                        DownloadManager downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setTitle("Downloading "+App.getName());
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,App.getUploaded_name());
                        DownloadReference.add(downloadManager.enqueue(request));
                        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                        registerReceiver(downloadReceiver, filter);
                        switch (App.getType()){
                            case StaticNames.MobileAppDataBaseReference:
                                mobileApp.setNo_of_download(mobileApp.getNo_of_download()+1);
                                AppDataBaseReference.child(App.getType()).child(App.getId()).setValue(mobileApp);
                                break;
                            case StaticNames.DeskTopAppDataBaseReference:
                                deskTopApp.setNo_of_download(deskTopApp.getNo_of_download()+1);
                                AppDataBaseReference.child(App.getType()).child(App.getId()).setValue(deskTopApp);
                                break;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AppDetailsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        final RecyclerView recyclerView=findViewById(R.id.reviews);
        reviewsAdapter=new ReviewsAdapter(App);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(reviewsAdapter);
        AnonymousReviewDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Review review=dataSnapshot1.getValue(Review.class);
                    reviewsAdapter.getDataItems().add(review);
                }

                reviewsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ReviewDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    AnonymousReview review=dataSnapshot1.getValue(AnonymousReview.class);
                    reviewsAdapter.getDataItems().add(review);
                }

                reviewsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        sharedPreferences=getSharedPreferences(StaticNames.ApplicationId,MODE_PRIVATE);
    }

    public static class ReviewDialog extends DialogFragment{
        private static app App;
        public static ReviewDialog newInstance(app App) {
            Bundle args = new Bundle();
            ReviewDialog.App=App;
            ReviewDialog fragment = new ReviewDialog();
            fragment.setArguments(args);
            return fragment;
        }
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View view=inflater.inflate(R.layout.reviews_add_layout,container);
            final MultiAutoCompleteTextView multiAutoCompleteTextView=view.findViewById(R.id.reviews_text);
            Button button=view.findViewById(R.id.review_button);
            assert App!=null;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String user=sharedPreferences.getString(StaticNames.UserId,"notfound");

                    if(user.equals("notfound")){
                        AnonymousReview review=new AnonymousReview("",StaticNames.Anonymous,"",0,
                                multiAutoCompleteTextView.getText().toString());
                        String uploadId =  AnonymousReviewDatabaseReference.push().getKey();
                        review.setId(uploadId);
                        AnonymousReviewDatabaseReference.child(uploadId).setValue(review);
                    }else{
                        Review review=new Review("",user,App!=null?App.getDeveloper_id():"","",0,
                                multiAutoCompleteTextView.getText().toString());
                        String uploadId =  ReviewDatabaseReference.push().getKey();
                        review.setId(uploadId);
                        AnonymousReviewDatabaseReference.child(uploadId).setValue(review);
                    }

                    ReviewDialog.this.dismiss();
                }
            });
            return  view;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return super.onCreateDialog(savedInstanceState);
        }
    }

    public static class DownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){

                Toast toast = Toast.makeText(context,
                        "App Download complete", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            }
            if(DownloadReference.contains(referenceId)){

                DownloadReference.remove(referenceId);
            }
        }

    }
}
