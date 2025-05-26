package intentovideojuegoaparte.Controladores;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import intentovideojuegoaparte.*;
import intentovideojuegoaparte.Estructuras.Cola;
import intentovideojuegoaparte.Estructuras.ListaEnlazada;
import intentovideojuegoaparte.Unidades.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController {

    private Cola<Unidad> colaTurnos = new Cola<>();
    private Unidad unidadSeleccionada = null;
    private String faccionJugador;
    private ListaEnlazada<Unidad> unidadesJugador = new ListaEnlazada<>();
    private ListaEnlazada<Unidad> unidadesIA = new ListaEnlazada<>();
    private Tablero tablero;
    private int contadorTurnos = 0;
    private static final int TURNO_RECLUTAMIENTO = 5;
    private List<int[]> casillasResaltadas = new ArrayList<>();
    private ListaEnlazada<String> logAcciones = new ListaEnlazada<>();
    private boolean juegoTerminado = false;

    @FXML private GridPane gridTablero;
    @FXML private Label labelTurno;

    @FXML
    public void guardarPartidaManual() {
        guardarPartida();
    }

    public void inicializarJuego(Tablero tablero, List<Unidad> todasUnidades, String faccionJugador) {
        this.tablero = tablero;
        this.faccionJugador = faccionJugador;

        this.unidadesJugador = new ListaEnlazada<>();
        this.unidadesIA = new ListaEnlazada<>();
        this.colaTurnos = new Cola<>();

        for (Unidad u : todasUnidades) {
            if (u.getFaccion().equals(faccionJugador)) {
                unidadesJugador.agregar(u);
            } else {
                unidadesIA.agregar(u);
            }
            colaTurnos.encolar(u);
        }

        dibujarTablero();
    }

    private void dibujarTablero() {
        gridTablero.getChildren().clear();

        for (int i = 0; i < tablero.getFilas(); i++) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                final int x = i;
                final int y = j;

                Casilla casilla = tablero.getCasilla(x, y);

                StackPane celda = new StackPane();
                Rectangle fondo = new Rectangle(40, 40);
                fondo.setStroke(Color.BLACK);

                boolean resaltada = casillasResaltadas.stream()
                        .anyMatch(pos -> pos[0] == x && pos[1] == y);

                if (casilla.estaOcupada()) {
                    Unidad u = casilla.getUnidad();
                    fondo.setFill(u.getFaccion().equals(faccionJugador)
                            ? Color.LIGHTGREEN
                            : Color.LIGHTCORAL);
                } else if (resaltada) {
                    fondo.setFill(Color.LIGHTBLUE); // casilla resaltada para moverse o atacar
                } else {
                    fondo.setFill(Color.BEIGE);
                }

                Text texto = new Text();
                if (casilla.estaOcupada()) {
                    Unidad u = casilla.getUnidad();
                    texto.setText(u.getNombre().substring(0, 1) + "\n" + u.getHP());
                }

                celda.getChildren().addAll(fondo, texto);

                celda.setOnMouseClicked(e -> manejarClick(x, y));

                gridTablero.add(celda, y, x); // column = y, row = x
            }
        }
    }

    private void manejarClick(int x, int y) {
        Unidad actual = colaTurnos.frente();
        if (actual == null) {
            labelTurno.setText("No hay unidades en turno.");
            return;
        }
        if (!actual.getFaccion().equals(faccionJugador)) {
            labelTurno.setText("Es turno de la IA");
            return;
        }

        Casilla casilla = tablero.getCasilla(x, y);

        if (unidadSeleccionada == null) {
            if (casilla.estaOcupada()
                    && casilla.getUnidad().getFaccion().equals(faccionJugador)
                    && casilla.getUnidad().estaViva()) {

                unidadSeleccionada = casilla.getUnidad();
                calcularCasillasResaltadas(unidadSeleccionada);
                labelTurno.setText("Seleccionado: " + unidadSeleccionada.getNombre());
                dibujarTablero();
            } else {
                labelTurno.setText("Selecciona una de tus unidades");
            }
            return;
        }

        Unidad seleccionada = unidadSeleccionada;

        // Atacar
        if (casilla.estaOcupada()
                && !casilla.getUnidad().getFaccion().equals(faccionJugador)) {

            Unidad enemigo = casilla.getUnidad();
            int distancia = Math.abs(seleccionada.getX() - x) + Math.abs(seleccionada.getY() - y);

            if (distancia <= seleccionada.getRangoAtaque()) {
                seleccionada.atacar(enemigo);
                if (!enemigo.estaViva()) {
                    tablero.getCasilla(x, y).setUnidad(null);
                }

                logAcciones.agregar(seleccionada.getNombre() + " ataca a " + enemigo.getNombre() +
                        " en (" + x + "," + y + ")");

                unidadSeleccionada = null;
                casillasResaltadas.clear();
                dibujarTablero();

                // Fin del turno del jugador
                colaTurnos.desencolar();
                colaTurnos.encolar(actual);

                ejecutarTurnoIA();
                dibujarTablero();
                verificarFinDePartida();
                labelTurno.setText("Tu turno");
            } else {
                labelTurno.setText("Enemigo fuera de rango");
            }
            return;
        }

        // Mover
        if (!casilla.estaOcupada() && tablero.puedeMoverUnidad(seleccionada, x, y)) {
            tablero.moverUnidad(seleccionada, x, y);
            logAcciones.agregar(seleccionada.getNombre() + " se mueve a (" + x + "," + y + ")");

            unidadSeleccionada = null;
            casillasResaltadas.clear();
            dibujarTablero();

            // Fin del turno del jugador
            colaTurnos.desencolar();
            colaTurnos.encolar(actual);

            ejecutarTurnoIA();
            dibujarTablero();
            verificarFinDePartida();
            labelTurno.setText("Tu turno");
        } else {
            labelTurno.setText("Movimiento inválido");
            unidadSeleccionada = null;
            casillasResaltadas.clear();
            dibujarTablero();
        }
    }

    private String obtenerFaccionOpuesta(String faccion) {
        return faccion.equals("Científicos") ? "Humanistas" : "Científicos";
    }

    private void generarRefuerzo(String faccion, ListaEnlazada<Unidad> lista) {
        for (Unidad unidad : lista) {
            if (!unidad.estaViva()) continue;

            int x = unidad.getX();
            int y = unidad.getY();

            int[][] direcciones = {{0, -1}, {0, 1}}; // izquierda y derecha

            for (int[] dir : direcciones) {
                int nuevoX = x + dir[0];
                int nuevoY = y + dir[1];

                if (tablero.estaDentroDelTablero(nuevoX, nuevoY) &&
                        !tablero.getCasilla(nuevoX, nuevoY).estaOcupada()) {

                    // Crear una unidad aleatoria de la facción
                    Unidad refuerzo = crearUnidadAleatoria(faccion);
                    refuerzo.setPosicion(nuevoX, nuevoY);
                    tablero.getCasilla(nuevoX, nuevoY).setUnidad(refuerzo);

                    lista.agregar(refuerzo);

                    labelTurno.setText("¡" + faccion + " recibe refuerzos!");
                    return;
                }
            }
        }
    }

    private Unidad crearUnidadAleatoria(String faccion) {
        List<String> tipos;

        if (faccion.equals("Científicos")) {
            tipos = List.of("Ingeniero", "Matematico", "Biologo", "Fisico");
        } else if (faccion.equals("Humanistas")) {
            tipos = List.of("Poeta", "Filologo", "Filosofo", "Historiador");
        } else {
            throw new IllegalArgumentException("Facción desconocida: " + faccion);
        }

        String tipo = tipos.get(new Random().nextInt(tipos.size()));

        return switch (tipo) {
            case "Ingeniero" -> new Ingeniero(faccion);
            case "Matematico" -> new Matematico(faccion);
            case "Biologo" -> new Biologo(faccion);
            case "Fisico" -> new Fisico(faccion);
            case "Poeta" -> new Poeta(faccion);
            case "Filologo" -> new Filologo(faccion);
            case "Filosofo" -> new Filosofo(faccion);
            case "Historiador" -> new Historiador(faccion);
            default -> throw new IllegalStateException("Tipo de unidad no reconocido: " + tipo);
        };
    }

    private void ejecutarTurnoIA() {
        Unidad ia = colaTurnos.frente();

        if (ia == null || !ia.getFaccion().equals(obtenerFaccionOpuesta(faccionJugador))) {
            return; // no es turno de la IA
        }

        boolean accionRealizada = false;

        if (ia.estaViva()) {
            Unidad objetivo = encontrarUnidadJugadorMasCercana(ia);
            if (objetivo != null) {
                int dx = objetivo.getX() - ia.getX();
                int dy = objetivo.getY() - ia.getY();
                int distancia = Math.abs(dx) + Math.abs(dy);

                if (distancia <= ia.getRangoAtaque()) {
                    ia.atacar(objetivo);
                    logAcciones.agregar(ia.getNombre() + " ataca a " + objetivo.getNombre() +
                            " en (" + objetivo.getX() + "," + objetivo.getY() + ")");

                    if (!objetivo.estaViva()) {
                        tablero.getCasilla(objetivo.getX(), objetivo.getY()).setUnidad(null);
                        logAcciones.agregar(objetivo.getNombre() + " ha sido eliminado.");
                    }

                    accionRealizada = true;
                } else {
                    int nuevaX = ia.getX() + Integer.signum(dx);
                    int nuevaY = ia.getY() + Integer.signum(dy);
                    if (tablero.puedeMoverUnidad(ia, nuevaX, nuevaY)) {
                        tablero.moverUnidad(ia, nuevaX, nuevaY);
                        logAcciones.agregar(ia.getNombre() + " se mueve a (" + nuevaX + "," + nuevaY + ")");
                        accionRealizada = true;
                    }
                }
            }
        }

        // Rotar el turno (IA sale de la cola y vuelve al final)
        colaTurnos.desencolar();
        colaTurnos.encolar(ia);

        // Aumentar turno solo después de que la IA actuó
        if (accionRealizada) {
            contadorTurnos++;

            if (contadorTurnos % 5 == 0) {
                generarRefuerzo(faccionJugador, unidadesJugador);
                generarRefuerzo(obtenerFaccionOpuesta(faccionJugador), unidadesIA);
            }
        }

        dibujarTablero();
        verificarFinDePartida();

        // Actualizar mensaje según quién sigue
        Unidad siguiente = colaTurnos.frente();
        if (siguiente != null && siguiente.getFaccion().equals(faccionJugador)) {
            labelTurno.setText("Tu turno");
        } else {
            labelTurno.setText("Es turno de la IA");
            ejecutarTurnoIA(); // IA sigue si hay más unidades IA en la cola
        }
    }

    private Unidad encontrarUnidadJugadorMasCercana(Unidad desde) {
        Unidad masCercana = null;
        int menorDistancia = Integer.MAX_VALUE;

        for (Unidad u : unidadesJugador) {
            if (!u.estaViva()) continue;
            int distancia = Math.abs(u.getX() - desde.getX()) + Math.abs(u.getY() - desde.getY());
            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                masCercana = u;
            }
        }
        return masCercana;
    }

    private void calcularCasillasResaltadas(Unidad unidad) {
        casillasResaltadas.clear();
        int rango = unidad.getRangoMovimiento();
        int x0 = unidad.getX();
        int y0 = unidad.getY();

        for (int dx = -rango; dx <= rango; dx++) {
            for (int dy = -rango; dy <= rango; dy++) {
                int x = x0 + dx;
                int y = y0 + dy;

                if (!tablero.estaDentroDelTablero(x, y)) continue;
                if (Math.abs(dx) + Math.abs(dy) > rango) continue;
                if (!tablero.puedeMoverUnidad(unidad, x, y)) continue;

                casillasResaltadas.add(new int[]{x, y});
            }
        }

        for (Unidad enemigo : unidadesIA) {
            if (!enemigo.estaViva()) continue;
            int dist = Math.abs(x0 - enemigo.getX()) + Math.abs(y0 - enemigo.getY());
            if (dist <= unidad.getRangoAtaque()) {
                casillasResaltadas.add(new int[]{enemigo.getX(), enemigo.getY()});
            }
        }
    }

    private void verificarFinDePartida() {
        if (juegoTerminado) return;

        boolean jugadorVivo = false;
        for (Unidad u : unidadesJugador) {
            jugadorVivo = jugadorVivo || u.estaViva();
        }

        boolean iaViva = false;
        for (Unidad u : unidadesIA) {
            iaViva = iaViva || u.estaViva();
        }

        if (!jugadorVivo) {
            juegoTerminado = true;
            mostrarMensajeFinal("¡La IA ha ganado!");
        } else if (!iaViva) {
            juegoTerminado = true;
            mostrarMensajeFinal("¡Has ganado!");
        }
    }

    private void mostrarMensajeFinal(String mensaje) {
        // Construir el contenido del log
        StringBuilder contenido = new StringBuilder("Registro de acciones:\n\n");
        for (String entrada : logAcciones) {
            contenido.append("- ").append(entrada).append("\n");
        }

        // Guardar el log en un archivo
        try (PrintWriter writer = new PrintWriter("log.txt")) {
            for (String linea : logAcciones) {
                writer.println(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Crear la ventana emergente con el mensaje y el log
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Fin de la partida");
        alerta.setHeaderText(mensaje);

        TextArea areaTexto = new TextArea(contenido.toString());
        areaTexto.setWrapText(true);
        areaTexto.setEditable(false);
        areaTexto.setPrefWidth(500);
        areaTexto.setPrefHeight(300);

        alerta.getDialogPane().setContent(areaTexto);
        alerta.setResizable(true);
        alerta.showAndWait();

        // Cerrar la ventana del juego
        Stage stage = (Stage) gridTablero.getScene().getWindow();
        stage.close();
    }

    public void guardarPartida() {
        File archivo = new File("save/log.json");

        EstadoJuego estado = new EstadoJuego();
        estado.filas = tablero.getFilas();
        estado.columnas = tablero.getColumnas();

        Unidad actual = colaTurnos.frente();
        estado.turnoActual = actual.getFaccion();
        estado.contadorTurnos = contadorTurnos;

        estado.casillas = new ArrayList<>();
        for (int i = 0; i < estado.filas; i++) {
            for (int j = 0; j < estado.columnas; j++) {
                Casilla c = tablero.getCasilla(i, j);
                EstadoCasilla ec = new EstadoCasilla();
                ec.x = i;
                ec.y = j;
                ec.costoMovimiento = c.getCostoMovimiento();
                ec.modDefensa = c.getModificadorDefensa();
                ec.modMovimiento = c.getModificadorMovimiento();
                estado.casillas.add(ec);
            }
        }

        estado.unidades = new ArrayList<>();
        List<Unidad> todas = new ArrayList<>();
        for (Unidad u : unidadesJugador) {
            todas.add(u);
        }
        for (Unidad u : unidadesIA) {
            todas.add(u);
        }

        for (Unidad u : todas) {
            EstadoUnidad eu = new EstadoUnidad();
            eu.tipo = u.getClass().getSimpleName();
            eu.x = u.getX();
            eu.y = u.getY();
            eu.hp = u.getHP();
            eu.faccion = u.getFaccion();
            estado.unidades.add(eu);
        }

        try (FileWriter writer = new FileWriter(archivo)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(estado, writer);
            labelTurno.setText("Partida guardada.");
        } catch (IOException e) {
            e.printStackTrace();
            labelTurno.setText("Error al guardar.");
        }
    }

    public void inicializarJuegoDesdeCarga(Tablero tablero, ListaEnlazada<Unidad> unidades, String turno, int turnosJugados) {
        this.tablero = tablero;
        this.contadorTurnos = turnosJugados;

        this.unidadesJugador = new ListaEnlazada<>();
        this.unidadesIA = new ListaEnlazada<>();
        this.colaTurnos = new Cola<>();

        for (Unidad u : unidades) {
            if (u.getFaccion().equals(faccionJugador)) {
                unidadesJugador.agregar(u);
            } else {
                unidadesIA.agregar(u);
            }
            colaTurnos.encolar(u);
        }

        dibujarTablero();

        Unidad actual = colaTurnos.frente();
        if (actual.getFaccion().equals(faccionJugador)) {
            labelTurno.setText("Tu turno");
        } else {
            labelTurno.setText("Es turno de la IA");
            ejecutarTurnoIA();
        }
    }

    public void setFaccionJugador(String faccion) {
        this.faccionJugador = faccion;
    }
}