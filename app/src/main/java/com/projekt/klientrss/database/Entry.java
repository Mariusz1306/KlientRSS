package com.projekt.klientrss.database;

public class Entry {
    public static final String TABLE_NAME = "entries";

    public static final String COLUMN_ID = "entryId";
    public static final String COLUMN_NAME = "entryName";
    public static final String COLUMN_DESCRIPTION = "entryDescription";
    public static final String COLUMN_LINK = "entryLink";
    public static final String COLUMN_CHANNELID = "entryChannelId";
    public static final String COLUMN_DATE = "entryDate";

    private int id;
    private String name;
    private String link;
    private String description;
    private int channelId;
    private String date;

    public Entry() {
    }

    public Entry(int id, String name, String description, String link, int channelId, String date ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.link = link;
        this.channelId = channelId;
        this.date = date;
    }

    public int getId() {
        return this.id;
    }

    public void setId( int id ){ this.id = id; }

    public String getName() {
        return this.name;
    }

    public void setName( String name ){ this.name = name; }

    public String getLink() {
        return this.link;
    }

    public void setLink( String link ){ this.link = link; }

    public String getDescription() {
        return this.description;
    }

    public void setDescription( String description ){ this.description = description; }

    public int getChannelId() {
        return this.channelId;
    }

    public void setChannelId( int channelId ){ this.channelId = channelId; }

    public String getDate() {
        return this.date;
    }

    public void setDate( String date ){ this.date = date; }

}
