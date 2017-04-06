package com.example.feng.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by feng on 16/6/20.
 */
public class MyDataBase extends SQLiteOpenHelper {
    public static final String CREATE_MEMO = "create table Memo(" +
            "memoId integer primary key autoincrement," +
            "memoContent text," +
            "memoTime varchar(20)," +
            "memoVersion int)";
    public static final String CREATE_DIARY = "create table Diary(" +
            "diaryId integer primary key autoincrement," +
            "diaryContent text," +
            "diaryTime varchar(20)," +
            "diaryWeather varchar(6)," +
            "diaryMood varchar(6)," +
            "diaryVersion int)";
    public static final String CREATE_PASSWORD = "create table Passworld(" +
            "userName varchar(20)," +
            "userId integer,"+
            "passWord varchar(20)," +
            "updateTime varchar(20)" ;
    public static final String CREATE_NOTIFY = "create table Notify(" +
            "notifyId integer primary key autoincrement," +
            "notifyContent text," +
            "notifyTime varchar(20)," +
            "notifyType varchar(6)," +
            "notifyLing int)";


    public MyDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MyDataBase(Context context){
        super(context,"Note.db",null,1);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DIARY);
        db.execSQL(CREATE_MEMO);
        db.execSQL(CREATE_PASSWORD);
        db.execSQL(CREATE_NOTIFY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
