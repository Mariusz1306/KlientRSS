package com.projekt.klientrss.database;

public class Category {
    public static final String TABLE_NAME = "categories";

    public static final String COLUMN_CATID = "catId";
    public static final String COLUMN_NAME = "catName";
    public static final String COLUMN_URL = "catType";

    private int catId;
    private String name;
    private String url;

    public Category() {
    }

    public Category(int catId, String name, String url) {
        this.catId = catId;
        this.name = name;
        this.url = url;
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
