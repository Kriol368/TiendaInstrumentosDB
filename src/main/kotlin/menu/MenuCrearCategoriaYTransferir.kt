package org.example.menu

import org.example.dao.CategoriaDAO.crearCategoriaYTransferirInstrumento

fun menuCrearCategoriaYTransferir() {
    println("\n=== CREAR CATEGORÍA Y TRANSFERIR INSTRUMENTO ===")

    println("\n--- Nueva Categoría ---")
    print("Nombre de la categoría: ")
    val nombreCategoria = readlnOrNull()?.trim() ?: ""

    print("Descripción: ")
    val descripcion = readlnOrNull()?.trim() ?: ""

    if (nombreCategoria.isEmpty() || descripcion.isEmpty()) {
        println("El nombre y descripción son obligatorios")
        return
    }

    println("\n--- Instrumento a Transferir ---")
    print("ID del Instrumento: ")
    val idInstrumento = readlnOrNull()?.toIntOrNull()

    if (idInstrumento == null) {
        println("ID de instrumento no válido")
        return
    }

    crearCategoriaYTransferirInstrumento(nombreCategoria, descripcion, idInstrumento)
}