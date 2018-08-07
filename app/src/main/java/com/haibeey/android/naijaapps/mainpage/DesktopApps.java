package com.haibeey.android.naijaapps.mainpage;

import android.content.Context;
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
import com.haibeey.android.naijaapps.models.DeskTopApp;
import com.haibeey.android.naijaapps.models.MobileApp;

public class DesktopApps extends Fragment {

    protected RecyclerView recyclerView;
    protected DesktopAdapter desktopAdapter;
    private FirebaseStorage mFireBaseStorage;
    private DatabaseReference mDatabaseReference;

    public DesktopApps() {
        // Required empty public constructor
    }

    public static DesktopApps newInstance() {
        DesktopApps fragment = new DesktopApps();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    private void initialize(){
        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child(StaticNames.MobileAppDataBaseReference);
    }

    private  synchronized  void loadData(){
        if(mDatabaseReference!=null){
            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        DeskTopApp app = postSnapshot.getValue(DeskTopApp.class);
                        if(app.getType().equals(StaticNames.DeskTopAppDataBaseReference)){
                            if(desktopAdapter!=null){
                                desktopAdapter.getDataItems().add(app);
                            }
                        }

                    }
                    desktopAdapter.notifyDataSetChanged();
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
        View view=inflater.inflate(R.layout.fragment_destop_apps, container, false);
        recyclerView=view.findViewById(R.id.desktop_app_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        desktopAdapter=new DesktopAdapter(getContext());
        recyclerView.setAdapter(desktopAdapter);
        loadData();
        return  view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initialize();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
