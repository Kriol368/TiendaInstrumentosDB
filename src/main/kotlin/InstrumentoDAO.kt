package org.example

data class Instrumento(
    val id: Int? = null,
    val nombre: String,
    val fabricante: String,
    val anoFabricacion: Int,
    val precio: Double
)

object InstrumentoDAO {
    fun listarInstrumentos(): List<Instrumento> {
        val lista = mutableListOf<Instrumento>()
        conectarBD()?.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery("SELECT * FROM instrumento").use { rs ->
                    while (rs.next()) {
                        lista.add(
                            Instrumento(
                                id = rs.getInt("id"),
                                nombre = rs.getString("nombre"),
                                fabricante = rs.getString("fabricante"),
                                anoFabricacion = rs.getInt("ano_fabricacion"),
                                precio = rs.getDouble("precio")
                            )
                        )
                    }
                }
            }
        } ?: println("No se pudo establecer la conexión.")
        return lista
    }

    // Consultar instrumento por ID
    fun consultarInstrumentoPorId(id: Int): Instrumento? {
        var instrumento: Instrumento? = null
        conectarBD()?.use { conn ->
            conn.prepareStatement("SELECT * FROM instrumento WHERE id = ?").use { pstmt ->
                pstmt.setInt(1, id)
                pstmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        instrumento = Instrumento(
                            id = rs.getInt("id"),
                            nombre = rs.getString("nombre"),
                            fabricante = rs.getString("fabricante"),
                            anoFabricacion = rs.getInt("ano_fabricacion"),
                            precio = rs.getDouble("precio")
                        )
                    }
                }
            }
        } ?: println("No se pudo establecer la conexión.")
        return instrumento
    }

    fun insertarInstrumento(instrumento: Instrumento) {
        conectarBD()?.use { conn ->
            conn.prepareStatement(
                "INSERT INTO instrumento(nombre, fabricante, ano_fabricacion, precio) VALUES (?, ?, ?, ?)"
            ).use { pstmt ->
                pstmt.setString(1, instrumento.nombre)
                pstmt.setString(2, instrumento.fabricante)
                pstmt.setInt(3, instrumento.anoFabricacion)
                pstmt.setDouble(4, instrumento.precio)
                pstmt.executeUpdate()
                println("Instrumento '${instrumento.nombre}' insertado con éxito.")
            }
        } ?: println("No se pudo establecer la conexión.")
    }

    fun actualizarInstrumento(instrumento: Instrumento) {
        if (instrumento.id == null) {
            println("No se puede actualizar una instrumento sin id.")
            return
        }
        conectarBD()?.use { conn ->
            conn.prepareStatement(
                "UPDATE instrumento SET nombre = ?, fabricante = ?, ano_fabricacion = ?, precio = ? WHERE id = ?"
            ).use { pstmt ->
                pstmt.setString(1, instrumento.nombre)
                pstmt.setString(2, instrumento.fabricante)
                pstmt.setInt(3, instrumento.anoFabricacion)
                pstmt.setDouble(4, instrumento.precio)
                pstmt.setInt(5, instrumento.id)
                val filas = pstmt.executeUpdate()
                if (filas > 0) {
                    println("Instrumento con id=${instrumento.id} actualizada con éxito.")
                } else {
                    println("No se encontró ninguna instrumento con id=${instrumento.id}.")
                }
            }
        } ?: println("No se pudo establecer la conexión.")
    }

    fun eliminarInstrumento(id: Int) {
        conectarBD()?.use { conn ->
            conn.prepareStatement("DELETE FROM instrumento WHERE id = ?").use { pstmt ->
                pstmt.setInt(1, id)
                val filas = pstmt.executeUpdate()
                if (filas > 0) {
                    println("Instrumento con id=$id eliminada correctamente.")
                } else {
                    println("No se encontró ninguna instrumento con id=$id.")
                }
            }
        } ?: println("No se pudo establecer la conexión.")
    }
}