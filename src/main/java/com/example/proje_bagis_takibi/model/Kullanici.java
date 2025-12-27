package com.example.proje_bagis_takibi.model;

public abstract class Kullanici {

    protected int id;
    protected String ad;
    protected String email;
    protected String sifre;

    public Kullanici(int id, String ad, String email, String sifre) {
        this.id = id;
        this.ad = ad;
        this.email = email;
        this.sifre = sifre;
    }

    public int getId() { return id; }
    public String getAd() { return ad; }
    public String getEmail() { return email; }
    public String getSifre() { return sifre; }

    public abstract String getRol();
}
