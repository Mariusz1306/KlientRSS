package com.projekt.klientrss.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter {
    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    public CategoryAdapter(Context context)
    {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    public CategoryAdapter createDatabase() throws SQLException
    {
        try
        {
            mDbHelper.createDataBase();
        }
        catch (IOException mIOException)
        {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public CategoryAdapter open() throws SQLException
    {
        try
        {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close()
    {
        mDbHelper.close();
    }


    public List<Category> getCategory(long id) {
        // get readable database as we are not inserting anything
        List<Category> notes = new ArrayList<>();
        SQLiteDatabase db = mDb;

        Cursor cursor = db.query(Category.TABLE_NAME,
                new String[]{Category.COLUMN_CATID, Category.COLUMN_NAME, Category.COLUMN_URL},
                Category.COLUMN_CATID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Category note = new Category(
                cursor.getInt(cursor.getColumnIndex(Category.COLUMN_CATID)),
                cursor.getString(cursor.getColumnIndex(Category.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(Category.COLUMN_URL)));

        notes.add(note);

        // close the db connection
        db.close();

        return notes;
    }

    public Category getCategoryByName( String name ) {
        // get readable database as we are not inserting anything

        String selectQuery = "SELECT  * FROM categories WHERE catName LIKE \'" + name + "\' ORDER BY " +
                Category.COLUMN_NAME + " ASC";

        SQLiteDatabase db = mDb;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Category note = new Category(
                cursor.getInt(cursor.getColumnIndex(Category.COLUMN_CATID)),
                cursor.getString(cursor.getColumnIndex(Category.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(Category.COLUMN_URL)));


        // close the db connection
        db.close();

        return note;
    }

    public List<Category> getAllCategories() {
        List<Category> notes = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Category.TABLE_NAME + " ORDER BY " +
                Category.COLUMN_NAME + " ASC";

        SQLiteDatabase db = mDb;
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Category note = new Category();
                note.setCatId(cursor.getInt(cursor.getColumnIndex(Category.COLUMN_CATID)));
                note.setName(cursor.getString(cursor.getColumnIndex(Category.COLUMN_NAME)));
                note.setUrl(cursor.getString(cursor.getColumnIndex(Category.COLUMN_URL)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
       //db.close();

        // return notes list
        return notes;
    }

    public int getCategoryCount() {
        String countQuery = "SELECT  * FROM " + Category.TABLE_NAME;
        SQLiteDatabase db = mDb;
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
}
