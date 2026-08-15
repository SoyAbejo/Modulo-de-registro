package com.petservices.servlet;

import com.petservices.dao.ClienteDAO;
import com.petservices.dao.MascotaDAO;
import com.petservices.modelo.Cliente;
import com.petservices.modelo.Mascota;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet: MascotaServlet
 * URL de mapeo: /mascotas
 * ──────────────────────────────────────────────────────────────────────────
 * Controlador principal del módulo de GESTIÓN DE MASCOTAS (CRUD).
 * Usa el parámetro "accion" para determinar la operación a ejecutar.
 *
 *  doGet  → listar, editar (cargar formulario), eliminar
 *  doPost → crear, actualizar
 *
 * Adicionalmente carga la lista de clientes (dueños) para el <select>
 * del formulario y resuelve el nombre del dueño de cada mascota.
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA2-EV02
 */
public class MascotaServlet extends BaseProtectedServlet {

    private final MascotaDAO mascotaDAO = new MascotaDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();

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
                // Cargar la mascota seleccionada en el formulario de edición
                int idEditar = parseInt(request.getParameter("id"), -1);
                Mascota mascotaEditar = mascotaDAO.buscarPorId(idEditar);
                if (mascotaEditar != null) {
                    request.setAttribute("mascotaEditar", mascotaEditar);
                } else {
                    request.setAttribute("mensaje", "❌ No se encontró la mascota con id " + idEditar + ".");
                    request.setAttribute("tipoMensaje", "error");
                }
                cargarListaYForwardear(request, response);
                break;

            case "eliminar":
                int idEliminar = parseInt(request.getParameter("id"), -1);
                boolean eliminado = mascotaDAO.eliminar(idEliminar);
                if (eliminado) {
                    request.setAttribute("mensaje", "✅ Mascota eliminada correctamente.");
                    request.setAttribute("tipoMensaje", "exito");
                } else {
                    request.setAttribute("mensaje", "❌ No se encontró la mascota.");
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
        String nombre  = limpiar(request.getParameter("nombre"));
        String especie = limpiar(request.getParameter("especie"));
        String raza    = limpiar(request.getParameter("raza"));
        int    edad    = parseInt(request.getParameter("edad"), 0);
        int    idCliente = parseInt(request.getParameter("idCliente"), -1);

        if (nombre.isEmpty() || especie.isEmpty() || raza.isEmpty()) {
            request.setAttribute("mensaje", "❌ Los campos nombre, especie y raza son obligatorios.");
            request.setAttribute("tipoMensaje", "error");
            cargarListaYForwardear(request, response);
            return;
        }
        if (edad < 0) {
            request.setAttribute("mensaje", "❌ La edad no puede ser negativa.");
            request.setAttribute("tipoMensaje", "error");
            cargarListaYForwardear(request, response);
            return;
        }

        // Verificar que el dueño seleccionado exista
        Cliente dueno = clienteDAO.buscarPorId(idCliente);
        if (dueno == null) {
            request.setAttribute("mensaje", "❌ Debes seleccionar un dueño (cliente) válido.");
            request.setAttribute("tipoMensaje", "error");
            cargarListaYForwardear(request, response);
            return;
        }

        if ("crear".equals(accion)) {
            // ── CREAR nueva mascota ──────────────────────────────────────
            Mascota nueva = new Mascota();
            nueva.setNombre(nombre);
            nueva.setEspecie(especie);
            nueva.setRaza(raza);
            nueva.setEdad(edad);
            nueva.setIdCliente(dueno.getIdCliente());
            nueva.setNombreCliente(dueno.getNombre());

            mascotaDAO.insertar(nueva);
            request.setAttribute("mensaje", "✅ Mascota '" + nombre + "' registrada correctamente.");
            request.setAttribute("tipoMensaje", "exito");

        } else if ("actualizar".equals(accion)) {
            // ── ACTUALIZAR mascota existente ─────────────────────────────
            int id = parseInt(request.getParameter("id"), -1);
            Mascota existente = mascotaDAO.buscarPorId(id);
            if (existente != null) {
                existente.setNombre(nombre);
                existente.setEspecie(especie);
                existente.setRaza(raza);
                existente.setEdad(edad);
                existente.setIdCliente(dueno.getIdCliente());
                existente.setNombreCliente(dueno.getNombre());
                mascotaDAO.actualizar(existente);
                request.setAttribute("mensaje", "✅ Mascota actualizada correctamente.");
                request.setAttribute("tipoMensaje", "exito");
            } else {
                request.setAttribute("mensaje", "❌ Mascota no encontrada.");
                request.setAttribute("tipoMensaje", "error");
            }
        }

        cargarListaYForwardear(request, response);
    }

    // ──────────────────────────────────────────────────────────────────────
    // Método auxiliar: carga la lista de mascotas + clientes y hace forward
    // ──────────────────────────────────────────────────────────────────────
    private void cargarListaYForwardear(HttpServletRequest request,
                                        HttpServletResponse response)
            throws ServletException, IOException {
        List<Mascota> listaMascotas = mascotaDAO.listarTodos();
        List<Cliente> listaClientes = clienteDAO.listarTodos();
        request.setAttribute("listaMascotas", listaMascotas);
        request.setAttribute("listaClientes", listaClientes);
        forward(request, response, "/vistas/mascotas.jsp");
    }

}
