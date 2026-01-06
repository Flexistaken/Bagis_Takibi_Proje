package com.example.proje_bagis_takibi.service;

import com.example.proje_bagis_takibi.model.Kurum;
import com.example.proje_bagis_takibi.util.FileUtil;
import com.example.proje_bagis_takibi.util.ValidationUtil;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class KurumService {
    private final String kurumDosya = "data/kurumlar.txt";

    // Kurum adına göre arama (contains mantığı)
    public List<Kurum> kurumAra(String aramaMetni) {
        List<Kurum> tumKurumlar = FileUtil.kurumlariOku(kurumDosya);
        return tumKurumlar.stream()
                .filter(k -> k.getAd().toLowerCase().contains(aramaMetni.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Kurum> kurumlariGetir() {
        //tüm kurumları gui'da çıkartmak için boş arama
        return kurumAra("");
    }

    // Yeni kurum ekleme (Admin yetkisi)
    public void kurumEkle(String ad) {
        ValidationUtil.bosMu(ad, "Kurum adı");
        int yeniId = FileUtil.kurumSonId(kurumDosya) + 1;
        Kurum yeniKurum = new Kurum(yeniId, ad);
        FileUtil.kurumEkle(kurumDosya, yeniKurum);
    }

    // Kurum silme işlemi
    public void kurumSil(int id) {
        List<Kurum> list = FileUtil.kurumlariOku(kurumDosya);
        list.removeIf(k -> k.getId() == id);
        verileriKaydet(list);
    }
    public void kurumGuncelle(int kurumId, String yeniAd) {
        ValidationUtil.bosMu(yeniAd, "Kurum adı");
        FileUtil.kurumGuncelle(kurumDosya, kurumId, yeniAd);
    }


    private void verileriKaydet(List<Kurum> liste) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(kurumDosya))) {
            for (Kurum k : liste) {
                bw.write(k.getId() + "," + k.getAd());
                bw.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}