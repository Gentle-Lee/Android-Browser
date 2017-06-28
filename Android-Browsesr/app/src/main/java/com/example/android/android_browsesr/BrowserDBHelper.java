package com.example.android.android_browsesr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tommy on 2017/6/27.
 */

public class BrowserDBHelper extends SQLiteOpenHelper {

    private final String[] TABLE_NAME = {"BOOKMARK_DB","HISTORY_DB","DOWNLOADED_DB","DOWNLOADMANAGER_DB"};

    public BrowserDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String bookmarkSql = "create table if not exists " + TABLE_NAME[0] + " (Id integer primary key,BookmarkName text,BookmarkUrl text)";
        String historySql = "create table if not exists " + TABLE_NAME[1] + " (Id integer primary key, HistoryName text,HistoryUrl text)";
        String downloaedSql = "create table if not exists " + TABLE_NAME[2] + " (Id integer primary key, FileName text,FilePath text)";
        String downloadManagerdSql = "create table if not exists " + TABLE_NAME[3] + " (Id integer primary key, FileName text,FilePath text,)";
        sqLiteDatabase.execSQL(bookmarkSql);
        sqLiteDatabase.execSQL(historySql);
        sqLiteDatabase.execSQL(downloaedSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        for (int j = 0;i<TABLE_NAME.length;j++){
            String sql = "DROP TABLE IF EXISTS " + TABLE_NAME[j];
            sqLiteDatabase.execSQL(sql);
        }
        onCreate(sqLiteDatabase);
    }

}
