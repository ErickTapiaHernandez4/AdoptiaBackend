package com.adoptia.backend

import com.adoptia.backend.plugins.configureRouting
import com.adoptia.backend.plugins.configureSerialization
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
    install(CORS) {
        anyHost()
    }
    configureSerialization()
    configureRouting()
}
