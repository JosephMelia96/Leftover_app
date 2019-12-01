package com.example.bcs421_leftoversapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;



public class HomeFragment extends Fragment implements View.OnClickListener {

    Button btn_searchRecipe;
    ImageView breakfast, lunch, dinner, dessert;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        btn_searchRecipe = v.findViewById(R.id.btn_searchRecipes);

        //set on click listeners to all image view
        v.findViewById(R.id.breakfastView).setOnClickListener(this);
        v.findViewById(R.id.lunchView).setOnClickListener(this);
        v.findViewById(R.id.dinnerView).setOnClickListener(this);
        v.findViewById(R.id.dessertView).setOnClickListener(this);

        btn_searchRecipe.setOnClickListener(view-> {
            startActivity(new Intent(getActivity(),RecipeSearchActivity.class));
        });


        return v;
    }

    //process onClick event
    @Override
    public void onClick(View v) {
        //create intent object to pass clicked image value to next activity
        Intent intent = new Intent(getActivity(),RecipeSearchActivity.class);

        //switch will get id of clicked image and run on corresponding case
        switch (v.getId()) {
            case R.id.breakfastView:
                intent.putExtra("category", "breakfast"); //add image value to intent
                startActivity(intent); //start new activity
                break;
            case R.id.lunchView:
                intent.putExtra("category", "lunch");
                startActivity(intent);
                break;
            case R.id.dinnerView:
                intent.putExtra("category", "dinner");
                startActivity(intent);
                break;
            case R.id.dessertView:
                intent.putExtra("category", "dessert");
                startActivity(intent);
                break;
        }
    }
}
