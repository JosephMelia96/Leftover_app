package com.example.bcs421_leftoversapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.bcs421_leftoversapp.adapters.RecipeSearchResultAdapter;
import com.example.bcs421_leftoversapp.service.recipe.RecipeService;
import com.example.bcs421_leftoversapp.service.recipe.recipepuppy.RecipePuppyService;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Main application {@link android.app.Activity} which presents the user with a search view,
 * allowing them to find a recipe containing a specific term.
 */
public class RecipeSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private final int MAX_RECIPES_TO_SHOW = 20;

    private RecipeService recipeService;
    private RecipeSearchResultAdapter adapter;
    private SearchView searchView;
    private ListView searchResultList;
    private Subject<String> searchTextSubject;
    private Observable<String> onSearchTextChanged;
    private ImageView breakfast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_search);
        initialiseApiClient();
        searchView = findViewById(R.id.searchBar);
        searchResultList = findViewById(R.id.searchResultList);
        adapter = new RecipeSearchResultAdapter(this);
        searchResultList.setAdapter(this.adapter);
        searchView.setOnQueryTextListener(this);
        searchTextSubject = BehaviorSubject.create();
        // Debounce searches by 300ms to prevent lots of API requests in quick succession
        onSearchTextChanged = searchTextSubject.debounce(300, TimeUnit.MILLISECONDS);

        //if intent key value is not null then set query to passed value and run query
        //will automatically load which ever category was pressed
        if (getIntent().getStringExtra("category") != null) {
            searchView.setQuery(getIntent().getStringExtra("category"),false);
        }
        subscribeToSearchTextChanges();


        searchResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(RecipeSearchActivity.this, "You Choose: " + adapter.getItem(i).getIngredients(), Toast.LENGTH_SHORT).show();
                Intent ex = new Intent(RecipeSearchActivity.this, ShowRecipie.class);
                ex.putExtra("ingr", adapter.getItem(i).getIngredients());
                startActivity(ex);
            }
        });
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


    public void subscribeToSearchTextChanges() {

        onSearchTextChanged.subscribe(text -> recipeService.searchRecipes(text, MAX_RECIPES_TO_SHOW)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(recipes -> {
                    adapter.clear();
                    adapter.addAll(recipes);
                    //check adapter list and remove recipes without thumbnails
                    for (int i = adapter.getCount()-1; i >= 0 ; i--) { //

                        if(String.valueOf(adapter.getItem(i).getThumbnail()).equals("")) {
                            adapter.remove(adapter.getItem(i));
                        }
                    }
                })
                .subscribe());

    }

}
