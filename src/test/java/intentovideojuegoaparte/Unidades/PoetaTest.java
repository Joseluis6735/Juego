package intentovideojuegoaparte.Unidades;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PoetaTest {

    @Test
    public void testConstructor() {
        Poeta p = new Poeta("Letras");
        assertEquals("Poeta", p.getNombre());
        assertEquals("Letras", p.getFaccion());
        assertTrue(p.getHP() > 0);
    }
}
