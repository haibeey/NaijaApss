package com.haibeey.android.naijaapps.mainpage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.haibeey.android.naijaapps.AppDetails.AppDetailsActivity;
import com.haibeey.android.naijaapps.R;
import com.haibeey.android.naijaapps.StaticNames;
import com.haibeey.android.naijaapps.models.DeskTopApp;
import com.haibeey.android.naijaapps.models.MobileApp;
import com.haibeey.android.naijaapps.models.app;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MobileAdapter extends RecyclerView.Adapter<MobileAdapter.ViewHolder> {
    private ArrayList<MobileApp> DataItems=new ArrayList<>();
    private Context context;
    private StorageReference storageReference;

    protected  MobileAdapter(Context context){
        this.context=context;
        storageReference=FirebaseStorage.getInstance().getReference().child(StaticNames.AppImageReference);

    }
    @NonNull
    @Override
    public MobileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.apps_item_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MobileAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    public void setDataItems(ArrayList<MobileApp> dataItems) {
        DataItems = dataItems;
        notifyDataSetChanged();
    }

    public ArrayList<MobileApp> getDataItems() {
        return DataItems;
    }

    @Override
    public int getItemCount() {
        return DataItems.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{
        private View itemView;
        private ImageView imageView;
        private TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            this.textView=itemView.findViewById(R.id.show_text);
            imageView=itemView.findViewById(R.id.app_image_view);
        }

        private void bind(final int position){
            storageReference.child(
                    DataItems.get(position).getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).fit().into(imageView);
                }
            });
            textView.setText(DataItems.get(position).getUploaded_name());
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MobileApp mobileApp=DataItems.get(position);
                    Intent intent=new Intent(context, AppDetailsActivity.class);
                    intent.putExtra(StaticNames.id_,mobileApp.getId());
                    intent.putExtra(StaticNames.id_dev,mobileApp.getDeveloper_id());
                    intent.putExtra(StaticNames.views_,mobileApp.getViews());
                    intent.putExtra(StaticNames.no_downloaded_,mobileApp.getNo_of_download());
                    intent.putExtra(StaticNames.name_,mobileApp.getName());
                    intent.putExtra(StaticNames.name_uploaded_,mobileApp.getUploaded_name());
                    intent.putExtra(StaticNames.time_upload_,mobileApp.getTimeOfUpload());
                    intent.putExtra(StaticNames.type_,mobileApp.getType());
                    context.startActivity(intent);
                }
            });
        }
    }
}
