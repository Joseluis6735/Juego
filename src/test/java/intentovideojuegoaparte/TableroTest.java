package intentovideojuegoaparte;

import intentovideojuegoaparte.Unidades.Unidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TableroTest {

    private Tablero tablero;
    private Unidad unidad;

    @BeforeEach
    void setUp() {
        tablero = new Tablero(5, 5); // tablero 5x5
        unidad = new UnidadStub("Test", 100, 10, 5, 3, 1, "Habilidad");
        unidad.setPosicion(2, 2); // posición inicial
        tablero.getCasilla(2, 2).setUnidad(unidad);
    }

    @Test
    void testInicializacionCasillas() {
        for (int i = 0; i < tablero.getFilas(); i++) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                assertNotNull(tablero.getCasilla(i, j));
            }
        }
    }

    @Test
    void testEstaDentroDelTablero() {
        assertTrue(tablero.estaDentroDelTablero(0, 0));
        assertTrue(tablero.estaDentroDelTablero(4, 4));
        assertFalse(tablero.estaDentroDelTablero(-1, 0));
        assertFalse(tablero.estaDentroDelTablero(5, 5));
    }

    @Test
    void testGetSetCasilla() {
        Casilla nueva = new Casilla(1, 0, 0);
        tablero.setCasilla(1, 1, nueva);
        assertEquals(nueva, tablero.getCasilla(1, 1));
    }

    @Test
    void testPuedeMoverUnidadAUnLugarVacioConCostoAdecuado() {
        // Se busca una casilla libre con costo que la unidad pueda pagar
        for (int i = 0; i < tablero.getFilas(); i++) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                Casilla c = tablero.getCasilla(i, j);
                int costo = c.getCostoMovimiento() + c.getModificadorMovimiento();
                if (!c.estaOcupada() && costo <= unidad.getRangoMovimiento()) {
                    assertTrue(tablero.puedeMoverUnidad(unidad, i, j));
                    return; // Solo verificamos una válida
                }
            }
        }
    }

    @Test
    void testNoPuedeMoverUnidadAFueraDelTablero() {
        assertFalse(tablero.puedeMoverUnidad(unidad, -1, -1));
        assertFalse(tablero.puedeMoverUnidad(unidad, 10, 10));
    }

    @Test
    void testNoPuedeMoverUnidadACasillaOcupada() {
        Unidad otra = new UnidadStub("Otra", 100, 10, 5, 3, 1, "Otra hab");
        tablero.getCasilla(1, 1).setUnidad(otra);
        assertFalse(tablero.puedeMoverUnidad(unidad, 1, 1));
    }

    @Test
    void testMoverUnidadConExito() {
        // Buscar casilla donde se pueda mover
        for (int i = 0; i < tablero.getFilas(); i++) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                Casilla c = tablero.getCasilla(i, j);
                int costo = c.getCostoMovimiento() + c.getModificadorMovimiento();
                if (!c.estaOcupada() && costo <= unidad.getRangoMovimiento()) {
                    tablero.moverUnidad(unidad, i, j);

                    assertEquals(unidad, tablero.getCasilla(i, j).getUnidad());
                    assertNull(tablero.getCasilla(2, 2).getUnidad()); // la original se vacía
                    assertEquals(i, unidad.getX());
                    assertEquals(j, unidad.getY());
                    return;
                }
            }
        }
        fail("No se encontró casilla adecuada para mover");
    }

    // Stub de Unidad para las pruebas
    static class UnidadStub extends Unidad {
        public UnidadStub(String nombre, int hp, int ataque, int defensa, int rangoMovimiento, int rangoAtaque, String habilidad) {
            super(nombre, hp, ataque, defensa, rangoMovimiento, rangoAtaque, habilidad);
        }
    }
}
