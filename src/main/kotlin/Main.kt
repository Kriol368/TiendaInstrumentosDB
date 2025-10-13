package org.example

import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

fun main() {
    val conn = TiendaInstrumentosDB.getConnection()
    if (conn != null) {
        println("Conectado a la BD correctamente.")
        TiendaInstrumentosDB.closeConnection(conn)
    }
}

object TiendaInstrumentosDB {
    private val dbPath = "datos/tiendaInstrumentos.sqlite"
    private val dbFile = File(dbPath)
    private val url = "jdbc:sqlite:${dbFile.absolutePath}"

    fun getConnection(): Connection? {
        return try {
            DriverManager.getConnection(url)
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }

    fun testConnection(): Boolean {
        return getConnection()?.use { conn ->
            println("Conexión establecida con éxito a ${dbFile.absolutePath}")
            true
        } ?: false
    }

    fun closeConnection(conn: Connection?) {
        try {
            conn?.close()
            println("Conexión cerrada correctamente.")
        } catch (e: SQLException) {
            println("Error al cerrar la conexión: ${e.message}")
        }
    }
}
