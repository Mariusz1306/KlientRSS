package com.projekt.klientrss.database;

public class Channel {
    public static final String TABLE_NAME = "channels";

    public static final String COLUMN_ID = "channelId";
    public static final String COLUMN_CATID = "catId";
    public static final String COLUMN_NAME = "channelName";
    public static final String COLUMN_URL = "channelUrl";

    private int id;
    private int catId;
    private String name;
    private String url;

    public Channel() {
    }

    public Channel(int id, int catId, String name, String url) {
        this.id = id;
        this.catId = catId;
        this.name = name;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }


    public String getName() {
        return name;
    }

    public void setName(String note) {
        this.name = note;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
