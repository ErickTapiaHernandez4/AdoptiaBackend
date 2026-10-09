package com.adoptia.backend.data

import com.adoptia.backend.data.model.Usuario
import kotlinx.serialization.json.Json

class UsuarioJsonRepository {

    private val usuarios: List<Usuario> by lazy {
        val texto = this::class.java.classLoader
            .getResourceAsStream("usuarios.json")
            ?.bufferedReader()
            ?.readText()
            ?: "[]"
        Json.decodeFromString(texto)
    }

    fun buscar(usuario: String, password: String): Usuario? =
        usuarios.find { it.usuario == usuario && it.password == password }
}
