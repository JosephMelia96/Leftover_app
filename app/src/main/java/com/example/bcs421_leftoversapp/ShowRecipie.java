package com.example.bcs421_leftoversapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ShowRecipie extends AppCompatActivity {

    TextView ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipie);
        ingredients=findViewById(R.id.ingredients);
        ingredients.setText(getIntent().getStringExtra("ingr"));
    }
}
