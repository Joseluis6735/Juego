package intentovideojuegoaparte.Unidades;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class BiologoTest {

    @Test
    public void testConstructor() {
        Biologo b = new Biologo("Científicos");
        assertEquals("Biologo", b.getNombre());
        assertEquals("Científicos", b.getFaccion());
        assertTrue(b.getHP() > 0);
    }
}
