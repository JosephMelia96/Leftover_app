package com.example.bcs421_leftoversapp.service.recipe.recipepuppy;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Service definition for Retrofit which defines an API client for the RecipePreview Puppy API.
 * @see <a href="http://www.recipepuppy.com/about/api/">RecipePreview Puppy API</a>
 */
public interface RecipePuppyApiClient {

    /**
     * Public URL where the RecipePreview Puppy API can be located.
     */
    String SERVICE_ENDPOINT = "http://www.recipepuppy.com/";

    @GET("api/")
    Observable<GetRecipeResponse> searchRecipes(@Query("q") String searchTerm, @Query("p") int page);

}