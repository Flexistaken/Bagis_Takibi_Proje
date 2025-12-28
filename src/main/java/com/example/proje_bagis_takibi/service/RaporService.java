package com.example.proje_bagis_takibi.service;

import com.example.proje_bagis_takibi.model.Bagis;
import com.example.proje_bagis_takibi.model.Kullanici;
import com.example.proje_bagis_takibi.model.Kurum;
import com.example.proje_bagis_takibi.util.FileUtil;
import java.util.*;
import java.util.stream.Collectors;

public class RaporService {
    private final String bagisDosya = "data/bagislar.txt";
    private final String kullaniciDosya = "data/kullanicilar.txt";
    private final String kurumDosya = "data/kurumlar.txt";

    // Toplam bağış tutarını hesaplar
    public double toplamBagisMiktari() {
        List<Bagis> list = FileUtil.bagislariOku(bagisDosya);
        return list.stream().mapToDouble(Bagis::getMiktar).sum();
    }

    // En çok bağış alan kurumun ID'sini bulur
    public int enCokBagisAlanKurumId() {
        List<Bagis> list = FileUtil.bagislariOku(bagisDosya);
        return list.stream()
                .collect(Collectors.groupingBy(Bagis::getKurumId, Collectors.summingDouble(Bagis::getMiktar)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(-1);
    }

    // En aktif bağışçıyı bulur (Bağış sayısına göre)
    public int enAktifBagisciId() {
        List<Bagis> list = FileUtil.bagislariOku(bagisDosya);
        return list.stream()
                .collect(Collectors.groupingBy(Bagis::getBagisciId, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(-1);
    }
    public List<String> sonBagislar(int limit) {
        List<Bagis> bagislar = FileUtil.bagislariOku(bagisDosya);

        return bagislar.stream()
                .skip(Math.max(0, bagislar.size() - limit))
                .map(b -> "Bagis ID " + b.getId()
                        + " | Kurum ID: " + b.getKurumId()
                        + " | " + b.getMiktar() + " TL")
                .toList();
    }

    public List<String> enCokBagisYapanBagiscilar(int limit) {
        List<Bagis> bagislar = FileUtil.bagislariOku(bagisDosya);
        List<Kullanici> kullanicilar = FileUtil.kullanicilariOku(kullaniciDosya);

        Map<Integer, Double> toplamlar = new HashMap<>();

        for (Bagis b : bagislar) {
            toplamlar.put(
                    b.getBagisciId(),
                    toplamlar.getOrDefault(b.getBagisciId(), 0.0) + b.getMiktar()
            );
        }

        return toplamlar.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .limit(limit)
                .map(e -> {
                    Kullanici k = kullanicilar.stream()
                            .filter(u -> u.getId() == e.getKey())
                            .findFirst()
                            .orElse(null);

                    return (k != null)
                            ? k.getAd() + " → " + e.getValue() + " TL"
                            : "Bilinmeyen Kullanici";
                })
                .toList();
    }

    public List<String> kurumBazliBagisRaporu() {
        List<Bagis> bagislar = FileUtil.bagislariOku(bagisDosya);
        List<Kurum> kurumlar = FileUtil.kurumlariOku(kurumDosya);

        List<String> rapor = new ArrayList<>();

        for (Kurum k : kurumlar) {
            double toplam = bagislar.stream()
                    .filter(b -> b.getKurumId() == k.getId())
                    .mapToDouble(Bagis::getMiktar)
                    .sum();

            rapor.add(k.getAd() + " → " + toplam + " TL");
        }
        return rapor;
    }

}