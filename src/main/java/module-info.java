module com.example.proje_bagis_takibi {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.bagis_takibi_proje to javafx.fxml;
    opens com.example.proje_bagis_takibi.model to javafx.base;

    exports com.example.bagis_takibi_proje;
}
