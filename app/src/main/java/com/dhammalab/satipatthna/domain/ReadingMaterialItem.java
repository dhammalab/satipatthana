package com.dhammalab.satipatthna.domain;

/**
 * Created by anthony.lipscomb on 10/10/2014.
 */
public class ReadingMaterialItem {
    private String name;
    private int pages;
    private String author;
    private String url;

    public ReadingMaterialItem() {}

    public ReadingMaterialItem(String name, int pages, String author, String url) {
        this.name = name;
        this.pages = pages;
        this.author = author;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
