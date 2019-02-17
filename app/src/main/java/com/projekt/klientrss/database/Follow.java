package com.projekt.klientrss.database;

public class Follow {
    public static final String TABLE_NAME = "followed";

    public static final String COLUMN_ID = "followId";
    public static final String COLUMN_CHANNELID = "channelId";
    public static final String COLUMN_DATE= "followDate";

    private int id;
    private int channelId;
    private String date;

    public Follow() {
    }

    public Follow(int id,  int channelId, String date ) {
        this.id = id;
        this.channelId = channelId;
        this.date = date;
    }

    public int getId() {
        return this.id;
    }

    public void setId( int id ){ this.id = id; }

    public int getChannelId() {
        return this.channelId;
    }

    public void setChannelId( int channelId ){ this.channelId = channelId; }

    public String getDate() {
        return this.date;
    }

    public void setDate( String date ){ this.date = date; }

}
