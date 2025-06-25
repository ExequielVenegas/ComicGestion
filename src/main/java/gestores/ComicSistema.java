package gestores;

import constantes.Constantes;
import modelos.Comic;
import modelos.Usuario;
import excepciones.ComicNoEncontradoException;
import excepciones.ComicYaVendidoException;
import utilidades.CsvUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Clase principal que gestiona el inventario de cómics y la información de usuarios en el sistema ComicSistema.
 * Se encarga de las operaciones de agregar, eliminar, buscar, vender/reservar cómics,
 * así como de gestionar usuarios y la persistencia de datos a través de archivos CSV y logs.
 */
public class ComicSistema {
    private ArrayList<Comic> comics;
    private HashMap<String, Usuario> usuarios;
    private HashSet<String> idsComicsRegistrados;
    private HashSet<String> emailsRegistrados;

    /**
     * Constructor de ComicSistema.
     * Inicializa las colecciones de cómics y usuarios, y carga los datos existentes
     * desde los archivos CSV al iniciar el sistema.
     */
    public ComicSistema() {
        this.comics = new ArrayList<>();
        this.usuarios = new HashMap<>();
        this.idsComicsRegistrados = new HashSet<>();
        this.emailsRegistrados = new HashSet<>();
        cargarComicsDesdeCSV();
        cargarUsuariosDesdeCSV();
    }

    // --- Métodos de Gestión de Cómics ---

    /**
     * Agrega un nuevo cómic al inventario del sistema.
     * Si ya existe un cómic con el mismo ID, no se añade y se imprime un mensaje.
     *
     * @param comic El objeto Comic a agregar.
     */
    public void agregarComic(Comic comic) {
        if (idsComicsRegistrados.contains(comic.getId())) {
            System.out.println("El cómic con ID: " + comic.getId() + " ya existe. No se puede registrar el mismo ID nuevamente.");
            return;
        }
        this.comics.add(comic);
        this.idsComicsRegistrados.add(comic.getId());
        System.out.println("Cómic '" + comic.getTitulo() + "' (ID: " + comic.getId() + ") agregado al inventario.");
        guardarInventario(); // Guarda los cambios inmediatamente
    }

    /**
     * Elimina un cómic del inventario basándose en su ID.
     *
     * @param id El ID del cómic a eliminar.
     * @return true si el cómic fue encontrado y eliminado, false en caso contrario.
     */
    public boolean eliminarComic(String id) {
        Comic comicAEliminar = null;
        for (Comic comic : comics) {
            if (comic.getId().equalsIgnoreCase(id)) {
                comicAEliminar = comic;
                break;
            }
        }

        if (comicAEliminar != null) {
            // Verificar si el cómic está vendido/reservado antes de eliminar (opcional, dependiendo de la lógica de negocio)
            if ("vendido".equalsIgnoreCase(comicAEliminar.getEstado()) || "reservado".equalsIgnoreCase(comicAEliminar.getEstado())) {
                System.out.println("No se puede eliminar el cómic con ID '" + id + "' porque está actualmente vendido/reservado.");
                return false;
            }

            comics.remove(comicAEliminar);
            idsComicsRegistrados.remove(comicAEliminar.getId());
            System.out.println("Cómic '" + comicAEliminar.getTitulo() + "' (ID: " + comicAEliminar.getId() + ") eliminado del inventario.");
            guardarInventario(); // Guarda los cambios inmediatamente
            return true;
        } else {
            System.out.println("Cómic con ID '" + id + "' no encontrado para eliminar.");
            return false;
        }
    }

    /**
     * Busca un cómic por su ID.
     *
     * @param id El ID del cómic a buscar.
     * @return El objeto Comic si es encontrado, o null si no existe.
     */
    public Comic buscarComicPorId(String id) {
        for (Comic comic : comics) {
            if (comic.getId().equalsIgnoreCase(id)) {
                return comic;
            }
        }
        return null;
    }

    /**
     * Lista todos los cómics disponibles en el inventario.
     * Si no hay cómics, imprime un mensaje indicándolo.
     */
    public void listarComics() {
        if (comics.isEmpty()) {
            System.out.println("El inventario de cómics está vacío.");
            return;
        }
        System.out.println("\n--- Listado de Cómics en Inventario ---");
        for (Comic comic : comics) {
            System.out.println(comic);
        }
        System.out.println("---------------------------------------");
    }

