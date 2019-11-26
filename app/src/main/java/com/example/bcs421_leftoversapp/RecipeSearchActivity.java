package com.example.bcs421_leftoversapp;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.bcs421_leftoversapp.adapters.RecipeSearchResultAdapter;
import com.example.bcs421_leftoversapp.service.recipe.RecipeService;
import com.example.bcs421_leftoversapp.service.recipe.recipepuppy.RecipePuppyService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Main application {@link android.app.Activity} which presents the user with a search view,
 * allowing them to find a recipe containing a specific term.
 */
public class RecipeSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private final int MAX_RECIPES_TO_SHOW = 25;

    private RecipeService recipeService;
    private RecipeSearchResultAdapter adapter;
    private SearchView searchView;
    private ListView searchResultList;
    private Subject<String> searchTextSubject;
    private Observable<String> onSearchTextChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_search);
        initialiseApiClient();
        searchView = findViewById(R.id.searchBar);
        searchResultList = (ListView) findViewById(R.id.searchResultList);
        adapter = new RecipeSearchResultAdapter(this);
        searchResultList.setAdapter(this.adapter);
        searchView.setOnQueryTextListener(this);
        searchTextSubject = BehaviorSubject.create();
        // Debounce searches by 300ms to prevent lots of API requests in quick succession
        onSearchTextChanged = searchTextSubject.debounce(300, TimeUnit.MILLISECONDS);
        subscribeToSearchTextChanges();
    }

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
            adapter.clear();
            return true;
        }
        searchTextSubject.onNext(newText);
        return true;
    }

    private void subscribeToSearchTextChanges() {
        onSearchTextChanged.subscribe(text -> recipeService.searchRecipes(text, MAX_RECIPES_TO_SHOW)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(recipes -> {
                    adapter.clear();
                    adapter.addAll(recipes);
                })
                .subscribe());
    }

}
