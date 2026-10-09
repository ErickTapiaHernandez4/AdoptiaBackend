package com.adoptia.backend.routes

import com.adoptia.backend.data.UsuarioJsonRepository
import com.adoptia.backend.data.model.LoginRequest
import com.adoptia.backend.data.model.LoginResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes() {
    val repositorio = UsuarioJsonRepository()

    post("/api/login") {
        val request = call.receive<LoginRequest>()
        val usuario = repositorio.buscar(request.usuario, request.password)

        if (usuario == null) {
            call.respond(HttpStatusCode.Unauthorized, "Credenciales inválidas")
            return@post
        }

        call.respond(
            LoginResponse(
                token = "local-token-${usuario.usuario}",
                rol = usuario.rol,
                nombre = usuario.nombre
            )
        )
    }
}
