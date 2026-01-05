package com.example.proje_bagis_takibi.model;

public class Kurum {

    private int id;
    private String ad;

    public Kurum(int id, String ad) {
        this.id = id;
        this.ad = ad;
    }

    public int getId() { return id; }
    public String getAd() { return ad; }

    @Override
    public String toString() {
        return ad;
    }
}