    /**
     * Registra una venta o reserva de un cómic a un usuario.
     * Cambia el estado del cómic a "vendido" y registra la transacción.
     *
     * @param idComic El ID del cómic a vender/reservar.
     * @param idUsuario El ID del usuario que realiza la compra/reserva.
     * @throws ComicNoEncontradoException Si el cómic no existe en el inventario.
     * @throws ComicYaVendidoException Si el cómic ya ha sido vendido o reservado.
     * @throws IllegalArgumentException Si el usuario no existe.
     */
    public void registrarVenta(String idComic, String idUsuario) throws ComicNoEncontradoException, ComicYaVendidoException {
        Comic comic = buscarComicPorId(idComic);
        if (comic == null) {
            throw new ComicNoEncontradoException("El cómic con ID '" + idComic + "' no se encuentra en el inventario.");
        }

        if ("vendido".equalsIgnoreCase(comic.getEstado()) || "reservado".equalsIgnoreCase(comic.getEstado())) {
            throw new ComicYaVendidoException("El cómic '" + comic.getTitulo() + "' (ID: " + comic.getId() + ") ya ha sido vendido o reservado.");
        }

        Usuario usuario = buscarUsuarioPorId(idUsuario);
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario con ID '" + idUsuario + "' no existe. Por favor, registre al usuario primero.");
        }

        comic.setEstado("vendido"); // O "reservado" dependiendo de la lógica de negocio
        guardarInventario(); // Guarda el cambio de estado del cómic

