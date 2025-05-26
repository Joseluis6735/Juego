package intentovideojuegoaparte;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Aseguramos la ruta correcta del FXML
        URL fxmlLocation = getClass().getResource("/intentovideojuegoaparte/menuopciones-view.fxml");

        if (fxmlLocation == null) {
            System.err.println("Error: No se pudo encontrar el archivo menuopciones-view.fxml");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Parent root = fxmlLoader.load();

        primaryStage.setTitle("Juego por Turnos");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}







