package com.example.rakshit.pets.data;

import android.provider.BaseColumns;

public final class PetsContract
{
    public static final class tableCols implements BaseColumns
    {
        public static final String TABLE_NAME = "pets";
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COL_NAME = "name";
        public static final String COL_BREED = "breed";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_WEIGHT = "weight";

        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
    }
}
