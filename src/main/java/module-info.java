module com.resma.facma {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires com.github.librepdf.openpdf;
    requires java.desktop;

    opens com.resma.facma to javafx.fxml;
    exports com.resma.facma;
}