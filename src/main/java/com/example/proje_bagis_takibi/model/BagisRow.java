package com.example.proje_bagis_takibi.model;

public class BagisRow {

    private int bagisId;
    private String kurum;
    private String bagisci;
    private String tur;
    private String miktar;
    private String tarih;

    public BagisRow(int bagisId, String kurum, String bagisci, String tur, String miktar, String tarih) {
        this.bagisId = bagisId;
        this.kurum = kurum;
        this.bagisci = bagisci;
        this.tur = tur;
        this.miktar = miktar;
        this.tarih = tarih;
    }


    public String getKurum() { return kurum; }
    public String getBagisci() { return bagisci; }
    public String getTur() { return tur; }
    public String getMiktar() { return miktar; }
    public String getTarih() { return tarih; }

    public int getBagisId() {
        return bagisId;
    }

}
