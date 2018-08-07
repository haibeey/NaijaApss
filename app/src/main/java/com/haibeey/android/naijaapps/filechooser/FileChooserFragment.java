package com.haibeey.android.naijaapps.filechooser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haibeey.android.naijaapps.R;

import java.io.File;


public class FileChooserFragment extends Fragment implements FileChooserAdapter.ClickListener{

    private ChangeFragment changeFragment;
    private FileChooserAdapter fileChooserAdapter;

    public FileChooserFragment() {
        // Required empty public constructor
    }

    public static FileChooserFragment newInstance(File file) {
        FileChooserFragment fragment = new FileChooserFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("file",file);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setFileChooserAdapter(FileChooserAdapter fileChooserAdapter) {
        this.fileChooserAdapter = fileChooserAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_file_chooser, container, false);
        FileChooserAdapter fileChooserAdapter=new FileChooserAdapter((File) getArguments().getSerializable("file"));
        fileChooserAdapter.setClickListener(this);
        RecyclerView recyclerView=view.findViewById(R.id.file_chooser_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(fileChooserAdapter);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        changeFragment= (ChangeFragment) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        changeFragment=null;
    }

    @Override
    public void onClickListener(File file) {
        if(changeFragment!=null){
            changeFragment.onChange(file);
        }
    }

    interface ChangeFragment{
        void onChange(File file);
    }
}
