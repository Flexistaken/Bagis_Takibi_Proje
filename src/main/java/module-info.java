module com.example.proje_bagis_takibi {
    requires javafx.controls;
    requires javafx.fxml;

    // Bu satır JavaFX'in ekranları çizmesini sağlar
    opens com.example.bagis_takibi_proje to javafx.fxml, javafx.graphics;

    // Bu satır diğer sınıfların bu pakete erişmesini sağlar
    exports com.example.bagis_takibi_proje;
}