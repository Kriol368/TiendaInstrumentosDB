package org.example

data class Proveedor(
    val id: Int? = null, val nombre: String, val telefono: Int, val email: String, val direccion: String
)

object ProveedorDAO {

    fun listarProveedores(): List<Proveedor> {
        val lista = mutableListOf<Proveedor>()
        conectarBD()?.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery("SELECT * FROM proveedor").use { rs ->
                    while (rs.next()) {
                        lista.add(
                            Proveedor(
                                id = rs.getInt("id"),
                                nombre = rs.getString("nombre"),
                                telefono = rs.getInt("telefono"),
                                email = rs.getString("email"),
                                direccion = rs.getString("direccion")
                            )
                        )
                    }
                }
            }
        } ?: println("No se pudo establecer la conexión.")
        return lista
    }

    fun consultarProveedorPorId(id: Int): Proveedor? {
        var proveedor: Proveedor? = null
        conectarBD()?.use { conn ->
            conn.prepareStatement("SELECT * FROM proveedor WHERE id = ?").use { pstmt ->
                pstmt.setInt(1, id)
                pstmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        proveedor = Proveedor(
                            id = rs.getInt("id"),
                            nombre = rs.getString("nombre"),
                            telefono = rs.getInt("telefono"),
                            email = rs.getString("email"),
                            direccion = rs.getString("direccion")
                        )
                    }
                }
            }
        } ?: println("No se pudo establecer la conexión.")
        return proveedor
    }

    fun insertarProveedor(proveedor: Proveedor) {
        conectarBD()?.use { conn ->
            conn.prepareStatement(
                "INSERT INTO proveedor(nombre, telefono, email, direccion) VALUES (?, ?, ?, ?)"
            ).use { pstmt ->
                pstmt.setString(1, proveedor.nombre)
                pstmt.setInt(2, proveedor.telefono)
                pstmt.setString(3, proveedor.email)
                pstmt.setString(4, proveedor.direccion)
                pstmt.executeUpdate()
                println("Proveedor '${proveedor.nombre}' insertado con éxito.")
            }
        } ?: println("No se pudo establecer la conexión.")
    }

    fun actualizarProveedor(proveedor: Proveedor) {
        if (proveedor.id == null) {
            println("No se puede actualizar un proveedor sin id.")
            return
        }
        conectarBD()?.use { conn ->
            conn.prepareStatement(
                "UPDATE proveedor SET nombre = ?, telefono = ?, email = ?, direccion = ? WHERE id = ?"
            ).use { pstmt ->
                pstmt.setString(1, proveedor.nombre)
                pstmt.setInt(2, proveedor.telefono)
                pstmt.setString(3, proveedor.email)
                pstmt.setString(4, proveedor.direccion)
                pstmt.setInt(5, proveedor.id)
                val filas = pstmt.executeUpdate()
                if (filas > 0) {
                    println("Proveedor con id=${proveedor.id} actualizado con éxito.")
                } else {
                    println("No se encontró ningún proveedor con id=${proveedor.id}.")
                }
            }
        } ?: println("No se pudo establecer la conexión.")
    }

    fun eliminarProveedor(id: Int) {
        conectarBD()?.use { conn ->
            conn.prepareStatement("SELECT COUNT(*) FROM instrumento WHERE proveedor_id = ?").use { pstmt ->
                pstmt.setInt(1, id)
                pstmt.executeQuery().use { rs ->
                    if (rs.next() && rs.getInt(1) > 0) {
                        println("No se puede eliminar el proveedor porque hay instrumentos asociados.")
                        return
                    }
                }
            }

            conn.prepareStatement("DELETE FROM proveedor WHERE id = ?").use { pstmt ->
                pstmt.setInt(1, id)
                val filas = pstmt.executeUpdate()
                if (filas > 0) {
                    println("Proveedor con id=$id eliminado correctamente.")
                } else {
                    println("No se encontró ningún proveedor con id=$id.")
                }
            }
        } ?: println("No se pudo establecer la conexión.")
    }
}