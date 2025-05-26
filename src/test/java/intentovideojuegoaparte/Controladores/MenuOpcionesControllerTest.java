package intentovideojuegoaparte.Controladores;

import intentovideojuegoaparte.*;
import intentovideojuegoaparte.Unidades.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MenuOpcionesControllerTest {

    private MenuOpcionesController controller;

    @BeforeEach
    void setUp() {
        controller = new MenuOpcionesController();
        // No inicializamos UI components porque se requiere entorno JavaFX.
        // Pero podemos testear métodos puros o crear mocks si es necesario.
    }

    @Test
    void testCrearUnidadDesdeTipo_Cientificos() {
        Unidad ingeniero = controller.crearUnidadDesdeTipo("Ingeniero", "Científicos");
        assertNotNull(ingeniero);
        assertEquals("Científicos", ingeniero.getFaccion());
        assertTrue(ingeniero instanceof Ingeniero);

        Unidad matematico = controller.crearUnidadDesdeTipo("Matematico", "Científicos");
        assertNotNull(matematico);
        assertTrue(matematico instanceof Matematico);
    }

    @Test
    void testCrearUnidadDesdeTipo_Humanistas() {
        Unidad poeta = controller.crearUnidadDesdeTipo("Poeta", "Humanistas");
        assertNotNull(poeta);
        assertTrue(poeta instanceof Poeta);
        assertEquals("Humanistas", poeta.getFaccion());

        Unidad filosofo = controller.crearUnidadDesdeTipo("Filosofo", "Humanistas");
        assertNotNull(filosofo);
        assertTrue(filosofo instanceof Filosofo);
    }

    @Test
    void testCrearUnidadDesdeTipo_TipoDesconocido() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.crearUnidadDesdeTipo("Desconocido", "Científicos");
        });

        String expectedMessage = "Tipo de unidad desconocido";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // Método iniciarJuego depende mucho de JavaFX y controles.
    // Se podría testear si los campos están vacíos o nulos usando métodos auxiliares si se refactoriza el código.

    // Ejemplo básico para mostrar alerta si campos están vacíos (refactorización recomendada)
    // Aquí solo un ejemplo conceptual, no ejecutable sin entorno JavaFX:
    /*
    @Test
    void testIniciarJuegoCamposVacios() {
        controller.campoFilas = mock(TextField.class);
        controller.campoColumnas = mock(TextField.class);
        when(controller.campoFilas.getText()).thenReturn("");
        when(controller.campoColumnas.getText()).thenReturn("");

        controller.iniciarJuego();

        // Aquí deberías verificar que mostrarAlerta fue llamada, para eso
        // mostrarAlerta debería ser sobrescribible o usar un spy.
    }
    */

}
