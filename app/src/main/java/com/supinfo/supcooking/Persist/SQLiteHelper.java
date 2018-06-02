package com.supinfo.supcooking.Persist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.supinfo.supcooking.Entity.User;

/**
 * Created by User on 20/03/2018.
 */

public class SQLiteHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "users.db";

    public static final String TABLE_NAME = "user";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_PHONENUMBER = "phoneNumber";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_POSTALADDRESS = "postalAddress";
    public static final String COLUMN_EMAIL = "email";

    // Commande sql pour la création de la base de données
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_USERNAME + " text not null, "
            + COLUMN_PASSWORD + " text not null, "
            + COLUMN_PHONENUMBER + " text , "
            + COLUMN_FIRSTNAME + " text , "
            + COLUMN_LASTNAME + " text , "
            + COLUMN_POSTALADDRESS + " text, "
            + COLUMN_EMAIL + " text not null) ";


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String password, String email, String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_PASSWORD, password);
        contentValues.put(COLUMN_USERNAME, username);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1) { return false; }
        else {return true; }
    }

    public void getAll(){
        Log.d("BDD", "Infos de la BDD");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME, null);
        if (result.getCount() > 0){

        while(result.moveToNext()) {
                Log.d("BDD", result.getString(0));
                Log.d("Email", result.getString(7));
                Log.d("Password", result.getString(2));
            }
        }
    }

    public boolean isExist(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] params = new String[]{  user.getEmail(), user.getUsername()};
        String sql = "select email, username from " + TABLE_NAME + " WHERE email LIKE ?  AND username LIKE ?";
        Cursor result = db.rawQuery( sql , params);
        return result.getCount() > 0;
    }

    public User getUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] params = new String[]{  user.getEmail(), user.getUsername()};
        String sql = "select * from " + TABLE_NAME + " WHERE email LIKE ?  AND username LIKE ?";
        Cursor result = db.rawQuery( sql , params);
        if (result.getCount() > 0){
            User u = new User(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6), result.getString(7));
            return u;
        }
        return null;
    }
}