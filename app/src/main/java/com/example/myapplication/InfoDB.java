package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class InfoDB {
    private final String DATABASE_NAME = "PasswordDataDB";
    private final String TABLE_USERS = "Users";
    private final String TABLE_PASSWORDS = "Passwords";
    private final String TABLE_RECYCLE = "RecycleBin";

    private final String COLUMN_USER_ID = "_id";
    private final String COLUMN_USERNAME = "_username";
    private final String COLUMN_PASSWORD = "_password";

    private final String COLUMN_PASSWORD_ID = "_id";
    private final String COLUMN_PASSWORD_USERNAME = "_username";
    private final String COLUMN_PASSWORD_PASSWORD = "_password";
    private final String COLUMN_PASSWORD_URL = "_url";
    private final String COLUMN_USER_ID_FK = "_user_id";

    private final String COLUMN_RECYCLE_ID = "_id";
    private final String COLUMN_RECYCLE_USERNAME = "_username";
    private final String COLUMN_RECYCLE_PASSWORD = "_password";
    private final String COLUMN_RECYCLE_URL = "_url";
    private final String COLUMN_USER_ID_FKR = "_user_id";

    private final int DATABASE_VERSION = 1;

    Context context;

    Helper helper;
    SQLiteDatabase sqLiteDatabase;

    public InfoDB(Context c)
    {
        context = c;
    }

    public void open()
    {
        helper = new Helper(context, DATABASE_NAME, null, DATABASE_VERSION);
        sqLiteDatabase = helper.getWritableDatabase();
    }

    public void close()
    {
        sqLiteDatabase.close();
        helper.close();
    }

    public long addUser(String username, String password) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, username);
        cv.put(COLUMN_PASSWORD, password);

        return sqLiteDatabase.insert(TABLE_USERS, null, cv);
    }

    public boolean authenticateUser(String username, String password) {
        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = sqLiteDatabase.query(TABLE_USERS, null, selection, selectionArgs, null, null, null);
        boolean isValid = cursor.moveToFirst();
        cursor.close();
        return isValid;
    }

    public void insert(String username, String password, String url,long userId) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PASSWORD_USERNAME, username);
        cv.put(COLUMN_PASSWORD_PASSWORD, password);
        cv.put(COLUMN_PASSWORD_URL, url);
        cv.put(COLUMN_USER_ID_FK,userId);

        long temp = sqLiteDatabase.insert(TABLE_PASSWORDS, null, cv);
        if (temp == -1) {
            Toast.makeText(context, "Password not added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Password Added", Toast.LENGTH_SHORT).show();
        }
    }

    public void insertRecycle(String username, String password, String url,long userId) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_RECYCLE_USERNAME, username);
        cv.put(COLUMN_RECYCLE_PASSWORD, password);
        cv.put(COLUMN_RECYCLE_URL, url);
        cv.put(COLUMN_USER_ID_FKR,userId);

        long temp = sqLiteDatabase.insert(TABLE_RECYCLE, null, cv);
        if (temp == -1) {
            Toast.makeText(context, "Password not added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Password Added", Toast.LENGTH_SHORT).show();
        }
    }
    public long getUserIdByUsername(String username) {
        SQLiteDatabase db = helper.getReadableDatabase();
        long userId = -1;

        String[] projection = {
                COLUMN_USER_ID
        };

        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = { username };

        Cursor cursor = db.query(
                TABLE_USERS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            cursor.close();
        }
        return userId;
    }

    public void RestorePassword(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        long userId = -1;
        String username = "";
        String password = "";
        String url = "";

        String[] projection = {
                COLUMN_USER_ID_FK,
                COLUMN_RECYCLE_USERNAME,
                COLUMN_RECYCLE_PASSWORD,
                COLUMN_RECYCLE_URL
        };

        String selection = COLUMN_RECYCLE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(
                TABLE_RECYCLE,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_USER_ID_FKR));
            username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECYCLE_USERNAME));
            password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECYCLE_PASSWORD));
            url = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECYCLE_URL));
            cursor.close();
        }

        if (userId != -1) {
            insert(username, password, url, userId);
        }

        int rows = db.delete(TABLE_RECYCLE, COLUMN_RECYCLE_ID + "=?", new String[]{String.valueOf(id)});
        if (rows > 0) {
            Toast.makeText(context, "Password Restored successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Password not Restored", Toast.LENGTH_SHORT).show();
        }
    }

    public void deletePassword(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        long userId = -1;
        String username = "";
        String password = "";
        String url = "";

        String[] projection = {
                COLUMN_USER_ID_FK,
                COLUMN_PASSWORD_USERNAME,
                COLUMN_PASSWORD_PASSWORD,
                COLUMN_PASSWORD_URL
        };

        String selection = COLUMN_PASSWORD_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(
                TABLE_PASSWORDS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_USER_ID_FK));
            username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD_USERNAME));
            password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD_PASSWORD));
            url = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD_URL));
            cursor.close();
        }

        if (userId != -1) {
            insertRecycle(username, password, url, userId);
        }

        int rows = db.delete(TABLE_PASSWORDS, COLUMN_PASSWORD_ID + "=?", new String[]{String.valueOf(id)});
        if (rows > 0) {
            Toast.makeText(context, "Password deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Password not deleted", Toast.LENGTH_SHORT).show();
        }
    }

    public void deletePasswordRecycle(int id){
        int rows = sqLiteDatabase.delete(TABLE_RECYCLE, COLUMN_RECYCLE_ID+"=?", new String[]{id+""});
        if(rows > 0)
        {
            Toast.makeText(context, "Password deleted successfully", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "Password not deleted", Toast.LENGTH_SHORT).show();
        }
    }

    public void updatePassword(int id, String newUsername, String newPassword,String newUrl) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PASSWORD_USERNAME, newUsername);
        cv.put(COLUMN_PASSWORD_PASSWORD, newPassword);
        cv.put(COLUMN_PASSWORD_URL, newUrl);

        int rows = sqLiteDatabase.update(TABLE_PASSWORDS, cv, COLUMN_PASSWORD_ID+"=?", new String[]{id+""});
        if(rows>0)
        {
            Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "Failed to Update Password", Toast.LENGTH_SHORT).show();
        }
    }



    public ArrayList<Info> readAllPasswords(long userId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<Info> passwords = new ArrayList<>();

        String[] projection = {
                COLUMN_PASSWORD_ID + " AS _id",
                COLUMN_PASSWORD_USERNAME,
                COLUMN_PASSWORD_PASSWORD,
                COLUMN_PASSWORD_URL
        };

        String selection = COLUMN_USER_ID_FK + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };


        Cursor cursor = db.query(
                TABLE_PASSWORDS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id=cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD_ID));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD_USERNAME));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD_PASSWORD));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD_URL));
                Info info = new Info(id,username, password, url);
                passwords.add(info);
            }
            cursor.close();
        }

        return passwords;
    }

    public ArrayList<Info> readAllRecyclePasswords(long userId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<Info> passwords = new ArrayList<>();

        String[] projection = {
                COLUMN_RECYCLE_ID + " AS _id",
                COLUMN_RECYCLE_USERNAME,
                COLUMN_RECYCLE_PASSWORD,
                COLUMN_RECYCLE_URL
        };

        String selection = COLUMN_USER_ID_FKR + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };


        Cursor cursor = db.query(
                TABLE_RECYCLE,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id=cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RECYCLE_ID));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECYCLE_USERNAME));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECYCLE_PASSWORD));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECYCLE_URL));
                Info info = new Info(id,username, password, url);
                passwords.add(info);
            }
            cursor.close();
        }

        return passwords;
    }





    private class Helper extends SQLiteOpenHelper
    {
        public Helper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createUsers = "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT UNIQUE NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL)";
            db.execSQL(createUsers);

            String createPasswords = "CREATE TABLE " + TABLE_PASSWORDS + " (" +
                    COLUMN_PASSWORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PASSWORD_USERNAME + " TEXT NOT NULL, " +
                    COLUMN_PASSWORD_PASSWORD + " TEXT NOT NULL, " +
                    COLUMN_PASSWORD_URL + " TEXT NOT NULL, " +
                    COLUMN_USER_ID_FK + " INTEGER, " +
                    "FOREIGN KEY (" + COLUMN_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
            db.execSQL(createPasswords);

            String createRecycle = "CREATE TABLE " + TABLE_RECYCLE + " (" +
                    COLUMN_RECYCLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RECYCLE_USERNAME + " TEXT NOT NULL, " +
                    COLUMN_RECYCLE_PASSWORD + " TEXT NOT NULL, " +
                    COLUMN_RECYCLE_URL + " TEXT NOT NULL, " +
                    COLUMN_USER_ID_FKR + " INTEGER, " +
                    "FOREIGN KEY (" + COLUMN_USER_ID_FKR + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
            db.execSQL(createRecycle);}

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECYCLE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSWORDS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }
    }

}
