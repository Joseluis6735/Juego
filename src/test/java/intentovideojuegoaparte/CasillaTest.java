package intentovideojuegoaparte;

import intentovideojuegoaparte.Unidades.Unidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CasillaTest {

    private Casilla casilla;

    @BeforeEach
    void setUp() {
        casilla = new Casilla(2, 3, -1);  // valores arbitrarios
    }

    @Test
    void testConstructorYGetters() {
        assertEquals(2, casilla.getCostoMovimiento());
        assertEquals(3, casilla.getModificadorDefensa());
        assertEquals(-1, casilla.getModificadorMovimiento());
    }

    @Test
    void testCasillaInicialmenteVacia() {
        assertFalse(casilla.estaOcupada());
        assertNull(casilla.getUnidad());
    }

    @Test
    void testSetYGetUnidad() {
        Unidad unidad = new UnidadStub("Unidad de prueba");
        casilla.setUnidad(unidad);

        assertTrue(casilla.estaOcupada());
        assertEquals(unidad, casilla.getUnidad());
    }

    @Test
    void testQuitarUnidad() {
        Unidad unidad = new UnidadStub("Unidad temporal");
        casilla.setUnidad(unidad);

        casilla.setUnidad(null);
        assertFalse(casilla.estaOcupada());
        assertNull(casilla.getUnidad());
    }

    // Clase auxiliar para pruebas
    static class UnidadStub extends Unidad {
        public UnidadStub(String nombre) {
            super(nombre, 100, 10, 5, 3, 2, "Habilidad de prueba"); // argumentos ejemplo
        }

    }
}
