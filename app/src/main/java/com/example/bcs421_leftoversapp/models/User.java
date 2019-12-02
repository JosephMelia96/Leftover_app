package com.example.bcs421_leftoversapp.models;

public class User {

    private String mEmail;

    private User() { };

    public User(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }
}
