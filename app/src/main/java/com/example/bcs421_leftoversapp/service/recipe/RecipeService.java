package com.example.bcs421_leftoversapp.service.recipe;


import com.example.bcs421_leftoversapp.models.RecipePreview;

import java.util.List;

import io.reactivex.Observable;

/**
 * Service which can query and fetch recipes from a recipe providing API.
 */
public interface RecipeService {

    /**
     * Search the recipe database for recipes containing a specific term.
     * @param term the term to search for.
     * @param maxResults the maximum number of results to return to the client.
     * @return an {@link Observable} which emits the list of matching recipes.
     */
    Observable<List<RecipePreview>> searchRecipes(String term, int maxResults);

}