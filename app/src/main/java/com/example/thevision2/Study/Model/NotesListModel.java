package com.example.thevision2.Study.Model;

public class NotesListModel {
    public String pdfNames;
    public String url;

    public NotesListModel() {}

    public NotesListModel(String pdfNames, String url) {
        this.pdfNames = pdfNames;
        this.url = url;
    }

    public String getPdfNames() {
        return pdfNames;
    }

    public void setPdfNames(String pdfNames) {
        this.pdfNames = pdfNames;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
