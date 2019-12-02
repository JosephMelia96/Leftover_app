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

public final class RecipesContract {

    // Database fields
    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;
    private Context mContext;

    //reference to table column names for queries
    private String[] mAllColumns = {
            RecipesEntry._ID,
            RecipesEntry.COL_TITLE,
            RecipesEntry.COL_INGREDIENTS,
            RecipesEntry.COL_THUMBNAIL,
            RecipesEntry.COL_HREF,
            RecipesEntry.COL_USER_ID
    };

    //column and table names
    public static final class RecipesEntry implements BaseColumns {
        public static final String TABLE_NAME="recipes";
        public static final String COL_TITLE="title";
        public static final String COL_INGREDIENTS="ingredients";
        public static final String COL_THUMBNAIL="thumbnail";
        public static final String COL_HREF="href";
        public static final String COL_USER_ID="user_id";
    }

    //constructor to open recipe table
    public RecipesContract(Context context) {
        mDbHelper = new DbHelper(context);
        this.mContext = context;
        //open the database
        try {
            open();
        } catch (SQLException e) {
            Log.e("ChildContract ", "SQLException on opening database " + e.getMessage());
        }
    }

    //open database
    public void open() throws SQLException{
        mDb = mDbHelper.getWritableDatabase();
    }

    //close database
    public void close() {
        mDbHelper.close();
    }

    //used to add recipe into database
    public Recipe createChild(String title, String ingredients, String thumbnail, String href, long userId) {
        ContentValues cv = new ContentValues();
        cv.put(RecipesEntry.COL_TITLE,title);
        cv.put(RecipesEntry.COL_INGREDIENTS,ingredients);
        cv.put(RecipesEntry.COL_THUMBNAIL,thumbnail);
        cv.put(RecipesEntry.COL_HREF,href);
        cv.put(RecipesEntry.COL_USER_ID,userId);
        long insertId = mDb.insert(RecipesEntry.TABLE_NAME, null, cv);
        Cursor cursor = mDb.query(RecipesEntry.TABLE_NAME, mAllColumns, RecipesEntry._ID +
                " = " + insertId,null, null, null, null);
        cursor.moveToFirst();
        Recipe newRecipe = cursorToChild(cursor);
        cursor.close();
        return newRecipe;
    }

    //used to set data to specific recipe object
    private Recipe cursorToChild(Cursor cursor) {
        Recipe recipe = new Recipe();
        recipe.setId(cursor.getLong(0));
        recipe.setTitle(cursor.getString(1));
        recipe.setIngredients(cursor.getString(2));
        recipe.setThumbnail(cursor.getString(3));
        recipe.setHref(cursor.getString(4));

        //get The User by id
        long parentId = cursor.getLong(5);
        UsersContract contract = new UsersContract(mContext);
        User user = contract.getParentById(parentId);
        if (contract != null) {
            recipe.setUser(user);
        }
        return recipe;
    }




}
