package com.adoptia.backend.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val usuario: String,
    val password: String,
    val rol: String,
    val nombre: String
)

@Serializable
data class LoginRequest(
    val usuario: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val token: String,
    val rol: String,
    val nombre: String
)
