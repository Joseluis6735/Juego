package intentovideojuegoaparte;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.*;

public class Partida {
    private Tablero tablero;
    private List<Unidad> unidades;
    private String turnoActual; // "Ciencias" o "Letras"
    private int turnoNumero;

    public Partida() {
        tablero = new Tablero(8, 8);
        unidades = new ArrayList<>();
        turnoActual = "Ciencias";
        turnoNumero = 1;
        inicializarUnidades();
        colocarUnidades();
    }

    private void inicializarUnidades() {
        // Para pruebas: 2 unidades de cada facción
        unidades.add(new Matematico());
        unidades.add(new Fisico());

        unidades.add(new Poeta());
        unidades.add(new Filosofo());
    }

    private void colocarUnidades() {
        // Ciencias en fila 0
        int filaC = 0;
        int col = 0;
        for(Unidad u : unidades) {
            if(u.getFaccion().equals("Ciencias")) {
                u.setPosicion(filaC, col);
                tablero.getCasilla(filaC, col).setUnidad(u);
                col++;
            }
        }
        // Letras en fila 7
        int filaL = 7;
        col = 0;
        for(Unidad u : unidades) {
            if(u.getFaccion().equals("Letras")) {
                u.setPosicion(filaL, col);
                tablero.getCasilla(filaL, col).setUnidad(u);
                col++;
            }
        }
    }

    public List<Unidad> getUnidades() { return unidades; }
    public Tablero getTablero() { return tablero; }
    public String getTurnoActual() { return turnoActual; }
    public int getTurnoNumero() { return turnoNumero; }

    public void cambiarTurno() {
        turnoActual = turnoActual.equals("Ciencias") ? "Letras" : "Ciencias";
        turnoNumero++;
        // Reset acciones
        for(Unidad u : unidades) {
            if(u.getFaccion().equals(turnoActual)) {
                u.setHaActuado(false);
            }
        }
        // Si turno es IA, activar turno automático
        if(turnoActual.equals("Letras")) {
            turnoIA();
        }
    }

    // Mover unidad si es su turno y no ha actuado
    public boolean moverUnidad(Unidad u, int x, int y) {
        if(!u.getFaccion().equals(turnoActual)) return false;
        if(u.haActuado()) return false;
        boolean exito = tablero.moverUnidad(u, x, y);
        if(exito) u.setHaActuado(true);
        return exito;
    }

    // Atacar unidad enemiga si está en rango
    public boolean atacar(Unidad atacante, Unidad objetivo) {
        if(!atacante.getFaccion().equals(turnoActual)) return false;
        if(atacante.haActuado()) return false;
        if(!atacante.enemigoEnRango(objetivo)) return false;

        atacante.atacar(objetivo);
        atacante.setHaActuado(true);

        if(objetivo.getHpActual() <= 0) {
            eliminarUnidad(objetivo);
        }
        return true;
    }

    private void eliminarUnidad(Unidad u) {
        tablero.getCasilla(u.getPosX(), u.getPosY()).setUnidad(null);
        unidades.remove(u);
    }

    public boolean partidaTerminada() {
        boolean cienciasVivas = unidades.stream().anyMatch(u -> u.getFaccion().equals("Ciencias"));
        boolean letrasVivas = unidades.stream().anyMatch(u -> u.getFaccion().equals("Letras"));
        return !(cienciasVivas && letrasVivas);
    }

    public String ganador() {
        if(!partidaTerminada()) return "No hay ganador aún";
        boolean cienciasVivas = unidades.stream().anyMatch(u -> u.getFaccion().equals("Ciencias"));
        if(cienciasVivas) return "Ciencias";
        return "Letras";
    }

    // IA sencilla que mueve y ataca con unidades del turno (Letras)
    private void turnoIA() {
        for(Unidad u : new ArrayList<>(unidades)) {
            if(!u.getFaccion().equals("Letras")) continue;
            if(u.haActuado()) continue;

            // Busca enemigo más cercano para atacar o acercarse
            Unidad objetivo = null;
            int minDist = Integer.MAX_VALUE;
            for(Unidad enemigo : unidades) {
                if(!enemigo.getFaccion().equals("Ciencias")) continue;
                int dist = u.distanciaA(enemigo.getPosX(), enemigo.getPosY());
                if(dist < minDist) {
                    minDist = dist;
                    objetivo = enemigo;
                }
            }

            if(objetivo == null) continue; // sin enemigos

            if(u.enemigoEnRango(objetivo)) {
                atacar(u, objetivo);
            } else {
                // Moverse hacia enemigo si puede
                int dx = Integer.compare(objetivo.getPosX(), u.getPosX());
                int dy = Integer.compare(objetivo.getPosY(), u.getPosY());
                int newX = u.getPosX() + dx;
                int newY = u.getPosY() + dy;

                if(tablero.posicionValida(newX, newY) && tablero.getCasilla(newX, newY).estaLibre()) {
                    moverUnidad(u, newX, newY);
                }
            }
        }
        cambiarTurno(); // fin turno IA
    }

    // Guardar partida en JSON
    public void guardar(String archivo) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try(FileWriter writer = new FileWriter(archivo)) {
            gson.toJson(this, writer);
        }
    }

    // Cargar partida desde JSON
    public static Partida cargar(String archivo) throws IOException {
        Gson gson = new Gson();
        try(FileReader reader = new FileReader(archivo)) {
            return gson.fromJson(reader, Partida.class);
        }
    }
}

