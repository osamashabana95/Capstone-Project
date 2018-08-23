package com.example.osama.spot.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.osama.spot.database.PostContract;

public class PostDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "postsDb.db";


    private static final int VERSION = 1;

    public PostDBHelper(Context context) {
        super(context, DATABASE_NAME, null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE = "CREATE TABLE "  + PostContract.PostEntry.TABLE_NAME + " (" +
                PostContract.PostEntry._ID                + " INTEGER PRIMARY KEY, " +
                PostContract.PostEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                PostContract.PostEntry.COLUMN_TITLE    + " TEXT NOT NULL,"+
                PostContract.PostEntry.COLUMN_PATH + " TEXT ); " ;
        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PostContract.PostEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
