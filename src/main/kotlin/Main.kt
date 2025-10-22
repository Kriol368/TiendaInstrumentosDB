package org.example

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

const val URL_BD = "jdbc:sqlite:src/main/resources/tiendaInstrumentos.sqlite"

fun main() {
    menu()
}

fun conectarBD(): Connection? {
    return try {
        DriverManager.getConnection(URL_BD)
    } catch (e: SQLException) {
        e.printStackTrace()
        null
    }
}

fun menu() {
    val scanner = Scanner(System.`in`)
    var opcion: Int

    do {
        println("\n=== MENÚ PRINCIPAL ===")
        println("1. Gestionar Instrumentos")
        println("2. Gestionar Categorías")
        println("3. Gestionar Proveedores")
        println("4. Salir")
        print("Seleccione una opción: ")

        opcion = try {
            scanner.nextInt()
        } catch (_: Exception) {
            -1
        }
        scanner.nextLine()

        when (opcion) {
            1 -> menuInstrumentos()
            2 -> menuCategorias()
            3 -> menuProveedores()
            4 -> println("Saliendo del programa...")
            else -> println("Introduce una opcion válida")
        }
    } while (opcion != 4)

    scanner.close()
}

// MENÚ INSTRUMENTOS

fun menuInstrumentos() {
    val scanner = Scanner(System.`in`)
    var opcion: Int

    do {
        println("\n=== GESTIÓN DE INSTRUMENTOS ===")
        println("1. Listar todos los instrumentos")
        println("2. Consultar instrumento por ID")
        println("3. Insertar nuevo instrumento")
        println("4. Actualizar instrumento")
        println("5. Eliminar instrumento")
        println("6. Volver al menú principal")
        print("Seleccione una opción: ")

        opcion = try {
            scanner.nextInt()
        } catch (_: Exception) {
            -1
        }
        scanner.nextLine()

        when (opcion) {
            1 -> listarInstrumentos()
            2 -> consultarInstrumentoPorId()
            3 -> insertarInstrumento()
            4 -> actualizarInstrumento()
            5 -> eliminarInstrumento()
            6 -> println("Volviendo al menú principal...")
            else -> println("Introduce una opcion válida")
        }
    } while (opcion != 6)
}

fun listarInstrumentos() {
    val instrumentos = InstrumentoDAO.listarInstrumentos()

    if (instrumentos.isEmpty()) {
        println("No se encontro ningun instrumento")
    } else {
        instrumentos.forEach { instrumento ->
            println(
                """
                |ID: ${instrumento.id}
                |Nombre: ${instrumento.nombre}
                |Fabricante: ${instrumento.fabricante}
                |Año: ${instrumento.anoFabricacion}
                |Precio: ${instrumento.precio}€
                |Categoría ID: ${instrumento.categoriaId}
                |Proveedor ID: ${instrumento.proveedorId}
                |-------------------------------
            """.trimMargin()
            )
        }
    }
}

fun consultarInstrumentoPorId() {
    print("Ingrese el ID del instrumento: ")

    val id = try {
        readlnOrNull()?.toInt() ?: -1
    } catch (_: Exception) {
        -1
    }

    if (id == -1) {
        println("ID debe ser un número válido")
        return
    }

    val instrumento = InstrumentoDAO.consultarInstrumentoPorId(id)

    if (instrumento != null) {
        println("\nInstrumento encontrado:")
        println(
            """
            |ID: ${instrumento.id}
            |Nombre: ${instrumento.nombre}
            |Fabricante: ${instrumento.fabricante}
            |Año: ${instrumento.anoFabricacion}
            |Precio: ${instrumento.precio}€
            |Categoría ID: ${instrumento.categoriaId}
            |Proveedor ID: ${instrumento.proveedorId}
        """.trimMargin()
        )
    } else {
        println("No se encontró ningún instrumento con ID: $id")
    }
}

