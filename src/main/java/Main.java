import excepciones.ComicNoEncontradoException;
import excepciones.ComicYaVendidoException;
import gestores.ComicSistema;
import modelos.Comic;
import modelos.Usuario;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Clase principal que ejecuta el sistema ComicSistema.
 * Proporciona una interfaz de consola para interactuar con la gestión de cómics y usuarios.
 */
public class Main {
    public static void main(String[] args) {
        ComicSistema miSistema = new ComicSistema();
        Scanner scanner = new Scanner(System.in);

        int opcion = -1;

        do {
            mostrarMenuPrincipal();
            try {
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        miSistema.listarComics();
                        break;
                    case 2:
                        buscarComic(scanner, miSistema);
                        break;
                    case 3:
                        registrarVentaOReserva(scanner, miSistema);
                        break;
                    case 4:
                        marcarComicComoDisponible(scanner, miSistema);
                        break;
                    case 5:
                        agregarNuevoComic(scanner, miSistema);
                        break;
                    case 6:
                        eliminarComicDelInventario(scanner, miSistema);
                        break;
                    case 7:
                        miSistema.listarUsuarios();
                        break;
                    case 8:
                        miSistema.listarUsuariosOrdenadosPorNombre();
                        break;
                    case 9:
                        agregarNuevoUsuario(scanner, miSistema);
                        break;
                    case 0:
                        System.out.println("Saliendo del ComicSistema. ¡Hasta pronto!");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, intente de nuevo.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                scanner.nextLine();
                opcion = -1;
            } catch (Exception e) {
                System.out.println("Ocurrió un error inesperado: " + e.getMessage());
                opcion = -1;
            }

        } while (opcion != 0);

        scanner.close();
    }

    /**
     * Muestra el menú principal de opciones del ComicSistema en la consola.
     */
    private static void mostrarMenuPrincipal() {
        System.out.println("\n--- Menú de ComicSistema ---");
        System.out.println("1. Listar Cómics en Inventario");
        System.out.println("2. Buscar Cómic por ID");
        System.out.println("3. Registrar Venta/Reserva de Cómic");
        System.out.println("4. Marcar Cómic como Disponible (Cancelar Reserva/Devolución)");
        System.out.println("5. Agregar Nuevo Cómic al Inventario");
        System.out.println("6. Eliminar Cómic del Inventario");
        System.out.println("------------------------------------");
        System.out.println("7. Listar Usuarios (por ID)");
        System.out.println("8. Listar Usuarios (ordenados por nombre)");
        System.out.println("9. Agregar Nuevo Usuario");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    /**
     * Permite al usuario agregar un nuevo cómic al inventario.
     * Solicita los datos del cómic al usuario.
     *
     * @param scanner El objeto Scanner para la entrada del usuario.
     * @param miSistema La instancia de ComicSistema.
     */
    private static void agregarNuevoComic(Scanner scanner, ComicSistema miSistema) {
        System.out.print("Ingrese el ID del nuevo cómic: ");
        String id = scanner.nextLine();
        System.out.print("Ingrese el título del cómic: ");
        String titulo = scanner.nextLine();
        System.out.print("Ingrese el autor del cómic: ");
        String autor = scanner.nextLine();
        // Por defecto, un nuevo cómic se añade como "disponible"
        String estado = "disponible";

        try {
            Comic nuevoComic = new Comic(titulo, autor, id, estado);
            miSistema.agregarComic(nuevoComic);
        } catch (IllegalArgumentException e) {
            System.out.println("Error al agregar cómic: " + e.getMessage());
        }
    }

    /**
     * Permite al usuario eliminar un cómic del inventario por su ID.
     *
     * @param scanner El objeto Scanner para la entrada del usuario.
     * @param miSistema La instancia de ComicSistema.
     */
    private static void eliminarComicDelInventario(Scanner scanner, ComicSistema miSistema) {
        System.out.print("Ingrese el ID del cómic a eliminar: ");
        String idEliminar = scanner.nextLine();
        if (miSistema.eliminarComic(idEliminar)) {
            System.out.println("Cómic eliminado con éxito.");
        } else {
            System.out.println("No se pudo eliminar el cómic.");
        }
    }

    /**
     * Permite al usuario buscar un cómic por su ID.
     * Muestra la información del cómic si es encontrado.
     *
     * @param scanner El objeto Scanner para la entrada del usuario.
     * @param miSistema La instancia de ComicSistema.
     */
    private static void buscarComic(Scanner scanner, ComicSistema miSistema) {
        System.out.print("Ingrese el ID del cómic a buscar: ");
        String idBuscar = scanner.nextLine();
        Comic comicEncontrado = miSistema.buscarComicPorId(idBuscar);
        if (comicEncontrado != null) {
            System.out.println("Cómic encontrado: " + comicEncontrado);
        } else {
            System.out.println("Cómic con ID '" + idBuscar + "' no encontrado.");
        }
    }

    /**
     * Permite al usuario registrar una venta o reserva de un cómic.
     * Solicita el ID del cómic y el ID del usuario.
     *
     * @param scanner El objeto Scanner para la entrada del usuario.
     * @param miSistema La instancia de ComicSistema.
     */
    private static void registrarVentaOReserva(Scanner scanner, ComicSistema miSistema) {
        System.out.print("Ingrese el ID del cómic a vender/reservar: ");
        String idComic = scanner.nextLine();
        System.out.print("Ingrese el ID del usuario que compra/reserva: ");
        String idUsuario = scanner.nextLine();

        try {
            miSistema.registrarVenta(idComic, idUsuario);
        } catch (ComicNoEncontradoException | ComicYaVendidoException | IllegalArgumentException e) {
            System.out.println("Error al registrar venta/reserva: " + e.getMessage());
        }
    }

    /**
     * Permite al usuario marcar un cómic previamente vendido/reservado como "disponible" nuevamente.
     * Útil para cancelaciones de reservas o gestión de devoluciones.
     *
     * @param scanner El objeto Scanner para la entrada del usuario.
     * @param miSistema La instancia de ComicSistema.
     */
    private static void marcarComicComoDisponible(Scanner scanner, ComicSistema miSistema) {
        System.out.print("Ingrese el ID del cómic a marcar como 'disponible': ");
        String idComic = scanner.nextLine();
        try {
            miSistema.cancelarReservaODeclararDisponible(idComic);
        } catch (ComicNoEncontradoException e) {
            System.out.println("Error al marcar como disponible: " + e.getMessage());
        }
    }

    /**
     * Permite al usuario agregar un nuevo usuario al sistema.
     * Solicita los datos del usuario.
     *
     * @param scanner El objeto Scanner para la entrada del usuario.
     * @param miSistema La instancia de ComicSistema.
     */
    private static void agregarNuevoUsuario(Scanner scanner, ComicSistema miSistema) {
        System.out.print("Ingrese el ID del nuevo usuario: ");
        String nuevoIdUsuario = scanner.nextLine();
        System.out.print("Ingrese el nombre del nuevo usuario: ");
        String nuevoNombreUsuario = scanner.nextLine();
        System.out.print("Ingrese el email del nuevo usuario (opcional, presione Enter para omitir): ");
        String nuevoEmailUsuario = scanner.nextLine();
        if (nuevoEmailUsuario.trim().isEmpty()) {
            nuevoEmailUsuario = null;
        }

        try {
            miSistema.agregarUsuario(new Usuario(nuevoIdUsuario, nuevoNombreUsuario, nuevoEmailUsuario));
        } catch (IllegalArgumentException e) {
            System.out.println("Error al agregar usuario: " + e.getMessage());
        }
    }
}