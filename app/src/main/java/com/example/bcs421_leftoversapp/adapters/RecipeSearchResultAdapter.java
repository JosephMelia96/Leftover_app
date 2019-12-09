package com.example.bcs421_leftoversapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.bcs421_leftoversapp.R;
import com.example.bcs421_leftoversapp.ShowRecipe;
import com.example.bcs421_leftoversapp.models.Recipe;
import com.example.bcs421_leftoversapp.models.RecipePreview;

import java.util.List;

public class RecipeSearchResultAdapter extends RecyclerView.Adapter<RecipeSearchResultAdapter.ViewHolder> {
    private List<RecipePreview> recipes;
    private Context context;

    public RecipeSearchResultAdapter(Context context, List<RecipePreview> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    public RecipeSearchResultAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.search_result, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final RecipePreview recipe = recipes.get(position);

        holder.recipeThumbView.setImageURI(Uri.parse(recipe.getThumbnail()));
        holder.recipeTitleView.setText(recipe.getTitle());
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ex = new Intent(context, ShowRecipe.class);
                ex.putExtra("ingr", recipe.getIngredients());
                ex.putExtra("img", recipe.getThumbnail());
                ex.putExtra("title", recipe.getTitle());
                ex.putExtra("href", recipe.getHref());
                context.startActivity(ex);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.recipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView recipeThumbView;
        private TextView recipeTitleView;
        private View parentView;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.parentView = view;
            this.recipeThumbView = (ImageView) view
                    .findViewById(R.id.searchThumb);
            this.recipeTitleView = (TextView)view
                    .findViewById(R.id.searchResultTitle);
        }
    }
}
