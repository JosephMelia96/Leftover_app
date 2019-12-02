package com.example.bcs421_leftoversapp.models;

public class User {

    private long mID;
    private String mEmail;

    public User() { };

    public User(String mEmail) {
        this.mEmail = mEmail;
    }

    public long getID() {
        return mID;
    }

    public void setID(long mID) {
        this.mID = mID;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }
}
