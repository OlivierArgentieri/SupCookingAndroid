package com.supinfo.supcooking.Persist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.supinfo.supcooking.Adapter.RowRecipe;
import com.supinfo.supcooking.Entity.Recipe;
import com.supinfo.supcooking.Entity.User;

import java.util.ArrayList;

/**
 * Created by User on 20/03/2018.
 */

public class SQLiteHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "supcooking.db";

    // Table User
    public static final String TABLE_USER = "user";

    public static final String COLUMN_ID_USER = "_idUser";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_PHONENUMBER = "phoneNumber";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_POSTALADDRESS = "postalAddress";
    public static final String COLUMN_EMAIL = "email";

    // Commande sql pour la création de la Table User
    private static final String DATABASE_CREATE_USER = "create table "
            + TABLE_USER + "("
            + COLUMN_ID_USER + " integer primary key autoincrement, "
            + COLUMN_USERNAME + " text not null, "
            + COLUMN_PASSWORD + " text not null, "
            + COLUMN_PHONENUMBER + " text , "
            + COLUMN_FIRSTNAME + " text , "
            + COLUMN_LASTNAME + " text , "
            + COLUMN_POSTALADDRESS + " text, "
            + COLUMN_EMAIL + " text not null) ";

    // Table User
    public static final String TABLE_RECIPE = "recipe";

    public static final String COLUMN_ID_RECIPE = "_idRecipe";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_COOKINGTIME = "cookingTime";
    public static final String COLUMN_PREPARATIONTTIME = "preparationTime";
    public static final String COLUMN_INGREDIENTS = "ingredients";
    public static final String COLUMN_PREPARATIONSTEPS = "preparationSteps";
    public static final String COLUMN_RATE = "rate";
    public static final String COLUMN_PICTURE = "picture";
    public static final String COLUMN_USER = "user";

    // Commande sql pour la création de la Table User
    private static final String DATABASE_CREATE_RECIPE = "create table "
            + TABLE_RECIPE + "("
            + COLUMN_ID_RECIPE + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_TYPE + " text not null, "
            + COLUMN_COOKINGTIME + " integer not null, "
            + COLUMN_PREPARATIONTTIME + " integer not null, "
            + COLUMN_INGREDIENTS + " text not null, "
            + COLUMN_PREPARATIONSTEPS + " text not null, "
            + COLUMN_RATE + " integer default 0, "
            + COLUMN_PICTURE + " text,"
            + COLUMN_USER + " integer,"
            + "FOREIGN KEY(user) REFERENCES user(_idUser)) ";


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_USER);
        database.execSQL(DATABASE_CREATE_RECIPE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE);
        onCreate(db);
    }

    // methode User
    public boolean insertUser(String password, String email, String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_PASSWORD, password);
        contentValues.put(COLUMN_USERNAME, username);
        long result = db.insert(TABLE_USER, null, contentValues);
        if(result == -1) { return false; }
        else {return true; }
    }

    public boolean InsertOrUpdateUser(User u){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, u.getUsername());
        contentValues.put(COLUMN_PASSWORD, u.getPassword());
        contentValues.put(COLUMN_PHONENUMBER, u.getPhoneNumber());
        contentValues.put(COLUMN_FIRSTNAME, u.getFirstname());
        contentValues.put(COLUMN_LASTNAME, u.getLastname());
        contentValues.put(COLUMN_POSTALADDRESS, u.getPostalAddress());
        contentValues.put(COLUMN_EMAIL, u.getEmail());

        User user = getUser(u);
        if (user != null){
            db.update(TABLE_USER, contentValues, COLUMN_USERNAME+"=? AND " + COLUMN_EMAIL+"=?", new String[] {u.getUsername(), u.getEmail()});
            return false;
        }
        else{
        db.insertWithOnConflict(TABLE_USER, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        return true;}
    }

    public void getAllUser(){
        Log.d("BDD", "Infos de la BDD");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_USER, null);
        if (result.getCount() > 0){

        while(result.moveToNext()) {
                Log.d("BDD", result.getString(0));
                Log.d("Email", result.getString(7));
                Log.d("Password", result.getString(2));
            }
        }
        db.close();
    }
    public User getUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] params = new String[]{  user.getEmail(), user.getUsername()};
        String sql = "select * from " + TABLE_USER + " WHERE email LIKE ?  OR username LIKE ?";
        Cursor result = db.rawQuery( sql , params);

        if (result.getCount() > 0){
            result.moveToNext();
            User u = new User(result.getInt(0), result.getString(7), result.getString(2), result.getString(1));
            return u;
        }
        return null;
    }

    public User getUser(String email, String username){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] params = new String[]{  email, username};
        String sql = "select * from " + TABLE_USER + " WHERE email LIKE ?  OR username LIKE ?";
        Cursor result = db.rawQuery( sql , params);

        if (result.getCount() > 0){
            result.moveToNext();
            User u = new User(result.getInt(0), result.getString(7), result.getString(2), result.getString(1));
            return u;
        }
        return null;
    }

    public User getUserLogin(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] params = new String[]{ username, password};
        String sql = "select * from " + TABLE_USER + " WHERE username LIKE ?  OR password LIKE ?";
        Cursor result = db.rawQuery( sql , params);

        if (result.getCount() > 0){
            result.moveToNext();
            User u = new User(result.getInt(0), result.getString(7), result.getString(2), result.getString(1));
            return u;
        }
        return null;
    }

    public User getUserById(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] params = new String[]{ id.toString()};
        String sql = "select * from " + TABLE_USER + " WHERE _idUser LIKE ? ";
        Cursor result = db.rawQuery( sql , params);

        if (result.getCount() > 0){
            result.moveToNext();
            User u = new User(result.getInt(0), result.getString(7), result.getString(2), result.getString(1));
            return u;
        }
        return null;
    }


    // Method Recipe
    public boolean insertOrUpdateRecipe(Recipe recipe, User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID_RECIPE, recipe.getId());
        contentValues.put(COLUMN_NAME, recipe.getName());
        contentValues.put(COLUMN_TYPE, recipe.getType());
        contentValues.put(COLUMN_COOKINGTIME, recipe.getCookingTime());
        contentValues.put(COLUMN_PREPARATIONTTIME, recipe.getPreparationTime());
        contentValues.put(COLUMN_INGREDIENTS, recipe.getIngredients());
        contentValues.put(COLUMN_PREPARATIONSTEPS, recipe.getPreparationSteps());
        contentValues.put(COLUMN_RATE, recipe.getRate());
        contentValues.put(COLUMN_PICTURE, recipe.getPicture());
        contentValues.put(COLUMN_USER, getUser(user).getId());

        int id = (int) db.insertWithOnConflict(TABLE_RECIPE, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1){
            db.update(TABLE_RECIPE, contentValues, COLUMN_ID_RECIPE+"=?", new String[] {String.valueOf(recipe.getId())});
            return false;
        }
        else
        {return true; }
    }

    public ArrayList<Recipe> getAllRecipe(){
        Log.d("BDD", "Infos de la BDD recipe");
        ArrayList<Recipe> Recipes = new ArrayList<Recipe>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_RECIPE, null);
        if (result.getCount() > 0){
            // todo faire la liste des recettes
            while(result.moveToNext()) {
                Recipes.add(new Recipe(result.getInt(0), result.getString(1), result.getString(2), result.getInt(3), result.getInt(4), result.getString(5), result.getString(6), result.getFloat(7),  result.getString(8)));
            }
        }
        return Recipes;
    }

}