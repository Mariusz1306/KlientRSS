package com.projekt.klientrss.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class FollowAdapter{
    protected static final String TAG = "FollowAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    public FollowAdapter(Context context)
    {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    public FollowAdapter createDatabase() throws SQLException{
        try {
            mDbHelper.createDataBase();
        }catch (IOException mIOException){
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public FollowAdapter open() throws SQLException
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


    public Follow getFollowById( long id ) {
        // get readable database as we are not inserting anything

        Follow note = null;
        SQLiteDatabase db = mDb;

        Cursor cursor = db.query(Follow.TABLE_NAME,
                new String[]{Follow.COLUMN_ID, Follow.COLUMN_CHANNELID, Follow.COLUMN_DATE},
                Follow.COLUMN_CHANNELID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            note = new Follow(
                    cursor.getInt(cursor.getColumnIndex(Follow.COLUMN_ID)),
                    cursor.getInt(cursor.getColumnIndex(Follow.COLUMN_CHANNELID)),
                    cursor.getString(cursor.getColumnIndex(Follow.COLUMN_DATE)));
        }
        // close the db connection
        db.close();

        return note;
    }

    public List<Follow> getAllFollows() {
        List<Follow> notes = null;

        String selectQuery = "SELECT  * FROM " + Follow.TABLE_NAME + " ORDER BY " + Follow.COLUMN_DATE + " DESC";

        SQLiteDatabase db = mDb;
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            notes = new ArrayList<>();
            do {
                Follow note = new Follow(
                        cursor.getInt(cursor.getColumnIndex(Follow.COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndex(Follow.COLUMN_CHANNELID)),
                        cursor.getString(cursor.getColumnIndex(Follow.COLUMN_DATE)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public void addFollow( Follow entry ){

        String sql = "INSERT INTO followed( channelId ) VALUES (?)";

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( Follow.COLUMN_CHANNELID, entry.getChannelId() );

        db.insert( Follow.TABLE_NAME, null, values);
        db.close();
    }


    public void removeFollowById( int id ){

        String sql = "DELETE FROM " + Follow.TABLE_NAME + " WHERE " + Follow.COLUMN_CHANNELID + " = " + id + ";";

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL( sql );

        db.close();
    }


    public int getFollowsCount() {
        String countQuery = "SELECT  * FROM " + Follow.TABLE_NAME;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        db.close();


        // return count
        return count;
    }

}
