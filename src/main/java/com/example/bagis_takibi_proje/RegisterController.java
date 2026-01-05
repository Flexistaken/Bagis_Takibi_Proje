package com.example.bagis_takibi_proje;

import com.example.proje_bagis_takibi.service.AuthService;
import javafx.fxml.FXML;
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

            mesajLabel.setStyle("-fx-text-fill:green;");
            mesajLabel.setText("Kayıt başarılı!");

            // pencereyi kapat
            Stage stage = (Stage) adField.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            mesajLabel.setStyle("-fx-text-fill:red;");
            mesajLabel.setText(e.getMessage());
        }
    }
}
