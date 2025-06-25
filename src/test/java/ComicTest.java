
import modelos.Comic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ComicTest {

    private Comic comicEjemplo;

    @BeforeEach
    void setUp() {
        comicEjemplo = new Comic("The Amazing Spider-Man #1", "Stan Lee", "ASM001", "disponible");
    }

    @Test
    @DisplayName("Test de constructor y getters")
    void testConstructorAndGetters() {
        assertEquals("The Amazing Spider-Man #1", comicEjemplo.getTitulo(), "El título debería coincidir");
        assertEquals("Stan Lee", comicEjemplo.getAutor(), "El autor debería coincidir");
        assertEquals("ASM001", comicEjemplo.getId(), "El ID debería coincidir");
        assertEquals("disponible", comicEjemplo.getEstado(), "El estado debería coincidir");
    }

    @Test
    @DisplayName("Test de constructor con parámetros nulos")
    void testConstructorNullParameters() {
        // Título nulo
        Exception exceptionTitulo = assertThrows(NullPointerException.class, () ->
                new Comic(null, "Autor", "ID123", "disponible"));
        assertTrue(exceptionTitulo.getMessage().contains("título"), "Mensaje de error para título nulo incorrecto.");

        // Autor nulo
        Exception exceptionAutor = assertThrows(NullPointerException.class, () ->
                new Comic("Titulo", null, "ID123", "disponible"));
        assertTrue(exceptionAutor.getMessage().contains("autor"), "Mensaje de error para autor nulo incorrecto.");

        // ID nulo
        Exception exceptionId = assertThrows(NullPointerException.class, () ->
                new Comic("Titulo", "Autor", null, "disponible"));
        assertTrue(exceptionId.getMessage().contains("ID único"), "Mensaje de error para ID nulo incorrecto.");

        // Estado nulo
        Exception exceptionEstado = assertThrows(NullPointerException.class, () ->
                new Comic("Titulo", "Autor", "ID123", null));
        assertTrue(exceptionEstado.getMessage().contains("estado"), "Mensaje de error para estado nulo incorrecto.");
    }

    @Test
    @DisplayName("Test de setters")
    void testSetters() {
        comicEjemplo.setEstado("vendido");
        assertEquals("vendido", comicEjemplo.getEstado(), "El estado debería cambiar a 'vendido'");

        comicEjemplo.setId("ASM001-REV");
        assertEquals("ASM001-REV", comicEjemplo.getId(), "El ID debería cambiar a 'ASM001-REV'");
    }

    @Test
    @DisplayName("Test de toString")
    void testToString() {
        String expectedString = "\nID: ASM001\nTÍTULO: The Amazing Spider-Man #1\nAUTOR: Stan Lee\nESTADO: disponible\n";
        assertEquals(expectedString, comicEjemplo.toString(), "El toString debería coincidir con el formato esperado.");
    }
}