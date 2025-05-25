module intentovideojuegoaparte {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens intentovideojuegoaparte to javafx.fxml;
    exports intentovideojuegoaparte;
}