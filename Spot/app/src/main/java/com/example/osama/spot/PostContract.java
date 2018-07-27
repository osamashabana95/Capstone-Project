package com.example.osama.spot;

import android.net.Uri;
import android.provider.BaseColumns;

public class PostContract {
    public static final  String AUTHORITY ="com.example.osama.spot";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    public static final  String PATH_POSTS ="posts";

    public static final class PostEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_POSTS).build();
        public static final String TABLE_NAME = "posts";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_NAME = "subreddit_name";
        public static final String COLUMN_PATH = "thumbnail_path";

    }
}
