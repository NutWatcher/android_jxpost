package com.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.model.Account;

import java.util.List;

/**
 * Created by 扬洋 on 2015/8/17.
 */
public class DB_Account extends SQLiteOpenHelper {

    private static final String DB_Name = "jx_post";
    private static final int version = 1; //数据库版本

    public DB_Account(Context context) {
        super(context, DB_Name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists Clear_Account (ID integer " +
                "primary key autoincrement not null," +
                "extUserId varchar(30),customerName varchar(100),balance varchar(100))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}

