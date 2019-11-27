package com.example.bcs421_leftoversapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import com.example.bcs421_leftoversapp.R;
import com.example.bcs421_leftoversapp.models.RecipePreview;

public class RecipeSearchResultAdapter extends ArrayAdapter<RecipePreview> {

    public RecipeSearchResultAdapter(@NonNull Context context) {
        super(context, R.layout.search_result);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_result, parent, false);
        }
        TextView itemTitle = convertView.findViewById(R.id.searchResultTitle);
        ImageView itemThumb = convertView.findViewById(R.id.searchThumb);
        RecipePreview recipe = getItem(position);
        itemTitle.setText(recipe.getTitle());
        Glide.with(convertView).load(String.valueOf(recipe.getThumbnail())).into(itemThumb);
        return convertView;
    }
}