fun insertarInstrumento() {
    print("Nombre: ")
    val nombre = readlnOrNull()?.trim() ?: ""

    print("Fabricante: ")
    val fabricante = readlnOrNull()?.trim() ?: ""

    print("Año de fabricación: ")
    val anoFabricacion = try {
        readlnOrNull()?.toInt() ?: 0
    } catch (_: Exception) {
        0
    }

    print("Precio: ")
    val precio = try {
        readlnOrNull()?.toDouble() ?: 0.0
    } catch (_: Exception) {
        0.0
    }

    print("ID de Categoría: ")
    val categoriaId = try {
        readlnOrNull()?.toInt() ?: 0
    } catch (_: Exception) {
        0
    }

    print("ID de Proveedor: ")
    val proveedorId = try {
        readlnOrNull()?.toInt() ?: 0
    } catch (_: Exception) {
        0
    }

    if (nombre.isEmpty() || fabricante.isEmpty() || anoFabricacion == 0 || precio == 0.0 || categoriaId == 0 || proveedorId == 0) {
        println("Todos los campos son obligatorios y deben ser válidos")
        return
    }

    val nuevoInstrumento = Instrumento(
        nombre = nombre,
        fabricante = fabricante,
        anoFabricacion = anoFabricacion,
        precio = precio,
        categoriaId = categoriaId,
        proveedorId = proveedorId
    )

    InstrumentoDAO.insertarInstrumento(nuevoInstrumento)
}

fun actualizarInstrumento() {
    print("Ingrese el ID del instrumento a actualizar: ")

    val id = try {
        readlnOrNull()?.toInt() ?: -1
    } catch (_: Exception) {
        -1
    }

    if (id == -1) {
        println("ID debe ser un número válido")
        return
    }

    val instrumentoExistente = InstrumentoDAO.consultarInstrumentoPorId(id)
    if (instrumentoExistente == null) {
        println("No existe un instrumento con ID: $id")
        return
    }

    println("\nInstrumento actual:")
    println(
        """
        |ID: ${instrumentoExistente.id}
        |Nombre: ${instrumentoExistente.nombre}
        |Fabricante: ${instrumentoExistente.fabricante}
        |Año: ${instrumentoExistente.anoFabricacion}
        |Precio: ${instrumentoExistente.precio}€
        |Categoría ID: ${instrumentoExistente.categoriaId}
        |Proveedor ID: ${instrumentoExistente.proveedorId}
    """.trimMargin()
    )

    println("\nIngrese los nuevos datos (deje vacío para mantener el valor actual):")

    print("Nuevo nombre [${instrumentoExistente.nombre}]: ")
    val nuevoNombre = readlnOrNull()?.trim()

    print("Nuevo fabricante [${instrumentoExistente.fabricante}]: ")
    val nuevoFabricante = readlnOrNull()?.trim()

    print("Nuevo año [${instrumentoExistente.anoFabricacion}]: ")
    val nuevoAnoStr = readlnOrNull()?.trim()

    print("Nuevo precio [${instrumentoExistente.precio}]: ")
    val nuevoPrecioStr = readlnOrNull()?.trim()

    print("Nueva categoría ID [${instrumentoExistente.categoriaId}]: ")
    val nuevaCategoriaStr = readlnOrNull()?.trim()

    print("Nuevo proveedor ID [${instrumentoExistente.proveedorId}]: ")
    val nuevoProveedorStr = readlnOrNull()?.trim()

    val instrumentoActualizado = Instrumento(
        id = id,
        nombre = if (!nuevoNombre.isNullOrBlank()) nuevoNombre else instrumentoExistente.nombre,
        fabricante = if (!nuevoFabricante.isNullOrBlank()) nuevoFabricante else instrumentoExistente.fabricante,
        anoFabricacion = if (!nuevoAnoStr.isNullOrBlank()) nuevoAnoStr.toInt() else instrumentoExistente.anoFabricacion,
        precio = if (!nuevoPrecioStr.isNullOrBlank()) nuevoPrecioStr.toDouble() else instrumentoExistente.precio,
        categoriaId = if (!nuevaCategoriaStr.isNullOrBlank()) nuevaCategoriaStr.toInt() else instrumentoExistente.categoriaId,
        proveedorId = if (!nuevoProveedorStr.isNullOrBlank()) nuevoProveedorStr.toInt() else instrumentoExistente.proveedorId
    )

    InstrumentoDAO.actualizarInstrumento(instrumentoActualizado)
}

