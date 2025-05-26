package intentovideojuegoaparte.Unidades;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MatematicoTest {

    @Test
    public void testConstructor() {
        Matematico m = new Matematico("Científicos");
        assertEquals("Matemático", m.getNombre());
        assertEquals("Científicos", m.getFaccion());
        assertTrue(m.getHP() > 0);
    }
}
