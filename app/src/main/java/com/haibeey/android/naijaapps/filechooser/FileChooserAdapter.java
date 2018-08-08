package com.haibeey.android.naijaapps.filechooser;

import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haibeey.android.naijaapps.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FileChooserAdapter extends RecyclerView.Adapter<FileChooserAdapter.ViewHolder>{

    private ClickListener clickListener;
    private File[] files;
    protected FileChooserAdapter(File file){
        files=file.listFiles();
        
    }


    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_file_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return files.length;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.file_name_view);
            imageView=itemView.findViewById(R.id.folder_file_view);
        }
        public void bind(final int position){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener!=null){
                        clickListener.onClickListener(files[position]);
                    }
                }
            });

            if(files[position].isFile()){
                imageView.setImageResource(R.drawable.ic_insert_drive_file_black_24dp);
            }
            textView.setText(getName(files[position].getAbsolutePath()));
        }

        private String getName(String string){
            String[] strings=string.split("/");
            return strings[strings.length-1];
        }
    }


    interface ClickListener{
        void onClickListener(File file);
    }
}

