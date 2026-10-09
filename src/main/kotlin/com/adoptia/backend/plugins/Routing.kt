package com.adoptia.backend.plugins

import com.adoptia.backend.routes.authRoutes
import com.adoptia.backend.routes.mascotaRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        authRoutes()
        mascotaRoutes()
    }
}
