package com.example.bcs421_leftoversapp.models;

public class User {

    private long mID;
    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mPassword;

    public User() { };

    //used for local user
    public User(String mFirstName, String mLastName, String mEmail, String mPassword) {
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mEmail = mEmail;
        this.mPassword = mPassword;
    }

    //used for gmail and facebook
    public User(String mEmail) {
        this.mEmail = mEmail;
    }

    public long getID() {
        return mID;
    }

    public void setID(long mID) {
        this.mID = mID;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}
