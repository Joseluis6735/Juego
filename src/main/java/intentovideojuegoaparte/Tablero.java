package intentovideojuegoaparte;

import intentovideojuegoaparte.Unidades.Unidad;
import java.util.Random;

public class Tablero {
    private final Casilla[][] casillas;
    private final int filas;
    private final int columnas;

    public Tablero(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.casillas = new Casilla[filas][columnas];
        inicializarCasillas();
    }

    private void inicializarCasillas() {
        Random rand = new Random();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int costoMovimiento = 1 + rand.nextInt(3); // 1-3
                int modDefensa = rand.nextInt(3) - 1;       // -1 a +1
                int modMovimiento = rand.nextInt(3) - 1;    // -1 a +1
                casillas[i][j] = new Casilla(costoMovimiento, modDefensa, modMovimiento);
            }
        }
    }

    public Casilla getCasilla(int x, int y) {
        if (estaDentroDelTablero(x, y)) {
            return casillas[x][y];
        }
        return null;
    }

    public void setCasilla(int x, int y, Casilla casilla) {
        if (estaDentroDelTablero(x, y)) {
            casillas[x][y] = casilla;
        }
    }

    public boolean estaDentroDelTablero(int x, int y) {
        return x >= 0 && x < filas && y >= 0 && y < columnas;
    }

    public boolean puedeMoverUnidad(Unidad unidad, int x, int y) {
        if (!estaDentroDelTablero(x, y)) return false;

        Casilla destino = getCasilla(x, y);
        if (destino.estaOcupada()) return false;

        int costo = destino.getCostoMovimiento() + destino.getModificadorMovimiento();
        return unidad.getRangoMovimiento() >= costo;
    }

    public void moverUnidad(Unidad unidad, int destinoX, int destinoY) {
        if (!puedeMoverUnidad(unidad, destinoX, destinoY)) return;

        Casilla origen = getCasilla(unidad.getX(), unidad.getY());
        Casilla destino = getCasilla(destinoX, destinoY);

        origen.setUnidad(null);
        destino.setUnidad(unidad);
        unidad.setPosicion(destinoX, destinoY);
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }
}