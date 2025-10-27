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