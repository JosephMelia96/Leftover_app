package com.example.bcs421_leftoversapp.models;

public class Recipe {
    private long mId;
    private String mTitle;
    private String mIngredients;
    private String mThumbnail;
    private String mHref;
    private User user;

    //default constructor
    public Recipe(){ };

    //parameter constructor
    public Recipe(String mTitle, String mIngredients, String mThumbnail, String mHref) {
        this.mTitle = mTitle;
        this.mIngredients = mIngredients;
        this.mThumbnail = mThumbnail;
        this.mHref = mHref;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getIngredients() {
        return mIngredients;
    }

    public void setIngredients(String mIngredients) {
        this.mIngredients = mIngredients;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String mThumbnail) {
        this.mThumbnail = mThumbnail;
    }

    public String getHref() {
        return mHref;
    }

    public void setHref(String mHref) {
        this.mHref = mHref;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
