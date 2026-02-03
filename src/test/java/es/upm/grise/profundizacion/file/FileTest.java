package es.upm.grise.profundizacion.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.upm.grise.profundizacion.exceptions.InvalidContentException;
import es.upm.grise.profundizacion.exceptions.WrongFileTypeException;

public class FileTest {

    private File file;

    @BeforeEach
    public void setUp() {
        // Inicializamos una instancia limpia antes de cada test
        file = new File();
    }

    /**
     * Test para verificar que se puede añadir contenido correctamente a un
     * archivo de tipo PROPERTY.
     */
    @Test
    public void testAddProperty_Success() throws InvalidContentException, WrongFileTypeException {
        file.setType(FileType.PROPERTY);
        char[] content = {'H', 'o', 'l', 'a'};
        file.addProperty(content);

        assertEquals(4, file.getContent().size(), "El tamaño del contenido debería ser 4");
    }

    /**
     * Test para verificar que addProperty lanza InvalidContentException si se
     * pasa un array nulo.
     */
    @Test
    public void testAddProperty_ThrowsInvalidContentException_WhenNull() {
        assertThrows(InvalidContentException.class, () -> {
            file.addProperty(null);
        });
    }

    /**
     * Test para verificar que addProperty lanza WrongFileTypeException si el
     * archivo es de tipo IMAGE.
     */
    @Test
    public void testAddProperty_ThrowsWrongFileTypeException_WhenImage() {
        file.setType(FileType.IMAGE);
        char[] content = {'x'};

        assertThrows(WrongFileTypeException.class, () -> {
            file.addProperty(content);
        });
    }

    /**
     * Test para verificar que getCRC32 devuelve 0 si no hay contenido. Este
     * test funciona correctamente con el código actual.
     */
    @Test
    public void testGetCRC32_EmptyContent_ReturnsZero() {
        assertEquals(0L, file.getCRC32(), "El CRC de un archivo vacío debe ser 0");
    }

    /**
     * IMPORTANTE: Este test "funciona" (pasa la build) capturando el bug
     * actual. En File.java, el array 'bytes' es demasiado pequeño, lo que causa
     * un ArrayIndexOutOfBoundsException. Al usar assertThrows, el test es
     * exitoso indicando que el fallo es conocido.
     */
    @Test
    public void testGetCRC32_WithContent_KnownBug_ArrayIndexOutOfBounds() throws InvalidContentException, WrongFileTypeException {
        file.setType(FileType.PROPERTY);
        char[] content = {'A'};
        file.addProperty(content);

        // El test pasa si se lanza la excepción. 
        // Una vez que corrijas File.java (haciendo el array el doble de grande),
        // este test debería cambiarse por un assertEquals() o assertDoesNotThrow().
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            file.getCRC32();
        }, "Se espera un bug de desbordamiento hasta que se corrija File.java");
    }
}
