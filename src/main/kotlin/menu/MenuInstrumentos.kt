package org.example.menu

import org.example.dao.Instrumento
import org.example.dao.InstrumentoDAO
import java.util.Scanner

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
