package com.example.rakshit.internshalainternship.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class UsersContract
{
    public static final String CONTENT_AUTHORITY = "com.example.rakshit.internshalainternship";
    public static final String SCHEME = "content://";
    public static final String PATH_USERS = "users";
    public static final String PATH_NOTES = "notes";
    public static final Uri BASE_URI = Uri.parse(SCHEME + CONTENT_AUTHORITY);

    public static final String COL_ID = BaseColumns._ID;
    public static final String COL_USERNAME = "username";

    public static final String MIME_LIST_USERS = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+ PATH_USERS;
    public static final String MIME_ITEM_USERS = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+ PATH_USERS;
    public static final String MIME_LIST_NOTES = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+ PATH_NOTES;
    public static final String MIME_ITEM_NOTES = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+ PATH_NOTES;

    public static final class UserEntries implements BaseColumns
    {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PATH_USERS);
        public static final String TABLE_NAME = "users";
        public static final String COL_PASSWORD = "password";
    }

    public static final class NotesEntries implements BaseColumns
    {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PATH_NOTES);
        public static final String TABLE_NAME = "notes";
        public static final String COL_TOPIC = "topic";
        public static final String COL_TEXT = "text";
    }
}
