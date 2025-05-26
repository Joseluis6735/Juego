package intentovideojuegoaparte.Estructuras;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ColaTest {

    private Cola<String> cola;

    @BeforeEach
    void setUp() {
        cola = new Cola<>();
    }

    @Test
    void testColaInicialmenteVacia() {
        assertTrue(cola.estaVacia());
        assertNull(cola.frente());
        assertNull(cola.desencolar());
    }

    @Test
    void testEncolarUnElemento() {
        cola.encolar("A");
        assertFalse(cola.estaVacia());
        assertEquals("A", cola.frente());
    }

    @Test
    void testEncolarYDesencolarUnElemento() {
        cola.encolar("A");
        String valor = cola.desencolar();
        assertEquals("A", valor);
        assertTrue(cola.estaVacia());
        assertNull(cola.frente());
    }

    @Test
    void testOrdenFIFO() {
        cola.encolar("A");
        cola.encolar("B");
        cola.encolar("C");

        assertEquals("A", cola.desencolar());
        assertEquals("B", cola.desencolar());
        assertEquals("C", cola.desencolar());
        assertTrue(cola.estaVacia());
    }

    @Test
    void testFrenteSinDesencolar() {
        cola.encolar("X");
        cola.encolar("Y");
        assertEquals("X", cola.frente()); // no debería eliminarlo
        assertEquals("X", cola.frente()); // sigue siendo el mismo
    }

    @Test
    void testDesencolarHastaVacia() {
        cola.encolar("Uno");
        cola.encolar("Dos");
        cola.desencolar();
        cola.desencolar();
        assertTrue(cola.estaVacia());
        assertNull(cola.frente());
        assertNull(cola.desencolar());
    }
}
