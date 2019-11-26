package com.example.bcs421_leftoversapp.service.recipe.recipepuppy;

import com.example.bcs421_leftoversapp.models.RecipePreview;

import java.util.List;

/**
 * API type which represents the structure of the JSON object returned in response to a
 * GET request to the Recipe Puppy API.
 */
public class GetRecipeResponse {

    private String title;
    private String version;
    private List<RecipePreview> results;

    private GetRecipeResponse() {}

    /**
     * Constructs a new instance.
     * @param title the title.
     * @param version the version.
     * @param results the list of results embedded in the response.
     */
    public GetRecipeResponse(String title, String version, List<RecipePreview> results) {
        this.title = title;
        this.version = version;
        this.results = results;
    }

    /**
     * Extracts the recipes from the response.
     * @return the list of recipes embedded in the response.
     */
    public List<RecipePreview> getRecipes() {
        return results;
    }

}