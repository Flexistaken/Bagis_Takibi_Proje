package com.example.proje_bagis_takibi.service;

import com.example.proje_bagis_takibi.model.Kurum;
import com.example.proje_bagis_takibi.util.FileUtil;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class KurumService {
    private final String dosyaYolu = "kurumlar.txt";

    // Kurum adına göre arama (contains mantığı)
    public List<Kurum> kurumAra(String aramaMetni) {
        List<Kurum> tumKurumlar = FileUtil.kurumlariOku(dosyaYolu);
        return tumKurumlar.stream()
                .filter(k -> k.getAd().toLowerCase().contains(aramaMetni.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Yeni kurum ekleme (Admin yetkisi)
    public void kurumEkle(String ad) {
        int yeniId = FileUtil.kurumSonId(dosyaYolu) + 1;
        Kurum yeniKurum = new Kurum(yeniId, ad);
        FileUtil.kurumEkle(dosyaYolu, yeniKurum);
    }

    // Kurum silme işlemi
    public void kurumSil(int id) {
        List<Kurum> list = FileUtil.kurumlariOku(dosyaYolu);
        list.removeIf(k -> k.getId() == id);
        verileriKaydet(list);
    }

    private void verileriKaydet(List<Kurum> liste) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dosyaYolu))) {
            for (Kurum k : liste) {
                bw.write(k.getId() + "," + k.getAd());
                bw.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}