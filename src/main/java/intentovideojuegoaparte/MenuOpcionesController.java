package intentovideojuegoaparte;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MenuOpcionesController {
    @FXML private TextField campoFilas;
    @FXML private TextField campoColumnas;
    @FXML private ChoiceBox<String> choiceFaccion;
    @FXML private ChoiceBox<String> choiceUnidad1;
    @FXML private ChoiceBox<String> choiceUnidad2;
    @FXML
    public void initialize() {
        choiceFaccion.getItems().addAll("Científicos", "Humanistas");

        choiceFaccion.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            choiceUnidad1.getItems().clear();
            choiceUnidad2.getItems().clear();

            if ("Científicos".equals(newVal)) {
                choiceUnidad1.getItems().addAll("Ingeniero", "Matematico", "Fisico", "Biologo");
                choiceUnidad2.getItems().addAll("Ingeniero", "Matematico", "Fisico", "Biologo");
            } else if ("Humanistas".equals(newVal)) {
                choiceUnidad1.getItems().addAll("Poeta", "Filologo", "Filosofo", "Historiador");
                choiceUnidad2.getItems().addAll("Poeta", "Filologo", "Filosofo", "Historiador");
            }

            // Seleccionar por defecto la primera opción
            choiceUnidad1.getSelectionModel().selectFirst();
            choiceUnidad2.getSelectionModel().selectLast();
        });

        // También seleccionar una facción por defecto si quieres
        choiceFaccion.getSelectionModel().selectFirst();
    }

    @FXML
    public void iniciarJuego() {
        try {
            if (campoFilas.getText().isEmpty() || campoColumnas.getText().isEmpty()) {
                mostrarAlerta("Debes ingresar el tamaño del tablero.");
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

            // Crear el tablero
            Tablero tablero = new Tablero(filas, columnas);
            List<Unidad> unidades = new ArrayList<>();

            // Crear unidades del jugador y colocarlas en las esquinas inferiores
            Unidad jugador1 = crearUnidadDesdeTipo(tipo1, faccionJugador);
            Unidad jugador2 = crearUnidadDesdeTipo(tipo2, faccionJugador);

            int x1 = filas - 1, y1 = 0;              // esquina inferior izquierda
            int x2 = filas - 1, y2 = columnas - 1;   // esquina inferior derecha

            jugador1.setPosicion(x1, y1);
            jugador2.setPosicion(x2, y2);
            tablero.getCasilla(x1, y1).setUnidad(jugador1);
            tablero.getCasilla(x2, y2).setUnidad(jugador2);
            unidades.add(jugador1);
            unidades.add(jugador2);

            // Crear unidades de la IA en las esquinas superiores
            String faccionIA = faccionJugador.equals("Científicos") ? "Humanistas" : "Científicos";

            Unidad ia1 = crearUnidadDesdeTipo(faccionIA.equals("Científicos") ? "Ingeniero" : "Poeta", faccionIA);
            Unidad ia2 = crearUnidadDesdeTipo(faccionIA.equals("Científicos") ? "Matematico" : "Filologo", faccionIA);

            int iaX1 = 0, iaY1 = 0;                    // esquina superior izquierda
            int iaX2 = 0, iaY2 = columnas - 1;         // esquina superior derecha

            ia1.setPosicion(iaX1, iaY1);
            ia2.setPosicion(iaX2, iaY2);
            tablero.getCasilla(iaX1, iaY1).setUnidad(ia1);
            tablero.getCasilla(iaX2, iaY2).setUnidad(ia2);
            unidades.add(ia1);
            unidades.add(ia2);

            // Cargar la vista del juego
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game-view.fxml"));
            Parent root = loader.load();

            GameController gameController = loader.getController();
            gameController.inicializarJuego(tablero, unidades, faccionJugador);

            Stage stage = new Stage();
            stage.setTitle("Juego de Turnos");
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana de opciones
            ((Stage) campoFilas.getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            mostrarAlerta("Tamaño del tablero no válido. Usa números enteros.");
            e.printStackTrace();
        } catch (Exception e) {
            mostrarAlerta("Error al iniciar el juego. Consulta la consola.");
            e.printStackTrace();
        }
    }

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

            // Reconstruir tablero
            Tablero tablero = new Tablero(estado.filas, estado.columnas);
            for (EstadoCasilla ec : estado.casillas) {
                tablero.setCasilla(ec.x, ec.y,
                        new Casilla(ec.costoMovimiento, ec.modDefensa, ec.modMovimiento));
            }

            // Reconstruir unidades
            List<Unidad> unidades = new ArrayList<>();
            for (EstadoUnidad eu : estado.unidades) {
                Unidad u = crearUnidadDesdeTipo(eu.tipo, eu.faccion);
                u.setPosicion(eu.x, eu.y);
                u.hp = eu.hp;
                tablero.getCasilla(eu.x, eu.y).setUnidad(u);
                unidades.add(u);
            }

            // Cargar vista del juego
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game-view.fxml"));
            Parent root = loader.load();

            GameController controller = loader.getController();

            String faccionSeleccionada = choiceFaccion.getValue();
            controller.setFaccionJugador(faccionSeleccionada);

            controller.inicializarJuegoDesdeCarga(tablero, unidades, estado.turnoActual, estado.contadorTurnos);

            Stage stage = new Stage();
            stage.setTitle("Partida cargada");
            stage.setScene(new Scene(root));
            stage.show();

            // Cierra la ventana del menú
            ((Stage) campoFilas.getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al cargar la partida.");
        }
    }

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

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}