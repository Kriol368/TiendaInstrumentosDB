package org.example.dao

import org.example.conectarBD
import java.sql.SQLException

data class Categoria(
    val id: Int? = null, val nombre: String, val descripcion: String
)

object CategoriaDAO {

    fun listarCategorias(): List<Categoria> {
        val lista = mutableListOf<Categoria>()
        conectarBD()?.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery("SELECT * FROM categoria").use { rs ->
                    while (rs.next()) {
                        lista.add(
                            Categoria(
                                id = rs.getInt("id"),
                                nombre = rs.getString("nombre"),
                                descripcion = rs.getString("descripcion")
                            )
                        )
                    }
                }
            }
        } ?: println("No se pudo establecer la conexión.")
        return lista
    }

    fun consultarCategoriaPorId(id: Int): Categoria? {
        var categoria: Categoria? = null
        conectarBD()?.use { conn ->
            conn.prepareStatement("SELECT * FROM categoria WHERE id = ?").use { pstmt ->
                pstmt.setInt(1, id)
                pstmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        categoria = Categoria(
                            id = rs.getInt("id"),
                            nombre = rs.getString("nombre"),
                            descripcion = rs.getString("descripcion")
                        )
                    }
                }
            }
        } ?: println("No se pudo establecer la conexión.")
        return categoria
    }

    fun insertarCategoria(categoria: Categoria) {
        conectarBD()?.use { conn ->
            conn.prepareStatement(
                "INSERT INTO categoria(nombre, descripcion) VALUES (?, ?)"
            ).use { pstmt ->
                pstmt.setString(1, categoria.nombre)
                pstmt.setString(2, categoria.descripcion)
                pstmt.executeUpdate()
                println("Categoría '${categoria.nombre}' insertada con éxito.")
            }
        } ?: println("No se pudo establecer la conexión.")
    }

    fun actualizarCategoria(categoria: Categoria) {
        if (categoria.id == null) {
            println("No se puede actualizar una categoría sin id.")
            return
        }
        conectarBD()?.use { conn ->
            conn.prepareStatement(
                "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id = ?"
            ).use { pstmt ->
                pstmt.setString(1, categoria.nombre)
                pstmt.setString(2, categoria.descripcion)
                pstmt.setInt(3, categoria.id)
                val filas = pstmt.executeUpdate()
                if (filas > 0) {
                    println("Categoría con id=${categoria.id} actualizada con éxito.")
                } else {
                    println("No se encontró ninguna categoría con id=${categoria.id}.")
                }
            }
        } ?: println("No se pudo establecer la conexión.")
    }

    fun eliminarCategoria(id: Int) {
        conectarBD()?.use { conn ->
            conn.prepareStatement("SELECT COUNT(*) FROM instrumento WHERE categoria_id = ?").use { pstmt ->
                pstmt.setInt(1, id)
                pstmt.executeQuery().use { rs ->
                    if (rs.next() && rs.getInt(1) > 0) {
                        println("No se puede eliminar la categoría porque hay instrumentos asociados.")
                        return
                    }
                }
            }

            conn.prepareStatement("DELETE FROM categoria WHERE id = ?").use { pstmt ->
                pstmt.setInt(1, id)
                val filas = pstmt.executeUpdate()
                if (filas > 0) {
                    println("Categoría con id=$id eliminada correctamente.")
                } else {
                    println("No se encontró ninguna categoría con id=$id.")
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
    }

}