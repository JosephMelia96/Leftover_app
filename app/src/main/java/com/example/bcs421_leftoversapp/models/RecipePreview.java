package com.example.bcs421_leftoversapp.models;

/**
 * Represents a preview of a recipe as displayed for example in a search result.
 */
public class RecipePreview {

    private String title;
    private String href;
    private String ingredients;
    private String thumbnail;
    private Long id;

    /**
     * Default no-args constructor for GSON marshalling.
     */
    public RecipePreview() {}

    /**
     * Constructs a new recipe.
     * @param title the title of the recipe.
     * @param href the API link to the full recipe object.
     * @param ingredients a comma delimited list of the recipe ingredients.
     * @param thumbnail an image preview of the recipe.
     */
    public RecipePreview(String title, String href, String ingredients, String thumbnail, Long id) {
        this.title = title;
        this.href = href;
        this.ingredients = ingredients;
        this.thumbnail = thumbnail;
        this.id = id;
    }

    /**
     * Gets the recipe title.
     * @return the recipe's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the link to the full recipe.
     * @return the API link to the full recipe object.
     */
    public String getHref() {
        return href;
    }

    /**
     * Gets the ingredients for the recipe.
     * @return comma separated list of ingredients.
     */
    public String getIngredients() {
        return ingredients;
    }

    /**
     * Gets the link to a graphic thumbnail of the recipe.
     * @return link to a graphic thumbnail of the recipe.
     */
    public String getThumbnail() {
        return thumbnail;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.title;
    }

}