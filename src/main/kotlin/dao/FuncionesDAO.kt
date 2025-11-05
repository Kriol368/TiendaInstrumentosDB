package org.example.dao

import org.example.conectarBD

object FuncionesDAO {

    fun contarInstrumentosPorProveedor(proveedorId: Int): Int {
        var total = -1
        conectarBD()?.use { conn ->
            conn.prepareStatement("SELECT ContarInstrumentosPorProveedor(?)").use { pstmt ->
                pstmt.setInt(1, proveedorId)
                pstmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        total = rs.getInt(1)
                    }
                }
            }
        } ?: println("No se pudo establecer la conexión.")
        return total
    }

    fun calcularPromedioPreciosPorCategoria(categoriaId: Int): Double {
        var promedio = -1.0
        conectarBD()?.use { conn ->
            conn.prepareStatement("SELECT CalcularPromedioPreciosPorCategoria(?)").use { pstmt ->
                pstmt.setInt(1, categoriaId)
                pstmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        promedio = rs.getDouble(1)
                    }
                }
            }
        } ?: println("No se pudo establecer la conexión.")
        return promedio
    }
}