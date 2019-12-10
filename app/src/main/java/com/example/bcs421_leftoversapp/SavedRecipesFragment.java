package com.example.bcs421_leftoversapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bcs421_leftoversapp.DataBase.RecipesContract;
import com.example.bcs421_leftoversapp.DataBase.UsersContract;
import com.example.bcs421_leftoversapp.adapters.RecipeSearchResultAdapter;
import com.example.bcs421_leftoversapp.models.Recipe;
import com.example.bcs421_leftoversapp.models.RecipePreview;
import com.example.bcs421_leftoversapp.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

//fragment to show recipes bookmarked by user, recipes can be swiped to delete
public class SavedRecipesFragment extends Fragment implements RecipeSearchResultAdapter.OnRecipeListener {

    UsersContract mUserContract;
    RecipesContract mRecipeContract;
    GoogleSignInClient mGoogleSignInClient;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecipePreview mRecipePreview;
    NavigationView navigationView;
    ArrayList<RecipePreview> recipePreviewsList;
    ArrayList<Recipe> savedRecipeList;
    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_saved_recipes, container, false);

        this.mUserContract = new UsersContract(getActivity());
        this.mRecipeContract = new RecipesContract(getActivity());
        mRecyclerView = v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true); // since size of view will not change, set this to true to increase apps efficiency
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        navigationView = getActivity().findViewById(R.id.nav_view);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        User user = mUserContract.getParentIdByEmail(acct.getEmail());
        //add recipes to list
        addItemsToList(user.getID());

        // Create a new ItemTouchHelper with a SimpleCallback that handles both LEFT and RIGHT swipe directions
        // Create an item touch helper to handle swiping items off the list
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mRecipeContract.removeSavedRecipe(savedRecipeList.get(viewHolder.getLayoutPosition()).getId());
                Toast.makeText(getContext(), "Recipe Unsaved", Toast.LENGTH_SHORT).show();
                addItemsToList(user.getID());
            }
        }).attachToRecyclerView(mRecyclerView);
        return v;
    }

    public void addItemsToList(Long id) {
        savedRecipeList = new ArrayList<Recipe>();
        savedRecipeList = mRecipeContract.getRecipesOfUser(id);
        recipePreviewsList = new ArrayList<RecipePreview>();

        if (savedRecipeList.size() != 0) {
            for (int i = 0; i < savedRecipeList.size(); i++) {
                // use the RecipePreview constructor to create new Preview objects
                this.mRecipePreview = new RecipePreview(savedRecipeList.get(i).getTitle(),
                        savedRecipeList.get(i).getHref(), savedRecipeList.get(i).getIngredients(),
                        savedRecipeList.get(i).getThumbnail(), savedRecipeList.get(i).getId());
                recipePreviewsList.add(mRecipePreview); //add the recipePreview object to list
            }

            mAdapter = new RecipeSearchResultAdapter(getContext(), recipePreviewsList, this);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onRecipeClick(int position) {
        Intent intent = new Intent(getActivity(), ShowRecipe.class);
        intent.putExtra("title", recipePreviewsList.get(position).getTitle());
        intent.putExtra("ingr", recipePreviewsList.get(position).getIngredients());
        intent.putExtra("img", recipePreviewsList.get(position).getThumbnail());
        intent.putExtra("href", recipePreviewsList.get(position).getHref());
        startActivity(intent);
    }
}
