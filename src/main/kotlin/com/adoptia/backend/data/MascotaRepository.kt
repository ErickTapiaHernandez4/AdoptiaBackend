package com.adoptia.backend.data

import com.adoptia.backend.data.model.Mascota
import com.adoptia.backend.data.model.Solicitud
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File

// Guarda las mascotas y las solicitudes en archivos JSON dentro de la carpeta
// "datos" del proyecto del backend. No necesita base de datos.
// - Leer:    texto JSON  -> List<Mascota>   (decodeFromString = deserializar)
// - Guardar: List<Mascota> -> texto JSON    (encodeToString  = serializar)
class MascotaRepository {

    private val json = Json {
        prettyPrint = true      // el archivo queda ordenado y fácil de leer
        encodeDefaults = true   // también guarda los valores por defecto (ej. estado)
    }

    private val carpeta = File("datos")
    private val archivoMascotas = File(carpeta, "mascotas.json")
    private val archivoSolicitudes = File(carpeta, "solicitudes.json")

    // La primera vez que corre el servidor se crean los archivos.
    init {
        carpeta.mkdirs()
        if (!archivoMascotas.exists()) {
            guardarMascotas(mascotasIniciales())
        }
        if (!archivoSolicitudes.exists()) {
            guardarSolicitudes(emptyList())
        }
    }

    // ---------- Mascotas ----------

    @Synchronized
    fun listarMascotas(): List<Mascota> =
        json.decodeFromString(ListSerializer(Mascota.serializer()), archivoMascotas.readText())

    @Synchronized
    fun agregarMascota(m: Mascota) {
        val lista = listarMascotas()
        val nuevoId = (lista.maxOfOrNull { it.id } ?: 0) + 1
        guardarMascotas(lista + m.copy(id = nuevoId))
    }

    // ---------- Solicitudes ----------

    @Synchronized
    fun listarSolicitudes(): List<Solicitud> =
        json.decodeFromString(ListSerializer(Solicitud.serializer()), archivoSolicitudes.readText())

    @Synchronized
    fun crearSolicitud(mascotaId: Int, cliente: String) {
        val mascota = listarMascotas().find { it.id == mascotaId } ?: return
        val lista = listarSolicitudes()
        val nuevoId = (lista.maxOfOrNull { it.id } ?: 0) + 1
        val nueva = Solicitud(
            id = nuevoId,
            mascotaId = mascotaId,
            mascota = mascota.nombre,
            cliente = cliente,
            estado = "PENDIENTE"
        )
        guardarSolicitudes(lista + nueva)
    }

    @Synchronized
    fun cambiarEstado(id: Int, estado: String) {
        val lista = listarSolicitudes().map { s ->
            if (s.id == id) s.copy(estado = estado) else s
        }
        guardarSolicitudes(lista)
    }

    // ---------- Escritura de archivos ----------

    private fun guardarMascotas(lista: List<Mascota>) {
        archivoMascotas.writeText(json.encodeToString(ListSerializer(Mascota.serializer()), lista))
    }

    private fun guardarSolicitudes(lista: List<Solicitud>) {
        archivoSolicitudes.writeText(json.encodeToString(ListSerializer(Solicitud.serializer()), lista))
    }

    private fun mascotasIniciales() = listOf(
        Mascota(id = 1, nombre = "Firulais", tipo = "Perro", edad = "2 años", refugio = "Refugio Patitas Felices"),
        Mascota(id = 2, nombre = "Luna", tipo = "Gata", edad = "1 año", refugio = "Refugio Patitas Felices"),
        Mascota(id = 3, nombre = "Max", tipo = "Perro", edad = "4 años", refugio = "Refugio Patitas Felices"),
        Mascota(id = 4, nombre = "Nala", tipo = "Perra", edad = "6 meses", refugio = "Refugio Patitas Felices")
    )
}