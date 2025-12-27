package com.example.proje_bagis_takibi.service;

import com.example.proje_bagis_takibi.model.Bagis;
import com.example.proje_bagis_takibi.model.BagisTuru;
import com.example.proje_bagis_takibi.util.FileUtil;

public class BagisService {

    private static final String DOSYA = "data/bagislar.txt";

    public static void bagisYap(int bagisciId, int kurumId, BagisTuru tur, double miktar, String aciklama) {
        int id = FileUtil.sonIdyiBul(DOSYA) + 1;
        Bagis b = new Bagis(id, bagisciId, kurumId, tur, miktar, aciklama);

        FileUtil.bagisEkle(DOSYA, b);
        System.out.println("Bagis kaydedildi.");
    }
}
