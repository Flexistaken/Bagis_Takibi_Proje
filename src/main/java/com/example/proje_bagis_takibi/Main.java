package com.example.proje_bagis_takibi;

import com.example.proje_bagis_takibi.model.Kullanici;
import com.example.proje_bagis_takibi.service.AuthService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("=== BAGIS TAKIP SISTEMI ===");
        System.out.println("1 - Kayit Ol");
        System.out.println("2 - Giris Yap");
        System.out.print("Secim: ");

        int secim = sc.nextInt();
        sc.nextLine();

        if (secim == 1) {

            System.out.print("Ad: ");
            String ad = sc.nextLine();

            System.out.print("Email: ");
            String email = sc.nextLine();

            System.out.print("Sifre: ");
            String sifre = sc.nextLine();

            AuthService.kayitOl(ad, email, sifre);

        } else if (secim == 2) {

            System.out.print("Email: ");
            String email = sc.nextLine();

            System.out.print("Sifre: ");
            String sifre = sc.nextLine();

            Kullanici k = AuthService.girisYap(email, sifre);

            if (k != null) {
                System.out.println("Hosgeldin " + k.getAd() + " (rol: " + k.getRol() + ")");
            } else {
                System.out.println("Giris basarisiz!");
            }
        } else {
            System.out.println("Hatali secim!");
        }
    }
}
