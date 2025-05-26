module intentovideojuegoaparte {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;

    opens intentovideojuegoaparte to javafx.fxml;
    exports intentovideojuegoaparte;
    exports intentovideojuegoaparte.Estructuras;
    opens intentovideojuegoaparte.Estructuras to javafx.fxml;
    exports intentovideojuegoaparte.Unidades;
    opens intentovideojuegoaparte.Unidades to javafx.fxml;
    exports intentovideojuegoaparte.Controladores;
    opens intentovideojuegoaparte.Controladores to javafx.fxml;
}