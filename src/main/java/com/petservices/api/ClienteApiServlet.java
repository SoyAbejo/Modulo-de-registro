package com.petservices.api;

import com.petservices.dao.ClienteDAO;
import com.petservices.modelo.Cliente;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.petservices.api.util.ApiUtil.*;

/**
 * Servicio REST: /api/clientes
 * ──────────────────────────────────────────────────────────────────────────
 * GET    /api/clientes        -> lista todos los clientes
 * GET    /api/clientes/{id}   -> obtiene un cliente por id
 * POST   /api/clientes        -> crea un cliente (body JSON)
 * PUT    /api/clientes/{id}   -> actualiza un cliente (body JSON)
 * DELETE /api/clientes/{id}   -> elimina un cliente
 *
 * Todas las respuestas son JSON. Ver documentación completa en
 * DOCUMENTACION_API.md
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA5-EV03
 */
@WebServlet("/api/clientes/*")
public class ClienteApiServlet extends HttpServlet {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = extraerId(request);
        if (id == -1) {
            List<Cliente> lista = clienteDAO.listarTodos();
            enviarJson(response, HttpServletResponse.SC_OK, lista);
        } else {
            Cliente cliente = clienteDAO.buscarPorId(id);
            if (cliente == null) {
                enviarError(response, HttpServletResponse.SC_NOT_FOUND, "Cliente no encontrado con id " + id);
            } else {
                enviarJson(response, HttpServletResponse.SC_OK, cliente);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Cliente nuevo = GSON.fromJson(leerBody(request), Cliente.class);
        if (nuevo == null || nuevo.getNombre() == null || nuevo.getCorreo() == null) {
            enviarError(response, HttpServletResponse.SC_BAD_REQUEST, "Los campos 'nombre' y 'correo' son obligatorios");
            return;
        }
        boolean creado = clienteDAO.insertar(nuevo);
        if (creado) {
            enviarJson(response, HttpServletResponse.SC_CREATED, nuevo);
        } else {
            enviarError(response, HttpServletResponse.SC_CONFLICT, "El correo '" + nuevo.getCorreo() + "' ya está registrado");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = extraerId(request);
        if (id == -1) {
            enviarError(response, HttpServletResponse.SC_BAD_REQUEST, "Debe indicar el id del cliente en la URL: /api/clientes/{id}");
            return;
        }
        Cliente existente = clienteDAO.buscarPorId(id);
        if (existente == null) {
            enviarError(response, HttpServletResponse.SC_NOT_FOUND, "Cliente no encontrado con id " + id);
            return;
        }
        request.setCharacterEncoding("UTF-8");
        Cliente datos = GSON.fromJson(leerBody(request), Cliente.class);
        if (datos.getNombre() != null) existente.setNombre(datos.getNombre());
        if (datos.getCorreo() != null) existente.setCorreo(datos.getCorreo());
        if (datos.getContrasena() != null && !datos.getContrasena().isEmpty()) {
            existente.setContrasena(datos.getContrasena());
        }
        clienteDAO.actualizar(existente);
        enviarJson(response, HttpServletResponse.SC_OK, existente);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = extraerId(request);
        if (id == -1) {
            enviarError(response, HttpServletResponse.SC_BAD_REQUEST, "Debe indicar el id del cliente en la URL: /api/clientes/{id}");
            return;
        }
        boolean eliminado = clienteDAO.eliminar(id);
        if (eliminado) {
            enviarJson(response, HttpServletResponse.SC_OK, new MensajeDTO("Cliente eliminado correctamente"));
        } else {
            enviarError(response, HttpServletResponse.SC_NOT_FOUND, "Cliente no encontrado con id " + id);
        }
    }

    /** DTO simple para mensajes de confirmación. */
    static class MensajeDTO {
        String mensaje;
        MensajeDTO(String mensaje) { this.mensaje = mensaje; }
    }
}
