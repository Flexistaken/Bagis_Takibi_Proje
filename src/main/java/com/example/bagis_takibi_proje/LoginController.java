package com.example.bagis_takibi_proje;

import com.example.proje_bagis_takibi.model.Bagisci;
import com.example.proje_bagis_takibi.model.Kullanici;
import com.example.proje_bagis_takibi.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;


public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField sifreField;

    @FXML
    private Label hataLabel;

    private final AuthService authService = new AuthService();

    @FXML

    private void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String sifre = sifreField.getText();

        Kullanici k = authService.login(email, sifre);

        if (k == null) {
            hataLabel.setText("E-mail veya Şifre Hatalı!");
            return;
        }

        try {
            String fxml;

            if (k.getRol().equalsIgnoreCase("ADMIN")) {
                fxml = "admin-panel.fxml";
            } else {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("bagisci-panel.fxml")
                );

                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));

                BagisciController controller = loader.getController();
                controller.setAktifBagisci((Bagisci) k);

                stage.setTitle("Bagisci Paneli");
                stage.show();

                ((Stage) ((Node) event.getSource())
                        .getScene().getWindow()).close();
                return;
            }


            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(fxml)
            );

            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Bağış Takip Sistemi");

            stage.show();

            // login penceresini kapat
            ((Stage) ((Node) event.getSource())
                    .getScene().getWindow()).close();

        }catch (Exception e) {
            e.printStackTrace();
            hataLabel.setText("Panel acilirken hata olustu!");
        }

    }
}
