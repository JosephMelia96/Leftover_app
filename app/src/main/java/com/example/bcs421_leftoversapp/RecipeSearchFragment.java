package com.example.bcs421_leftoversapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bcs421_leftoversapp.adapters.RecipeSearchResultAdapter;
import com.example.bcs421_leftoversapp.service.recipe.RecipeService;
import com.example.bcs421_leftoversapp.service.recipe.recipepuppy.RecipePuppyService;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public class RecipeSearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    private final int MAX_RECIPES_TO_SHOW = 20;

    private RecipeService recipeService;
    private RecipeSearchResultAdapter adapter;
    private SearchView searchView;
    private ListView searchResultList;
    private Subject<String> searchTextSubject;
    private Observable<String> onSearchTextChanged;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe_search, container, false);

        initialiseApiClient();
        searchView = v.findViewById(R.id.searchBar);
        searchResultList = v.findViewById(R.id.searchResultList);
        adapter = new RecipeSearchResultAdapter(getContext());
        searchResultList.setAdapter(this.adapter);
        searchView.setOnQueryTextListener(this);
        searchTextSubject = BehaviorSubject.create();
        // Debounce searches by 300ms to prevent lots of API requests in quick succession
        onSearchTextChanged = searchTextSubject.debounce(300, TimeUnit.MILLISECONDS);

        //if intent key value is not null then set query to passed value and run query
        //will automatically load which ever category was pressed
//        if (getActivity().getIntent().getStringExtra("category") != null) {
//            searchView.setQuery(getActivity().getIntent().getStringExtra("category"),false);
//        }
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            searchView.setQuery(bundle.getString("category"),false);
        }

        subscribeToSearchTextChanges();

        searchResultList.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent ex = new Intent(getActivity(), ShowRecipe.class);
            ex.putExtra("ingr", adapter.getItem(i).getIngredients());
            ex.putExtra("img", adapter.getItem(i).getThumbnail());
            ex.putExtra("title", adapter.getItem(i).getTitle());
            ex.putExtra("href", adapter.getItem(i).getHref());
            startActivity(ex);

        });

        return v;
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
