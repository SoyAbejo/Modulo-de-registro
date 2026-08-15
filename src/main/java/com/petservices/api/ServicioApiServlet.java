package com.petservices.api;

import com.petservices.dao.ServicioDAO;
import com.petservices.modelo.Servicio;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.petservices.api.util.ApiUtil.*;

/**
 * Servicio REST: /api/servicios
 * ──────────────────────────────────────────────────────────────────────────
 * GET    /api/servicios       -> lista todos los tipos de servicio
 * GET    /api/servicios/{id}  -> obtiene un servicio por id
 * POST   /api/servicios       -> crea un servicio (body JSON)
 * PUT    /api/servicios/{id}  -> actualiza un servicio (body JSON)
 * DELETE /api/servicios/{id}  -> elimina un servicio
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA5-EV03
 */
@WebServlet("/api/servicios/*")
public class ServicioApiServlet extends HttpServlet {

    private final ServicioDAO servicioDAO = new ServicioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = extraerId(request);
        if (id == -1) {
            enviarJson(response, HttpServletResponse.SC_OK, servicioDAO.listarTodos());
        } else {
            Servicio s = servicioDAO.buscarPorId(id);
            if (s == null) {
                enviarError(response, HttpServletResponse.SC_NOT_FOUND, "Servicio no encontrado con id " + id);
            } else {
                enviarJson(response, HttpServletResponse.SC_OK, s);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Servicio nuevo = parsearJson(request, Servicio.class);
        if (nuevo == null || nuevo.getNombre() == null || nuevo.getTipo() == null) {
            enviarError(response, HttpServletResponse.SC_BAD_REQUEST, "Los campos 'nombre' y 'tipo' son obligatorios");
            return;
        }
        servicioDAO.insertar(nuevo);
        enviarJson(response, HttpServletResponse.SC_CREATED, nuevo);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = extraerId(request);
        if (id == -1) {
            enviarError(response, HttpServletResponse.SC_BAD_REQUEST, "Debe indicar el id en la URL: /api/servicios/{id}");
            return;
        }
        Servicio existente = servicioDAO.buscarPorId(id);
        if (existente == null) {
            enviarError(response, HttpServletResponse.SC_NOT_FOUND, "Servicio no encontrado con id " + id);
            return;
        }
        request.setCharacterEncoding("UTF-8");
        Servicio datos = parsearJson(request, Servicio.class);
        if (datos == null) {
            enviarError(response, HttpServletResponse.SC_BAD_REQUEST, "Cuerpo de la petición inválido o vacío");
            return;
        }
        if (datos.getNombre() != null) existente.setNombre(datos.getNombre());
        if (datos.getTipo() != null) existente.setTipo(datos.getTipo());
        if (datos.getIdMascota() != null) existente.setIdMascota(datos.getIdMascota());
        servicioDAO.actualizar(existente);
        enviarJson(response, HttpServletResponse.SC_OK, existente);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = extraerId(request);
        if (id == -1) {
            enviarError(response, HttpServletResponse.SC_BAD_REQUEST, "Debe indicar el id en la URL: /api/servicios/{id}");
            return;
        }
        boolean eliminado = servicioDAO.eliminar(id);
        if (eliminado) {
            enviarJson(response, HttpServletResponse.SC_OK, new MensajeDTO("Servicio eliminado correctamente"));
        } else {
            enviarError(response, HttpServletResponse.SC_NOT_FOUND, "Servicio no encontrado con id " + id);
        }
    }

    static class MensajeDTO {
        String mensaje;
        MensajeDTO(String mensaje) { this.mensaje = mensaje; }
    }
}
