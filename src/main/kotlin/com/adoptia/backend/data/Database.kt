package com.adoptia.backend.data

import java.sql.Connection
import java.sql.DriverManager

// Conexión a MySQL en Railway.
// Los datos se leen de variables de entorno para NO subir la contraseña a GitHub.
object Database {
    private fun variable(nombre: String): String =
        System.getenv(nombre) ?: error("Falta la variable de entorno $nombre")

    private val url: String by lazy {
        val host = variable("DB_HOST")
        val puerto = variable("DB_PORT")
        val base = variable("DB_NAME")
        "jdbc:mysql://$host:$puerto/$base?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC"
    }

    fun conexion(): Connection =
        DriverManager.getConnection(url, variable("DB_USER"), variable("DB_PASSWORD"))
}