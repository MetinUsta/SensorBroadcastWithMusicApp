package com.example.musicplayerv2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.musicplayerv2.Database.model.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper sInstance;
    private static final String DATABASE_NAME = "users_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating User Table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(User.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);
        onCreate(db);
    }

    public long insertUser(String first_name, String last_name, String email, String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(User.COLUMN_FIRST_NAME, first_name);
        values.put(User.COLUMN_LAST_NAME, last_name);
        values.put(User.COLUMN_EMAIL, email);
        values.put(User.COLUMN_PHONE, phone);
        values.put(User.COLUMN_PASSWORD, password);

        long id = db.insert(User.TABLE_NAME, null, values);

        return id;
    }

    public User getUser(String email){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(User.TABLE_NAME,
                new String[]{User.COLUMN_ID, User.COLUMN_FIRST_NAME, User.COLUMN_LAST_NAME, User.COLUMN_EMAIL, User.COLUMN_PHONE, User.COLUMN_PASSWORD},
                User.COLUMN_EMAIL + "=?",
                new String[]{email}, null, null, null, null);
        System.out.println("CURSOR:" + cursor);
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(User.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_LAST_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_PHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_PASSWORD))
            );
            cursor.close();
        }
        return user;
    }
}
