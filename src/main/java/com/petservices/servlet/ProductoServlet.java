package com.petservices.servlet;

import com.petservices.dao.ProductoDAO;
import com.petservices.modelo.Producto;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet: ProductoServlet
 * URL de mapeo: /productos
 * ──────────────────────────────────────────────────────────────────────────
 * Controlador principal del módulo de INVENTARIO DE PRODUCTOS (CRUD).
 * Usa el parámetro "accion" para determinar la operación a ejecutar.
 *
 *  doGet  → listar, editar (cargar formulario), eliminar
 *  doPost → crear, actualizar
 *
 * Reglas de negocio:
 *  - nombre y categoria obligatorios.
 *  - precio y stock no pueden ser negativos.
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA2-EV02
 */
public class ProductoServlet extends BaseProtectedServlet {

    private final ProductoDAO productoDAO = new ProductoDAO();

    // ──────────────────────────────────────────────────────────────────────
    // doGet: Operaciones de lectura y navegación
    // ──────────────────────────────────────────────────────────────────────
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        if (requireLogin(request, response)) {
            return;
        }

        String accion = request.getParameter("accion");
        if (accion == null) accion = "listar";

        switch (accion) {

            case "editar":
                int idEditar = parseInt(request.getParameter("id"), -1);
                Producto productoEditar = productoDAO.buscarPorId(idEditar);
                if (productoEditar != null) {
                    request.setAttribute("productoEditar", productoEditar);
                } else {
                    request.setAttribute("mensaje", "❌ No se encontró el producto con id " + idEditar + ".");
                    request.setAttribute("tipoMensaje", "error");
                }
                cargarListaYForwardear(request, response);
                break;

            case "eliminar":
                int idEliminar = parseInt(request.getParameter("id"), -1);
                boolean eliminado = productoDAO.eliminar(idEliminar);
                if (eliminado) {
                    request.setAttribute("mensaje", "✅ Producto eliminado correctamente.");
                    request.setAttribute("tipoMensaje", "exito");
                } else {
                    request.setAttribute("mensaje", "❌ No se encontró el producto.");
                    request.setAttribute("tipoMensaje", "error");
                }
                cargarListaYForwardear(request, response);
                break;

            case "listar":
            default:
                cargarListaYForwardear(request, response);
                break;
        }
    }

    // ──────────────────────────────────────────────────────────────────────
    // doPost: Operaciones de escritura (crear / actualizar)
    // ──────────────────────────────────────────────────────────────────────
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        if (requireLogin(request, response)) {
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");

        // ── Leer y validar los campos comunes ────────────────────────────
        String nombre    = limpiar(request.getParameter("nombre"));
        String categoria = limpiar(request.getParameter("categoria"));
        double precio    = parseDouble(request.getParameter("precio"), -1);
        int    stock     = parseInt(request.getParameter("stock"), -1);

        if (nombre.isEmpty() || categoria.isEmpty()) {
            request.setAttribute("mensaje", "❌ Los campos nombre y categoría son obligatorios.");
            request.setAttribute("tipoMensaje", "error");
            cargarListaYForwardear(request, response);
            return;
        }
        if (precio < 0) {
            request.setAttribute("mensaje", "❌ El precio no puede ser negativo.");
            request.setAttribute("tipoMensaje", "error");
            cargarListaYForwardear(request, response);
            return;
        }
        if (stock < 0) {
            request.setAttribute("mensaje", "❌ El stock no puede ser negativo.");
            request.setAttribute("tipoMensaje", "error");
            cargarListaYForwardear(request, response);
            return;
        }

        if ("crear".equals(accion)) {
            // ── CREAR nuevo producto ─────────────────────────────────────
            Producto nuevo = new Producto();
            nuevo.setNombre(nombre);
            nuevo.setCategoria(categoria);
            nuevo.setPrecio(precio);
            nuevo.setStock(stock);

            productoDAO.insertar(nuevo);
            request.setAttribute("mensaje", "✅ Producto '" + nombre + "' agregado al inventario.");
            request.setAttribute("tipoMensaje", "exito");

        } else if ("actualizar".equals(accion)) {
            // ── ACTUALIZAR producto existente ────────────────────────────
            int id = parseInt(request.getParameter("id"), -1);
            Producto existente = productoDAO.buscarPorId(id);
            if (existente != null) {
                existente.setNombre(nombre);
                existente.setCategoria(categoria);
                existente.setPrecio(precio);
                existente.setStock(stock);
                productoDAO.actualizar(existente);
                request.setAttribute("mensaje", "✅ Producto actualizado correctamente.");
                request.setAttribute("tipoMensaje", "exito");
            } else {
                request.setAttribute("mensaje", "❌ Producto no encontrado.");
                request.setAttribute("tipoMensaje", "error");
            }
        }

        cargarListaYForwardear(request, response);
    }

    // ──────────────────────────────────────────────────────────────────────
    // Método auxiliar: carga la lista de productos y hace forward
    // ──────────────────────────────────────────────────────────────────────
    private void cargarListaYForwardear(HttpServletRequest request,
                                        HttpServletResponse response)
            throws ServletException, IOException {
        List<Producto> listaProductos = productoDAO.listarTodos();
        request.setAttribute("listaProductos", listaProductos);
        forward(request, response, "/vistas/productos.jsp");
    }

}
