package com.example.bcs421_leftoversapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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


public class SavedRecipesFragment extends Fragment {

    UsersContract mUserContract;
    RecipesContract mRecipeContract;
    GoogleSignInClient mGoogleSignInClient;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecipePreview mRecipePreview;
    NavigationView navigationView;
    ArrayList<RecipePreview> recipePreviewsList;
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
//        addItemsToList(user.getID());

        //launch showRecipeActivity when list item is clicked
//        savedRecipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //Toast.makeText(RecipeSearchActivity.this, "You Choose: " + adapter.getItem(i).getIngredients(), Toast.LENGTH_SHORT).show();
//                Intent ex = new Intent(getActivity(), ShowRecipe.class);
//                ex.putExtra("ingr", adapter.getItem(i).getIngredients());
//                ex.putExtra("img", adapter.getItem(i).getThumbnail());
//                ex.putExtra("title", adapter.getItem(i).getTitle());
//                ex.putExtra("href", adapter.getItem(i).getHref());
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("Recipe Options");
//                builder.setPositiveButton("View Recipe", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        startActivity(ex);
//                    }
//                }).setNegativeButton("Unsave Recipe", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mRecipeContract.removeSavedRecipe(savedRecipeList.get(i).getId());
//                        addItemsToList(user.getID());
//                    }
//                });
//                builder.show();
//            }
//        });


        return v;
    }

//    public void addItemsToList(Long id){
//        ArrayList<Recipe> savedRecipeList = mRecipeContract.getRecipesOfUser(id);
//
//        if (savedRecipeList.size() != 0) {
//            for (int i = 0; i < savedRecipeList.size(); i++) {
//                // use the RecipePreview constructor to create new Preview objects
//                this.mRecipePreview = new RecipePreview(savedRecipeList.get(i).getTitle(),
//                        savedRecipeList.get(i).getHref(), savedRecipeList.get(i).getIngredients(),
//                        savedRecipeList.get(i).getThumbnail());
//                recipePreviewsList.add(mRecipePreview); //add the recipePreview object to list
//            }
//
//            mAdapter = new RecipeSearchResultAdapter(getContext(), recipePreviewsList);
//            mRecyclerView.setAdapter(mAdapter);
//        }
//    }

}
