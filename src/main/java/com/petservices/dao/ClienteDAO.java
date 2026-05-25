package com.petservices.dao;

import com.petservices.modelo.Cliente;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DAO (Data Access Object): ClienteDAO
 * ──────────────────────────────────────────────────────────────────────────
 * SIMULACIÓN EN MEMORIA — No requiere base de datos para la demo.
 * En producción, reemplaza los métodos por conexiones JDBC a MySQL
 * usando el modelo físico en petconnect_completo.sql.
 *
 * La lista "baseDatos" actúa como tabla en RAM y el AtomicInteger
 * simula el AUTO_INCREMENT de la tabla `cliente`.
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA2-EV02
 */
public class ClienteDAO {

    // ── "Base de datos" en memoria (static = compartida en toda la app) ──
    private static final List<Cliente>      baseDatos  = new ArrayList<>();
    private static final AtomicInteger      contadorId = new AtomicInteger(1);

    // ── Datos de prueba precargados (equivale a los INSERTs del .sql) ──
    static {
        baseDatos.add(new Cliente(contadorId.getAndIncrement(),
                "Alejandro Puerto", "alejandro@gmail.com", "123456", "2026-01-15"));
        baseDatos.add(new Cliente(contadorId.getAndIncrement(),
                "Daniel Mejia",     "daniel@gmail.com",    "123456", "2026-01-16"));
        baseDatos.add(new Cliente(contadorId.getAndIncrement(),
                "David Yaya",       "david@gmail.com",     "123456", "2026-01-17"));
    }

    // ──────────────────────────────────────────────────────────────────────
    // CREATE — Insertar nuevo cliente
    // Equivale a: INSERT INTO cliente (nombre, correo, contraseña) VALUES (?,?,?)
    // ──────────────────────────────────────────────────────────────────────
    public boolean insertar(Cliente c) {
        // Validar que el correo no esté duplicado (UNIQUE en BD real)
        if (buscarPorCorreo(c.getCorreo()) != null) {
            return false; // correo ya existe
        }
        c.setIdCliente(contadorId.getAndIncrement());
        c.setFechaRegistro(java.time.LocalDate.now().toString());
        baseDatos.add(c);
        return true;
    }

    // ──────────────────────────────────────────────────────────────────────
    // READ — Listar todos los clientes
    // Equivale a: SELECT * FROM cliente
    // ──────────────────────────────────────────────────────────────────────
    public List<Cliente> listarTodos() {
        return new ArrayList<>(baseDatos); // copia defensiva
    }

    // ──────────────────────────────────────────────────────────────────────
    // READ — Buscar por ID
    // Equivale a: SELECT * FROM cliente WHERE id_cliente = ?
    // ──────────────────────────────────────────────────────────────────────
    public Cliente buscarPorId(int id) {
        for (Cliente c : baseDatos) {
            if (c.getIdCliente() == id) return c;
        }
        return null;
    }

    // ──────────────────────────────────────────────────────────────────────
    // READ — Buscar por correo (usado en login)
    // Equivale a: SELECT * FROM cliente WHERE correo = ?
    // ──────────────────────────────────────────────────────────────────────
    public Cliente buscarPorCorreo(String correo) {
        for (Cliente c : baseDatos) {
            if (c.getCorreo().equalsIgnoreCase(correo)) return c;
        }
        return null;
    }

    // ──────────────────────────────────────────────────────────────────────
    // UPDATE — Actualizar datos de un cliente
    // Equivale a: UPDATE cliente SET nombre=?, correo=? WHERE id_cliente=?
    // ──────────────────────────────────────────────────────────────────────
    public boolean actualizar(Cliente nuevo) {
        for (int i = 0; i < baseDatos.size(); i++) {
            if (baseDatos.get(i).getIdCliente() == nuevo.getIdCliente()) {
                baseDatos.set(i, nuevo);
                return true;
            }
        }
        return false;
    }

    // ──────────────────────────────────────────────────────────────────────
    // DELETE — Eliminar cliente por ID
    // Equivale a: DELETE FROM cliente WHERE id_cliente = ?
    // ──────────────────────────────────────────────────────────────────────
    public boolean eliminar(int id) {
        return baseDatos.removeIf(c -> c.getIdCliente() == id);
    }

    // ──────────────────────────────────────────────────────────────────────
    // LOGIN — Validar credenciales
    // Equivale a: SELECT * FROM cliente WHERE correo=? AND contraseña=?
    // ──────────────────────────────────────────────────────────────────────
    public Cliente validarLogin(String correo, String contrasena) {
        for (Cliente c : baseDatos) {
            if (c.getCorreo().equalsIgnoreCase(correo)
                    && c.getContrasena().equals(contrasena)) {
                return c;
            }
        }
        return null;
    }
}
