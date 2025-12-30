package com.example.bagis_takibi_proje;

import com.example.proje_bagis_takibi.model.Admin;
import com.example.proje_bagis_takibi.model.Bagisci;
import com.example.proje_bagis_takibi.model.Kullanici;
import com.example.proje_bagis_takibi.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField sifreField;
    @FXML private Label hataLabel;

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
            FXMLLoader loader;
            String title;

            if (k.getRol().equalsIgnoreCase("ADMIN")) {
                loader = new FXMLLoader(getClass().getResource("admin-panel.fxml"));
                title = "Admin Paneli";
            } else {
                loader = new FXMLLoader(getClass().getResource("bagisci-panel.fxml"));
                title = "Bağışçı Paneli";
            }

            // Mevcut login penceresinin stage'ini alıp aynı stage'i kullanıyoruz (daha temiz)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle(title);

            // ✅ Kullanıcıyı ilgili controller'a gönder (Hoş geldin için)
            if (k.getRol().equalsIgnoreCase("ADMIN")) {
                AdminController controller = loader.getController();
                controller.setAktifAdmin((Admin) k);
            } else {
                BagisciController controller = loader.getController();
                controller.setAktifBagisci((Bagisci) k);
            }

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            hataLabel.setText("Panel açılırken hata oluştu!");
        }
    }

    @FXML
    private void kayitEkraniAc() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Kayıt Ol");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
