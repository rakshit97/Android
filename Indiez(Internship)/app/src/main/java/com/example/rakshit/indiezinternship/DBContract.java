package com.example.rakshit.indiezinternship;

import android.net.Uri;
import android.provider.BaseColumns;

public class DBContract
{
    public static final String CONTENT_AUTHORITY = "com.example.rakshit.indiezinternship";
    public static final String SCHEME = "content://";
    public static final String PETS_PATH = "words";
    public static final Uri BASE_URI = Uri.parse(SCHEME + CONTENT_AUTHORITY);

    public static final class tableCols implements BaseColumns
    {
        public static final String TABLE = "words";
        public static final String COL_ID = BaseColumns._ID;
        public static final String COL_WORD = "WORD";
    }
}
