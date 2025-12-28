package com.example.bagis_takibi_proje;

import com.example.proje_bagis_takibi.service.RaporService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class RaporlarController {

    @FXML
    private ListView<String> kurumRaporList;

    @FXML
    private ListView<String> bagisciRaporList;

    @FXML
    private ListView<String> sonBagisList;

    private final RaporService raporService = new RaporService();

    @FXML
    public void initialize() {
        kurumRaporList.setItems(
                FXCollections.observableArrayList(
                        raporService.kurumBazliBagisRaporu()
                )
        );

        bagisciRaporList.setItems(
                FXCollections.observableArrayList(
                        raporService.enCokBagisYapanBagiscilar(3)
                )
        );

        sonBagisList.setItems(
                FXCollections.observableArrayList(
                        raporService.sonBagislar(5)
                )
        );
    }
}
