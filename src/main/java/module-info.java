module com.example.proje_bagis_takibi {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.proje_bagis_takibi.controller to javafx.fxml;
    exports com.example.proje_bagis_takibi;
}
