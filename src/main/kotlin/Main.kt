package org.example

import org.example.menu.menuCategorias
import org.example.menu.menuCrearCategoriaYTransferir
import org.example.menu.menuFunciones
import org.example.menu.menuInstrumentos
import org.example.menu.menuProcedimientos
import org.example.menu.menuProveedores
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

const val URL_BD = "jdbc:mysql://98.90.164.146:3306/tiendainstrumentos"
const val USUARIO_BD = "abm"
const val PASSWORD_BD = "Taller2014"

fun main() {
    menu()
}

fun conectarBD(): Connection? {
    return try {
        DriverManager.getConnection(URL_BD, USUARIO_BD, PASSWORD_BD)
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
        println("5. Funciones")
        println("6. Procedimientos")
        println("7. Salir")
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
            5 -> menuFunciones()
            6 -> menuProcedimientos()
            7 -> println("Saliendo del programa...")
            else -> println("Introduce una opcion válida")
        }
    } while (opcion != 7)

    scanner.close()
}
