package com.projekt.klientrss.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChannelsAdapter{
    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    public ChannelsAdapter(Context context)
    {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    public ChannelsAdapter createDatabase() throws SQLException
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

    public ChannelsAdapter open() throws SQLException
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


    public Channel getChannel(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.query(Channel.TABLE_NAME,
                new String[]{Channel.COLUMN_ID, Channel.COLUMN_CATID, Channel.COLUMN_NAME, Channel.COLUMN_URL},
                Channel.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        Channel note = null;

        if (cursor != null) {
            cursor.moveToFirst();

            // prepare note object
            note = new Channel(
                    cursor.getInt(cursor.getColumnIndex(Channel.COLUMN_ID)),
                    cursor.getInt(cursor.getColumnIndex(Channel.COLUMN_CATID)),
                    cursor.getString(cursor.getColumnIndex(Channel.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(Channel.COLUMN_URL)));
        }

        // close the db connection
        db.close();

        return note;
    }

    public List<Channel> getAllChannels( int catId ) {
        List<Channel> notes = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Channel.TABLE_NAME + " WHERE " + Channel.COLUMN_CATID + " = " + Integer.toString( catId ) + " ORDER BY " +
                Channel.COLUMN_NAME + " DESC";

        SQLiteDatabase db = mDb;
            Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Channel note = new Channel();
                note.setId(cursor.getInt(cursor.getColumnIndex(Channel.COLUMN_ID)));
                note.setCatId(cursor.getInt(cursor.getColumnIndex(Channel.COLUMN_CATID)));
                note.setName(cursor.getString(cursor.getColumnIndex(Channel.COLUMN_NAME)));
                note.setUrl(cursor.getString(cursor.getColumnIndex(Channel.COLUMN_URL)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + Channel.TABLE_NAME;
        SQLiteDatabase db = mDb;
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        db.close();


        // return count
        return count;
    }

}
