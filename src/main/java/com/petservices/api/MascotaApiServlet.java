package com.petservices.api;

import com.petservices.dao.MascotaDAO;
import com.petservices.modelo.Mascota;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.petservices.api.util.ApiUtil.*;

/**
 * Servicio REST: /api/mascotas
 * ──────────────────────────────────────────────────────────────────────────
 * GET    /api/mascotas               -> lista todas las mascotas
 * GET    /api/mascotas/{id}          -> obtiene una mascota por id
 * GET    /api/mascotas?idCliente=1   -> lista las mascotas de un cliente
 * POST   /api/mascotas               -> crea una mascota (body JSON)
 * PUT    /api/mascotas/{id}          -> actualiza una mascota (body JSON)
 * DELETE /api/mascotas/{id}          -> elimina una mascota
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA5-EV03
 */
@WebServlet("/api/mascotas/*")
public class MascotaApiServlet extends HttpServlet {

    private final MascotaDAO mascotaDAO = new MascotaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = extraerId(request);
        if (id != -1) {
            Mascota mascota = mascotaDAO.buscarPorId(id);
            if (mascota == null) {
                enviarError(response, HttpServletResponse.SC_NOT_FOUND, "Mascota no encontrada con id " + id);
            } else {
                enviarJson(response, HttpServletResponse.SC_OK, mascota);
            }
            return;
        }
        String idClienteParam = request.getParameter("idCliente");
        if (idClienteParam != null) {
            List<Mascota> lista = mascotaDAO.buscarPorCliente(Integer.parseInt(idClienteParam));
            enviarJson(response, HttpServletResponse.SC_OK, lista);
        } else {
            enviarJson(response, HttpServletResponse.SC_OK, mascotaDAO.listarTodos());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Mascota nueva = GSON.fromJson(leerBody(request), Mascota.class);
        if (nueva == null || nueva.getNombre() == null || nueva.getRaza() == null) {
            enviarError(response, HttpServletResponse.SC_BAD_REQUEST, "Los campos 'nombre' y 'raza' son obligatorios");
            return;
        }
        mascotaDAO.insertar(nueva);
        enviarJson(response, HttpServletResponse.SC_CREATED, nueva);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = extraerId(request);
        if (id == -1) {
            enviarError(response, HttpServletResponse.SC_BAD_REQUEST, "Debe indicar el id de la mascota en la URL: /api/mascotas/{id}");
            return;
        }
        Mascota existente = mascotaDAO.buscarPorId(id);
        if (existente == null) {
            enviarError(response, HttpServletResponse.SC_NOT_FOUND, "Mascota no encontrada con id " + id);
            return;
        }
        request.setCharacterEncoding("UTF-8");
        Mascota datos = GSON.fromJson(leerBody(request), Mascota.class);
        if (datos.getNombre() != null) existente.setNombre(datos.getNombre());
        if (datos.getRaza() != null) existente.setRaza(datos.getRaza());
        if (datos.getIdServicio() != null) existente.setIdServicio(datos.getIdServicio());
        mascotaDAO.actualizar(existente);
        enviarJson(response, HttpServletResponse.SC_OK, existente);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = extraerId(request);
        if (id == -1) {
            enviarError(response, HttpServletResponse.SC_BAD_REQUEST, "Debe indicar el id de la mascota en la URL: /api/mascotas/{id}");
            return;
        }
        boolean eliminado = mascotaDAO.eliminar(id);
        if (eliminado) {
            enviarJson(response, HttpServletResponse.SC_OK, new MensajeDTO("Mascota eliminada correctamente"));
        } else {
            enviarError(response, HttpServletResponse.SC_NOT_FOUND, "Mascota no encontrada con id " + id);
        }
    }

    static class MensajeDTO {
        String mensaje;
        MensajeDTO(String mensaje) { this.mensaje = mensaje; }
    }
}
