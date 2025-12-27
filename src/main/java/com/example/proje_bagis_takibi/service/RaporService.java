package com.example.proje_bagis_takibi.service;

import com.example.proje_bagis_takibi.model.Bagis;
import com.example.proje_bagis_takibi.util.FileUtil;
import java.util.*;
import java.util.stream.Collectors;

public class RaporService {
    private final String dosyaYolu = "bagislar.txt";

    // Toplam bağış tutarını hesaplar
    public double toplamBagisMiktari() {
        List<Bagis> list = FileUtil.bagislariOku(dosyaYolu);
        return list.stream().mapToDouble(Bagis::getMiktar).sum();
    }

    // En çok bağış alan kurumun ID'sini bulur
    public int enCokBagisAlanKurumId() {
        List<Bagis> list = FileUtil.bagislariOku(dosyaYolu);
        return list.stream()
                .collect(Collectors.groupingBy(Bagis::getKurumId, Collectors.summingDouble(Bagis::getMiktar)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(-1);
    }

    // En aktif bağışçıyı bulur (Bağış sayısına göre)
    public int enAktifBagisciId() {
        List<Bagis> list = FileUtil.bagislariOku(dosyaYolu);
        return list.stream()
                .collect(Collectors.groupingBy(Bagis::getBagisciId, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(-1);
    }
}