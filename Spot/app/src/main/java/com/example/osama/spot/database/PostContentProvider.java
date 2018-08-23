package com.example.osama.spot.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.osama.spot.database.PostContract;
import com.example.osama.spot.database.PostDBHelper;

import static com.example.osama.spot.database.PostContract.PostEntry.TABLE_NAME;

public class PostContentProvider extends ContentProvider {

    public static final int POSTS = 100;
    public static final int POSTS_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private PostDBHelper mPostDBHelper;

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher =new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PostContract.AUTHORITY,PostContract.PATH_POSTS,POSTS);
        uriMatcher.addURI(PostContract.AUTHORITY,PostContract.PATH_POSTS +"/#",POSTS_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mPostDBHelper = new PostDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        final SQLiteDatabase db= mPostDBHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);

        Cursor retCursor;
        switch (match){
            case POSTS:
                retCursor = db.query(TABLE_NAME,null,s,null,null,null, s1);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db= mPostDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case POSTS:
                long id = db.insert(TABLE_NAME,null,contentValues);
                if(id>0){
                    returnUri = ContentUris.withAppendedId(PostContract.PostEntry.CONTENT_URI,id);
                }else {
                    throw new android.database.SQLException("Faild to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = mPostDBHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int res;
        switch (match){
            case POSTS:
                res =  db.delete(TABLE_NAME, s, strings);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


        getContext().getContentResolver().notifyChange(uri, null);
        return res;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
