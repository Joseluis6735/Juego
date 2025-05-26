package intentovideojuegoaparte.Unidades;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;;

public class FilosofoTest {

    @Test
    public void testConstructor() {
        Filosofo f = new Filosofo("Letras");
        assertEquals("Filosofo", f.getNombre());
        assertEquals("Letras", f.getFaccion());
        assertTrue(f.getHP() > 0);
    }
}
