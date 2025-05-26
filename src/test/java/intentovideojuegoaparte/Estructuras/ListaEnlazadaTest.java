package intentovideojuegoaparte.Estructuras;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class ListaEnlazadaTest {

    private ListaEnlazada<String> lista;

    @BeforeEach
    void setUp() {
        lista = new ListaEnlazada<>();
    }

    @Test
    void testListaInicialmenteVacia() {
        assertTrue(lista.estaVacia());
        assertFalse(lista.contiene("A"));
    }

    @Test
    void testAgregarUnElemento() {
        lista.agregar("A");
        assertFalse(lista.estaVacia());
        assertTrue(lista.contiene("A"));
    }

    @Test
    void testAgregarMultiplesElementos() {
        lista.agregar("A");
        lista.agregar("B");
        lista.agregar("C");

        assertTrue(lista.contiene("A"));
        assertTrue(lista.contiene("B"));
        assertTrue(lista.contiene("C"));
    }

    @Test
    void testEliminarElementoCabeza() {
        lista.agregar("A");
        lista.agregar("B");
        lista.eliminar("A");
        assertFalse(lista.contiene("A"));
        assertTrue(lista.contiene("B"));
    }

    @Test
    void testEliminarElementoIntermedio() {
        lista.agregar("A");
        lista.agregar("B");
        lista.agregar("C");
        lista.eliminar("B");

        assertTrue(lista.contiene("A"));
        assertFalse(lista.contiene("B"));
        assertTrue(lista.contiene("C"));
    }

    @Test
    void testEliminarElementoNoExistente() {
        lista.agregar("A");
        lista.eliminar("Z");
        assertTrue(lista.contiene("A"));
    }

    @Test
    void testEliminarElementoUltimo() {
        lista.agregar("A");
        lista.agregar("B");
        lista.eliminar("B");

        assertTrue(lista.contiene("A"));
        assertFalse(lista.contiene("B"));
    }

    @Test
    void testIterador() {
        lista.agregar("X");
        lista.agregar("Y");
        lista.agregar("Z");

        Iterator<String> it = lista.iterator();
        assertTrue(it.hasNext());
        assertEquals("X", it.next());
        assertEquals("Y", it.next());
        assertEquals("Z", it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void testGetCabeza() {
        lista.agregar("A");
        lista.agregar("B");
        assertNotNull(lista.getCabeza());
        assertEquals("A", lista.getCabeza().valor);
    }
}
