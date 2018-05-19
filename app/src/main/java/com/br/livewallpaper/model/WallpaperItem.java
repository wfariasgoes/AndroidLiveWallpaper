package com.br.livewallpaper.model;

public class WallpaperItem {

    public String imageUrl;
    public String categoryId;
    public long viewCount;

    public WallpaperItem() {
    }


    public WallpaperItem(String imageUrl, String categoryId) {
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getImageLink() {
        return imageUrl;
    }

    public void setImageLink(String imageLink) {
        this.imageUrl = imageLink;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }
}
