package com.petservices.servlet;

import com.petservices.dao.ServicioDAO;
import com.petservices.modelo.Servicio;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet: ServicioServlet
 * URL de mapeo: /servicios
 * ──────────────────────────────────────────────────────────────────────────
 * Controlador del módulo de CATÁLOGO DE SERVICIOS (CRUD).
 * Usa el parámetro "accion" para determinar la operación a ejecutar.
 *
 *  doGet  → listar, editar (cargar formulario), eliminar
 *  doPost → crear, actualizar
 *
 * Reglas de negocio:
 *  - tipo y nombre son obligatorios.
 *
 * Proyecto: PetServices - SENA GA7-220501096
 */
public class ServicioServlet extends BaseProtectedServlet {

    private final ServicioDAO servicioDAO = new ServicioDAO();

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
                Servicio servicioEditar = servicioDAO.buscarPorId(idEditar);
                if (servicioEditar != null) {
                    request.setAttribute("servicioEditar", servicioEditar);
                } else {
                    request.setAttribute("mensaje", "❌ No se encontró el servicio con id " + idEditar + ".");
                    request.setAttribute("tipoMensaje", "error");
                }
                cargarListaYForwardear(request, response);
                break;

            case "eliminar":
                int idEliminar = parseInt(request.getParameter("id"), -1);
                boolean eliminado = servicioDAO.eliminar(idEliminar);
                if (eliminado) {
                    request.setAttribute("mensaje", "✅ Servicio eliminado correctamente.");
                    request.setAttribute("tipoMensaje", "exito");
                } else {
                    request.setAttribute("mensaje", "❌ No se encontró el servicio.");
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
        String tipo   = limpiar(request.getParameter("tipo"));
        String nombre = limpiar(request.getParameter("nombre"));

        if (tipo.isEmpty() || nombre.isEmpty()) {
            request.setAttribute("mensaje", "❌ Los campos tipo y nombre son obligatorios.");
            request.setAttribute("tipoMensaje", "error");
            cargarListaYForwardear(request, response);
            return;
        }

        if ("crear".equals(accion)) {
            // ── CREAR nuevo servicio ─────────────────────────────────────
            Servicio nuevo = new Servicio();
            nuevo.setTipo(tipo);
            nuevo.setNombre(nombre);

            servicioDAO.insertar(nuevo);
            request.setAttribute("mensaje", "✅ Servicio '" + nombre + "' agregado al catálogo.");
            request.setAttribute("tipoMensaje", "exito");

        } else if ("actualizar".equals(accion)) {
            // ── ACTUALIZAR servicio existente ────────────────────────────
            int id = parseInt(request.getParameter("id"), -1);
            Servicio existente = servicioDAO.buscarPorId(id);
            if (existente != null) {
                existente.setTipo(tipo);
                existente.setNombre(nombre);
                servicioDAO.actualizar(existente);
                request.setAttribute("mensaje", "✅ Servicio actualizado correctamente.");
                request.setAttribute("tipoMensaje", "exito");
            } else {
                request.setAttribute("mensaje", "❌ Servicio no encontrado.");
                request.setAttribute("tipoMensaje", "error");
            }
        }

        cargarListaYForwardear(request, response);
    }

    // ──────────────────────────────────────────────────────────────────────
    // Método auxiliar: carga la lista de servicios y hace forward
    // ──────────────────────────────────────────────────────────────────────
    private void cargarListaYForwardear(HttpServletRequest request,
                                        HttpServletResponse response)
            throws ServletException, IOException {
        List<Servicio> listaServicios = servicioDAO.listarTodos();
        request.setAttribute("listaServicios", listaServicios);
        forward(request, response, "/vistas/servicios.jsp");
    }

}