fun eliminarInstrumento() {
    print("Ingrese el ID del instrumento a eliminar: ")

    val id = try {
        readlnOrNull()?.toInt() ?: -1
    } catch (_: Exception) {
        -1
    }

    if (id == -1) {
        println("ID debe ser un número válido")
        return
    }

    print("¿Está seguro de eliminar el instrumento con ID $id? (s/n): ")
    val confirmacion = readlnOrNull()?.trim()?.lowercase()

    if (confirmacion == "s") {
        InstrumentoDAO.eliminarInstrumento(id)
    } else {
        println("Operación cancelada")
    }
}

// MENÚ CATEGORÍAS

fun menuCategorias() {
    val scanner = Scanner(System.`in`)
    var opcion: Int

    do {
        println("\n=== GESTIÓN DE CATEGORÍAS ===")
        println("1. Listar todas las categorías")
        println("2. Consultar categoría por ID")
        println("3. Insertar nueva categoría")
        println("4. Actualizar categoría")
        println("5. Eliminar categoría")
        println("6. Volver al menú principal")
        print("Seleccione una opción: ")

        opcion = try {
            scanner.nextInt()
        } catch (_: Exception) {
            -1
        }
        scanner.nextLine()

        when (opcion) {
            1 -> listarCategorias()
            2 -> consultarCategoriaPorId()
            3 -> insertarCategoria()
            4 -> actualizarCategoria()
            5 -> eliminarCategoria()
            6 -> println("Volviendo al menú principal...")
            else -> println("Introduce una opcion válida")
        }
    } while (opcion != 6)
}

fun listarCategorias() {
    val categorias = CategoriaDAO.listarCategorias()

    if (categorias.isEmpty()) {
        println("No se encontraron categorías")
    } else {
        categorias.forEach { categoria ->
            println(
                """
                |ID: ${categoria.id}
                |Nombre: ${categoria.nombre}
                |Descripción: ${categoria.descripcion}
                |-------------------------------
            """.trimMargin()
            )
        }
    }
}

fun consultarCategoriaPorId() {
    print("Ingrese el ID de la categoría: ")

    val id = try {
        readlnOrNull()?.toInt() ?: -1
    } catch (_: Exception) {
        -1
    }

    if (id == -1) {
        println("ID debe ser un número válido")
        return
    }

    val categoria = CategoriaDAO.consultarCategoriaPorId(id)

    if (categoria != null) {
        println("\nCategoría encontrada:")
        println(
            """
            |ID: ${categoria.id}
            |Nombre: ${categoria.nombre}
            |Descripción: ${categoria.descripcion}
        """.trimMargin()
        )
    } else {
        println("No se encontró ninguna categoría con ID: $id")
    }
}

fun insertarCategoria() {
    print("Nombre: ")
    val nombre = readlnOrNull()?.trim() ?: ""

    print("Descripción: ")
    val descripcion = readlnOrNull()?.trim() ?: ""

    if (nombre.isEmpty() || descripcion.isEmpty()) {
        println("Todos los campos son obligatorios")
        return
    }

    val nuevaCategoria = Categoria(
        nombre = nombre, descripcion = descripcion
    )

    CategoriaDAO.insertarCategoria(nuevaCategoria)
}

