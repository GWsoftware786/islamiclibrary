package com.gwsoftware.alahazratkakalam.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class DataObjectModel implements Serializable {

    HashMap<String, ArrayList<Pdf>> pdf;
    HashMap<String, ArrayList<HamaraIslam>> hamaraislam;
    HashMap<String, ArrayList<HamaraIslam>> naats;
    ArrayList<Quraan> quran;
    ArrayList<String> categories;

    public HashMap<String, ArrayList<HamaraIslam>> getNaats() {
        return naats;
    }

    public void setNaats(HashMap<String, ArrayList<HamaraIslam>> naats) {
        this.naats = naats;
    }

    public HashMap<String, ArrayList<HamaraIslam>> getHamaraislam() {
        return hamaraislam;
    }

    public void setHamaraislam(HashMap<String, ArrayList<HamaraIslam>> hamaraislam) {
        this.hamaraislam = hamaraislam;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public HashMap<String, ArrayList<Pdf>> getPdf() {
        return pdf;
    }


    public void setPdf(HashMap<String, ArrayList<Pdf>> pdf) {
        this.pdf = pdf;
    }

    public ArrayList<Quraan> getQuraan() {
        return quran;
    }

    public void setQuraan(ArrayList<Quraan> quraan) {
        this.quran = quraan;
    }


    public class PdfCategories implements Serializable {
    }


    public class Pdf implements Serializable {
        String pdfId;
        String pdf_url;
        String pdf_name;
        String pdf_size;
        String author;
        String pdf_category;
        String pdf_pages;
        String pdf_thumb;
        String language;
        String category1;

        public String getCategory1() {
            return category1;
        }

        public void setCategory1(String category1) {
            this.category1 = category1;
        }

        public String getPdfId() {
            return pdfId;
        }

        public void setPdfId(String pdfId) {
            this.pdfId = pdfId;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getPdf_thumb() {
            return pdf_thumb;
        }

        public void setPdf_thumb(String pdf_thumb) {
            this.pdf_thumb = pdf_thumb;
        }

        public String getPdf_url() {
            return pdf_url;
        }

        public void setPdf_url(String pdf_url) {
            this.pdf_url = pdf_url;
        }

        public String getPdf_name() {
            return pdf_name;
        }

        public void setPdf_name(String pdf_name) {
            this.pdf_name = pdf_name;
        }

        public String getPdf_size() {
            return pdf_size;
        }

        public void setPdf_size(String pdf_size) {
            this.pdf_size = pdf_size;
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

        public String getPdf_pages() {
            return pdf_pages;
        }

        public void setPdf_pages(String pdf_pages) {
            this.pdf_pages = pdf_pages;
        }
    }

    public class Quraan implements Serializable {

        String para_name;
        String para_number;
        String id;
        String downloadIrl;
        String thumbImage;
        String para_size;
        String pdf_pages;
        String pdf_category;
        String author;

        public String getPdf_category() {
            return pdf_category;
        }

        public void setPdf_category(String pdf_category) {
            this.pdf_category = pdf_category;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getPdf_pages() {
            return pdf_pages;
        }

        public void setPdf_pages(String pdf_pages) {
            this.pdf_pages = pdf_pages;
        }

        public String getPara_size() {
            return para_size;
        }

        public void setPara_size(String para_size) {
            this.para_size = para_size;
        }

        public String getDownloadIrl() {
            return downloadIrl;
        }

        public void setDownloadIrl(String downloadIrl) {
            this.downloadIrl = downloadIrl;
        }

        public String getThumbImage() {
            return thumbImage;
        }

        public void setThumbImage(String thumbImage) {
            this.thumbImage = thumbImage;
        }

        public String getPara_name() {
            return para_name;
        }

        public void setPara_name(String para_name) {
            this.para_name = para_name;
        }

        public String getPara_number() {
            return para_number;
        }

        public void setPara_number(String para_number) {
            this.para_number = para_number;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public class HamaraIslam implements Serializable {
        String name;
        String duration;
        String url;
        String size;
        String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }
    }

    public class Calender implements Serializable {
        String pdfId;
        String pdf_url;
        String pdf_name;
        String pdf_size;
        String author;
        String pdf_category;
        String pdf_pages;
        String pdf_thumb;
        String language;

        public String getPdfId() {
            return pdfId;
        }

        public void setPdfId(String pdfId) {
            this.pdfId = pdfId;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getPdf_thumb() {
            return pdf_thumb;
        }

        public void setPdf_thumb(String pdf_thumb) {
            this.pdf_thumb = pdf_thumb;
        }

        public String getPdf_url() {
            return pdf_url;
        }

        public void setPdf_url(String pdf_url) {
            this.pdf_url = pdf_url;
        }

        public String getPdf_name() {
            return pdf_name;
        }

        public void setPdf_name(String pdf_name) {
            this.pdf_name = pdf_name;
        }

        public String getPdf_size() {
            return pdf_size;
        }

        public void setPdf_size(String pdf_size) {
            this.pdf_size = pdf_size;
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

        public String getPdf_pages() {
            return pdf_pages;
        }

        public void setPdf_pages(String pdf_pages) {
            this.pdf_pages = pdf_pages;
        }
    }


}
