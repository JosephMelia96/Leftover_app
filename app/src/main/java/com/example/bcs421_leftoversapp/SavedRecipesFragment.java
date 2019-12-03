package com.example.bcs421_leftoversapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import java.util.List;


public class SavedRecipesFragment extends Fragment {

    UsersContract mUserContract;
    RecipesContract mRecipeContract;
    GoogleSignInClient mGoogleSignInClient;
    RecipePreview mRecipePreview;
    private RecipeSearchResultAdapter adapter;
    private ListView savedRecipeListView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_saved_recipes, container, false);

        this.mUserContract = new UsersContract(getActivity());
        this.mRecipeContract = new RecipesContract(getActivity());
        savedRecipeListView = v.findViewById(R.id.savedRecipeList);
        adapter = new RecipeSearchResultAdapter(getActivity());

        savedRecipeListView.setAdapter(this.adapter);
        adapter.clear();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        User user = mUserContract.getParentIdByEmail(acct.getEmail());
        List<Recipe> savedRecipeList = mRecipeContract.getRecipesOfUser(user.getID());
        Toast.makeText(getActivity(),"Works",Toast.LENGTH_SHORT).show();
        for (int i=0; i < savedRecipeList.size(); i++) {
            this.mRecipePreview = new RecipePreview(savedRecipeList.get(i).getTitle(),
                    savedRecipeList.get(i).getHref(),savedRecipeList.get(i).getIngredients(),
                    savedRecipeList.get(i).getThumbnail());
            adapter.add(mRecipePreview);
        }

        savedRecipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(RecipeSearchActivity.this, "You Choose: " + adapter.getItem(i).getIngredients(), Toast.LENGTH_SHORT).show();
                Intent ex = new Intent(getActivity(), ShowRecipie.class);
                ex.putExtra("ingr", adapter.getItem(i).getIngredients());
                ex.putExtra("img", adapter.getItem(i).getThumbnail());
                ex.putExtra("title", adapter.getItem(i).getTitle());
                ex.putExtra("href", adapter.getItem(i).getHref());
                startActivity(ex);
            }
        });

        //check adapter list and remove recipes without thumbnails
//        for (int i = adapter.getCount()-1; i >= 0 ; i--) { //
//            String title = adapter.getItem(i).getTitle();
//            for (int j=0 ; j < adapter.getCount();j++) {
//                if (adapter.getItem(j).getTitle().equals(title)) {
//                    adapter.remove(adapter.getItem(j));
//                }
//            }
//        }

        return v;
    }
}