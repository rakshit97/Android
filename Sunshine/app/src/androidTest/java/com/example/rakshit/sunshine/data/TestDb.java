package com.example.rakshit.sunshine.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

import static com.example.rakshit.sunshine.data.TestUtilities.TEST_LOCATION;

public class TestDb extends AndroidTestCase
{

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    void deleteTheDatabase()
    {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
    }

    public void setUp()
    {
        deleteTheDatabase();
    }

    /*
        Students: Uncomment this test once you've written the code to create the Location
        table.  Note that you will have to have chosen the same column names that I did in
        my solution for this test to compile, so if you haven't yet done that, this is
        a good time to change your column names to match mine.
        Note that this only tests that the Location table has the correct columns, since we
        give you the code for the weather table.  This test does not look at the
     */
    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(WeatherContract.LocationEntries.TABLE_NAME);
        tableNameHashSet.add(WeatherContract.WeatherEntries.TABLE_NAME);

        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new WeatherDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + WeatherContract.LocationEntries.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(WeatherContract.LocationEntries._ID);
        locationColumnHashSet.add(WeatherContract.LocationEntries.COLUMN_CITY);
        locationColumnHashSet.add(WeatherContract.LocationEntries.COLUMN_LATITUDE);
        locationColumnHashSet.add(WeatherContract.LocationEntries.COLUMN_LONGITUDE);
        locationColumnHashSet.add(WeatherContract.LocationEntries.COLUMN_LOCATION);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                locationColumnHashSet.isEmpty());
        db.close();
    }

    /*
        Students:  Here is where you will build code to test that we can insert and query the
        location database.  We've done a lot of work for you.  You'll want to look in TestUtilities
        where you can uncomment out the "createNorthPoleLocationValues" function.  You can
        also make use of the ValidateCurrentRecord function from within TestUtilities.
    */
    public void testLocationTable() {
        // First step: Get reference to writable database
        SQLiteDatabase db = new WeatherDbHelper(this.mContext).getWritableDatabase();

        // Create ContentValues of what you want to insert
        // (you can use the createNorthPoleLocationValues if you wish)
        ContentValues testValues = new ContentValues();
        testValues.put(WeatherContract.LocationEntries.COLUMN_LOCATION, TEST_LOCATION);
        testValues.put(WeatherContract.LocationEntries.COLUMN_CITY, "North Pole");
        testValues.put(WeatherContract.LocationEntries.COLUMN_LATITUDE, 64.7488);
        testValues.put(WeatherContract.LocationEntries.COLUMN_LONGITUDE, -147.353);

        // Insert ContentValues into database and get a row ID back
        long row_id = db.insert(WeatherContract.LocationEntries.TABLE_NAME, null, testValues);
        assertTrue(row_id!=-1);

        // Query the database and receive a Cursor back
        Cursor c = db.query(WeatherContract.LocationEntries.TABLE_NAME, null, null, null, null, null, null);

        // Move the cursor to a valid database row
        assertTrue("Error: no data found", c.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: Incorrect data", c, testValues);

        // Finally, close the cursor and database
        c.close();
        db.close();

    }

    /*
        Students:  Here is where you will build code to test that we can insert and query the
        database.  We've done a lot of work for you.  You'll want to look in TestUtilities
        where you can use the "createWeatherValues" function.  You can
        also make use of the validateCurrentRecord function from within TestUtilities.
     */
    public void testWeatherTable()
    {
        SQLiteDatabase db = new WeatherDbHelper(this.mContext).getWritableDatabase();
        long id = insertLocation(db);
        assertTrue(id!=-1);

        // Query the database and receive a Cursor back
        Cursor c = db.query(WeatherContract.WeatherEntries.TABLE_NAME, null, null, null, null, null, null);

        // Move the cursor to a valid database row
        assertTrue("Error: no data found", c.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: Incorrect data", c, TestUtilities.createWeatherValues(1));

        // Finally, close the cursor and database
        c.close();
        db.close();
    }


    /*
        Students: This is a helper method for the testWeatherTable quiz. You can move your
        code from testLocationTable to here so that you can call this code from both
        testWeatherTable and testLocationTable.
     */
    public long insertLocation(SQLiteDatabase db)
    {

        ContentValues testValues = TestUtilities.createWeatherValues(1);
        return db.insert(WeatherContract.WeatherEntries.TABLE_NAME, null, testValues);
    }
}
