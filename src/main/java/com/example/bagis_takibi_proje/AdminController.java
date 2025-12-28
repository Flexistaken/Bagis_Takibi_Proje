package com.example.bagis_takibi_proje;

import com.example.proje_bagis_takibi.model.Kurum;
import com.example.proje_bagis_takibi.service.KurumService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import com.example.proje_bagis_takibi.model.Bagis;
import com.example.proje_bagis_takibi.service.BagisService;
import javafx.scene.control.TableColumn;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;





import java.util.List;

public class AdminController {

    @FXML
    private TableView<Kurum> kurumTable;

    @FXML
    private TableColumn<Kurum, Integer> idColumn;

    @FXML
    private TableColumn<Kurum, String> adColumn;

    @FXML
    private TextField kurumAdField;

    private final KurumService kurumService = new KurumService();

    @FXML
    private TableView<Bagis> bagisTable;

    @FXML
    private TableColumn<Bagis, Integer> bagisIdColumn;

    @FXML
    private TableColumn<Bagis, Integer> bagisciIdColumn;

    @FXML
    private TableColumn<Bagis, Integer> bagisKurumIdColumn;

    @FXML
    private TableColumn<Bagis, Double> bagisMiktarColumn;

    private final BagisService bagisService = new BagisService();


    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        adColumn.setCellValueFactory(new PropertyValueFactory<>("ad"));
        kurumTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, eski, yeni) -> {
                    if (yeni != null) {
                        kurumAdField.setText(yeni.getAd());
                    }
                });
        bagisIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        bagisciIdColumn.setCellValueFactory(new PropertyValueFactory<>("bagisciId"));
        bagisKurumIdColumn.setCellValueFactory(new PropertyValueFactory<>("kurumId"));
        bagisMiktarColumn.setCellValueFactory(new PropertyValueFactory<>("miktar"));
        bagislariListele();
    }

    @FXML
    private void kurumlariListele() {
        List<Kurum> kurumlar = kurumService.kurumlariGetir();
        ObservableList<Kurum> data = FXCollections.observableArrayList(kurumlar);
        kurumTable.setItems(data);
    }

    @FXML
    private void kurumEkle() {
        String ad = kurumAdField.getText();

        try {
            kurumService.kurumEkle(ad);
            kurumAdField.clear();
            kurumlariListele(); // tabloyu yenile

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Kurum basariyla eklendi.");
            alert.showAndWait();

        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Hata");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void kurumSil() {
        Kurum seciliKurum = kurumTable.getSelectionModel().getSelectedItem();

        if (seciliKurum == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Lutfen silmek icin bir kurum secin.");
            alert.showAndWait();
            return;
        }

        Alert onay = new Alert(Alert.AlertType.CONFIRMATION);
        onay.setHeaderText("Kurum Sil");
        onay.setContentText(
                seciliKurum.getAd() + " kurumu silinsin mi?"
        );

        if (onay.showAndWait().get().getButtonData().isDefaultButton()) {
            kurumService.kurumSil(seciliKurum.getId());
            kurumlariListele(); // tabloyu yenile
        }
    }

    @FXML
    private void kurumGuncelle() {
        Kurum seciliKurum = kurumTable.getSelectionModel().getSelectedItem();

        if (seciliKurum == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Lutfen guncellemek icin bir kurum secin.");
            alert.showAndWait();
            return;
        }

        String yeniAd = kurumAdField.getText();

        try {
            kurumService.kurumGuncelle(seciliKurum.getId(), yeniAd);
            kurumAdField.clear();
            kurumlariListele();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Kurum adi guncellendi.");
            alert.showAndWait();

        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Hata");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
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
        Bagis seciliBagis = bagisTable.getSelectionModel().getSelectedItem();

        if (seciliBagis == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Lutfen silmek icin bir bagis secin.");
            alert.showAndWait();
            return;
        }

        Alert onay = new Alert(Alert.AlertType.CONFIRMATION);
        onay.setHeaderText("Bagis Sil");
        onay.setContentText(
                "Bagis ID " + seciliBagis.getId() + " silinsin mi?"
        );

        if (onay.showAndWait().get().getButtonData().isDefaultButton()) {
            bagisService.bagisSilAdmin(seciliBagis.getId());
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

}
