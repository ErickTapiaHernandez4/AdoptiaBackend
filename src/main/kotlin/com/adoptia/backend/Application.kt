package com.adoptia.backend

import com.adoptia.backend.plugins.configureRouting
import com.adoptia.backend.plugins.configureSerialization
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.cors.routing.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(CallLogging)

    // CORS: permite que la versión WEB (servida por Nginx en otra dirección)
    // le haga peticiones a este backend desde el navegador.
    install(CORS) {
        anyHost()
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Options)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowNonSimpleContentTypes = true   // para mandar JSON (application/json)
    }

    configureSerialization()
    configureRouting()
}