package org.example.menu

import org.example.dao.ProcedimientosDAO
import java.util.Scanner

fun menuProcedimientos() {
    val scanner = Scanner(System.`in`)
    var opcion: Int

    do {
        println("\n=== PROCEDIMIENTOS ===")
        println("1. Obtener instrumentos por rango de precio")
        println("2. Actualizar precios por categoría")
        println("3. Volver al menú principal")
        print("Seleccione una opción: ")

        opcion = try {
            scanner.nextInt()
        } catch (_: Exception) {
            -1
        }
        scanner.nextLine()

        when (opcion) {
            1 -> obtenerInstrumentosPorRangoPrecio()
            2 -> actualizarPreciosPorCategoria()
            3 -> println("Volviendo al menú principal...")
            else -> println("Introduce una opción válida")
        }
    } while (opcion != 3)
}

fun obtenerInstrumentosPorRangoPrecio() {
    println("\n=== OBTENER INSTRUMENTOS POR RANGO DE PRECIO ===")

    print("Precio mínimo: ")
    val precioMin = try {
        readlnOrNull()?.toDouble() ?: -1.0
    } catch (_: Exception) {
        -1.0
    }

    print("Precio máximo: ")
    val precioMax = try {
        readlnOrNull()?.toDouble() ?: -1.0
    } catch (_: Exception) {
        -1.0
    }

    if (precioMin < 0 || precioMax < 0 || precioMin > precioMax) {
        println("Los precios deben ser números válidos y el precio mínimo no puede ser mayor al máximo")
        return
    }

    val instrumentos = ProcedimientosDAO.obtenerInstrumentosPorRangoPrecio(precioMin, precioMax)

    if (instrumentos.isEmpty()) {
        println("No se encontraron instrumentos en el rango de precio $${"%.2f".format(precioMin)} - $${"%.2f".format(precioMax)}")
    } else {
        println("\n=== INSTRUMENTOS ENCONTRADOS ===")
        instrumentos.forEach { instrumento ->
            println(
                """
                |ID: ${instrumento.id}
                |Instrumento: ${instrumento.nombre}
                |Fabricante: ${instrumento.fabricante}
                |Precio: $${"%.2f".format(instrumento.precio)}
                |Categoría: ${instrumento.categoria}
                |Proveedor: ${instrumento.proveedor}
                |Teléfono proveedor: ${instrumento.telefonoProveedor}
                |-------------------------------
            """.trimMargin()
            )
        }
        println("Total encontrados: ${instrumentos.size}")
    }
}

fun actualizarPreciosPorCategoria() {
    println("\n=== ACTUALIZAR PRECIOS POR CATEGORÍA ===")

    print("ID de la categoría: ")
    val categoriaId = try {
        readlnOrNull()?.toInt() ?: -1
    } catch (_: Exception) {
        -1
    }

    if (categoriaId == -1) {
        println("ID debe ser un número válido")
        return
    }

    print("Porcentaje de aumento/disminución: ")
    val porcentaje = try {
        readlnOrNull()?.toDouble() ?: 0.0
    } catch (_: Exception) {
        0.0
    }

    if (porcentaje == 0.0) {
        println("El porcentaje no puede ser 0")
        return
    }

    print("¿Está seguro de actualizar los precios de la categoría $categoriaId en un $porcentaje%? (s/n): ")
    val confirmacion = readlnOrNull()?.trim()?.lowercase()

    if (confirmacion == "s") {
        val resultado = ProcedimientosDAO.actualizarPreciosPorCategoria(categoriaId, porcentaje)
        println("$resultado instrumento(s) actualizado(s) en la categoría $categoriaId")
    } else {
        println("Operación cancelada")
    }
}