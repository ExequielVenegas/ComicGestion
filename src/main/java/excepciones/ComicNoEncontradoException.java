package excepciones;

/**
 * Excepción lanzada cuando un cómic o coleccionable específico no se encuentra
 * en el sistema de inventario de la tienda.
 */
public class ComicNoEncontradoException extends Exception {

    /**
     * Construye una nueva ComicNoEncontradoException con el mensaje de detalle especificado.
     *
     * @param mensaje El mensaje de detalle. El mensaje se guarda para ser recuperado posteriormente
     *                por el metodo {@link Throwable#getMessage()}.
     */
    public ComicNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}