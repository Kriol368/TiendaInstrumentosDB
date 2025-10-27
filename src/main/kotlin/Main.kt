package org.example

import org.example.InstrumentoDAO.crearCategoriaYTransferirInstrumento
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
        println("4. Crear categoria y transferir un instrumento")
        println("5. Salir")
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
            4 -> menuCrearCategoriaYTransferir()
            5 -> println("Saliendo del programa...")
            else -> println("Introduce una opcion válida")
        }
    } while (opcion != 4)

    scanner.close()
}

fun menuCrearCategoriaYTransferir() {
    println("\n=== CREAR CATEGORÍA Y TRANSFERIR INSTRUMENTO ===")

    println("\n--- Nueva Categoría ---")
    print("Nombre de la categoría: ")
    val nombreCategoria = readlnOrNull()?.trim() ?: ""

    print("Descripción: ")
    val descripcion = readlnOrNull()?.trim() ?: ""

    if (nombreCategoria.isEmpty() || descripcion.isEmpty()) {
        println("El nombre y descripción son obligatorios")
        return
    }

    println("\n--- Instrumento a Transferir ---")
    print("ID del Instrumento: ")
    val idInstrumento = readlnOrNull()?.toIntOrNull()

    if (idInstrumento == null) {
        println("ID de instrumento no válido")
        return
    }

    crearCategoriaYTransferirInstrumento(nombreCategoria, descripcion, idInstrumento)
}