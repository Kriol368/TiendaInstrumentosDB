package org.example

import java.sql.SQLException

data class Instrumento(
    val id: Int? = null,
    val nombre: String,
    val fabricante: String,
    val anoFabricacion: Int,
    val precio: Double,
    val categoriaId: Int,
    val proveedorId: Int
)

object InstrumentoDAO {

    fun listarInstrumentos(): List<Instrumento> {
        val lista = mutableListOf<Instrumento>()
        conectarBD()?.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery(
                    """
                    SELECT i.*, c.nombre as categoria_nombre, p.nombre as proveedor_nombre 
                    FROM instrumento i
                    LEFT JOIN categoria c ON i.categoria_id = c.id
                    LEFT JOIN proveedor p ON i.proveedor_id = p.id
                """.trimIndent()
                ).use { rs ->
                    while (rs.next()) {
                        lista.add(
                            Instrumento(
                                id = rs.getInt("id"),
                                nombre = rs.getString("nombre"),
                                fabricante = rs.getString("fabricante"),
                                anoFabricacion = rs.getInt("ano_fabricacion"),
                                precio = rs.getDouble("precio"),
                                categoriaId = rs.getInt("categoria_id"),
                                proveedorId = rs.getInt("proveedor_id")
                            )
                        )
                    }
                }
            }
        } ?: println("No se pudo establecer la conexión.")
        return lista
    }

    fun consultarInstrumentoPorId(id: Int): Instrumento? {
        var instrumento: Instrumento? = null
        conectarBD()?.use { conn ->
            conn.prepareStatement(
                """
                SELECT i.*, c.nombre as categoria_nombre, p.nombre as proveedor_nombre 
                FROM instrumento i
                LEFT JOIN categoria c ON i.categoria_id = c.id
                LEFT JOIN proveedor p ON i.proveedor_id = p.id
                WHERE i.id = ?
            """.trimIndent()
            ).use { pstmt ->
                pstmt.setInt(1, id)
                pstmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        instrumento = Instrumento(
                            id = rs.getInt("id"),
                            nombre = rs.getString("nombre"),
                            fabricante = rs.getString("fabricante"),
                            anoFabricacion = rs.getInt("ano_fabricacion"),
                            precio = rs.getDouble("precio"),
                            categoriaId = rs.getInt("categoria_id"),
                            proveedorId = rs.getInt("proveedor_id")
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
                "INSERT INTO instrumento(nombre, fabricante, ano_fabricacion, precio, categoria_id, proveedor_id) VALUES (?, ?, ?, ?, ?, ?)"
            ).use { pstmt ->
                pstmt.setString(1, instrumento.nombre)
                pstmt.setString(2, instrumento.fabricante)
                pstmt.setInt(3, instrumento.anoFabricacion)
                pstmt.setDouble(4, instrumento.precio)
                pstmt.setInt(5, instrumento.categoriaId)
                pstmt.setInt(6, instrumento.proveedorId)
                pstmt.executeUpdate()
                println("Instrumento '${instrumento.nombre}' insertado con éxito.")
            }
        } ?: println("No se pudo establecer la conexión.")
    }

    fun actualizarInstrumento(instrumento: Instrumento) {
        if (instrumento.id == null) {
            println("No se puede actualizar un instrumento sin id.")
            return
        }
        conectarBD()?.use { conn ->
            conn.prepareStatement(
                "UPDATE instrumento SET nombre = ?, fabricante = ?, ano_fabricacion = ?, precio = ?, categoria_id = ?, proveedor_id = ? WHERE id = ?"
            ).use { pstmt ->
                pstmt.setString(1, instrumento.nombre)
                pstmt.setString(2, instrumento.fabricante)
                pstmt.setInt(3, instrumento.anoFabricacion)
                pstmt.setDouble(4, instrumento.precio)
                pstmt.setInt(5, instrumento.categoriaId)
                pstmt.setInt(6, instrumento.proveedorId)
                pstmt.setInt(7, instrumento.id)
                val filas = pstmt.executeUpdate()
                if (filas > 0) {
                    println("Instrumento con id=${instrumento.id} actualizado con éxito.")
                } else {
                    println("No se encontró ningún instrumento con id=${instrumento.id}.")
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
                    println("Instrumento con id=$id eliminado correctamente.")
                } else {
                    println("No se encontró ningún instrumento con id=$id.")
                }
            }
        } ?: println("No se pudo establecer la conexión.")
    }

    fun crearCategoriaYTransferirInstrumento(nombreCategoria: String, descripcion: String, idInstrumento: Int) {
        conectarBD()?.use { conn ->
            try {
                conn.autoCommit = false

                conn.prepareStatement(
                    "INSERT INTO categoria (nombre, descripcion) VALUES (?, ?)"
                ).use { insertStmt ->
                    insertStmt.setString(1, nombreCategoria)
                    insertStmt.setString(2, descripcion)
                    insertStmt.executeUpdate()
                }

                var idNuevaCategoria: Int? = null
                conn.prepareStatement("SELECT last_insert_rowid()").use { stmt ->
                    stmt.executeQuery().use { rs ->
                        if (rs.next()) {
                            idNuevaCategoria = rs.getInt(1)
                        } else {
                            throw SQLException("No se pudo obtener el ID de la nueva categoría")
                        }
                    }
                }

                var nombreInstrumento: String? = null
                var idCategoriaActual: Int? = null
                conn.prepareStatement("SELECT nombre, categoria_id FROM instrumento WHERE id = ?").use { checkStmt ->
                    checkStmt.setInt(1, idInstrumento)
                    checkStmt.executeQuery().use { rs ->
                        if (rs.next()) {
                            nombreInstrumento = rs.getString("nombre")
                            idCategoriaActual = rs.getInt("categoria_id")
                        } else {
                            throw SQLException("El instrumento con ID $idInstrumento no existe")
                        }
                    }
                }

                conn.prepareStatement("UPDATE instrumento SET categoria_id = ? WHERE id = ?").use { updateStmt ->
                    updateStmt.setInt(1, idNuevaCategoria!!)
                    updateStmt.setInt(2, idInstrumento)
                    val filasActualizadas = updateStmt.executeUpdate()

                    if (filasActualizadas == 0) {
                        throw SQLException("No se pudo transferir el instrumento a la nueva categoría")
                    }
                }

                conn.commit()
                println("Transacción completada:")
                println("   - Nueva categoría creada: '$nombreCategoria' (ID: $idNuevaCategoria)")
                println("   - Instrumento '$nombreInstrumento' transferido desde categoría $idCategoriaActual a $idNuevaCategoria")

            } catch (e: SQLException) {
                conn.rollback()
                println("Error en la transacción: ${e.message}")
                println("   Transacción revertida - no se creó la categoría ni se transfirió el instrumento")
            } finally {
                conn.autoCommit = true
            }
        } ?: println("No se pudo establecer la conexión con la base de datos")
    }}