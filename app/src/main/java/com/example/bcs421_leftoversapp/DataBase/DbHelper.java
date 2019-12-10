package com.example.bcs421_leftoversapp.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static com.example.bcs421_leftoversapp.DataBase.UsersContract.*;
import static com.example.bcs421_leftoversapp.DataBase.RecipesContract.*;

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "save_recipe.db";

    //query to create users table
    public static final String SQL_CREATE_USERS_TABLE = "CREATE TABLE " + UsersEntry.TABLE_NAME + " (" +
            UsersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            UsersEntry.COL_FIRST_NAME + " TEXT NOT NULL, " +
            UsersEntry.COL_LAST_NAME + " TEXT NOT NULL, " +
            UsersEntry.COL_EMAIL + " TEXT NOT NULL, " +
            UsersEntry.COL_PASSWORD + " TEXT NOT NULL" +
            "); ";

    //query to create recipes table
    public static final String SQL_CREATE_RECIPES_TABLE = "CREATE TABLE " + RecipesEntry.TABLE_NAME + " (" +
            RecipesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RecipesEntry.COL_TITLE + " TEXT NOT NULL, " +
            RecipesEntry.COL_INGREDIENTS + " TEXT NOT NULL, " +
            RecipesEntry.COL_THUMBNAIL + " TEXT NOT NULL, " +
            RecipesEntry.COL_HREF + " TEXT NOT NULL, " +
            RecipesEntry.COL_USER_ID + " INTEGER NOT NULL" +
            "); ";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USERS_TABLE);
        db.execSQL(SQL_CREATE_RECIPES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UsersEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RecipesEntry.TABLE_NAME);

        onCreate(db);

    }
}
