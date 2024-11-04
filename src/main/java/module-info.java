module application.javafx1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires java.sql;

    opens application.javafx1 to javafx.fxml;
    opens application.javafx1.controller to javafx.fxml;

    exports application.javafx1;
    exports application.javafx1.controller;
}