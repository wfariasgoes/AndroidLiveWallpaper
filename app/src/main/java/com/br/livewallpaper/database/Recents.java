package com.br.livewallpaper.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

@Entity(tableName = "recents", primaryKeys = {"imageLink","categoryId"})
public class Recents {

    @ColumnInfo(name = "imageLink")
    @NonNull
    private String imageLink;

    @ColumnInfo(name = "categoryId")
    @NonNull
    private String categoryId;

    @ColumnInfo(name = "saveTime")
    @NonNull
    private String saveTime;

    public Recents(@NonNull String imageLink, @NonNull String categoryId, @NonNull String saveTime) {
        this.imageLink = imageLink;
        this.categoryId = categoryId;
        this.saveTime = saveTime;
    }

    @NonNull
    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(@NonNull String imageLink) {
        this.imageLink = imageLink;
    }

    @NonNull
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@NonNull String categoryId) {
        this.categoryId = categoryId;
    }

    @NonNull
    public String getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(@NonNull String saveTime) {
        this.saveTime = saveTime;
    }

}
