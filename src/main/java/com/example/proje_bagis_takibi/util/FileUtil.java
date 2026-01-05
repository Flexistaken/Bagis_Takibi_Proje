package com.example.proje_bagis_takibi.util;

import com.example.proje_bagis_takibi.model.*;
import com.example.proje_bagis_takibi.model.Bagis;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class    FileUtil {

    /* ================= KULLANICI ================= */

    public static List<Kullanici> kullanicilariOku(String dosya) {
        List<Kullanici> liste = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(dosya))) {
            String satir;
            while ((satir = br.readLine()) != null) {
                if (satir.trim().isEmpty()) continue;

                String[] d = satir.split(",");

                int id = Integer.parseInt(d[0]);
                String ad = d[1];
                String email = d[2];
                String sifre = d[3];
                String rol = d[4];

                if (rol.equalsIgnoreCase("ADMIN")) {
                    liste.add(new Admin(id, ad, email, sifre));
                } else {
                    liste.add(new Bagisci(id, ad, email, sifre));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return liste;
    }

    public static void kullaniciEkle(String dosya, Kullanici k) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dosya, true))) {
            bw.write(
                    k.getId() + "," +
                            k.getAd() + "," +
                            k.getEmail() + "," +
                            k.getSifre() + "," +
                            k.getRol()
            );
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int bagisSonId(String dosya) {
        int max = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(dosya))) {
            String satir;
            while ((satir = br.readLine()) != null) {
                if (satir.trim().isEmpty()) continue;

                // bagislar.txt → , "VIRGUL" ile ayrılıyor
                int id = Integer.parseInt(satir.split(",")[0]);
                if (id > max) max = id;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return max;
    }

    public static int sonIdyiBul(String dosya) {
        int max = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(dosya))) {
            String satir;
            while ((satir = br.readLine()) != null) {
                if (satir.trim().isEmpty()) continue;
                int id = Integer.parseInt(satir.split(",")[0]);
                if (id > max) max = id;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return max;
    }
    public static void bagisSil(String dosya, int bagisId) {
        List<String> yeniListe = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(dosya))) {
            String satir;
            while ((satir = br.readLine()) != null) {
                if (satir.trim().isEmpty()) continue;

                int id = Integer.parseInt(satir.split(",")[0]);
                if (id != bagisId) {
                    yeniListe.add(satir);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dosya))) {
            for (String s : yeniListe) {
                bw.write(s);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /* ================= KURUM ================= */

    public static List<Kurum> kurumlariOku(String dosya) {
        List<Kurum> liste = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(dosya))) {
            String satir;
            while ((satir = br.readLine()) != null) {
                if (satir.trim().isEmpty()) continue;
                String[] d = satir.split(",");
                liste.add(new Kurum(
                        Integer.parseInt(d[0]),
                        d[1]
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return liste;
    }

    public static void kurumEkle(String dosya, Kurum k) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dosya, true))) {
            bw.write(k.getId() + "," + k.getAd());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int kurumSonId(String dosya) {
        return sonIdyiBul(dosya);
    }
    //kurumları güncelleme
    public static void kurumGuncelle(String dosya, int kurumId, String yeniAd) {
        List<String> yeniListe = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(dosya))) {
            String satir;
            while ((satir = br.readLine()) != null) {
                if (satir.trim().isEmpty()) continue;

                String[] d = satir.split(",");
                int id = Integer.parseInt(d[0]);

                if (id == kurumId) {
                    // ID aynı, sadece ad değişiyor
                    yeniListe.add(id + "," + yeniAd);
                } else {
                    yeniListe.add(satir);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dosya))) {
            for (String s : yeniListe) {
                bw.write(s);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /* ================= BAGIS (ileride lazım) ================= */

    public static List<Bagis> bagislariOku(String dosya) {
        List<Bagis> liste = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(dosya))) {
            String satir;
            while ((satir = br.readLine()) != null) {
                if (satir.trim().isEmpty()) continue;

                String[] d = satir.split(",");

                int id = Integer.parseInt(d[0]);
                int bagisciId = Integer.parseInt(d[1]);
                int kurumId = Integer.parseInt(d[2]);
                BagisTuru tur = BagisTuru.valueOf(d[3]);
                double miktar = Double.parseDouble(d[4]);
                String aciklama = d[5];

                LocalDate tarih;

                // ESKİ KAYIT: tarih yok
                if (d.length < 7) {
                    tarih = LocalDate.now();
                } else {
                    tarih = LocalDate.parse(d[6]);
                }

                liste.add(new Bagis(
                        id,
                        bagisciId,
                        kurumId,
                        tur,
                        miktar,
                        aciklama,
                        tarih
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return liste;
    }


    public static void bagisEkle(String dosya, Bagis b) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dosya, true))) {
            bw.write(
                    b.getId() + "," +
                            b.getBagisciId() + "," +
                            b.getKurumId() + "," +
                            b.getTur() + "," +
                            b.getMiktar() + "," +
                            b.getAciklama() + "," +
                            b.getTarih()
            );
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

