package com.example.proje_bagis_takibi.service;

import com.example.proje_bagis_takibi.model.*;
import com.example.proje_bagis_takibi.util.FileUtil;
import com.example.proje_bagis_takibi.util.ValidationUtil;
import java.util.List;

public class AuthService {
    private final String kullaniciDosya = "data/kullanicilar.txt";

    // Giriş kontrolü ve rol belirleme
    public Kullanici login(String email, String sifre) {
        List<Kullanici> kullanicilar = FileUtil.kullanicilariOku(kullaniciDosya);
        for (Kullanici k : kullanicilar) {
            if (k.getEmail().equals(email) && k.getSifre().equals(sifre)) {
                return k;
            }
        }
        return null;
    }

    // Yeni bağışçı kaydı oluşturma
    public void kayitOl(String ad, String email, String sifre) {
        ValidationUtil.bosMu(ad, "Ad Soyad");
        ValidationUtil.bosMu(email, "Email");
        ValidationUtil.emailGecerliMi(email);
        ValidationUtil.bosMu(sifre, "Sifre");

        int yeniId = FileUtil.sonIdyiBul(kullaniciDosya) + 1;
        Bagisci yeniBagisci = new Bagisci(yeniId, ad, email, sifre);
        FileUtil.kullaniciEkle(kullaniciDosya, yeniBagisci);
    }
}