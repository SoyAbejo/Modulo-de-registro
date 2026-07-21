package com.petservices.api;

import com.petservices.dao.ProductoDAO;
import com.petservices.modelo.Producto;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.petservices.api.util.ApiUtil.*;

/**
 * Servicio REST: /api/productos
 * ──────────────────────────────────────────────────────────────────────────
 * GET    /api/productos                 -> lista todos los productos
 * GET    /api/productos/{id}            -> obtiene un producto por id
 * GET    /api/productos?stockMenorA=20  -> filtra productos con stock bajo
 * POST   /api/productos                 -> crea un producto (body JSON)
 * PUT    /api/productos/{id}            -> actualiza un producto (body JSON)
 * DELETE /api/productos/{id}            -> elimina un producto
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA5-EV03
 */
@WebServlet("/api/productos/*")
public class ProductoApiServlet extends HttpServlet {

    private final ProductoDAO productoDAO = new ProductoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = extraerId(request);
        if (id != -1) {
            Producto p = productoDAO.buscarPorId(id);
            if (p == null) {
                enviarError(response, HttpServletResponse.SC_NOT_FOUND, "Producto no encontrado con id " + id);
            } else {
                enviarJson(response, HttpServletResponse.SC_OK, p);
            }
            return;
        }
        String stockMenorA = request.getParameter("stockMenorA");
        List<Producto> lista = productoDAO.listarTodos();
        if (stockMenorA != null) {
            int limite = Integer.parseInt(stockMenorA);
            lista.removeIf(p -> p.getStock() >= limite);
        }
        enviarJson(response, HttpServletResponse.SC_OK, lista);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Producto nuevo = GSON.fromJson(leerBody(request), Producto.class);
        if (nuevo == null || nuevo.getNombre() == null) {
            enviarError(response, HttpServletResponse.SC_BAD_REQUEST, "El campo 'nombre' es obligatorio");
            return;
        }
        productoDAO.insertar(nuevo);
        enviarJson(response, HttpServletResponse.SC_CREATED, nuevo);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = extraerId(request);
        if (id == -1) {
            enviarError(response, HttpServletResponse.SC_BAD_REQUEST, "Debe indicar el id en la URL: /api/productos/{id}");
            return;
        }
        Producto existente = productoDAO.buscarPorId(id);
        if (existente == null) {
            enviarError(response, HttpServletResponse.SC_NOT_FOUND, "Producto no encontrado con id " + id);
            return;
        }
        request.setCharacterEncoding("UTF-8");
        Producto datos = GSON.fromJson(leerBody(request), Producto.class);
        if (datos.getNombre() != null) existente.setNombre(datos.getNombre());
        if (datos.getDescripcion() != null) existente.setDescripcion(datos.getDescripcion());
        if (datos.getPrecio() != 0) existente.setPrecio(datos.getPrecio());
        if (datos.getEstado() != null) existente.setEstado(datos.getEstado());
        existente.setStock(datos.getStock());
        productoDAO.actualizar(existente);
        enviarJson(response, HttpServletResponse.SC_OK, existente);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = extraerId(request);
        if (id == -1) {
            enviarError(response, HttpServletResponse.SC_BAD_REQUEST, "Debe indicar el id en la URL: /api/productos/{id}");
            return;
        }
        boolean eliminado = productoDAO.eliminar(id);
        if (eliminado) {
            enviarJson(response, HttpServletResponse.SC_OK, new MensajeDTO("Producto eliminado correctamente"));
        } else {
            enviarError(response, HttpServletResponse.SC_NOT_FOUND, "Producto no encontrado con id " + id);
        }
    }

    static class MensajeDTO {
        String mensaje;
        MensajeDTO(String mensaje) { this.mensaje = mensaje; }
    }
}
