package com.adoptia.backend.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Mascota(
    val id: Int = 0,
    val nombre: String,
    val tipo: String,
    val edad: String,
    val refugio: String
)

@Serializable
data class Solicitud(
    val id: Int = 0,
    val mascotaId: Int,
    val mascota: String = "",
    val cliente: String,
    val estado: String = "PENDIENTE"
)
