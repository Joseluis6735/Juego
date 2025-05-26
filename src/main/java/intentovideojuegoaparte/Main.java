package intentovideojuegoaparte;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("menuopciones-view.fxml"));
        Scene scene = new Scene(root, 600, 600);
        scene.getStylesheets().add(getClass().getResource("/Estilo/app.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Configuración del Juego");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}







