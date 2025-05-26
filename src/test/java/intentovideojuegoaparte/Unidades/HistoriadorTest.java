package intentovideojuegoaparte.Unidades;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class HistoriadorTest {

    @Test
    public void testConstructor() {
        Historiador h = new Historiador("Letras");
        assertEquals("Historiador", h.getNombre());
        assertEquals("Letras", h.getFaccion());
        assertTrue(h.getHP() > 0);
    }
}
