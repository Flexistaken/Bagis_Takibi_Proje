package com.example.bagis_takibi_proje;

import com.example.proje_bagis_takibi.model.BagisTuru;
import com.example.proje_bagis_takibi.service.BagisService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BagisFormController {

    @FXML private TextField kurumIdField;
    @FXML private TextField miktarField;
    @FXML private TextField aciklamaField;

    private int bagisciId;
    private final BagisService bagisService = new BagisService();

    // popup açılırken çağrılacak
    public void init(int bagisciId, int kurumId) {
        this.bagisciId = bagisciId;
        kurumIdField.setText(String.valueOf(kurumId));
    }

    @FXML
    private void bagisYap() {
        try {
            int kurumId = Integer.parseInt(kurumIdField.getText());
            double miktar = Double.parseDouble(miktarField.getText());
            String aciklama = aciklamaField.getText();

            boolean basarili = bagisService.bagisYap(
                    bagisciId,
                    kurumId,
                    BagisTuru.PARA,
                    miktar,
                    aciklama
            );

            if (!basarili) {
                hata("Bağış miktarı 0'dan büyük olmalıdır.");
                return;
            }

            pencereyiKapat();

        } catch (NumberFormatException e) {
            hata("Lütfen miktarı sayı olarak girin.");
        } catch (Exception e) {
            e.printStackTrace();
            hata("Bağış yapılırken hata oluştu.");
        }
    }

    @FXML
    private void iptal() {
        pencereyiKapat();
    }

    private void pencereyiKapat() {
        Stage stage = (Stage) kurumIdField.getScene().getWindow();
        stage.close();
    }

    private void hata(String mesaj) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText("Hata");
        a.setContentText(mesaj);
        a.showAndWait();
    }
}