fun actualizarCategoria() {
    print("Ingrese el ID de la categoría a actualizar: ")

    val id = try {
        readlnOrNull()?.toInt() ?: -1
    } catch (_: Exception) {
        -1
    }

    if (id == -1) {
        println("ID debe ser un número válido")
        return
    }

    val categoriaExistente = CategoriaDAO.consultarCategoriaPorId(id)
    if (categoriaExistente == null) {
        println("No existe una categoría con ID: $id")
        return
    }

    println("\nCategoría actual:")
    println(
        """
        |ID: ${categoriaExistente.id}
        |Nombre: ${categoriaExistente.nombre}
        |Descripción: ${categoriaExistente.descripcion}
    """.trimMargin()
    )

    println("\nIngrese los nuevos datos (deje vacío para mantener el valor actual):")

    print("Nuevo nombre [${categoriaExistente.nombre}]: ")
    val nuevoNombre = readlnOrNull()?.trim()

    print("Nueva descripción [${categoriaExistente.descripcion}]: ")
    val nuevaDescripcion = readlnOrNull()?.trim()

    val categoriaActualizada = Categoria(
        id = id,
        nombre = if (!nuevoNombre.isNullOrBlank()) nuevoNombre else categoriaExistente.nombre,
        descripcion = if (!nuevaDescripcion.isNullOrBlank()) nuevaDescripcion else categoriaExistente.descripcion
    )

    CategoriaDAO.actualizarCategoria(categoriaActualizada)
}

fun eliminarCategoria() {
    print("Ingrese el ID de la categoría a eliminar: ")

    val id = try {
        readlnOrNull()?.toInt() ?: -1
    } catch (_: Exception) {
        -1
    }

    if (id == -1) {
        println("ID debe ser un número válido")
        return
    }

    print("¿Está seguro de eliminar la categoría con ID $id? (s/n): ")
    val confirmacion = readlnOrNull()?.trim()?.lowercase()

    if (confirmacion == "s") {
        CategoriaDAO.eliminarCategoria(id)
    } else {
        println("Operación cancelada")
    }
}

// MENÚ PROVEEDORES

fun menuProveedores() {
    val scanner = Scanner(System.`in`)
    var opcion: Int

    do {
        println("\n=== GESTIÓN DE PROVEEDORES ===")
        println("1. Listar todos los proveedores")
        println("2. Consultar proveedor por ID")
        println("3. Insertar nuevo proveedor")
        println("4. Actualizar proveedor")
        println("5. Eliminar proveedor")
        println("6. Volver al menú principal")
        print("Seleccione una opción: ")

        opcion = try {
            scanner.nextInt()
        } catch (_: Exception) {
            -1
        }
        scanner.nextLine()

        when (opcion) {
            1 -> listarProveedores()
            2 -> consultarProveedorPorId()
            3 -> insertarProveedor()
            4 -> actualizarProveedor()
            5 -> eliminarProveedor()
            6 -> println("Volviendo al menú principal...")
            else -> println("Introduce una opcion válida")
        }
    } while (opcion != 6)
}

fun listarProveedores() {
    val proveedores = ProveedorDAO.listarProveedores()

    if (proveedores.isEmpty()) {
        println("No se encontraron proveedores")
    } else {
        proveedores.forEach { proveedor ->
            println(
                """
                |ID: ${proveedor.id}
                |Nombre: ${proveedor.nombre}
                |Teléfono: ${proveedor.telefono}
                |Email: ${proveedor.email}
                |Dirección: ${proveedor.direccion}
                |-------------------------------
            """.trimMargin()
            )
        }
    }
}

fun consultarProveedorPorId() {
    print("Ingrese el ID del proveedor: ")

    val id = try {
        readlnOrNull()?.toInt() ?: -1
    } catch (_: Exception) {
        -1
    }

    if (id == -1) {
        println("ID debe ser un número válido")
        return
    }

    val proveedor = ProveedorDAO.consultarProveedorPorId(id)

    if (proveedor != null) {
        println("\nProveedor encontrado:")
        println(
            """
            |ID: ${proveedor.id}
            |Nombre: ${proveedor.nombre}
            |Teléfono: ${proveedor.telefono}
            |Email: ${proveedor.email}
            |Dirección: ${proveedor.direccion}
        """.trimMargin()
        )
    } else {
        println("No se encontró ningún proveedor con ID: $id")
    }
}

