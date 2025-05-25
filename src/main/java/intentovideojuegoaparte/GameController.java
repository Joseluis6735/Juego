package intentovideojuegoaparte;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.List;

public class GameController {

    @FXML private GridPane gridTablero;
    @FXML private Label labelTurno;

    private Tablero tablero;
    private List<Unidad> unidadesJugador;
    private Unidad unidadSeleccionada;

    public void inicializarJuego(Tablero tablero, List<Unidad> unidadesJugador, String faccion) {
        this.tablero = tablero;
        this.unidadesJugador = unidadesJugador;

        dibujarTablero();
    }

    private void dibujarTablero() {
        gridTablero.getChildren().clear();

        for (int i = 0; i < tablero.getFilas(); i++) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                Casilla casilla = tablero.getCasilla(i, j);

                StackPane celda = new StackPane();
                Rectangle fondo = new Rectangle(40, 40);

                fondo.setFill(casilla.estaOcupada() ? Color.LIGHTCORAL : Color.BEIGE);
                fondo.setStroke(Color.BLACK);

                Text texto = new Text();
                if (casilla.estaOcupada()) {
                    Unidad u = casilla.getUnidad();
                    texto.setText(u.getNombre().charAt(0) + "\n" + u.getHP());
                }

                celda.getChildren().addAll(fondo, texto);

                final int x = i, y = j;
                celda.setOnMouseClicked(e -> manejarClick(x, y));

                gridTablero.add(celda, y, x); // columnas = y, filas = x
            }
        }
    }

    private void manejarClick(int x, int y) {
        Casilla casilla = tablero.getCasilla(x, y);

        // Selección
        if (unidadSeleccionada == null && casilla.estaOcupada()
                && unidadesJugador.contains(casilla.getUnidad())) {
            unidadSeleccionada = casilla.getUnidad();
            labelTurno.setText("Seleccionado: " + unidadSeleccionada.getNombre());
            return;
        }

        // Movimiento
        if (unidadSeleccionada != null) {
            if (tablero.puedeMoverUnidad(unidadSeleccionada, x, y)) {
                tablero.moverUnidad(unidadSeleccionada, x, y);
                unidadSeleccionada = null;
                labelTurno.setText("Movimiento completado");
                dibujarTablero();
                return;
            } else {
                labelTurno.setText("Movimiento inválido");
            }
        }
    }
}
