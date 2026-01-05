package com.example.proje_bagis_takibi.service;

import com.example.proje_bagis_takibi.model.Bagis;
import com.example.proje_bagis_takibi.model.BagisTuru;
import com.example.proje_bagis_takibi.util.FileUtil;
import com.example.proje_bagis_takibi.util.ValidationUtil;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;

public class BagisService {
    private final String bagisDosya = "data/bagislar.txt";

    // Bağış kaydı ve zorunlu miktar kontrolü bunları validation kullanarak ypaıyoruz
    public boolean bagisYap(
            int bagisciId,
            int kurumId,
            BagisTuru tur,
            double miktar,
            String aciklama
    ) {
        ValidationUtil.pozitifMi(miktar, "Bagis miktari");
        ValidationUtil.bosMu(aciklama, "Aciklama");

        int yeniId = FileUtil.bagisSonId(bagisDosya) + 1;

        Bagis bagis = new Bagis(
                yeniId,
                bagisciId,
                kurumId,
                tur,
                miktar,
                aciklama,
                LocalDate.now() // YENİ
        );


        FileUtil.bagisEkle(bagisDosya, bagis);
        return true;
    }
    //admin bağış silme
    public void bagisSilAdmin(int bagisId) {
        FileUtil.bagisSil(bagisDosya, bagisId);
    }

    // Kullanıcının kendi bağışlarını listelemesi
    public List<Bagis> kullaniciBagislariniGetir(int bagisciId) {
        List<Bagis> hepsi = FileUtil.bagislariOku(bagisDosya);
        return hepsi.stream()
                .filter(b -> b.getBagisciId() == bagisciId)
                .collect(Collectors.toList());
    }

    // Admin için tüm bağışları listeleme
    public List<Bagis> tumBagislariGetir() {
        return FileUtil.bagislariOku(bagisDosya);
    }
}