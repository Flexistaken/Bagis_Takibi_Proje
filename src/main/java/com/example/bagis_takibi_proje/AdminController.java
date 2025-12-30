package com.example.bagis_takibi_proje;

import com.example.proje_bagis_takibi.model.Bagis;
import com.example.proje_bagis_takibi.model.Kurum;
import com.example.proje_bagis_takibi.service.BagisService;
import com.example.proje_bagis_takibi.service.KurumService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


public class AdminController {

    @FXML private TableView<Kurum> kurumTable;
    @FXML private TableColumn<Kurum, Integer> idColumn;
    @FXML private TableColumn<Kurum, String> adColumn;
    @FXML private TextField kurumAdField;

    @FXML private TableView<Bagis> bagisTable;
    @FXML private TableColumn<Bagis, Integer> bagisIdColumn;
    @FXML private TableColumn<Bagis, Integer> bagisciIdColumn;
    @FXML private TableColumn<Bagis, Integer> bagisKurumIdColumn;
    @FXML private TableColumn<Bagis, Double> bagisMiktarColumn;

    private final KurumService kurumService = new KurumService();
    private final BagisService bagisService = new BagisService();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        adColumn.setCellValueFactory(new PropertyValueFactory<>("ad"));

        bagisIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        bagisciIdColumn.setCellValueFactory(new PropertyValueFactory<>("bagisciId"));
        bagisKurumIdColumn.setCellValueFactory(new PropertyValueFactory<>("kurumId"));
        bagisMiktarColumn.setCellValueFactory(new PropertyValueFactory<>("miktar"));

        kurumTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        bagisTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );
        kurumlariListele();
        bagislariListele();
    }

    @FXML
    private void kurumlariListele() {
        kurumTable.setItems(
                FXCollections.observableArrayList(
                        kurumService.kurumlariGetir()
                )
        );
    }

    @FXML
    private void kurumEkle() {
        kurumPopupAc(false);
    }

    @FXML
    private void kurumSil() {
        Kurum secili = kurumTable.getSelectionModel().getSelectedItem();
        if (secili == null) {
            uyar("Lütfen silmek icin kurum seciniz.");
            return;
        }

        if (onay(secili.getAd() + " silinsin mi?")) {
            kurumService.kurumSil(secili.getId());
            kurumlariListele();
        }
    }

    @FXML
    private void kurumGuncelle() {
        kurumPopupAc(true);
    }

    @FXML
    private void bagislariListele() {
        bagisTable.setItems(
                FXCollections.observableArrayList(
                        bagisService.tumBagislariGetir()
                )
        );
    }

    @FXML
    private void bagisSil() {
        Bagis secili = bagisTable.getSelectionModel().getSelectedItem();
        if (secili == null) {
            uyar("Lütfen silmek için bir bağış seçiniz.");
            return;
        }

        if (onay("Bagis ID " + secili.getId() + " silinsin mi?")) {
            bagisService.bagisSilAdmin(secili.getId());
            bagislariListele();
        }
    }

    @FXML
    private void raporlarAc() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("raporlar.fxml")
            );
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Raporlar");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cikisYap() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("login.fxml")
            );

            Stage stage = (Stage) kurumTable.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Giriş");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void kurumPopupAc(boolean guncelle) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("kurum-form.fxml")
            );

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

            // popup kapandıktan sonra tabloyu yenile
            kurumlariListele();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /* -------- ALERT YARDIMCI METODLAR -------- */

    private void bilgi(String mesaj) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(mesaj);
        a.showAndWait();
    }

    private void hata(String mesaj) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText("Hata");
        a.setContentText(mesaj);
        a.showAndWait();
    }

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
}