fun insertarProveedor() {
    print("Nombre: ")
    val nombre = readlnOrNull()?.trim() ?: ""

    print("Teléfono: ")
    val telefono = try {
        readlnOrNull()?.toInt() ?: 0
    } catch (_: Exception) {
        0
    }

    print("Email: ")
    val email = readlnOrNull()?.trim() ?: ""

    print("Dirección: ")
    val direccion = readlnOrNull()?.trim() ?: ""

    if (nombre.isEmpty() || telefono == 0 || email.isEmpty() || direccion.isEmpty()) {
        println("Todos los campos son obligatorios y deben ser válidos")
        return
    }

    val nuevoProveedor = Proveedor(
        nombre = nombre, telefono = telefono, email = email, direccion = direccion
    )

    ProveedorDAO.insertarProveedor(nuevoProveedor)
}

fun actualizarProveedor() {
    print("Ingrese el ID del proveedor a actualizar: ")

    val id = try {
        readlnOrNull()?.toInt() ?: -1
    } catch (_: Exception) {
        -1
    }

    if (id == -1) {
        println("ID debe ser un número válido")
        return
    }

    val proveedorExistente = ProveedorDAO.consultarProveedorPorId(id)
    if (proveedorExistente == null) {
        println("No existe un proveedor con ID: $id")
        return
    }

    println("\nProveedor actual:")
    println(
        """
        |ID: ${proveedorExistente.id}
        |Nombre: ${proveedorExistente.nombre}
        |Teléfono: ${proveedorExistente.telefono}
        |Email: ${proveedorExistente.email}
        |Dirección: ${proveedorExistente.direccion}
    """.trimMargin()
    )

    println("\nIngrese los nuevos datos (deje vacío para mantener el valor actual):")

    print("Nuevo nombre [${proveedorExistente.nombre}]: ")
    val nuevoNombre = readlnOrNull()?.trim()

    print("Nuevo teléfono [${proveedorExistente.telefono}]: ")
    val nuevoTelefonoStr = readlnOrNull()?.trim()

    print("Nuevo email [${proveedorExistente.email}]: ")
    val nuevoEmail = readlnOrNull()?.trim()

    print("Nueva dirección [${proveedorExistente.direccion}]: ")
    val nuevaDireccion = readlnOrNull()?.trim()

    val proveedorActualizado = Proveedor(
        id = id,
        nombre = if (!nuevoNombre.isNullOrBlank()) nuevoNombre else proveedorExistente.nombre,
        telefono = if (!nuevoTelefonoStr.isNullOrBlank()) nuevoTelefonoStr.toInt() else proveedorExistente.telefono,
        email = if (!nuevoEmail.isNullOrBlank()) nuevoEmail else proveedorExistente.email,
        direccion = if (!nuevaDireccion.isNullOrBlank()) nuevaDireccion else proveedorExistente.direccion
    )

    ProveedorDAO.actualizarProveedor(proveedorActualizado)
}

fun eliminarProveedor() {
    print("Ingrese el ID del proveedor a eliminar: ")

    val id = try {
        readlnOrNull()?.toInt() ?: -1
    } catch (_: Exception) {
        -1
    }

    if (id == -1) {
        println("ID debe ser un número válido")
        return
    }

    print("¿Está seguro de eliminar el proveedor con ID $id? (s/n): ")
    val confirmacion = readlnOrNull()?.trim()?.lowercase()

    if (confirmacion == "s") {
        ProveedorDAO.eliminarProveedor(id)
    } else {
        println("Operación cancelada")
    }
}