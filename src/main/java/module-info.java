module org.example.restaurante {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens org.example.restaurante to javafx.fxml;
    exports org.example.restaurante;
}