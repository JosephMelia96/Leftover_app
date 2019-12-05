package com.example.bcs421_leftoversapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.bcs421_leftoversapp.DataBase.UsersContract;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class AccountSettingsFragment extends Fragment {

    GoogleSignInClient mGoogleSignInClient;
    UsersContract mUsersContract;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_account_settings,container,false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());

        this.mUsersContract = new UsersContract(getContext()); //initialize UsersContract
        //display user info
        if (acct != null) {
            String email = acct.getEmail();
            String name = acct.getDisplayName();

            TextView mtv_name = v.findViewById(R.id.mtv_name);
            TextView mtv_email = v.findViewById(R.id.mtv_email);
            ImageView pic = v.findViewById(R.id.acct_image);

            mtv_name.setText(name);
            mtv_email.setText(email);
            if (acct.getPhotoUrl() == null) {
                pic.setVisibility(View.GONE);
            } else {
                Glide.with(this).load(String.valueOf(acct.getPhotoUrl())).into(pic);
            }

        }


        return v;

    }
}
