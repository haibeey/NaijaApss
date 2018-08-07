package com.haibeey.android.naijaapps.AppDetails;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haibeey.android.naijaapps.R;
import com.haibeey.android.naijaapps.models.AnonymousReview;
import com.haibeey.android.naijaapps.models.Review;
import com.haibeey.android.naijaapps.models.app;
import com.haibeey.android.naijaapps.views.CircleTextView;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {


    private ArrayList<Object> DataItems=new ArrayList<>();
    private app App;
    public ReviewsAdapter(app App){
        this.App=App;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return DataItems.size();
    }

    public ArrayList<Object> getDataItems() {
        return DataItems;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private CircleTextView circleTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            circleTextView=itemView.findViewById(R.id.cir_text);
            textView=itemView.findViewById(R.id.the_text);
        }

        public void bind(int position){
            Object object=DataItems.get(position);
            circleTextView.setText((String.valueOf(App.getUploaded_name().charAt(0))));
            if(object instanceof Review){
                Review review=(Review) object;
                textView.setText(review.getText());
            }else {
                AnonymousReview anonymousReview=(AnonymousReview) object;
                textView.setText(anonymousReview.getText());
            }

        }
    }
}
