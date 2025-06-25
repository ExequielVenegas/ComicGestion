# ComicCollectorSystem

Este proyecto es un sistema de gestión para una tienda de cómics y coleccionables, desarrollado en Java. Permite administrar el inventario de cómics, registrar usuarios y gestionar las ventas o reservas de coleccionables. La información de los cómics y usuarios se persiste en archivos CSV, y las transacciones se registran en un archivo de log.

## Características Principales

* **Gestión de Cómics:**
    * Agregar nuevos cómics al inventario.
    * Eliminar cómics existentes.
    * Buscar cómics por su ID único.
    * Listar todos los cómics disponibles.
    * Registrar ventas o reservas de cómics.
    * Marcar cómics como disponibles nuevamente (cancelación de reserva o devolución).
* **Gestión de Usuarios:**
    * Agregar nuevos usuarios al sistema.
    * Eliminar usuarios.
    * Buscar usuarios por su ID.
    * Listar usuarios (ordenados por ID o por nombre).
* **Persistencia de Datos:**
    * Los datos de cómics se guardan en `comics.csv`.
    * Los datos de usuarios se guardan en `usuarios.csv`.
    * Las transacciones de venta/reserva/disponibilidad se registran en `ventas_log.txt`.
* **Manejo de Excepciones:** Implementación de excepciones personalizadas para un manejo robusto de errores.
* **Uso de Colecciones:** Utiliza `ArrayList` para cómics, `HashMap` para usuarios y `HashSet`/`TreeSet` para validaciones y ordenación.

## Estructura del Proyecto

El proyecto está organizado en los siguientes paquetes:

* `modelos`: Contiene las clases que representan las entidades principales (`Comic`, `Usuario`).
* `excepciones`: Define las clases de excepción personalizadas (`ComicNoEncontradoException`, `ComicYaVendidoException`).
* `gestores`: Contiene la lógica principal del sistema (`ComicSistema`).
* `utilidades`: Proporciona clases de utilidad para operaciones comunes (`CsvUtil`).
* `constantes`: Almacena las constantes globales del sistema (rutas de archivos, etc.).
  
## Requisitos del Sistema

* Java Development Kit (JDK) 17 o superior.
* Entorno de Desarrollo Integrado (IDE) como Apache NetBeans, IntelliJ IDEA o Eclipse.

---
