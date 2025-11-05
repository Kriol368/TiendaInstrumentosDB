package org.example.dao

import org.example.conectarBD

data class InstrumentoPrecio(
    val id: Int,
    val nombre: String,
    val fabricante: String,
    val precio: Double,
    val categoria: String?,
    val proveedor: String?,
    val telefonoProveedor: String?
)

object ProcedimientosDAO {

    fun obtenerInstrumentosPorRangoPrecio(precioMin: Double, precioMax: Double): List<InstrumentoPrecio> {
        val lista = mutableListOf<InstrumentoPrecio>()
        conectarBD()?.use { conn ->
            conn.prepareStatement("CALL ObtenerInstrumentosPorRangoPrecio(?, ?)").use { pstmt ->
                pstmt.setDouble(1, precioMin)
                pstmt.setDouble(2, precioMax)
                pstmt.executeQuery().use { rs ->
                    while (rs.next()) {
                        lista.add(
                            InstrumentoPrecio(
                                id = rs.getInt("id"),
                                nombre = rs.getString("instrumento"),
                                fabricante = rs.getString("fabricante"),
                                precio = rs.getDouble("precio"),
                                categoria = rs.getString("categoria"),
                                proveedor = rs.getString("proveedor"),
                                telefonoProveedor = rs.getString("telefono")
                            )
                        )
                    }
                }
            }
        } ?: println("No se pudo establecer la conexión.")
        return lista
    }

    fun actualizarPreciosPorCategoria(categoriaId: Int, porcentaje: Double): Int {
        var instrumentosActualizados = -1
        conectarBD()?.use { conn ->
            conn.prepareStatement("CALL ActualizarPreciosPorCategoria(?, ?)").use { pstmt ->
                pstmt.setInt(1, categoriaId)
                pstmt.setDouble(2, porcentaje)
                pstmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        instrumentosActualizados = rs.getInt("Instrumentos actualizados")
                    }
                }
            }
        } ?: println("No se pudo establecer la conexión.")
        return instrumentosActualizados
    }
}