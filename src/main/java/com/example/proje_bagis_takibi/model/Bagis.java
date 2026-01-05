package com.example.proje_bagis_takibi.model;

import com.example.proje_bagis_takibi.model.BagisTuru;
import java.time.LocalDate;

public class Bagis {

    private int id;
    private int bagisciId;
    private int kurumId;
    private BagisTuru tur;
    private double miktar;
    private String aciklama;
    private LocalDate tarih; // YENİ

    // ESKİ CONSTRUCTOR (geri uyum)
    public Bagis(int id, int bagisciId, int kurumId, BagisTuru tur, double miktar, String aciklama) {
        this(id, bagisciId, kurumId, tur, miktar, aciklama, LocalDate.now());
    }

    // YENİ CONSTRUCTOR
    public Bagis(int id, int bagisciId, int kurumId, BagisTuru tur,
                 double miktar, String aciklama, LocalDate tarih) {
        this.id = id;
        this.bagisciId = bagisciId;
        this.kurumId = kurumId;
        this.tur = tur;
        this.miktar = miktar;
        this.aciklama = aciklama;
        this.tarih = tarih;
    }

    // getter'lar
    public int getId() { return id; }
    public int getBagisciId() { return bagisciId; }
    public int getKurumId() { return kurumId; }
    public BagisTuru getTur() { return tur; }
    public double getMiktar() { return miktar; }
    public String getAciklama() { return aciklama; }
    public LocalDate getTarih() { return tarih; }
}
