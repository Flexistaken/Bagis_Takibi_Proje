package com.example.proje_bagis_takibi.model;

public class Admin extends Kullanici {

    public Admin(int id, String ad, String email, String sifre) {
        super(id, ad, email, sifre);
    }

    @Override
    public String getRol() {
        return "ADMIN";
    }
}
