package org.example

import java.util.Scanner

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