package com.example.proje_bagis_takibi.service;

import com.example.proje_bagis_takibi.model.Bagisci;
import com.example.proje_bagis_takibi.model.Kullanici;
import com.example.proje_bagis_takibi.util.FileUtil;

import java.util.List;

public class AuthService {

    private static final String DOSYA = "data/kullanicilar.txt";

    public static void kayitOl(String ad, String email, String sifre) {
        List<Kullanici> liste = FileUtil.kullanicilariOku(DOSYA);

        for (Kullanici k : liste) {
            if (k.getEmail().equalsIgnoreCase(email)) {
                System.out.println("Bu email sistemde zaten kayitli.");
                return;
            }
        }

        int id = FileUtil.sonIdyiBul(DOSYA) + 1;
        FileUtil.kullaniciEkle(DOSYA, new Bagisci(id, ad, email, sifre));
        System.out.println("Kayit basarili.");
    }

    public static Kullanici girisYap(String email, String sifre) {
        for (Kullanici k : FileUtil.kullanicilariOku(DOSYA)) {
            if (k.getEmail().equalsIgnoreCase(email) && k.getSifre().equals(sifre)) {
                return k;
            }
        }
        System.out.println("Hata, giris basarisiz!");
        return null;
    }
}
