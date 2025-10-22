package org.example

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
}