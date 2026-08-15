package com.petservices.api;

import com.petservices.dao.CitaDAO;
import com.petservices.modelo.Cita;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.petservices.api.util.ApiUtil.*;

/**
 * Servicio REST: /api/citas
 * ──────────────────────────────────────────────────────────────────────────
 * GET    /api/citas              -> lista todas las citas
 * GET    /api/citas/{id}         -> obtiene una cita por id
 * GET    /api/citas?estado=X     -> filtra citas por estado
 * POST   /api/citas              -> agenda una cita (body JSON)
 * PUT    /api/citas/{id}         -> actualiza una cita (fecha/hora/estado)
 * DELETE /api/citas/{id}         -> cancela (elimina) una cita
 *
 * Regla de negocio: el campo "estado" solo admite los valores definidos
 * en el modelo físico: pendiente, confirmada, cancelada, completada.
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA5-EV03
 */
@WebServlet("/api/citas/*")
public class CitaApiServlet extends HttpServlet {

    private static final List<String> ESTADOS_VALIDOS =
            Arrays.asList("pendiente", "confirmada", "cancelada", "completada");

    private final CitaDAO citaDAO = new CitaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = extraerId(request);
        if (id != -1) {
            Cita cita = citaDAO.buscarPorId(id);
            if (cita == null) {
                enviarError(response, HttpServletResponse.SC_NOT_FOUND, "Cita no encontrada con id " + id);
            } else {
                enviarJson(response, HttpServletResponse.SC_OK, cita);
            }
            return;
        }
        String estado = request.getParameter("estado");
        List<Cita> lista = citaDAO.listarTodos();
        if (estado != null) {
            lista.removeIf(c -> !c.getEstado().equalsIgnoreCase(estado));
        }
        enviarJson(response, HttpServletResponse.SC_OK, lista);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Cita nueva = parsearJson(request, Cita.class);
        if (nueva == null || nueva.getFecha() == null || nueva.getHora() == null) {
            enviarError(response, HttpServletResponse.SC_BAD_REQUEST, "Los campos 'fecha' y 'hora' son obligatorios");
            return;
        }
        if (nueva.getEstado() == null) nueva.setEstado("pendiente");
        if (!ESTADOS_VALIDOS.contains(nueva.getEstado())) {
            enviarError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Estado inválido. Valores permitidos: " + ESTADOS_VALIDOS);
            return;
        }
        citaDAO.insertar(nueva);
        enviarJson(response, HttpServletResponse.SC_CREATED, nueva);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = extraerId(request);
        if (id == -1) {
            enviarError(response, HttpServletResponse.SC_BAD_REQUEST, "Debe indicar el id en la URL: /api/citas/{id}");
            return;
        }
        Cita existente = citaDAO.buscarPorId(id);
        if (existente == null) {
            enviarError(response, HttpServletResponse.SC_NOT_FOUND, "Cita no encontrada con id " + id);
            return;
        }
        request.setCharacterEncoding("UTF-8");
        Cita datos = parsearJson(request, Cita.class);
        if (datos == null) {
            enviarError(response, HttpServletResponse.SC_BAD_REQUEST, "Cuerpo de la petición inválido o vacío");
            return;
        }
        if (datos.getFecha() != null) existente.setFecha(datos.getFecha());
        if (datos.getHora() != null) existente.setHora(datos.getHora());
        if (datos.getEstado() != null) {
            if (!ESTADOS_VALIDOS.contains(datos.getEstado())) {
                enviarError(response, HttpServletResponse.SC_BAD_REQUEST,
                        "Estado inválido. Valores permitidos: " + ESTADOS_VALIDOS);
                return;
            }
            existente.setEstado(datos.getEstado());
        }
        citaDAO.actualizar(existente);
        enviarJson(response, HttpServletResponse.SC_OK, existente);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = extraerId(request);
        if (id == -1) {
            enviarError(response, HttpServletResponse.SC_BAD_REQUEST, "Debe indicar el id en la URL: /api/citas/{id}");
            return;
        }
        boolean eliminada = citaDAO.eliminar(id);
        if (eliminada) {
            enviarJson(response, HttpServletResponse.SC_OK, new MensajeDTO("Cita eliminada correctamente"));
        } else {
            enviarError(response, HttpServletResponse.SC_NOT_FOUND, "Cita no encontrada con id " + id);
        }
    }

    static class MensajeDTO {
        String mensaje;
        MensajeDTO(String mensaje) { this.mensaje = mensaje; }
    }
}
