package com.example.bagis_takibi_proje;

import com.example.proje_bagis_takibi.model.BagisTuru;
import com.example.proje_bagis_takibi.model.Bagisci;
import com.example.proje_bagis_takibi.model.Kurum;
import com.example.proje_bagis_takibi.service.BagisService;
import com.example.proje_bagis_takibi.service.KurumService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.example.proje_bagis_takibi.model.Bagis;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;


public class BagisciController {

    @FXML
    private TableView<Kurum> kurumTable;

    @FXML
    private TableColumn<Kurum, Integer> kurumIdColumn;

    @FXML
    private TableColumn<Kurum, String> kurumAdColumn;

    @FXML
    private TextField bagisKurumIdField;

    @FXML
    private TextField bagisMiktarField;

    @FXML
    private TextField bagisAciklamaField;

    private final KurumService kurumService = new KurumService();
    private final BagisService bagisService = new BagisService();

    @FXML
    private TableView<Bagis> bagisTable;

    @FXML
    private TableColumn<Bagis, Integer> bagisKurumIdColumn;

    @FXML
    private TableColumn<Bagis, Double> bagisMiktarColumn;

    @FXML
    private TableColumn<Bagis, String> bagisAciklamaColumn;


    // 🔴 Login'den set edeceğiz
    private Bagisci aktifBagisci;

    public void setAktifBagisci(Bagisci bagisci) {
        this.aktifBagisci = bagisci;
        bagislarimiListele();
    }


    @FXML
    public void initialize() {
        kurumIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        kurumAdColumn.setCellValueFactory(new PropertyValueFactory<>("ad"));

        bagisKurumIdColumn.setCellValueFactory(new PropertyValueFactory<>("kurumId"));
        bagisMiktarColumn.setCellValueFactory(new PropertyValueFactory<>("miktar"));
        bagisAciklamaColumn.setCellValueFactory(new PropertyValueFactory<>("aciklama"));
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
    private void bagisYap() {
        try {
            int kurumId = Integer.parseInt(bagisKurumIdField.getText());
            double miktar = Double.parseDouble(bagisMiktarField.getText());
            String aciklama = bagisAciklamaField.getText();

            bagisService.bagisYap(
                    aktifBagisci.getId(),
                    kurumId,
                    BagisTuru.PARA,
                    miktar,
                    aciklama
            );

            bagisKurumIdField.clear();
            bagisMiktarField.clear();
            bagisAciklamaField.clear();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Bagis basariyla yapildi.");
            alert.showAndWait();
            //bağış yaptıktan sonra tabloyu güncelleme
            bagislarimiListele();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Hata");
            alert.setContentText("Bagis yapilirken hata olustu.");
            alert.showAndWait();
        }
    }

    private void bagislarimiListele() {
        if (aktifBagisci == null) return;

        bagisTable.setItems(
                FXCollections.observableArrayList(
                        bagisService.kullaniciBagislariniGetir(
                                aktifBagisci.getId()
                        )
                )
        );
    }

}
