package com.example.bagis_takibi_proje;

import com.example.proje_bagis_takibi.model.Admin;
import com.example.proje_bagis_takibi.model.Bagisci;
import com.example.proje_bagis_takibi.model.Kullanici;
import com.example.proje_bagis_takibi.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
            hataLabel.setText("E-posta veya şifre hatalı!");
            return;
        }

        try {
            // ===== BAĞIŞÇI PANELİ =====
            if (!k.getRol().equalsIgnoreCase("ADMIN")) {

                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("bagisci-panel.fxml")
                );

                Parent root = loader.load();
                root.getStyleClass().add("login-root");

                BagisciController controller = loader.getController();
                controller.setAktifBagisci((Bagisci) k);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Bağışçı Paneli");
                stage.setMaximized(true);
                stage.show();

                // login penceresini kapat
                ((Stage) ((Node) event.getSource())
                        .getScene().getWindow()).close();
                return;
            }

            // ===== ADMIN PANELİ =====
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("admin-panel.fxml")
            );

            Parent root = loader.load();
            root.getStyleClass().add("login-root");

            AdminController controller = loader.getController();
            controller.setAktifAdmin((Admin) k); // 🔥 ADMIN İSMİ GÖNDERİLİYOR

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Bağış Takip Sistemi - Admin");
            stage.setMaximized(true);
            stage.show();

            // login penceresini kapat
            ((Stage) ((Node) event.getSource())
                    .getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
            hataLabel.setText("Panel açılırken hata oluştu!");
        }
    }

    @FXML
    private void kayitEkraniAc() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("register.fxml")
            );

            Parent root = loader.load();
            root.getStyleClass().add("login-root");

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Kayıt Ol");
            stage.setMaximized(true);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