        guardarDetallesVenta(comic, usuario);
        System.out.println("Venta/Reserva registrada exitosamente: '" + comic.getTitulo() + "' (ID: " + comic.getId() + ") a " + usuario.getNombre() + " (ID: " + usuario.getId() + ").");
    }

    /**
     * Cancela una reserva o marca un cómic como disponible después de una devolución.
     * Cambia el estado del cómic a "disponible".
     *
     * @param idComic El ID del cómic a marcar como disponible.
     * @throws ComicNoEncontradoException Si el cómic no existe en el inventario.
     * @throws IllegalArgumentException Si el cómic ya está disponible.
     */
    public void cancelarReservaODeclararDisponible(String idComic) throws ComicNoEncontradoException {
        Comic comic = buscarComicPorId(idComic);
        if (comic == null) {
            throw new ComicNoEncontradoException("El cómic con ID '" + idComic + "' no se encuentra en el inventario.");
        }

        if ("disponible".equalsIgnoreCase(comic.getEstado())) {
            System.out.println("El cómic '" + comic.getTitulo() + "' (ID: " + comic.getId() + ") ya está disponible.");
            return;
        }

        comic.setEstado("disponible");
        guardarInventario(); // Guarda el cambio de estado
        guardarDetallesDevolucion(comic); // Adaptar este log si es necesario
        System.out.println("Cómic '" + comic.getTitulo() + "' (ID: " + comic.getId() + ") ahora está 'disponible'.");
    }


    // --- Métodos de Persistencia de Datos (CSV y Logs) ---

    /**
     * Carga el inventario de cómics desde el archivo CSV especificado en {@link Constantes#COMICS_CSV}.
     * Limpia el inventario actual y los IDs registrados antes de cargar.
     */
    private void cargarComicsDesdeCSV() {
        comics.clear();
        idsComicsRegistrados.clear();
        try {
            List<Comic> comicsCargados = CsvUtil.leerCsv(Constantes.COMICS_CSV, campos -> {
                if (campos.length >= 4) {
                    String id = campos[0].trim();
                    String titulo = campos[1].trim();
                    String autor = campos[2].trim();
                    String estado = campos[3].trim();
                    idsComicsRegistrados.add(id);
                    return new Comic(titulo, autor, id, estado);
                }
                return null; // En caso de línea mal formada
            });
            // Filtrar nulos si existen y añadir a la lista principal
            comicsCargados.stream().filter(Objects::nonNull).forEach(comics::add);
            System.out.println("Inventario de cómics cargado exitosamente desde " + Constantes.COMICS_CSV);
        } catch (IOException e) {
            System.out.println("Advertencia: No se pudo cargar el inventario de cómics desde " + Constantes.COMICS_CSV + ". Se iniciará con el inventario vacío. Mensaje: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error de datos en " + Constantes.COMICS_CSV + ": " + e.getMessage());
        }
    }

    /**
     * Guarda el inventario actual de cómics en el archivo CSV especificado en {@link Constantes#COMICS_CSV}.
     */
    public void guardarInventario() {
        try {
            CsvUtil.escribirCsv(Constantes.COMICS_CSV, comics, comic ->
                            comic.getId() + "," + comic.getTitulo() + "," + comic.getAutor() + "," + comic.getEstado(),
                    "ID,Titulo,Autor,Estado"
            );
        } catch (IOException e) {
            System.out.println("Error al guardar el inventario de cómics: " + e.getMessage());
        }
    }

    /**
     * Registra los detalles de una venta o reserva de cómic en un archivo de log.
     *
     * @param comic El cómic que fue vendido/reservado.
     * @param usuario El usuario que realizó la compra/reserva.
     */
    private void guardarDetallesVenta(Comic comic, Usuario usuario) {
        try (FileWriter writer = new FileWriter(Constantes.VENTAS_LOG_TXT, true)) { // 'true' para modo append
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String fechaHora = LocalDateTime.now().format(formatter);
            String logEntry = String.format("VENTA/RESERVA - Fecha/Hora: %s, Cómic ID: %s, Título: %s, Usuario ID: %s, Nombre Usuario: %s%n",
                    fechaHora, comic.getId(), comic.getTitulo(), usuario.getId(), usuario.getNombre());
            writer.write(logEntry);
        } catch (IOException e) {
            System.out.println("Error al registrar detalles de venta/reserva: " + e.getMessage());
        }
    }

    /**
     * Registra los detalles de una devolución o cambio de estado a "disponible" de un cómic en un archivo de log.
     *
     * @param comic El cómic que se marcó como disponible.
     */
    private void guardarDetallesDevolucion(Comic comic) {
        try (FileWriter writer = new FileWriter(Constantes.VENTAS_LOG_TXT, true)) { // 'true' para modo append
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String fechaHora = LocalDateTime.now().format(formatter);
            String logEntry = String.format("DISPONIBLE - Fecha/Hora: %s, Cómic ID: %s, Título: %s, Estado anterior: %s, Estado actual: %s%n",
                    fechaHora, comic.getId(), comic.getTitulo(), "vendido/reservado", comic.getEstado()); // Asume estado anterior
            writer.write(logEntry);
        } catch (IOException e) {
            System.out.println("Error al registrar detalles de disponibilidad: " + e.getMessage());
        }
    }

    // --- Métodos de Gestión de Usuarios ---

    /**
     * Agrega un nuevo usuario al sistema.
     * Valida que el ID y el email del usuario (si se proporciona) no estén ya registrados.
     *
     * @param usuario El objeto Usuario a agregar.
     */
    public void agregarUsuario(Usuario usuario) {
        if (usuarios.containsKey(usuario.getId())) {
            System.out.println("El usuario con ID: " + usuario.getId() + " ya existe. No se puede agregar.");
            return;
        }
        if (usuario.getEmail() != null && !usuario.getEmail().isEmpty() && emailsRegistrados.contains(usuario.getEmail())) {
            System.out.println("El email: " + usuario.getEmail() + " ya está registrado por otro usuario. No se puede agregar.");
            return;
        }

        usuarios.put(usuario.getId(), usuario);
        if (usuario.getEmail() != null && !usuario.getEmail().isEmpty()) {
            emailsRegistrados.add(usuario.getEmail());
        }
        System.out.println("Usuario '" + usuario.getNombre() + "' (ID: " + usuario.getId() + ") agregado.");
        guardarUsuarios(); // Guarda los cambios inmediatamente
    }

    /**
     * Carga la información de los usuarios desde el archivo CSV especificado en {@link Constantes#USUARIOS_CSV}.
     * Limpia la colección de usuarios y emails registrados antes de cargar.
     */
    private void cargarUsuariosDesdeCSV() {
        usuarios.clear();
        emailsRegistrados.clear();
        try {
            List<Usuario> usuariosCargados = CsvUtil.leerCsv(Constantes.USUARIOS_CSV, campos -> {
                if (campos.length >= 2) { // ID, Nombre, Email (opcional)
                    String id = campos[0].trim();
                    String nombre = campos[1].trim();
                    String email = campos.length > 2 && !campos[2].trim().isEmpty() ? campos[2].trim() : null;
                    if (email != null) {
                        emailsRegistrados.add(email);
                    }
                    return new Usuario(id, nombre, email);
                }
                return null;
            });
            usuariosCargados.stream().filter(Objects::nonNull).forEach(usuario -> usuarios.put(usuario.getId(), usuario));
            System.out.println("Usuarios cargados exitosamente desde " + Constantes.USUARIOS_CSV);
        } catch (IOException e) {
            System.out.println("Advertencia: No se pudo cargar usuarios desde " + Constantes.USUARIOS_CSV + ". Se iniciará con usuarios vacíos. Mensaje: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error de datos en " + Constantes.USUARIOS_CSV + ": " + e.getMessage());
        }
    }

    /**
     * Guarda la información actual de los usuarios en el archivo CSV especificado en {@link Constantes#USUARIOS_CSV}.
     */
    public void guardarUsuarios() {
        try {
            CsvUtil.escribirCsv(Constantes.USUARIOS_CSV, new ArrayList<>(usuarios.values()), usuario ->
                            usuario.getId() + "," + usuario.getNombre() + "," + (usuario.getEmail() != null ? usuario.getEmail() : ""),
                    "ID,Nombre,Email"
            );
        } catch (IOException e) {
            System.out.println("Error al guardar usuarios: " + e.getMessage());
        }
    }

    /**
     * Busca un usuario por su ID.
     *
     * @param id El ID del usuario a buscar.
     * @return El objeto Usuario si es encontrado, o null si no existe.
     */
    public Usuario buscarUsuarioPorId(String id) {
        return usuarios.get(id);
    }

    /**
     * Elimina un usuario del sistema basándose en su ID.
     * También elimina su email de la lista de emails registrados si lo tenía.
     *
     * @param id El ID del usuario a eliminar.
     * @return true si el usuario fue encontrado y eliminado, false en caso contrario.
     */
    public boolean eliminarUsuario(String id) {
        Usuario usuarioEliminado = usuarios.remove(id); // Elimina del HashMap
        if (usuarioEliminado != null) {
            if (usuarioEliminado.getEmail() != null && !usuarioEliminado.getEmail().isEmpty()) {
                emailsRegistrados.remove(usuarioEliminado.getEmail()); // Elimina el email del HashSet de emails registrados
            }
            System.out.println("Usuario '" + usuarioEliminado.getNombre() + "' (ID: " + usuarioEliminado.getId() + ") eliminado.");
            guardarUsuarios(); // Guarda los cambios inmediatamente
            return true;
        } else {
            System.out.println("Usuario con ID '" + id + "' no encontrado para eliminar.");
            return false;
        }
    }

    /**
     * Lista todos los usuarios registrados en el sistema, ordenados por su ID.
     * Si no hay usuarios, imprime un mensaje indicándolo.
     */
    public void listarUsuarios() {
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados en el sistema.");
            return;
        }
        System.out.println("\n--- Listado de Usuarios (por ID) ---");
        usuarios.values().stream()
                .sorted(Comparator.comparing(Usuario::getId)) // Ordena por ID
                .forEach(System.out::println);
        System.out.println("------------------------------------");
    }

    /**
     * Lista todos los usuarios registrados en el sistema, ordenados alfabéticamente por su nombre.
     * Utiliza un TreeSet para mantener el orden.
     * Si no hay usuarios, imprime un mensaje indicándolo.
     */
    public void listarUsuariosOrdenadosPorNombre() {
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados en el sistema.");
            return;
        }
        System.out.println("\n--- Listado de Usuarios (ordenados por nombre) ---");
        TreeSet<Usuario> usuariosOrdenados = new TreeSet<>(Comparator.comparing(Usuario::getNombre));
        usuariosOrdenados.addAll(usuarios.values()); // Añade todos los usuarios al TreeSet para que se ordenen
        usuariosOrdenados.forEach(System.out::println);
        System.out.println("-----------------------------------------------");
    }
}