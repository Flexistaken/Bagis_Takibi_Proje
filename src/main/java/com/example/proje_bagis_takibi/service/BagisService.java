package com.example.proje_bagis_takibi.service;

import com.example.proje_bagis_takibi.model.Bagis;
import com.example.proje_bagis_takibi.util.FileUtil;
import java.util.List;
import java.util.stream.Collectors;

public class BagisService {
    private final String dosyaYolu = "bagislar.txt";

    // Bağış kaydı ve zorunlu miktar kontrolü
    public boolean bagisYap(Bagis bagis) {
        if (bagis.getMiktar() <= 0) {
            return false; // Miktar 0'dan büyük olmalı kontrolü
        }
        FileUtil.bagisEkle(dosyaYolu, bagis);
        return true;
    }

    // Kullanıcının kendi bağışlarını listelemesi
    public List<Bagis> kullaniciBagislariniGetir(int bagisciId) {
        List<Bagis> hepsi = FileUtil.bagislariOku(dosyaYolu);
        return hepsi.stream()
                .filter(b -> b.getBagisciId() == bagisciId)
                .collect(Collectors.toList());
    }

    // Admin için tüm bağışları listeleme
    public List<Bagis> tumBagislariGetir() {
        return FileUtil.bagislariOku(dosyaYolu);
    }
}