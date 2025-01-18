module org.example.restaurante {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.restaurante to javafx.fxml;
    exports org.example.restaurante;
}