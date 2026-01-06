package com.example.bagis_takibi_proje;

import com.example.proje_bagis_takibi.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {

    @FXML
    private TextField adField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField sifreField;

    @FXML
    private Label mesajLabel;

    private final AuthService authService = new AuthService();

    @FXML
    private void kayitOl() {
        try {
            authService.kayitOl(
                    adField.getText(),
                    emailField.getText(),
                    sifreField.getText()
            );

            // ✅ Başarılı mesaj (label)
            mesajLabel.setStyle("-fx-text-fill: green;");
            mesajLabel.setText("Kayıt başarılı!");

            // ✅ Başarılı popup
            bilgi("Kayıt başarılı! Giriş yapabilirsiniz.");

            // pencereyi kapat
            Stage stage = (Stage) adField.getScene().getWindow();
            stage.close();

        } catch (IllegalArgumentException e) {
            // ⚠️ Validation hataları (email geçersiz, boş alan vb.)

            mesajLabel.setStyle("-fx-text-fill: red;");
            mesajLabel.setText(e.getMessage());

            uyar(e.getMessage());

        } catch (Exception e) {
            // ❌ Beklenmeyen sistemsel hata

            mesajLabel.setStyle("-fx-text-fill: red;");
            mesajLabel.setText("Kayıt sırasında hata oluştu!");

            hata("Kayıt sırasında beklenmeyen bir hata oluştu.");
            e.printStackTrace();
        }
    }

    /* ================= ALERT METODLARI ================= */

    private void bilgi(String mesaj) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Bilgi");
        alert.setContentText(mesaj);
        alert.showAndWait();
    }

    private void uyar(String mesaj) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setTitle("Uyarı");
        alert.setContentText(mesaj);
        alert.showAndWait();
    }

    private void hata(String mesaj) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Hata");
        alert.setTitle("Hata");
        alert.setContentText(mesaj);
        alert.showAndWait();
    }
}
