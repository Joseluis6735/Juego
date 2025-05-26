package intentovideojuegoaparte.Unidades;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class FilologoTest {

    @Test
    public void testConstructor() {
        Filologo f = new Filologo("Letras");
        assertEquals("Filólogo", f.getNombre());
        assertEquals("Letras", f.getFaccion());
        assertTrue(f.getHP() > 0);
    }
}
