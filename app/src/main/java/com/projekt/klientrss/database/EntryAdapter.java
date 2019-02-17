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

public class EntryAdapter{
    protected static final String TAG = "EntryAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    public EntryAdapter(Context context)
    {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    public EntryAdapter createDatabase() throws SQLException{
        try {
            mDbHelper.createDataBase();
        }catch (IOException mIOException){
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public EntryAdapter open() throws SQLException
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


    public Entry getEntryById( long id ) {
        // get readable database as we are not inserting anything

        Entry note = null;
        SQLiteDatabase db = mDb;

        Cursor cursor = db.query(Entry.TABLE_NAME,
                new String[]{Entry.COLUMN_ID, Entry.COLUMN_NAME, Entry.COLUMN_DESCRIPTION, Entry.COLUMN_LINK, Entry.COLUMN_CHANNELID, Entry.COLUMN_DATE},
                Entry.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            note = new Entry(
                    cursor.getInt(cursor.getColumnIndex(Entry.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(Entry.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(Entry.COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(Entry.COLUMN_LINK)),
                    cursor.getInt(cursor.getColumnIndex(Entry.COLUMN_CHANNELID)),
                    cursor.getString(cursor.getColumnIndex(Entry.COLUMN_DATE)));
        }
        // close the db connection
        db.close();

        return note;
    }

    public List<Entry> getAllEntries() {
        List<Entry> notes = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Entry.TABLE_NAME + " ORDER BY " + Entry.COLUMN_DATE + " DESC";

        SQLiteDatabase db = mDb;
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Entry note = new Entry();
                note.setId(cursor.getInt(cursor.getColumnIndex(Entry.COLUMN_ID)));
                note.setName(cursor.getString(cursor.getColumnIndex(Entry.COLUMN_NAME)));
                note.setDescription(cursor.getString(cursor.getColumnIndex(Entry.COLUMN_DESCRIPTION)));
                note.setLink(cursor.getString(cursor.getColumnIndex(Entry.COLUMN_LINK)));
                note.setChannelId( cursor.getInt(cursor.getColumnIndex(Entry.COLUMN_CHANNELID)) );
                note.setDate(cursor.getString(cursor.getColumnIndex(Entry.COLUMN_DATE)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public void addEntry( Entry entry ){

        String sql = "INSERT INTO entries(entryName, entryDescription, entryLink, entryChannelId) VALUES (?, ?, ?, ?)";

        SQLiteDatabase db = mDb;
        ContentValues values = new ContentValues();
        values.put( Entry.COLUMN_NAME, entry.getName() );
        values.put( Entry.COLUMN_DESCRIPTION, entry.getDescription() );
        values.put( Entry.COLUMN_LINK, entry.getLink() );
        values.put( Entry.COLUMN_CHANNELID, entry.getChannelId() );

        db.insert( Entry.TABLE_NAME, null, values);
        db.close();

    }


    public void removeEntryById( int id ){

        String sql = "DELETE FROM " + Entry.TABLE_NAME + " WHERE " + Entry.COLUMN_ID + " = " + id + ";";

        SQLiteDatabase db = mDb;

        db.execSQL( sql );
        db.close();

    }


    public int getEntriesCount() {
        String countQuery = "SELECT  * FROM " + Entry.TABLE_NAME;
        SQLiteDatabase db = mDb;
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        db.close();


        // return count
        return count;
    }

}
