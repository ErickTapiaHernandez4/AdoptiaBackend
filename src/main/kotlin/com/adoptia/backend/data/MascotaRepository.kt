package com.adoptia.backend.data

import com.adoptia.backend.data.model.Mascota
import com.adoptia.backend.data.model.Solicitud

// Guarda mascotas y solicitudes en la base de datos MySQL de Railway.
class MascotaRepository {

    // Al arrancar el servidor: crea las tablas si no existen y mete mascotas de ejemplo.
    init {
        Database.conexion().use { c ->
            c.createStatement().use { st ->
                st.executeUpdate(
                    """
                    CREATE TABLE IF NOT EXISTS mascotas (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        nombre VARCHAR(100) NOT NULL,
                        tipo VARCHAR(50) NOT NULL,
                        edad VARCHAR(50) NOT NULL,
                        refugio VARCHAR(150) NOT NULL
                    )
                    """.trimIndent()
                )
                st.executeUpdate(
                    """
                    CREATE TABLE IF NOT EXISTS solicitudes (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        mascota_id INT NOT NULL,
                        cliente VARCHAR(150) NOT NULL,
                        estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
                        FOREIGN KEY (mascota_id) REFERENCES mascotas(id)
                    )
                    """.trimIndent()
                )
                val total = st.executeQuery("SELECT COUNT(*) FROM mascotas").use { rs ->
                    rs.next()
                    rs.getInt(1)
                }
                if (total == 0) {
                    st.executeUpdate(
                        """
                        INSERT INTO mascotas (nombre, tipo, edad, refugio) VALUES
                        ('Firulais', 'Perro', '2 años', 'Refugio Patitas Felices'),
                        ('Luna', 'Gata', '1 año', 'Refugio Patitas Felices'),
                        ('Max', 'Perro', '4 años', 'Refugio Patitas Felices'),
                        ('Nala', 'Perra', '6 meses', 'Refugio Patitas Felices')
                        """.trimIndent()
                    )
                }
            }
        }
    }

    fun listarMascotas(): List<Mascota> =
        Database.conexion().use { c ->
            c.createStatement().use { st ->
                st.executeQuery("SELECT id, nombre, tipo, edad, refugio FROM mascotas ORDER BY id").use { rs ->
                    val lista = mutableListOf<Mascota>()
                    while (rs.next()) {
                        lista.add(
                            Mascota(
                                id = rs.getInt("id"),
                                nombre = rs.getString("nombre"),
                                tipo = rs.getString("tipo"),
                                edad = rs.getString("edad"),
                                refugio = rs.getString("refugio")
                            )
                        )
                    }
                    lista
                }
            }
        }

    fun agregarMascota(m: Mascota) {
        Database.conexion().use { c ->
            c.prepareStatement("INSERT INTO mascotas (nombre, tipo, edad, refugio) VALUES (?, ?, ?, ?)").use { ps ->
                ps.setString(1, m.nombre)
                ps.setString(2, m.tipo)
                ps.setString(3, m.edad)
                ps.setString(4, m.refugio)
                ps.executeUpdate()
            }
        }
    }

    fun listarSolicitudes(): List<Solicitud> =
        Database.conexion().use { c ->
            c.createStatement().use { st ->
                st.executeQuery(
                    "SELECT s.id, s.mascota_id, m.nombre AS mascota, s.cliente, s.estado " +
                            "FROM solicitudes s JOIN mascotas m ON m.id = s.mascota_id ORDER BY s.id"
                ).use { rs ->
                    val lista = mutableListOf<Solicitud>()
                    while (rs.next()) {
                        lista.add(
                            Solicitud(
                                id = rs.getInt("id"),
                                mascotaId = rs.getInt("mascota_id"),
                                mascota = rs.getString("mascota"),
                                cliente = rs.getString("cliente"),
                                estado = rs.getString("estado")
                            )
                        )
                    }
                    lista
                }
            }
        }

    fun crearSolicitud(mascotaId: Int, cliente: String) {
        Database.conexion().use { c ->
            c.prepareStatement("INSERT INTO solicitudes (mascota_id, cliente) VALUES (?, ?)").use { ps ->
                ps.setInt(1, mascotaId)
                ps.setString(2, cliente)
                ps.executeUpdate()
            }
        }
    }

    fun cambiarEstado(id: Int, estado: String) {
        Database.conexion().use { c ->
            c.prepareStatement("UPDATE solicitudes SET estado = ? WHERE id = ?").use { ps ->
                ps.setString(1, estado)
                ps.setInt(2, id)
                ps.executeUpdate()
            }
        }
    }
}