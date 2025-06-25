package modelos;

import java.util.Objects;

/**
 * Representa un usuario en el sistema ComicSistema.
 * Un usuario tiene un ID único, un nombre y, opcionalmente, un correo electrónico.
 * Implementa {@code Comparable} para permitir la ordenación de usuarios por su ID.
 */
public class Usuario implements Comparable<Usuario> {
    private String id;
    private String nombre;
    private String email;

    /**
     * Constructor para crear una nueva instancia de Usuario.
     *
     * @param id El identificador único del usuario. No puede ser nulo.
     * @param nombre El nombre completo del usuario. No puede ser nulo.
     * @param email El correo electrónico del usuario. Puede ser nulo o vacío. Si se proporciona, se normaliza a minúsculas.
     * @throws NullPointerException Si el ID o el nombre son nulos.
     */
    public Usuario(String id, String nombre, String email) {
        this.id = Objects.requireNonNull(id, "El ID del usuario no puede ser nulo.").trim();
        this.nombre = Objects.requireNonNull(nombre, "El nombre del usuario no puede ser nulo.").trim();
        // Validar y normalizar el email
        if (email != null && !email.trim().isEmpty()) {
            this.email = email.trim().toLowerCase();
        } else {
            this.email = null; // Si es nulo o vacío después de trim, se almacena como null
        }
    }

    /**
     * Obtiene el identificador único del usuario.
     *
     * @return El ID del usuario.
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return El nombre del usuario.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return El correo electrónico del usuario, o {@code null} si no está especificado.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     *
     * @param email El nuevo correo electrónico. Puede ser nulo o vacío. Si se proporciona, se normaliza a minúsculas.
     */
    public void setEmail(String email) {
        if (email != null && !email.trim().isEmpty()) {
            this.email = email.trim().toLowerCase();
        } else {
            this.email = null;
        }
    }

    /**
     * Retorna una representación en cadena del objeto Usuario.
     *
     * @return Una cadena que contiene el ID, Nombre y Email del usuario.
     */
    @Override
    public String toString() {
        return "ID: '" + id + "', Nombre: '" + nombre + "', Email: '" + (email != null ? email : "N/A") + "'";
    }

    /**
     * Compara este objeto Usuario con el objeto especificado para verificar si son iguales.
     * Dos usuarios se consideran iguales si tienen el mismo ID (ignorando mayúsculas/minúsculas).
     *
     * @param o El objeto con el que se va a comparar.
     * @return true si los objetos son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return id.equalsIgnoreCase(usuario.id);
    }

    /**
     * Retorna un valor de código hash para el objeto.
     * El código hash se basa en el ID del usuario (en minúsculas para consistencia con {@code equals}).
     *
     * @return Un valor de código hash para este objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id.toLowerCase()); // Usar toLowerCase para que el hashCode sea consistente con equals
    }

    /**
     * Compara este objeto Usuario con el objeto Usuario especificado para su ordenación.
     * Los usuarios se ordenan alfabéticamente por su ID.
     *
     * @param otroUsuario El usuario con el que se va a comparar.
     * @return Un valor negativo, cero o un valor positivo si este usuario es menor que, igual a o mayor que el usuario especificado.
     */
    @Override
    public int compareTo(Usuario otroUsuario) {
        return this.id.compareTo(otroUsuario.id);
    }
}