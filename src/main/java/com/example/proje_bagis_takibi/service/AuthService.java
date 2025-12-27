package com.example.proje_bagis_takibi.service;

import com.example.proje_bagis_takibi.model.*;
import com.example.proje_bagis_takibi.util.FileUtil;
import java.util.List;

public class AuthService {
    private final String dosyaYolu = "users.txt";

    // Giriş kontrolü ve rol belirleme
    public Kullanici login(String email, String sifre) {
        List<Kullanici> kullanicilar = FileUtil.kullanicilariOku(dosyaYolu);
        for (Kullanici k : kullanicilar) {
            if (k.getEmail().equals(email) && k.getSifre().equals(sifre)) {
                return k;
            }
        }
        return null;
    }

    // Yeni bağışçı kaydı oluşturma
    public void kayitOl(String ad, String email, String sifre) {
        int yeniId = FileUtil.sonIdyiBul(dosyaYolu) + 1;
        Bagisci yeniBagisci = new Bagisci(yeniId, ad, email, sifre);
        FileUtil.kullaniciEkle(dosyaYolu, yeniBagisci);
    }
}