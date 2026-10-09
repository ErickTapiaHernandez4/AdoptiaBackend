package com.adoptia.backend.routes

import com.adoptia.backend.data.MascotaRepository
import com.adoptia.backend.data.model.Mascota
import com.adoptia.backend.data.model.Solicitud
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.mascotaRoutes() {
    val repo = MascotaRepository()

    get("/api/mascotas") {
        call.respond(repo.listarMascotas())
    }

    post("/api/mascotas") {
        val mascota = call.receive<Mascota>()
        repo.agregarMascota(mascota)
        call.respond(HttpStatusCode.Created, "ok")
    }

    get("/api/solicitudes") {
        call.respond(repo.listarSolicitudes())
    }

    post("/api/solicitudes") {
        val solicitud = call.receive<Solicitud>()
        repo.crearSolicitud(solicitud.mascotaId, solicitud.cliente)
        call.respond(HttpStatusCode.Created, "ok")
    }

    // accion: "aprobar" o "rechazar"
    post("/api/solicitudes/{id}/{accion}") {
        val id = call.parameters["id"]?.toIntOrNull()
        val accion = call.parameters["accion"]
        if (id == null || (accion != "aprobar" && accion != "rechazar")) {
            call.respond(HttpStatusCode.BadRequest, "Solicitud inválida")
            return@post
        }
        repo.cambiarEstado(id, if (accion == "aprobar") "APROBADA" else "RECHAZADA")
        call.respond("ok")
    }
}
