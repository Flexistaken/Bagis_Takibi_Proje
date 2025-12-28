package com.example.proje_bagis_takibi;

import com.example.proje_bagis_takibi.model.*;
import com.example.proje_bagis_takibi.service.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final AuthService authService = new AuthService();
    private static final KurumService kurumService = new KurumService();
    private static final BagisService bagisService = new BagisService();
    private static final RaporService raporService = new RaporService();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== BAGIS TAKIP SISTEMI (3. KISI TEST) ===");
            System.out.println("1 - Kayit Ol (Bagisci)");
            System.out.println("2 - Giris Yap");
            System.out.println("0 - Cikis");
            System.out.print("Secim: ");

            int secim = sc.nextInt();
            sc.nextLine();

            if (secim == 1) {
                kayitOlustur();
            } else if (secim == 2) {
                girisIslemi();
            } else if (secim == 0) {
                System.out.println("Sistem kapatiliyor...");
                break;
            }
        }
    }

    private static void kayitOlustur() {
        System.out.print("Ad Soyad: ");
        String ad = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Sifre: ");
        String sifre = sc.nextLine();
        authService.kayitOl(ad, email, sifre);
        System.out.println("Kayit basarili! Artik giris yapabilirsiniz.");
    }

    private static void girisIslemi() {
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Sifre: ");
        String sifre = sc.nextLine();

        Kullanici k = authService.login(email, sifre);

        if (k != null) {
            System.out.println("\nHosgeldin " + k.getAd() + " [" + k.getRol() + "]");
            if (k.getRol().equalsIgnoreCase("ADMIN")) {
                adminPaneli();
            } else {
                bagisciPaneli((Bagisci) k);
            }
        } else {
            System.out.println("Hata: Email veya sifre hatali!");
        }
    }

    private static void adminPaneli() {
        while (true) {
            System.out.println("\n--- ADMIN YONETIM VE RAPORLAMA ---");
            System.out.println("1 - Genel Sistem Raporu");
            System.out.println("2 - Yeni Kurum Tanimla");
            System.out.println("3 - Mevcut Kurumu Sil");
            System.out.println("4 - Bagis Sil ");
            System.out.println("5 - Kurum Adi Guncelle");
            System.out.println("6 - Detayli Raporlar");
            System.out.println("0 - Oturumu Kapat");
            System.out.print("Secim: ");

            int secim = sc.nextInt();
            sc.nextLine();
            if (secim == 0) break;

            if (secim == 1) {
                System.out.println("\n>> TOPLAM BAGIS: " + raporService.toplamBagisMiktari() + " TL");
                System.out.println(">> EN COK BAGIS ALAN KURUM ID: " + raporService.enCokBagisAlanKurumId());
                System.out.println(">> EN AKTIF BAGISCI ID: " + raporService.enAktifBagisciId());
            }
            else if (secim == 2) {
                System.out.print("Kurum Adi: ");
                String ad = sc.nextLine();
                kurumService.kurumEkle(ad);
                System.out.println("Kurum sisteme kaydedildi.");
            }
            else if (secim == 3) {
                System.out.print("Silinecek Kurum ID: ");
                int id = sc.nextInt();
                kurumService.kurumSil(id);
                System.out.println("Kurum ve bagli veriler temizlendi.");
            }
            else if (secim == 4) {
                System.out.print("Silinecek Bagis ID: ");
                int bagisId = sc.nextInt();
                sc.nextLine();

                bagisService.bagisSilAdmin(bagisId);
                System.out.println("Bagis silindi.");
            }
            else if (secim == 5) {
                System.out.print("Guncellenecek Kurum ID: ");
                int id = sc.nextInt();
                sc.nextLine();

                System.out.print("Yeni Kurum Adi: ");
                String yeniAd = sc.nextLine();

                try {
                    kurumService.kurumGuncelle(id, yeniAd);
                    System.out.println("Kurum adi guncellendi.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Hata: " + e.getMessage());
                }
            }
            else if (secim == 6) {
                System.out.println("\n--- KURUM BAZLI BAGIS RAPORU ---");
                raporService.kurumBazliBagisRaporu()
                        .forEach(System.out::println);

                System.out.println("\n--- EN COK BAGIS YAPANLAR ---");
                raporService.enCokBagisYapanBagiscilar(3)
                        .forEach(System.out::println);

                System.out.println("\n--- SON BAGISLAR ---");
                raporService.sonBagislar(5)
                        .forEach(System.out::println);
            }

        }
    }

    private static void bagisciPaneli(Bagisci b) {
        while (true) {
            System.out.println("\n--- BAGISCI ISLEMLERI ---");
            System.out.println("1 - Kurumlari Listele / Ara");
            System.out.println("2 - Yeni Bagis Yap");
            System.out.println("3 - Gecmis Bagislarim");
            System.out.println("0 - Oturumu Kapat");
            System.out.print("Secim: ");

            int secim = sc.nextInt();
            sc.nextLine();
            if (secim == 0) break;

            if (secim == 1) {
                System.out.print("Aramak istediginiz kurum (bos birakirsaniz hepsi): ");
                String kelime = sc.nextLine();
                List<Kurum> sonuc = kurumService.kurumAra(kelime);
                System.out.println("\n--- KURUM LISTESI ---");
                sonuc.forEach(k -> System.out.println("ID: " + k.getId() + " | Ad: " + k.getAd()));
            } else if (secim == 2) {
                System.out.print("Bagis yapilacak Kurum ID: ");
                int kId = sc.nextInt();
                System.out.print("Bagis Miktari: ");
                double miktar = sc.nextDouble();
                sc.nextLine();
                System.out.print("Aciklama: ");
                String aciklama = sc.nextLine();

                // BagisTuru.PARA kullanildi (Enum ile uyumlu)
                boolean basarili = bagisService.bagisYap(
                        b.getId(),          // bagisciId
                        kId,                // kurumId
                        BagisTuru.PARA,     // bagis turu
                        miktar,
                        aciklama
                );

                try {
                    bagisService.bagisYap(
                            b.getId(),
                            kId,
                            BagisTuru.PARA,
                            miktar,
                            aciklama
                    );
                    System.out.println("Islem basarili. Desteginiz icin tesekkurler!");
                } catch (IllegalArgumentException e) {
                    System.out.println("Hata: " + e.getMessage());
                }


            } else if (secim == 3) {
                System.out.println("\n--- GECMIS BAGISLARINIZ ---");
                bagisService.kullaniciBagislariniGetir(b.getId())
                        .forEach(bagis -> System.out.println(bagis.getMiktar() + " TL -> Kurum ID: " + bagis.getKurumId()));
            }
        }
    }
}