package com.example.bcs421_leftoversapp.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.bcs421_leftoversapp.models.Recipe;
import com.example.bcs421_leftoversapp.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public final class UsersContract {

    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;
    private Context mContext;

    //column and table names
    public static final class UsersEntry implements BaseColumns {
        public static final String TABLE_NAME="users";
        public static final String COL_FIRST_NAME="firstName";
        public static final String COL_LAST_NAME="lastName";
        public static final String COL_EMAIL="email";
        public static final String COL_PASSWORD="password";
    }

    //reference to table column names for queries
    private String[] mAllColumns = {
            UsersEntry._ID,
            UsersEntry.COL_FIRST_NAME,
            UsersEntry.COL_LAST_NAME,
            UsersEntry.COL_EMAIL,
            UsersEntry.COL_PASSWORD
    };

    //constructor to open parents table
    public UsersContract(Context context){
        this.mContext = context;
        mDbHelper = new DbHelper(context);
        //open the database
        try {
            open();
        } catch (SQLException e) {
            Log.e("ParentContract ", Objects.requireNonNull(e.getMessage()));
        }
    }

    //open database
    public void open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
    }

    //close database
    public void close() {
        mDbHelper.close();
    }

    //add user into database
    public User createUser(String firstName, String lastName, String email, String password) {
        ContentValues cv = new ContentValues();
        cv.put(UsersEntry.COL_FIRST_NAME, firstName);
        cv.put(UsersEntry.COL_LAST_NAME, lastName);
        cv.put(UsersEntry.COL_EMAIL, email);
        cv.put(UsersEntry.COL_PASSWORD, password);

        long insertId = mDb.insert(UsersEntry.TABLE_NAME,null,cv);
        Cursor cursor = mDb.query(UsersEntry.TABLE_NAME, mAllColumns, UsersEntry._ID +
                " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        User newUser = cursorToUser(cursor);
        cursor.close();
        mDb.close();
        return newUser;
    }

    //return parent by searching by id
    public User getParentById(long id) {
        Cursor cursor = mDb.query(UsersEntry.TABLE_NAME, mAllColumns, UsersEntry._ID + " = ?",
                new String[] {String.valueOf(id) }, null,null,null);
        if(cursor != null) {
            cursor.moveToFirst();
        }

        User user = cursorToUser(cursor);
        return user;
    }

    //return parent by searching by email
    public User getParentIdByEmail(String email) {
        Cursor cursor = mDb.query(UsersEntry.TABLE_NAME, mAllColumns, UsersEntry.COL_EMAIL + " = ?",
                new String[] {String.valueOf(email) }, null,null,null);
        if(cursor != null) {
            cursor.moveToFirst();
        }

        User user = cursorToUser(cursor);
        mDb.close();
        return user;
    }

    //check for empty table
    public boolean checkForEmptyTable() {
        mDb = mDbHelper.getWritableDatabase();
        String count = "SELECT count(*) FROM " + UsersEntry.TABLE_NAME;
        Cursor cursor = mDb.rawQuery(count,null);
        cursor.moveToFirst();
        int icount = cursor.getInt(0);
        mDb.close();
        if(icount>0)
            return false;
        else
            return true;
    }

    //check for existing user table
    public boolean checkForExistingUser(String email) {
        String emailUser = "";
        Cursor cursor = mDb.query(UsersEntry.TABLE_NAME, mAllColumns, UsersEntry.COL_EMAIL + " = ?",
                new String[] {String.valueOf(email) }, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            User user = cursorToUser(cursor);
            emailUser = user.getEmail();
            cursor.moveToNext();
        }
        if (emailUser.equals(email))
            return true;
        else
            return false;
    }

    //set data to specific user object
    protected User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setID(cursor.getLong(0));
        user.setFirstName(cursor.getString(1));
        user.setLastName(cursor.getString(2));
        user.setEmail(cursor.getString(3));
        user.setPassword(cursor.getString(4));
        return user;
    }



}
