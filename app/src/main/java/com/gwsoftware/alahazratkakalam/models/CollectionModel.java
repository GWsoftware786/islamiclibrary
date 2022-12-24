package com.gwsoftware.alahazratkakalam.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CollectionModel {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "author")
    private String author;

    @ColumnInfo(name = "pdf_category")
    private String pdf_category;

    @ColumnInfo(name = "pdf_name")
    private String pdf_name;

    @ColumnInfo(name = "pdf_pages")
    private String pdf_pages;
    @ColumnInfo(name = "pdf_size")
    private String pdf_size;
    @ColumnInfo(name = "language")
    private String language;
    @ColumnInfo(name = "pdf_url")
    private String pdf_url;
    @ColumnInfo(name = "pdf_thumb")
    private String pdf_thumb;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPdf_category() {
        return pdf_category;
    }

    public void setPdf_category(String pdf_category) {
        this.pdf_category = pdf_category;
    }

    public String getPdf_name() {
        return pdf_name;
    }

    public void setPdf_name(String pdf_name) {
        this.pdf_name = pdf_name;
    }

    public String getPdf_pages() {
        return pdf_pages;
    }

    public void setPdf_pages(String pdf_pages) {
        this.pdf_pages = pdf_pages;
    }

    public String getPdf_size() {
        return pdf_size;
    }

    public void setPdf_size(String pdf_size) {
        this.pdf_size = pdf_size;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPdf_url() {
        return pdf_url;
    }

    public void setPdf_url(String pdf_url) {
        this.pdf_url = pdf_url;
    }

    public String getPdf_thumb() {
        return pdf_thumb;
    }

    public void setPdf_thumb(String pdf_thumb) {
        this.pdf_thumb = pdf_thumb;
    }
}
