package com.example.bagis_takibi_proje;

import com.example.proje_bagis_takibi.model.*;
import com.example.proje_bagis_takibi.service.BagisService;
import com.example.proje_bagis_takibi.service.KurumService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.time.format.DateTimeFormatter;

public class BagisciController {

    /* ===== DASHBOARD ===== */
    @FXML private Label toplamBagisimLabel;
    @FXML private Label aylikBagisLabel;
    @FXML private Label sonBagisLabel;

    /* ===== BAĞIŞ FORM ===== */
    @FXML private ComboBox<Kurum> kurumCombo;
    @FXML private ComboBox<BagisTuru> turCombo;
    @FXML private TextField miktarField;
    @FXML private TextField aciklamaField;
    @FXML private Button bagisYapBtn;
    @FXML private Label welcomeLabel;

    /* ===== TABLO ===== */
    @FXML private TableView<Bagis> bagisTable;
    @FXML private TableColumn<Bagis, String> bagisKurumColumn;
    @FXML private TableColumn<Bagis, String> bagisTurColumn;
    @FXML private TableColumn<Bagis, String> bagisMiktarColumn;
    @FXML private TableColumn<Bagis, String> bagisTarihColumn;

    private final KurumService kurumService = new KurumService();
    private final BagisService bagisService = new BagisService();
    private final Map<Integer, String> kurumMap = new HashMap<>();
    private Bagisci aktifBagisci;

    /* ===== LOGIN'DEN SET EDİLİR ===== */
    public void setAktifBagisci(Bagisci bagisci) {
        this.aktifBagisci = bagisci;

        if (welcomeLabel != null) {
            welcomeLabel.setText("Hoş geldin, " + bagisci.getAd() + " 👋");
        }

        bagislarimiListele();
        dashboardDoldur();
    }

    @FXML
    public void initialize() {
        kurumCombo.setItems(
                FXCollections.observableArrayList(kurumService.kurumlariGetir())
        );

        turCombo.setItems(
                FXCollections.observableArrayList(BagisTuru.values())
        );

        bagisKurumColumn.setCellValueFactory(cell ->
                javafx.beans.binding.Bindings.createStringBinding(
                        () -> kurumMap.getOrDefault(
                                cell.getValue().getKurumId(),
                                "Bilinmeyen Kurum"
                        )
                )
        );

        bagisTurColumn.setCellValueFactory(
                new PropertyValueFactory<>("tur")
        );

        bagisMiktarColumn.setCellValueFactory(c ->
                javafx.beans.binding.Bindings.createStringBinding(
                        () -> String.format("%,.0f ₺", c.getValue().getMiktar())
                )
        );

        bagisTarihColumn.setCellValueFactory(c ->
                javafx.beans.binding.Bindings.createStringBinding(
                        () -> c.getValue().getTarih()
                                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                )
        );

        for (Kurum k : kurumService.kurumlariGetir()) {
            kurumMap.put(k.getId(), k.getAd());
        }

        bagisTable.setPlaceholder(
                new Label("Henüz bağış yapmadınız. İlk bağışınızı yukarıdan yapabilirsiniz 💚")
        );


    }

    /* ===== BAĞIŞ YAP ===== */
    @FXML
    private void bagisYap() {

        if (aktifBagisci == null ||
                kurumCombo.getValue() == null ||
                turCombo.getValue() == null ||
                miktarField.getText().isBlank() ||
                aciklamaField.getText().isBlank()) {

            uyar("Lütfen tüm alanları doldurun.");
            return;
        }

        double miktar;
        try {
            miktar = Double.parseDouble(miktarField.getText());
        } catch (NumberFormatException e) {
            uyar("Bağış miktarı geçerli bir sayı olmalıdır.");
            return;
        }

        if (miktar <= 0) {
            uyar("Bağış miktarı 0'dan büyük olmalıdır.");
            return;
        }

        // ✅ Disable burada
        bagisYapBtn.setDisable(true);

        try {
            String aciklama = aciklamaField.getText().trim();

            bagisService.bagisYap(
                    aktifBagisci.getId(),
                    kurumCombo.getValue().getId(),
                    turCombo.getValue(),
                    miktar,
                    aciklama
            );

            miktarField.clear();
            aciklamaField.clear();

            bagislarimiListele();
            dashboardDoldur();

            bilgi("Bağış başarıyla yapıldı.");

        } catch (Exception e) {
            e.printStackTrace();
            uyar("Bağış yapılırken sistemsel hata oluştu.");

        } finally {
            // ✅ Hata olsa da olmasa da buton tekrar açılır
            bagisYapBtn.setDisable(false);
        }
    }




    /* ===== DASHBOARD ===== */
    private void dashboardDoldur() {
        var bagislar = bagisService.kullaniciBagislariniGetir(aktifBagisci.getId());

        double toplam = bagislar.stream()
                .mapToDouble(Bagis::getMiktar)
                .sum();
        toplamBagisimLabel.setText(String.format("%,.0f ₺", toplam));

        var now = java.time.LocalDate.now();
        double buAy = bagislar.stream()
                .filter(b -> b.getTarih().getYear() == now.getYear()
                        && b.getTarih().getMonth() == now.getMonth())
                .mapToDouble(Bagis::getMiktar)
                .sum();
        aylikBagisLabel.setText(String.format("%,.0f ₺", buAy));

        sonBagisLabel.setText(
                bagislar.isEmpty()
                        ? "-"
                        : bagislar.get(bagislar.size() - 1)
                        .getTarih().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        );
    }


    private void bagislarimiListele() {
        bagisTable.setItems(
                FXCollections.observableArrayList(
                        bagisService.kullaniciBagislariniGetir(aktifBagisci.getId())
                )
        );
    }

    @FXML
    private void cikisYap() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) bagisTable.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Giriş - Bağış Takip Sistemi");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* ===== ALERT ===== */
    private void bilgi(String m) {
        new Alert(Alert.AlertType.INFORMATION, m).showAndWait();
    }

    private void uyar(String m) {
        new Alert(Alert.AlertType.WARNING, m).showAndWait();
    }
}
