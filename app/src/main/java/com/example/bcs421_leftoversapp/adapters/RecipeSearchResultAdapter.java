package com.example.bcs421_leftoversapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bcs421_leftoversapp.R;
import com.example.bcs421_leftoversapp.models.RecipePreview;

import java.util.ArrayList;

//adapter class to use in RecipeSearchFragment
public class RecipeSearchResultAdapter extends RecyclerView.Adapter<RecipeSearchResultAdapter.RecipeViewHolder> {

    private ArrayList<RecipePreview> mRecipeList;
    private Context mContext;
    private OnRecipeListener onRecipeListener;

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // pass layout to recipe holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result, parent, false);
        return new RecipeViewHolder(v, onRecipeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        RecipePreview recipe = mRecipeList.get(position); //get item at position
        Glide.with(mContext).load(recipe.getThumbnail()).into(holder.mImageView); //set Thumbnail
        holder.mTextView.setText(recipe.getTitle()); //set Title
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    // will transfer data into local variable so we can extract all queried recipes
    public RecipeSearchResultAdapter(Context context, ArrayList<RecipePreview> recipeList, OnRecipeListener onRecipeListener) {
        this.mRecipeList = recipeList;
        this.mContext = context;
        this.onRecipeListener = onRecipeListener;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // initialize views
        public ImageView mImageView;
        public TextView mTextView;
        OnRecipeListener onRecipeListener;

        public RecipeViewHolder(@NonNull View itemView, OnRecipeListener onRecipeListener) {
            super(itemView);
            //add reference to view so adapter can use them
            mImageView = itemView.findViewById(R.id.searchThumb);
            mTextView = itemView.findViewById(R.id.searchResultTitle);
            this.onRecipeListener = onRecipeListener;

            itemView.setOnClickListener(this);
        }

        //when recipe is clicked, get that position in the adapter
        @Override
        public void onClick(View v) {
            onRecipeListener.onRecipeClick(getAdapterPosition());
        }
    }

    public interface OnRecipeListener {
        void onRecipeClick(int position);
    }

}