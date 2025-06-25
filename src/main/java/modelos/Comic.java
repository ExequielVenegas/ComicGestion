package modelos;

import java.util.Objects;

/**
 * Representa un cómic, novela gráfica u otro coleccionable dentro del sistema de la tienda.
 * Almacena información como el título, autor, un identificador único (ID) y el estado de disponibilidad.
 */
public class Comic {
    private String titulo;
    private String autor;
    private String id;
    private String estado;

    /**
     * Constructor para crear una nueva instancia de Comic.
     *
     * @param titulo El título del cómic o coleccionable. No puede ser nulo.
     * @param autor El autor o creador del cómic. No puede ser nulo.
     * @param id El identificador único del cómic (e.g., ISBN para novelas gráficas, o un ID personalizado para cómics individuales). No puede ser nulo.
     * @param estado El estado actual del cómic (e.g., "disponible", "vendido", "reservado"). No puede ser nulo.
     * @throws NullPointerException Si alguno de los parámetros requeridos (titulo, autor, id, estado) es nulo.
     */
    public Comic(String titulo, String autor, String id, String estado) {
        this.titulo = Objects.requireNonNull(titulo, "Debe ingresar un título para el cómic.").trim();
        this.autor = Objects.requireNonNull(autor, "Debe ingresar el autor del cómic.").trim();
        this.id = Objects.requireNonNull(id, "Debe ingresar un ID único para el cómic.").trim();
        this.estado = Objects.requireNonNull(estado, "Debe ingresar el estado del cómic.").trim();
    }

    /**
     * Obtiene el título del cómic.
     *
     * @return El título del cómic.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Obtiene el autor del cómic.
     *
     * @return El autor del cómic.
     */
    public String getAutor() {
        return autor;
    }

    /**
     * Obtiene el estado actual del cómic ("disponible", "vendido", "reservado").
     *
     * @return El estado del cómic.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Obtiene el identificador único del cómic.
     *
     * @return El ID del cómic.
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el estado del cómic.
     *
     * @param estado El nuevo estado del cómic (e.g., "disponible", "vendido", "reservado").
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Establece el identificador único del cómic.
     *
     * @param id El nuevo ID del cómic.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retorna una representación en cadena del objeto Comic.
     *
     * @return Una cadena que contiene el ID, Título, Autor y Estado del cómic.
     */
    @Override
    public String toString() {
        return  "\nID: " + id +  "\nTÍTULO: " + titulo + "\nAUTOR: " + autor + "\nESTADO: " + estado + "\n";
    }
}