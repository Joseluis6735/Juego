package intentovideojuegoaparte.Unidades;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class FisicoTest {

    @Test
    public void testConstructor() {
        Fisico f = new Fisico("Científicos");
        assertEquals("Fisico", f.getNombre());
        assertEquals("Científicos", f.getFaccion());
        assertTrue(f.getHP() > 0);
    }
}

