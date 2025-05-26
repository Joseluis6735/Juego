package intentovideojuegoaparte;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class MenuOpcionesController {

    @FXML
    private TextField campoFilas;
    @FXML
    private TextField campoColumnas;
    @FXML
    private ChoiceBox<String> choiceFaccion;
    @FXML
    private ChoiceBox<String> choiceUnidad1;
    @FXML
    private ChoiceBox<String> choiceUnidad2;

    // Listas constantes para unidades según facción
    private static final List<String> UNIDADES_CIENTIFICOS = List.of("Ingeniero", "Matematico", "Fisico", "Biologo");
    private static final List<String> UNIDADES_HUMANISTAS = List.of("Poeta", "Filologo", "Filosofo", "Historiador");

    public MenuOpcionesController() {
    }

    @FXML
    public void initialize() {
        // Cargar las facciones en el ChoiceBox
        choiceFaccion.getItems().addAll("Científicos", "Humanistas");

        // Listener para actualizar las unidades según la facción seleccionada
        choiceFaccion.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            choiceUnidad1.getItems().clear();
            choiceUnidad2.getItems().clear();

            if ("Científicos".equals(newVal)) {
                choiceUnidad1.getItems().addAll(UNIDADES_CIENTIFICOS);
                choiceUnidad2.getItems().addAll(UNIDADES_CIENTIFICOS);
            } else if ("Humanistas".equals(newVal)) {
                choiceUnidad1.getItems().addAll(UNIDADES_HUMANISTAS);
                choiceUnidad2.getItems().addAll(UNIDADES_HUMANISTAS);
            }

            choiceUnidad1.getSelectionModel().selectFirst();
            choiceUnidad2.getSelectionModel().selectLast();
        });

        // Selección inicial de facción para disparar el listener
        choiceFaccion.getSelectionModel().selectFirst();
    }

    /**
     * Inicia una nueva partida con los parámetros seleccionados.
     */
    @FXML
    public void iniciarJuego() {
        try {
            // Validaciones individuales para dar mensajes más claros
            if (campoFilas.getText().isEmpty()) {
                mostrarAlerta("Debes ingresar el número de filas del tablero.");
                return;
            }
            if (campoColumnas.getText().isEmpty()) {
                mostrarAlerta("Debes ingresar el número de columnas del tablero.");
                return;
            }

            int filas = Integer.parseInt(campoFilas.getText());
            int columnas = Integer.parseInt(campoColumnas.getText());

            String faccionJugador = choiceFaccion.getValue();
            String tipo1 = choiceUnidad1.getValue();
            String tipo2 = choiceUnidad2.getValue();

            if (faccionJugador == null || tipo1 == null || tipo2 == null) {
                mostrarAlerta("Selecciona facción y dos unidades.");
                return;
            }

            // Crear tablero y unidades
            Tablero tablero = new Tablero(filas, columnas);
            List<Unidad> unidades = new ArrayList<>();

            Unidad jugador1 = crearUnidadDesdeTipo(tipo1, faccionJugador);
            Unidad jugador2 = crearUnidadDesdeTipo(tipo2, faccionJugador);

            // Posiciones iniciales para jugador
            jugador1.setPosicion(filas - 1, 0);
            jugador2.setPosicion(filas - 1, columnas - 1);

            tablero.getCasilla(filas - 1, 0).setUnidad(jugador1);
            tablero.getCasilla(filas - 1, columnas - 1).setUnidad(jugador2);

            unidades.add(jugador1);
            unidades.add(jugador2);

            // Crear unidades IA de facción contraria
            String faccionIA = faccionJugador.equals("Científicos") ? "Humanistas" : "Científicos";

            Unidad ia1 = crearUnidadDesdeTipo(
                    faccionIA.equals("Científicos") ? "Ingeniero" : "Poeta",
                    faccionIA);
            Unidad ia2 = crearUnidadDesdeTipo(
                    faccionIA.equals("Científicos") ? "Matematico" : "Filologo",
                    faccionIA);

            // Posiciones IA
            ia1.setPosicion(0, 0);
            ia2.setPosicion(0, columnas - 1);

            tablero.getCasilla(0, 0).setUnidad(ia1);
            tablero.getCasilla(0, columnas - 1).setUnidad(ia2);

            unidades.add(ia1);
            unidades.add(ia2);

            // Cargar la vista del juego y pasar los datos
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game-view.fxml"));
            Parent root = loader.load();

            GameController gameController = loader.getController();
            gameController.inicializarJuego(tablero, unidades, faccionJugador);

            Stage stage = new Stage();
            stage.setTitle("Juego de Turnos");
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();

            // Cerrar ventana actual
            ((Stage) campoFilas.getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            mostrarAlerta("Tamaño del tablero no válido. Usa números enteros.");
            e.printStackTrace();
        } catch (Exception e) {
            mostrarAlerta("Error al iniciar el juego. Consulta la consola.");
            e.printStackTrace();
        }
    }

    /**
     * Carga una partida guardada desde archivo JSON.
     */
    @FXML
    public void cargarPartida() {
        File archivo = new File("save/log.json");
        if (!archivo.exists()) {
            mostrarAlerta("No hay partida guardada.");
            return;
        }

        try (FileReader reader = new FileReader(archivo)) {
            Gson gson = new Gson();
            EstadoJuego estado = gson.fromJson(reader, EstadoJuego.class);

            // Crear tablero con dimensiones guardadas
            Tablero tablero = new Tablero(estado.filas, estado.columnas);

            // Restaurar casillas
            for (EstadoCasilla ec : estado.casillas) {
                tablero.setCasilla(ec.x, ec.y, new Casilla(ec.x, ec.y, ec.costoMovimiento, ec.modDefensa, ec.modMovimiento));
            }

            List<Unidad> unidades = new ArrayList<>();

            // Restaurar unidades
            for (EstadoUnidad eu : estado.unidades) {
                Unidad u = crearUnidadDesdeTipo(eu.tipo, eu.faccion);
                u.setPosicion(eu.x, eu.y);
                u.hpMax = eu.hp;
                tablero.getCasilla(eu.x, eu.y).setUnidad(u);
                unidades.add(u);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("game-view.fxml"));
            Parent root = loader.load();

            GameController controller = loader.getController();
            String faccionSeleccionada = choiceFaccion.getValue();
            controller.setFaccionJugador(faccionSeleccionada);
            controller.inicializarJuegoDesdeCarga(tablero, unidades, estado.turnoActual, estado.contadorTurnos);

            Stage stage = new Stage();
            stage.setTitle("Partida cargada");
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();

            // Cerrar ventana actual
            ((Stage) campoFilas.getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al cargar la partida.");
        }
    }

    /**
     * Crea una unidad según el tipo y la facción indicados.
     * @param tipo Tipo de unidad
     * @param faccion Facción a la que pertenece
     * @return Objeto Unidad correspondiente
     */
    private Unidad crearUnidadDesdeTipo(String tipo, String faccion) {
        return switch (tipo) {
            case "Ingeniero" -> new Ingeniero(faccion);
            case "Matematico" -> new Matematico(faccion);
            case "Fisico" -> new Fisico(faccion);
            case "Biologo" -> new Biologo(faccion);
            case "Poeta" -> new Poeta(faccion);
            case "Filologo" -> new Filologo(faccion);
            case "Filosofo" -> new Filosofo(faccion);
            case "Historiador" -> new Historiador(faccion);
            default -> throw new IllegalArgumentException("Tipo de unidad desconocido: " + tipo);
        };
    }

    /**
     * Muestra un mensaje de alerta con el texto dado.
     * @param mensaje Mensaje a mostrar
     */
    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(AlertType.WARNING);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
