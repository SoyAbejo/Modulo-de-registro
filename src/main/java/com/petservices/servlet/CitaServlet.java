package com.petservices.servlet;

import com.petservices.dao.CitaDAO;
import com.petservices.dao.ClienteDAO;
import com.petservices.dao.MascotaDAO;
import com.petservices.modelo.Cita;
import com.petservices.modelo.Cliente;
import com.petservices.modelo.Mascota;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Servlet: CitaServlet
 * URL de mapeo: /citas
 * ──────────────────────────────────────────────────────────────────────────
 * Controlador principal del módulo de AGENDA DE CITAS (CRUD).
 * Usa el parámetro "accion" para determinar la operación a ejecutar.
 *
 *  doGet  → listar, editar (cargar formulario), eliminar
 *  doPost → crear, actualizar
 *
 * Carga la lista de clientes y de mascotas para los <select> dinámicos
 * del formulario y resuelve los nombres de cliente y mascota de cada cita.
 *
 * Reglas de negocio:
 *  - La mascota seleccionada debe pertenecer al cliente seleccionado.
 *  - El estado solo admite: pendiente, confirmada, cancelada.
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA2-EV02
 */
public class CitaServlet extends BaseProtectedServlet {

    private static final List<String> ESTADOS_VALIDOS =
            Arrays.asList("pendiente", "confirmada", "cancelada", "completada");

    private final CitaDAO    citaDAO    = new CitaDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final MascotaDAO mascotaDAO = new MascotaDAO();

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
                Cita citaEditar = citaDAO.buscarPorId(idEditar);
                if (citaEditar != null) {
                    request.setAttribute("citaEditar", citaEditar);
                } else {
                    request.setAttribute("mensaje", "❌ No se encontró la cita con id " + idEditar + ".");
                    request.setAttribute("tipoMensaje", "error");
                }
                cargarListaYForwardear(request, response);
                break;

            case "eliminar":
                int idEliminar = parseInt(request.getParameter("id"), -1);
                boolean eliminada = citaDAO.eliminar(idEliminar);
                if (eliminada) {
                    request.setAttribute("mensaje", "✅ Cita eliminada correctamente.");
                    request.setAttribute("tipoMensaje", "exito");
                } else {
                    request.setAttribute("mensaje", "❌ No se encontró la cita.");
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
        String fechaHora = limpiar(request.getParameter("fechaHora"));
        String servicio  = limpiar(request.getParameter("servicio"));
        String estado    = limpiar(request.getParameter("estado"));
        int idCliente    = parseInt(request.getParameter("idCliente"), -1);
        int idMascota    = parseInt(request.getParameter("idMascota"), -1);

        // Estado por defecto si no viene (crear desde cero)
        if (estado.isEmpty()) estado = "pendiente";

        if (fechaHora.isEmpty()) {
            request.setAttribute("mensaje", "❌ Debes indicar la fecha y hora de la cita.");
            request.setAttribute("tipoMensaje", "error");
            cargarListaYForwardear(request, response);
            return;
        }
        if (servicio.isEmpty()) {
            request.setAttribute("mensaje", "❌ Debes seleccionar un tipo de servicio.");
            request.setAttribute("tipoMensaje", "error");
            cargarListaYForwardear(request, response);
            return;
        }
        if (!ESTADOS_VALIDOS.contains(estado)) {
            request.setAttribute("mensaje", "❌ Estado inválido. Valores permitidos: " + ESTADOS_VALIDOS + ".");
            request.setAttribute("tipoMensaje", "error");
            cargarListaYForwardear(request, response);
            return;
        }

        // Validar cliente y mascota, y que la mascota pertenezca al cliente
        Cliente cliente = clienteDAO.buscarPorId(idCliente);
        Mascota mascota = mascotaDAO.buscarPorId(idMascota);
        if (cliente == null) {
            request.setAttribute("mensaje", "❌ Debes seleccionar un cliente válido.");
            request.setAttribute("tipoMensaje", "error");
            cargarListaYForwardear(request, response);
            return;
        }
        if (mascota == null) {
            request.setAttribute("mensaje", "❌ Debes seleccionar una mascota válida.");
            request.setAttribute("tipoMensaje", "error");
            cargarListaYForwardear(request, response);
            return;
        }
        if (mascota.getIdCliente() != cliente.getIdCliente()) {
            request.setAttribute("mensaje",
                    "❌ La mascota '" + mascota.getNombre() + "' no pertenece al cliente seleccionado.");
            request.setAttribute("tipoMensaje", "error");
            cargarListaYForwardear(request, response);
            return;
        }

        if ("crear".equals(accion)) {
            // ── CREAR nueva cita ─────────────────────────────────────────
            Cita nueva = new Cita();
            nueva.setIdCliente(cliente.getIdCliente());
            nueva.setNombreCliente(cliente.getNombre());
            nueva.setIdMascota(mascota.getIdMascota());
            nueva.setNombreMascota(mascota.getNombre());
            nueva.setFechaHora(fechaHora);
            nueva.setServicio(servicio);
            nueva.setEstado(estado);

            citaDAO.insertar(nueva);
            request.setAttribute("mensaje", "✅ Cita agendada correctamente para '" + mascota.getNombre() + "'.");
            request.setAttribute("tipoMensaje", "exito");

        } else if ("actualizar".equals(accion)) {
            // ── ACTUALIZAR cita existente ────────────────────────────────
            int id = parseInt(request.getParameter("id"), -1);
            Cita existente = citaDAO.buscarPorId(id);
            if (existente != null) {
                existente.setIdCliente(cliente.getIdCliente());
                existente.setNombreCliente(cliente.getNombre());
                existente.setIdMascota(mascota.getIdMascota());
                existente.setNombreMascota(mascota.getNombre());
                existente.setFechaHora(fechaHora);
                existente.setServicio(servicio);
                existente.setEstado(estado);
                citaDAO.actualizar(existente);
                request.setAttribute("mensaje", "✅ Cita actualizada correctamente.");
                request.setAttribute("tipoMensaje", "exito");
            } else {
                request.setAttribute("mensaje", "❌ Cita no encontrada.");
                request.setAttribute("tipoMensaje", "error");
            }
        }

        cargarListaYForwardear(request, response);
    }

    // ──────────────────────────────────────────────────────────────────────
    // Método auxiliar: carga citas + clientes + mascotas y hace forward
    // ──────────────────────────────────────────────────────────────────────
    private void cargarListaYForwardear(HttpServletRequest request,
                                        HttpServletResponse response)
            throws ServletException, IOException {
        List<Cita>    listaCitas    = citaDAO.listarTodos();
        List<Cliente> listaClientes = clienteDAO.listarTodos();
        List<Mascota> listaMascotas = mascotaDAO.listarTodos();
        request.setAttribute("listaCitas", listaCitas);
        request.setAttribute("listaClientes", listaClientes);
        request.setAttribute("listaMascotas", listaMascotas);
        forward(request, response, "/vistas/citas.jsp");
    }

}
