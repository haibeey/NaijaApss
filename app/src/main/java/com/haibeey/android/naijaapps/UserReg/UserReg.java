package com.haibeey.android.naijaapps.UserReg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.haibeey.android.naijaapps.R;
import com.haibeey.android.naijaapps.StaticNames;
import com.haibeey.android.naijaapps.models.developer;

import java.util.Arrays;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class UserReg extends Fragment {
    private View view;
    private int RC_SIGN=34;
    private String email;
    public UserReg() {
        // Required empty public constructor
    }

    public static UserReg newInstance() {
        return new UserReg();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_user_reg, container, false);

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.EmailBuilder().build()))
                        .build(),
                RC_SIGN);

        Button UploadButton=view.findViewById(R.id.user_upload_button);
        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckUploadEditTexts()){
                    UploadDetails();
                }else{
                    Snackbar.make(view,"You need to fill the right details",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        return  view;
    }

    private boolean CheckUploadEditTexts(){
        AutoCompleteTextView DeveloperFullName=view.findViewById(R.id.user_full_name);
        AutoCompleteTextView password=view.findViewById(R.id.user_password);

        if (TextUtils.isEmpty(DeveloperFullName.getText())){
            DeveloperFullName.setError("name field "+getString(R.string.cannot_be_empty));
            return false;
        }

        if (TextUtils.isEmpty(password.getText())){
            password.setError("password "+getString(R.string.cannot_be_empty));
            return false;
        }


        if(DeveloperFullName.getText().toString().split("\\w").length<=1){
            DeveloperFullName.setError("You need to add your surname");
            return false;
        }

        if(password.getText().toString().length()<7){
            password.setError("Short Password");
            return false;
        }

        return true;
    }

    private void UploadDetails(){
        AutoCompleteTextView DeveloperFullName=view.findViewById(R.id.user_full_name);
        AutoCompleteTextView password=view.findViewById(R.id.user_password);

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference(StaticNames.DeveloperDataBaseReference);
        developer D=new developer("",email,
                DeveloperFullName.getText().toString(),password.getText().toString());
        final String uploadId = databaseReference.push().getKey();
        D.setId(uploadId);
        assert uploadId != null;
        databaseReference.child(uploadId).setValue(D).addOnSuccessListener(new OnSuccessListener<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(Void aVoid) {

                SharedPreferences sharedPreferences=getActivity().getSharedPreferences(StaticNames.ApplicationId,MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(StaticNames.UserId,uploadId);
                editor.apply();
               Objects.requireNonNull(getActivity()).finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(),e.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN){
            if(resultCode==RESULT_OK){
                Toast.makeText(view.getContext(),"Reg Completed",Toast.LENGTH_SHORT).show();
                IdpResponse idpResponse = IdpResponse.fromResultIntent(data);
                assert idpResponse != null;
                email=idpResponse.getEmail();

            }else{
                Toast.makeText(getActivity(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
