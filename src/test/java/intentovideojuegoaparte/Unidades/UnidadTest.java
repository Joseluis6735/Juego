package intentovideojuegoaparte.Unidades;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UnidadTest {

    private Unidad atacante;
    private Unidad defensor;

    @BeforeEach
    public void setUp() {
        atacante = new Ingeniero("Científicos");
        defensor = new Ingeniero("Letras");
        atacante.setPosicion(0, 0);
        defensor.setPosicion(1, 1);
    }

    @Test
    public void testSetPosicion() {
        atacante.setPosicion(3, 4);
        assertEquals(3, atacante.getX());
        assertEquals(4, atacante.getY());
    }

    @Test
    public void testRecibirDanio() {
        defensor.recibirDanio(30);
        assertEquals(70, defensor.getHP());
    }

    @Test
    public void testNoHPNegativo() {
        defensor.recibirDanio(999);
        assertEquals(0, defensor.getHP());
    }

    @Test
    public void testEstaViva() {
        assertTrue(defensor.estaViva());
        defensor.recibirDanio(100);
        assertFalse(defensor.estaViva());
    }

    @Test
    public void testAtacar_ReduceHP() {
        int hpAntes = defensor.getHP();
        atacante.atacar(defensor);
        assertTrue(defensor.getHP() <= hpAntes);
    }
}
