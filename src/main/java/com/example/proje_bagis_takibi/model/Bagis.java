package com.example.proje_bagis_takibi.model;

public class Bagis {

    private int id;
    private int bagisciId;
    private int kurumId;
    private BagisTuru tur;
    private double miktar;
    private String aciklama;

    public Bagis(int id, int bagisciId, int kurumId, BagisTuru tur, double miktar,String aciklama) {
        this.id = id;
        this.bagisciId = bagisciId;
        this.kurumId = kurumId;
        this.tur = tur;
        this.miktar = miktar;
        this.aciklama=aciklama;
    }

    public int getId() { return id; }
    public int getBagisciId() { return bagisciId; }
    public int getKurumId() { return kurumId; }
    public BagisTuru getTur() { return tur; }
    public double getMiktar() { return miktar; }
    public String getAciklama() { return aciklama; }
}
