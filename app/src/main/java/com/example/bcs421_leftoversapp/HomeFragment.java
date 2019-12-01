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
        v.findViewById(R.id.breakfastView).setOnClickListener(this);
        v.findViewById(R.id.lunchView).setOnClickListener(this);
        v.findViewById(R.id.dinnerView).setOnClickListener(this);
        v.findViewById(R.id.dessertView).setOnClickListener(this);

        btn_searchRecipe.setOnClickListener(view-> {
            startActivity(new Intent(getActivity(),RecipeSearchActivity.class));
        });


        return v;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(),RecipeSearchActivity.class);
        switch (v.getId()) {
            case R.id.breakfastView:
                intent.putExtra("category", "breakfast");
                startActivity(intent);
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
