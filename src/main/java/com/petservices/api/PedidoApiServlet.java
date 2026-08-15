package com.petservices.api;

import com.petservices.dao.PedidoDAO;
import com.petservices.modelo.Pedido;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.petservices.api.util.ApiUtil.*;

/**
 * Servicio REST: /api/pedidos
 * ──────────────────────────────────────────────────────────────────────────
 * GET    /api/pedidos       -> lista todos los pedidos
 * GET    /api/pedidos/{id}  -> obtiene un pedido por id
 * POST   /api/pedidos       -> crea un pedido (body JSON)
 * PUT    /api/pedidos/{id}  -> actualiza el estado/datos de un pedido
 * DELETE /api/pedidos/{id}  -> elimina un pedido
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA5-EV03
 */
@WebServlet("/api/pedidos/*")
public class PedidoApiServlet extends HttpServlet {

    private final PedidoDAO pedidoDAO = new PedidoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = extraerId(request);
        if (id == -1) {
            enviarJson(response, HttpServletResponse.SC_OK, pedidoDAO.listarTodos());
        } else {
            Pedido p = pedidoDAO.buscarPorId(id);
            if (p == null) {
                enviarError(response, HttpServletResponse.SC_NOT_FOUND, "Pedido no encontrado con id " + id);
            } else {
                enviarJson(response, HttpServletResponse.SC_OK, p);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Pedido nuevo = parsearJson(request, Pedido.class);
        if (nuevo == null || nuevo.getTotal() <= 0) {
            enviarError(response, HttpServletResponse.SC_BAD_REQUEST, "El campo 'total' debe ser mayor que 0");
            return;
        }
        pedidoDAO.insertar(nuevo);
        enviarJson(response, HttpServletResponse.SC_CREATED, nuevo);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = extraerId(request);
        if (id == -1) {
            enviarError(response, HttpServletResponse.SC_BAD_REQUEST, "Debe indicar el id en la URL: /api/pedidos/{id}");
            return;
        }
        Pedido existente = pedidoDAO.buscarPorId(id);
        if (existente == null) {
            enviarError(response, HttpServletResponse.SC_NOT_FOUND, "Pedido no encontrado con id " + id);
            return;
        }
        request.setCharacterEncoding("UTF-8");
        Pedido datos = parsearJson(request, Pedido.class);
        if (datos == null) {
            enviarError(response, HttpServletResponse.SC_BAD_REQUEST, "Cuerpo de la petición inválido o vacío");
            return;
        }
        if (datos.getEstado() != null) existente.setEstado(datos.getEstado());
        if (datos.getTotal() > 0) existente.setTotal(datos.getTotal());
        if (datos.getCantidad() > 0) existente.setCantidad(datos.getCantidad());
        pedidoDAO.actualizar(existente);
        enviarJson(response, HttpServletResponse.SC_OK, existente);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = extraerId(request);
        if (id == -1) {
            enviarError(response, HttpServletResponse.SC_BAD_REQUEST, "Debe indicar el id en la URL: /api/pedidos/{id}");
            return;
        }
        boolean eliminado = pedidoDAO.eliminar(id);
        if (eliminado) {
            enviarJson(response, HttpServletResponse.SC_OK, new MensajeDTO("Pedido eliminado correctamente"));
        } else {
            enviarError(response, HttpServletResponse.SC_NOT_FOUND, "Pedido no encontrado con id " + id);
        }
    }

    static class MensajeDTO {
        String mensaje;
        MensajeDTO(String mensaje) { this.mensaje = mensaje; }
    }
}
