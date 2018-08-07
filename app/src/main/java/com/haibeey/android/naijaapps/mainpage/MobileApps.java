package com.haibeey.android.naijaapps.mainpage;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.haibeey.android.naijaapps.R;
import com.haibeey.android.naijaapps.StaticNames;
import com.haibeey.android.naijaapps.models.MobileApp;

import java.io.InvalidClassException;


public class MobileApps extends Fragment {

    protected RecyclerView recyclerView;
    protected MobileAdapter mobileAdapter;
    private DatabaseReference mDatabaseReference;

    public MobileApps() {
        // Required empty public constructor
    }

    public static MobileApps newInstance() {
        MobileApps fragment = new MobileApps();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void initialize(){
        mDatabaseReference=FirebaseDatabase.getInstance().getReference().child(StaticNames.MobileAppDataBaseReference);
    }

    private  synchronized  void loadData(){
        if(mDatabaseReference!=null){
            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        MobileApp app = postSnapshot.getValue(MobileApp.class);
                        if(app.getType().equals(StaticNames.MobileAppDataBaseReference)){
                            if(mobileAdapter!=null){
                                mobileAdapter.getDataItems().add(app);
                            }
                        }

                    }
                    mobileAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Snackbar.make(recyclerView,"Something is Not Right",Snackbar.LENGTH_SHORT);
                }
            });
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_mobile_apps, container, false);
        recyclerView=view.findViewById(R.id.mobile_app_recyclerview);
        mobileAdapter=new MobileAdapter(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView.setAdapter(mobileAdapter);
        loadData();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initialize();
    }
}
