package com.example.bagis_takibi_proje;

import com.example.proje_bagis_takibi.model.Kurum;
import com.example.proje_bagis_takibi.service.KurumService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class KurumFormController {

    @FXML
    private Label titleLabel;

    @FXML
    private TextField kurumAdField;

    private final KurumService kurumService = new KurumService();

    private Kurum guncellenecekKurum; // null ise EKLE modunda

    /* ================= EKLE MODU ================= */
    public void ekleModu() {
        titleLabel.setText("Kurum Ekle");
        guncellenecekKurum = null;
        kurumAdField.clear();
    }

    /* ================= GÜNCELLE MODU ================= */
    public void guncelleModu(Kurum kurum) {
        titleLabel.setText("Kurum Güncelle");
        this.guncellenecekKurum = kurum;
        kurumAdField.setText(kurum.getAd());
    }

    @FXML
    private void kaydet() {
        String ad = kurumAdField.getText();

        try {
            if (guncellenecekKurum == null) {
                kurumService.kurumEkle(ad);
            } else {
                kurumService.kurumGuncelle(
                        guncellenecekKurum.getId(),
                        ad
                );
            }

            pencereyiKapat();

        } catch (IllegalArgumentException e) {
            // basit validation
            titleLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void iptal() {
        pencereyiKapat();
    }

    private void pencereyiKapat() {
        Stage stage = (Stage) kurumAdField.getScene().getWindow();
        stage.close();
    }
}
