package intentovideojuegoaparte.Unidades;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class IngenieroTest {

    @Test
    public void testConstructor() {
        Ingeniero i = new Ingeniero("Científicos");
        assertEquals("Ingeniero", i.getNombre());
        assertEquals("Científicos", i.getFaccion());
        assertTrue(i.getHP() > 0);
    }
}
