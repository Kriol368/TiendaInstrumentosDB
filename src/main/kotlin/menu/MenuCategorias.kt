package org.example.menu

import org.example.dao.Categoria
import org.example.dao.CategoriaDAO
import java.util.Scanner

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