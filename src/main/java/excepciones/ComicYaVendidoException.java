package excepciones;

/**
 * Excepción lanzada cuando se intenta realizar una operación (e.g., venta o reserva)
 * sobre un cómic que ya ha sido vendido o está actualmente no disponible.
 */
public class ComicYaVendidoException extends Exception {

    /**
     * Construye una nueva ComicYaVendidoException con el mensaje de detalle especificado.
     *
     * @param mensaje El mensaje de detalle. El mensaje se guarda para ser recuperado posteriormente
     *                por el metodo {@link Throwable#getMessage()}.
     */
    public ComicYaVendidoException(String mensaje) {
        super(mensaje);
    }
}