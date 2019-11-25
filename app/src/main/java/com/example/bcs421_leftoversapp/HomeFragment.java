package com.example.bcs421_leftoversapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HomeFragment extends Fragment {

    EditText searchBar;
    TextView apiOutput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home,container,false);

        searchBar = v.findViewById(R.id.search);
        apiOutput = v.findViewById(R.id.apiText);

        final OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url("https://recipe-puppy.p.rapidapi.com/?p=1&i=onions%252Cgarlic&q=omelet")
                .get()
                .addHeader("x-rapidapi-host", "recipe-puppy.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "d80f17b0admsh0a6a81c192d0d1dp19d544jsn192a4c9b69db")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    response = client.newCall(request).execute();
                    final String myResponse = response.body().string();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            apiOutput.setText(myResponse);
                        }
                    });
                }
            }
        });



        return v;

    }
}
