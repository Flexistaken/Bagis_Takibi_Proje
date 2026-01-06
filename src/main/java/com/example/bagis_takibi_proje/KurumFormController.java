package com.example.bagis_takibi_proje;

import com.example.proje_bagis_takibi.model.Kurum;
import com.example.proje_bagis_takibi.service.KurumService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class KurumFormController {

    @FXML private Label titleLabel;
    @FXML private TextField kurumAdField;

    private final KurumService kurumService = new KurumService();
    private Kurum guncellenecekKurum;

    // --- KRİTİK EKLEME: İşlem durumunu tutan değişken ---
    private boolean islemBasarili = false;

    // AdminController'ın bu değeri okuyabilmesi için getter metodu
    public boolean isIslemBasarili() {
        return islemBasarili;
    }

    /* ================= EKLE MODU ================= */
    public void ekleModu() {
        titleLabel.setText("Kurum Ekle");
        guncellenecekKurum = null;
        kurumAdField.clear();
        islemBasarili = false; // Her açılışta resetle
    }

    /* ================= GÜNCELLE MODU ================= */
    public void guncelleModu(Kurum kurum) {
        titleLabel.setText("Kurum Güncelle");
        this.guncellenecekKurum = kurum;
        kurumAdField.setText(kurum.getAd());
        islemBasarili = false; // Her açılışta resetle
    }

    @FXML
    private void kaydet() {
        String ad = kurumAdField.getText();

        if (ad == null || ad.trim().isEmpty()) {
            titleLabel.setText("Kurum adı boş olamaz!");
            titleLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            if (guncellenecekKurum == null) {
                kurumService.kurumEkle(ad);
            } else {
                kurumService.kurumGuncelle(guncellenecekKurum.getId(), ad);
            }

            // --- İŞLEM BAŞARILI: Bayrağı true yap ve kapat ---
            this.islemBasarili = true;
            pencereyiKapat();

        } catch (IllegalArgumentException e) {
            titleLabel.setText(e.getMessage());
            titleLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void iptal() {
        // islemBasarili default olarak false olduğu için
        // AdminController popup çıkmasını engelleyecektir.
        pencereyiKapat();
    }

    private void pencereyiKapat() {
        Stage stage = (Stage) kurumAdField.getScene().getWindow();
        stage.close();
    }
}