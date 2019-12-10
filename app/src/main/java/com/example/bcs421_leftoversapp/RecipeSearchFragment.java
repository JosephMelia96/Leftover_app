package com.example.bcs421_leftoversapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bcs421_leftoversapp.adapters.RecipeSearchResultAdapter;
import com.example.bcs421_leftoversapp.models.RecipePreview;
import com.example.bcs421_leftoversapp.service.recipe.RecipeService;
import com.example.bcs421_leftoversapp.service.recipe.recipepuppy.RecipePuppyService;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public class RecipeSearchFragment extends Fragment implements SearchView.OnQueryTextListener, RecipeSearchResultAdapter.OnRecipeListener {
  // value for how many recipes card to show
    private final int MAX_RECIPES_TO_SHOW = 50;

    private RecipeService recipeService;
    private SearchView searchView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Subject<String> searchTextSubject;
    private Observable<String> onSearchTextChanged;
    private ArrayList<RecipePreview> recipeList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe_search, container, false);

        initialiseApiClient();
        searchView = v.findViewById(R.id.searchBar);
        mRecyclerView = v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true); // since size of view will not change, set this to true to increase apps efficiency
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        searchView.setOnQueryTextListener(this);
        searchTextSubject = BehaviorSubject.create();
        // Debounce searches by 300ms to prevent lots of API requests in quick succession
        onSearchTextChanged = searchTextSubject.debounce(300, TimeUnit.MILLISECONDS);

        //if bundle is not null then set query to passed value and run query
        //will automatically load which ever category was pressed
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            searchView.setQuery(bundle.getString("category"),false);
        }

        subscribeToSearchTextChanges();

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                Intent intent = new Intent(getActivity(),ShowRecipe.class);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        return v;
    }

    //Create a new ItemTouchHelper with a SimpleCallback that handles both LEFT and RIGHT swipe directions
    // Create an item touch helper to handle swiping items off the list



        private void initialiseApiClient() {
        this.recipeService = new RecipePuppyService();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        if (newText.trim().length() == 0) {
            return true;
        }
        searchTextSubject.onNext(newText);
        return true;
    }


    public void subscribeToSearchTextChanges() {

        // Array list to hold recipePreview list passed by query
        recipeList = new ArrayList<>();

        onSearchTextChanged.subscribe(text -> recipeService.searchRecipes(text, MAX_RECIPES_TO_SHOW)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(recipes -> {
                    recipeList.addAll(recipes);
                    mAdapter = new RecipeSearchResultAdapter(getContext(),recipeList, this);
                    mRecyclerView.setAdapter(mAdapter);

                    // remove recipes without thumbnails from recipeList
                    for(int i = mAdapter.getItemCount()-1; i >= 0 ; i--) {
                        if (String.valueOf(recipeList.get(i).getThumbnail()).equals("")) {
                            recipeList.remove(i);
                        }
                    }
                })
                .subscribe());
    }

    @Override
    public void onRecipeClick(int position) {
        Intent intent = new Intent(getActivity(),ShowRecipe.class);
        intent.putExtra("title", recipeList.get(position).getTitle());
        intent.putExtra("ingr", recipeList.get(position).getIngredients());
        intent.putExtra("img", recipeList.get(position).getThumbnail());
        intent.putExtra("href", recipeList.get(position).getHref());
        startActivity(intent);
    }
}
