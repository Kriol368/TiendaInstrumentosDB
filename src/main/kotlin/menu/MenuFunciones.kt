package org.example.menu

import org.example.dao.FuncionesDAO
import java.util.Scanner

fun menuFunciones() {
    val scanner = Scanner(System.`in`)
    var opcion: Int

    do {
        println("\n=== FUNCIONES ===")
        println("1. Contar instrumentos por proveedor")
        println("2. Calcular promedio de precios por categoría")
        println("3. Volver al menú principal")
        print("Seleccione una opción: ")

        opcion = try {
            scanner.nextInt()
        } catch (_: Exception) {
            -1
        }
        scanner.nextLine()

        when (opcion) {
            1 -> contarInstrumentosPorProveedor()
            2 -> calcularPromedioPreciosPorCategoria()
            3 -> println("Volviendo al menú principal...")
            else -> println("Introduce una opción válida")
        }
    } while (opcion != 3)
}

fun contarInstrumentosPorProveedor() {
    println("\n=== CONTAR INSTRUMENTOS POR PROVEEDOR ===")

    print("Ingrese el ID del proveedor: ")
    val proveedorId = try {
        readlnOrNull()?.toInt() ?: -1
    } catch (_: Exception) {
        -1
    }

    if (proveedorId == -1) {
        println("ID debe ser un número válido")
        return
    }

    val totalInstrumentos = FuncionesDAO.contarInstrumentosPorProveedor(proveedorId)

    if (totalInstrumentos >= 0) {
        println("El proveedor con ID $proveedorId tiene $totalInstrumentos instrumento(s)")
    } else {
        println("Error al contar los instrumentos del proveedor")
    }
}

fun calcularPromedioPreciosPorCategoria() {
    println("\n=== CALCULAR PROMEDIO DE PRECIOS POR CATEGORÍA ===")

    print("Ingrese el ID de la categoría: ")
    val categoriaId = try {
        readlnOrNull()?.toInt() ?: -1
    } catch (_: Exception) {
        -1
    }

    if (categoriaId == -1) {
        println("ID debe ser un número válido")
        return
    }

    val promedio = FuncionesDAO.calcularPromedioPreciosPorCategoria(categoriaId)

    if (promedio >= 0) {
        if (promedio > 0) {
            println("El precio promedio de los instrumentos en la categoría $categoriaId es: $${"%.2f".format(promedio)}")
        } else {
            println("La categoría $categoriaId no tiene instrumentos o no existe")
        }
    } else {
        println("Error al calcular el promedio de precios")
    }
}