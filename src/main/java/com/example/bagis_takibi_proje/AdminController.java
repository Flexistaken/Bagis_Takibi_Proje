package com.example.bagis_takibi_proje;

import com.example.proje_bagis_takibi.model.Admin;
import com.example.proje_bagis_takibi.model.Bagis;
import com.example.proje_bagis_takibi.model.BagisRow;
import com.example.proje_bagis_takibi.model.Kurum;
import com.example.proje_bagis_takibi.service.BagisService;
import com.example.proje_bagis_takibi.service.KurumService;
import com.example.proje_bagis_takibi.util.FileUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class AdminController {

    /* ================= MENU ================= */
    @FXML private ToggleButton btnKurumEkle;
    @FXML private ToggleButton btnKurumGuncelle;
    @FXML private ToggleButton btnKurumSil;
    @FXML private ToggleButton btnBagisSil;
    @FXML private ToggleButton btnRaporlar;

    private ToggleGroup menuGroup;

    /* ================= TABLOLAR ================= */
    @FXML private TableView<Kurum> kurumTable;
    @FXML private TableColumn<Kurum, Integer> idColumn;
    @FXML private TableColumn<Kurum, String> adColumn;

    @FXML private TableView<BagisRow> bagisTable;
    @FXML private TableColumn<BagisRow, String> kurumColumn;
    @FXML private TableColumn<BagisRow, String> bagisciColumn;
    @FXML private TableColumn<BagisRow, String> turColumn;
    @FXML private TableColumn<BagisRow, String> miktarColumn;
    @FXML private TableColumn<BagisRow, String> tarihColumn;

    /* ================= DASHBOARD ================= */
    @FXML private Label toplamBagisLabel;
    @FXML private Label bugunBagisLabel;

    /* ================= INPUT ================= */
    @FXML private TextField kurumSearchField;

    /* ================= SERVICES ================= */
    private final KurumService kurumService = new KurumService();
    private final BagisService bagisService = new BagisService();

    @FXML private Label adminWelcomeLabel;
    private Admin aktifAdmin;


    /* ================= INITIALIZE ================= */
    @FXML
    public void initialize() {

        /* ---- MENU TOGGLE GROUP ---- */
        menuGroup = new ToggleGroup();
        btnKurumEkle.setToggleGroup(menuGroup);
        btnKurumGuncelle.setToggleGroup(menuGroup);
        btnKurumSil.setToggleGroup(menuGroup);
        btnBagisSil.setToggleGroup(menuGroup);
        btnRaporlar.setToggleGroup(menuGroup);



        /* ---- KURUM TABLOSU ---- */
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        adColumn.setCellValueFactory(new PropertyValueFactory<>("ad"));
        kurumTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        kurumTable.setPlaceholder(new Label("Henüz kurum eklenmedi."));

        /* ---- BAĞIŞ TABLOSU ---- */
        kurumColumn.setCellValueFactory(new PropertyValueFactory<>("kurum"));
        bagisciColumn.setCellValueFactory(new PropertyValueFactory<>("bagisci"));
        turColumn.setCellValueFactory(new PropertyValueFactory<>("tur"));
        miktarColumn.setCellValueFactory(new PropertyValueFactory<>("miktar"));
        tarihColumn.setCellValueFactory(new PropertyValueFactory<>("tarih"));

        miktarColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        tarihColumn.setStyle("-fx-alignment: CENTER;");
        turColumn.setStyle("-fx-alignment: CENTER;");

        bagisTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        bagisTable.setPlaceholder(new Label("Henüz bağış yok."));

        /* ---- SEARCH ---- */
        kurumSearchField.textProperty().addListener((obs, eski, yeni) ->
                kurumTable.setItems(
                        FXCollections.observableArrayList(
                                kurumService.kurumlariGetir()
                                        .stream()
                                        .filter(k -> k.getAd().toLowerCase().contains(yeni.toLowerCase()))
                                        .toList()
                        )
                )
        );

        /* ---- LOAD DATA ---- */
        kurumlariListele();
        bagislariListele();
    }

    public void setAktifAdmin(Admin admin) {
        this.aktifAdmin = admin;

        if (adminWelcomeLabel != null) {
            adminWelcomeLabel.setText(
                    "Hoş geldin, " + admin.getAd() + " (Admin)"
            );
        }
    }

    /* ================= KURUM ================= */
    @FXML
    private void kurumlariListele() {
        kurumTable.setItems(
                FXCollections.observableArrayList(kurumService.kurumlariGetir())
        );
    }

    @FXML
    private void kurumEkle() {
        kurumPopupAc(false);
    }

    @FXML
    private void kurumGuncelle() {
        kurumPopupAc(true);
    }

    @FXML
    private void kurumSil() {
        Kurum secili = kurumTable.getSelectionModel().getSelectedItem();
        if (secili == null) {
            uyar("Lütfen silmek için kurum seçiniz.");
            return;
        }

        if (onay(secili.getAd() + " silinsin mi?")) {
            kurumService.kurumSil(secili.getId());
            kurumlariListele();
            bilgi("Kurum başarıyla silindi.");
        }
    }

    /* ================= BAĞIŞ ================= */
    @FXML
    private void bagislariListele() {

        Map<Integer, String> kurumMap = new HashMap<>();
        for (Kurum k : kurumService.kurumlariGetir()) {
            kurumMap.put(k.getId(), k.getAd());
        }

        Map<Integer, String> bagisciMap = new HashMap<>();
        for (var k : FileUtil.kullanicilariOku("data/kullanicilar.txt")) {
            if (k instanceof com.example.proje_bagis_takibi.model.Bagisci b) {
                bagisciMap.put(b.getId(), b.getAd());
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        var rows = bagisService.tumBagislariGetir().stream()
                .map(b -> new BagisRow(
                        b.getId(),
                        kurumMap.getOrDefault(b.getKurumId(), "Bilinmeyen Kurum"),
                        bagisciMap.getOrDefault(b.getBagisciId(), "Bilinmeyen Bağışçı"),
                        b.getTur().toString(),
                        String.format("%,.0f ₺", b.getMiktar()),
                        b.getTarih().format(formatter)
                ))
                .toList();

        bagisTable.setItems(FXCollections.observableArrayList(rows));

        double toplam = bagisService.tumBagislariGetir()
                .stream().mapToDouble(Bagis::getMiktar).sum();

        double bugun = bagisService.tumBagislariGetir()
                .stream()
                .filter(b -> b.getTarih().equals(LocalDate.now()))
                .mapToDouble(Bagis::getMiktar)
                .sum();

        toplamBagisLabel.setText(String.format("%,.0f ₺", toplam));
        bugunBagisLabel.setText(String.format("%,.0f ₺", bugun));
    }

    @FXML
    private void bagisSil() {
        BagisRow secili = bagisTable.getSelectionModel().getSelectedItem();
        if (secili == null) {
            uyar("Lütfen silmek için bir bağış seçiniz.");
            return;
        }

        if (onay("Seçili bağış silinsin mi?")) {
            bagisService.bagisSilAdmin(secili.getBagisId());
            bagislariListele();
            bilgi("Bağış başarıyla silindi.");
        }
    }

    /* ================= RAPOR ================= */
    @FXML
    private void raporlarAc() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("raporlar.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Raporlar");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= ÇIKIŞ ================= */
    @FXML
    private void cikisYap() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) kurumTable.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Giriş - Bağış Takip Sistemi");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* ================= POPUP ================= */
    private void kurumPopupAc(boolean guncelle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("kurum-form.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Kurum");

            KurumFormController controller = loader.getController();

            if (guncelle) {
                Kurum secili = kurumTable.getSelectionModel().getSelectedItem();
                if (secili == null) {
                    uyar("Lütfen güncellenecek kurumu seçiniz.");
                    return;
                }
                controller.guncelleModu(secili);
            } else {
                controller.ekleModu();
            }

            stage.showAndWait();
            kurumlariListele();
            bilgi(guncelle ? "Kurum başarıyla güncellendi."
                    : "Kurum başarıyla eklendi.");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= ALERT ================= */
    private void uyar(String mesaj) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);
        a.setContentText(mesaj);
        a.showAndWait();
    }

    private boolean onay(String mesaj) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText(null);
        a.setContentText(mesaj);
        return a.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private void bilgi(String mesaj) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Bilgi");
        alert.setContentText(mesaj);
        alert.showAndWait();
    }
}
