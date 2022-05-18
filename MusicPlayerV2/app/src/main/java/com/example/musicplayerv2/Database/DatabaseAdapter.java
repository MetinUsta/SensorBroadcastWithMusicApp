package com.example.musicplayerv2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.musicplayerv2.Database.model.User;

public class DatabaseAdapter {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "users_db";

    private Context context;

    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;

    public DatabaseAdapter(Context c) {
        context = c;
    }

    public DatabaseAdapter openToRead() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, DATABASE_NAME, DATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        return this;
    }

    public DatabaseAdapter openToWrite() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, DATABASE_NAME, DATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        sqLiteHelper.close();
    }

    public long insertUser(String first_name, String last_name, String email, String phone, String password) {

        ContentValues values = new ContentValues();
        values.put(User.COLUMN_FIRST_NAME, first_name);
        values.put(User.COLUMN_LAST_NAME, last_name);
        values.put(User.COLUMN_EMAIL, email);
        values.put(User.COLUMN_PHONE, phone);
        values.put(User.COLUMN_PASSWORD, password);

        long id = sqLiteDatabase.insert(User.TABLE_NAME, null, values);
        return id;
    }

    public User getUser(String email){

        Cursor cursor = sqLiteDatabase.query(User.TABLE_NAME,
                new String[]{User.COLUMN_ID, User.COLUMN_FIRST_NAME, User.COLUMN_LAST_NAME, User.COLUMN_EMAIL, User.COLUMN_PHONE, User.COLUMN_PASSWORD},
                User.COLUMN_EMAIL + "=?",
                new String[]{email}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        User user = null;
        System.out.println("Cursor Count: " + cursor.getCount());
        if(cursor.getCount() > 0) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(User.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_LAST_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_PHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_PASSWORD))
            );
        }
        cursor.close();
        return user;
    }

    public class SQLiteHelper extends SQLiteOpenHelper{

        public SQLiteHelper(Context context, String name, int version) {
            super(context, name, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(User.CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);
            onCreate(db);
        }
    }
}
