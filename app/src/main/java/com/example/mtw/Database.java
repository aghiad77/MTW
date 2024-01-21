package com.example.mtw;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MTW_DB";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try
        {

            db.execSQL("CREATE TABLE user\n" +
                    "(\n" +
                    "userName TEXT Not NULL,\n" +
                    "email TEXT NOT NULL,\n" +
                    "phoneNumber TEXT NOT NULL,\n" +
                    "code TEXT NOT NULL\n" +
                    ");");

        } catch (Exception ex)
        {
            //Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try
        {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS user");

            // Create tables again
            onCreate(db);

        } catch (Exception ex) {

        }
    }

    public void saveLogin(String name, String email,String phone, String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            Cursor cursor = db.rawQuery("SELECT userName FROM user WHERE userName=?", new String[]{name});
            ContentValues values = new ContentValues();
            if (cursor == null || cursor.getCount() == 0 )
            {
                values.put("userName", name);
                values.put("email", email);
                values.put("phoneNumber", phone);
                values.put("code", code);
                db.insert("user", null, values);
            }
        } catch (Exception ex) {

        }
        db.close();
    }
    public boolean getIsUserLogged() {
        String result = getSetting();

        if (result == "" || result == null || !result.equals("logged"))
            return false;
        else
            return true;
    }
    public String getSetting() {

        SQLiteDatabase db = this.getWritableDatabase();
        String result="";
        try
        {
            Cursor cursor = db.rawQuery("SELECT userName FROM user",null);

            if (cursor == null || cursor.getCount() == 0 || !cursor.moveToFirst())
                return "";
            else
            {
                result = "logged";
            }
        } catch (Exception ex) {

        }

        db.close();
        return result;
    }
    public String getUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        String result="";
        try
        {
            Cursor cursor = db.rawQuery("SELECT userName FROM user",null);

            if (cursor == null || cursor.getCount() == 0 || !cursor.moveToFirst())
                return "";
            else
            {
                result = cursor.getString(0);
            }
        } catch (Exception ex) {

        }
        db.close();
        return result;
    }
    public String getEmail(){
        SQLiteDatabase db = this.getWritableDatabase();
        String result="";
        try
        {
            Cursor cursor = db.rawQuery("SELECT email FROM user",null);

            if (cursor == null || cursor.getCount() == 0 || !cursor.moveToFirst())
                return "";
            else
            {
                result = cursor.getString(0);
            }
        } catch (Exception ex) {

        }
        db.close();
        return result;
    }
    public String getphonenumber(){
        SQLiteDatabase db = this.getWritableDatabase();
        String result="";
        try
        {
            Cursor cursor = db.rawQuery("SELECT phoneNumber FROM user",null);

            if (cursor == null || cursor.getCount() == 0 || !cursor.moveToFirst())
                return "";
            else
            {
                result = cursor.getString(0);
            }
        } catch (Exception ex) {

        }
        db.close();
        return result;
    }

}
