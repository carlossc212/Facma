module com.resma.facma {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;

    opens com.resma.facma to javafx.fxml;
    exports com.resma.facma;
}