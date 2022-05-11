module com.example {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;

    opens com.example to javafx.fxml;
    opens com.example.modele to javafx.fxml;
    exports com.example;
    exports com.example.modele;
}


