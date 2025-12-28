package com.example.proje_bagis_takibi.util;

public class ValidationUtil {

    public static void bosMu(String deger, String alanAdi) {
        if (deger == null || deger.trim().isEmpty()) {
            throw new IllegalArgumentException(alanAdi + " bos birakilamaz!");
        }
    }

    public static void pozitifMi(double sayi, String alanAdi) {
        if (sayi <= 0) {
            throw new IllegalArgumentException(alanAdi + " 0'dan buyuk olmalidir!");
        }
    }

    public static void emailGecerliMi(String email) {
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Gecersiz email adresi!");
        }
    }
}
