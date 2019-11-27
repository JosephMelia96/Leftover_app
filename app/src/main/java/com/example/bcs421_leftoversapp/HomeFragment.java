package com.example.bcs421_leftoversapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;



public class HomeFragment extends Fragment {

    Button btn_searchRecipe;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        btn_searchRecipe = v.findViewById(R.id.btn_searchRecipes);

        btn_searchRecipe.setOnClickListener(view-> {
            startActivity(new Intent(getActivity(),RecipeSearchActivity.class));
        });


        return v;
    }
}
