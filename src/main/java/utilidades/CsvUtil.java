package utilidades;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Clase de utilidad para manejar operaciones de lectura y escritura de archivos CSV.
 * Proporciona métodos genéricos para leer datos de un CSV y mapearlos a objetos,
 * y para escribir listas de objetos a un CSV.
 */
public class CsvUtil {

    /**
     * Constructor privado para evitar la instanciación de esta clase de utilidades.
     */
    private CsvUtil() {
    }

    /**
     * Lee datos de un archivo CSV y los mapea a una lista de objetos de un tipo específico.
     * Se salta la primera línea (considerada como cabecera) y las líneas vacías.
     *
     * @param rutaArchivo La ruta completa al archivo CSV a leer.
     * @param mapper Una función que toma un array de String (campos de una línea CSV)
     * y retorna un objeto del tipo {@code T}.
     * @param <T> El tipo de objetos que se crearán a partir de las líneas del CSV.
     * @return Una {@code List} de objetos del tipo {@code T} leídos del CSV.
     * @throws IOException Si ocurre un error de entrada/salida al leer el archivo.
     */
    public static <T> List<T> leerCsv(String rutaArchivo, Function<String[], T> mapper) throws IOException {
        List<T> registros = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            boolean primeraLinea = true; // Para saltar la cabecera si existe
            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue; // Saltar la primera línea (cabecera)
                }
                if (linea.trim().isEmpty()) {
                    continue; // Saltar líneas vacías
                }
                String[] campos = linea.split(","); // Asume que el delimitador es la coma
                registros.add(mapper.apply(campos));
            }
        }
        return registros;
    }

    /**
     * Escribe una lista de objetos a un archivo CSV.
     * Permite especificar una cabecera para la primera línea del archivo.
     *
     * @param rutaArchivo La ruta completa al archivo CSV donde se escribirán los datos.
     * @param datos La {@code List} de objetos del tipo {@code T} a escribir.
     * @param mapper Una función que toma un objeto del tipo {@code T} y retorna su representación
     * en formato de línea CSV (String).
     * @param header La cadena de cabecera a escribir como primera línea del CSV (puede ser null o vacío si no se desea cabecera).
     * @param <T> El tipo de objetos que se escribirán en el CSV.
     * @throws IOException Si ocurre un error de entrada/salida al escribir el archivo.
     */
    public static <T> void escribirCsv(String rutaArchivo, List<T> datos, Function<T, String> mapper, String header) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            if (header != null && !header.trim().isEmpty()) {
                bw.write(header);
                bw.newLine();
            }
            for (T dato : datos) {
                bw.write(mapper.apply(dato));
                bw.newLine();
            }
            bw.flush();
        }
    }
}