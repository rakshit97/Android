package com.example.rakshit.pets.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class PetsContract
{
    public static final String CONTENT_AUTHORITY = "com.example.rakshit.pets";
    public static final String SCHEME = "content://";
    public static final String PETS_PATH = "pets";
    public static final Uri BASE_URI = Uri.parse(SCHEME + CONTENT_AUTHORITY);
    public static final String MIME_LIST = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PETS_PATH;
    public static final String MIME_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PETS_PATH;

    public static final class tableCols implements BaseColumns
    {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PETS_PATH);
        public static final String TABLE_NAME = "pets";
        public static final String COL_ID = BaseColumns._ID;
        public static final String COL_NAME = "name";
        public static final String COL_BREED = "breed";
        public static final String COL_GENDER = "gender";
        public static final String COL_WEIGHT = "weight";

        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
    }
}
