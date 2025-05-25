package intentovideojuegoaparte;


import java.util.Arrays;

public class Tablero {
    private int filas;
    private int columnas;
    private Casilla[][] casillas;

    public Tablero(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        casillas = new Casilla[filas][columnas];
        // Inicializa casillas con valores por defecto
        for(int i = 0; i < filas; i++) {
            for(int j = 0; j < columnas; j++) {
                casillas[i][j] = new Casilla(i, j, 1, 0, 0);
            }
        }
    }


    public Casilla getCasilla(int x, int y) {
        if(x < 0 || y < 0 || x >= filas || y >= columnas) return null;
        return casillas[x][y];
    }
    public void setCasilla(int x, int y, Casilla casilla) {
        if(x < 0 || y < 0 || x >= filas || y >= columnas) return;
    }

    public boolean posicionValida(int x, int y) {
        return x >= 0 && y >= 0 && x < filas && y < columnas;
    }

    public boolean moverUnidad(Unidad u, int destX, int destY) {
        if(!posicionValida(destX, destY)) return false;
        Casilla origen = getCasilla(u.getPosX(), u.getPosY());
        Casilla destino = getCasilla(destX, destY);

        if(destino == null || !destino.estaLibre()) return false;

        int distancia = Math.abs(destX - u.getPosX()) + Math.abs(destY - u.getPosY());
        if(distancia > u.getRangoMovimiento()) return false;

        // Mover unidad
        origen.setUnidad(null);
        destino.setUnidad(u);
        u.setPosicion(destX, destY);
        return true;
    }
}
