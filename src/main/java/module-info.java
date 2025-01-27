module com.resma.facma {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires com.github.librepdf.openpdf;
    requires java.desktop;
    requires java.sql;

    opens com.resma.facma to javafx.fxml;
    exports com.resma.facma;
    opens com.resma.facma.controller to javafx.fxml;
    exports com.resma.facma.controller;
    opens com.resma.facma.entity to javafx.fxml;
    exports com.resma.facma.entity;
}