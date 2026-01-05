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
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;



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

    @FXML
    private Label welcomeLabel;

    // login'den set
    private Bagisci aktifBagisci;

    public void setAktifBagisci(Bagisci bagisci) {
        this.aktifBagisci = bagisci;
        welcomeLabel.setText("Hoş geldin, " + bagisci.getAd());
        bagislarimiListele(); // istersen burada otomatik yükle
    }



    @FXML
    public void initialize() {
        kurumIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        kurumAdColumn.setCellValueFactory(new PropertyValueFactory<>("ad"));

        bagisKurumIdColumn.setCellValueFactory(new PropertyValueFactory<>("kurumId"));
        bagisMiktarColumn.setCellValueFactory(new PropertyValueFactory<>("miktar"));
        bagisAciklamaColumn.setCellValueFactory(new PropertyValueFactory<>("aciklama"));

        kurumTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        bagisTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        kurumlariListele();

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
            alert.setContentText("Bağış başarıyla yapıldı.");
            alert.showAndWait();
            //bağış yaptıktan sonra tabloyu güncelleme
            bagislarimiListele();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Hata");
            alert.setContentText("Bağış yapılırken hata oluştu!");
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
    @FXML
    private void cikisYap() {
        try {
            // 1. FXML dosyasını loader ile hazırla
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));

            // 2. Sahneyi (Scene) doğrudan loader üzerinden oluştur
            // Parent hatasını önlemek için değişken tanımlamadan direkt yükleme yapıyoruz
            Scene scene = new Scene(loader.load());

            // 3. Mevcut pencereyi (Stage) kurumTable üzerinden yakala
            Stage stage = (Stage) kurumTable.getScene().getWindow();

            // 4. Yeni sahneyi ata ve başlığı güncelle
            stage.setScene(scene);
            stage.setTitle("Giriş - Bağış Takip Sistemi");

            // stage.setMaximized(true) satırını sildik çünkü zaten büyük,
            // böylece mevcut boyutunu koruyarak sadece içeriği değiştirir.
            stage.show();

        } catch (IOException e) {
            System.err.println("Giriş ekranı yüklenirken hata oluştu!");
            e.printStackTrace();
        }
    }

    @FXML
    private void bagisPopupAc() {
        try {
            if (kurumTable.getSelectionModel().getSelectedItem() == null) {
                uyar("Lütfen bağış yapmak için bir kurum seçiniz.");
                return;
            }

            int kurumId = kurumTable.getSelectionModel()
                    .getSelectedItem()
                    .getId();

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("bagis-form.fxml")
            );

            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Bağış Yap");

            BagisFormController controller = loader.getController();
            controller.init(aktifBagisci.getId(), kurumId);

            stage.showAndWait();

            // popup kapandıktan sonra bağışlarımı yenile
            bagislarimiListele();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //alert
    private void uyar(String mesaj) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);
        a.setContentText(mesaj);
        a.showAndWait();
    }


}
