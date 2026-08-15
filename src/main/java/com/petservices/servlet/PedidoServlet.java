package com.petservices.servlet;

import com.petservices.dao.ClienteDAO;
import com.petservices.dao.PedidoDAO;
import com.petservices.modelo.Cliente;
import com.petservices.modelo.Pedido;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Servlet: PedidoServlet
 * URL de mapeo: /pedidos
 * ──────────────────────────────────────────────────────────────────────────
 * Controlador principal del módulo de HISTORIAL DE PEDIDOS (CRUD).
 * Usa el parámetro "accion" para determinar la operación a ejecutar.
 *
 *  doGet  → listar, editar (cargar formulario), eliminar
 *  doPost → crear, actualizar
 *
 * Carga la lista de clientes para el <select> del formulario y resuelve
 * el nombre del cliente de cada pedido.
 *
 * Reglas de negocio:
 *  - El total debe ser mayor que 0.
 *  - El estado solo admite: pendiente, pagado, entregado.
 *  - La fecha se asigna automáticamente al crear el pedido.
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA2-EV02
 */
public class PedidoServlet extends BaseProtectedServlet {

    private static final List<String> ESTADOS_VALIDOS =
            Arrays.asList("pendiente", "pagado", "entregado");

    private final PedidoDAO  pedidoDAO  = new PedidoDAO();
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
                int idEditar = parseInt(request.getParameter("id"), -1);
                Pedido pedidoEditar = pedidoDAO.buscarPorId(idEditar);
                if (pedidoEditar != null) {
                    request.setAttribute("pedidoEditar", pedidoEditar);
                } else {
                    request.setAttribute("mensaje", "❌ No se encontró el pedido con id " + idEditar + ".");
                    request.setAttribute("tipoMensaje", "error");
                }
                cargarListaYForwardear(request, response);
                break;

            case "eliminar":
                int idEliminar = parseInt(request.getParameter("id"), -1);
                boolean eliminado = pedidoDAO.eliminar(idEliminar);
                if (eliminado) {
                    request.setAttribute("mensaje", "✅ Pedido eliminado correctamente.");
                    request.setAttribute("tipoMensaje", "exito");
                } else {
                    request.setAttribute("mensaje", "❌ No se encontró el pedido.");
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
        double total    = parseDouble(request.getParameter("total"), -1);
        String estado   = limpiar(request.getParameter("estado"));
        int    idCliente = parseInt(request.getParameter("idCliente"), -1);

        // Estado por defecto si no viene (crear desde cero)
        if (estado.isEmpty()) estado = "pendiente";

        if (total <= 0) {
            request.setAttribute("mensaje", "❌ El total del pedido debe ser mayor que 0.");
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

        // Verificar que el cliente seleccionado exista
        Cliente cliente = clienteDAO.buscarPorId(idCliente);
        if (cliente == null) {
            request.setAttribute("mensaje", "❌ Debes seleccionar un cliente válido.");
            request.setAttribute("tipoMensaje", "error");
            cargarListaYForwardear(request, response);
            return;
        }

        if ("crear".equals(accion)) {
            // ── CREAR nuevo pedido ───────────────────────────────────────
            Pedido nuevo = new Pedido();
            nuevo.setTotal(total);
            nuevo.setEstado(estado);
            nuevo.setIdCliente(cliente.getIdCliente());
            nuevo.setNombreCliente(cliente.getNombre());
            // fecha la asigna el DAO automáticamente (fecha actual)

            pedidoDAO.insertar(nuevo);
            request.setAttribute("mensaje", "✅ Pedido creado correctamente para '" + cliente.getNombre() + "'.");
            request.setAttribute("tipoMensaje", "exito");

        } else if ("actualizar".equals(accion)) {
            // ── ACTUALIZAR pedido existente ──────────────────────────────
            int id = parseInt(request.getParameter("id"), -1);
            Pedido existente = pedidoDAO.buscarPorId(id);
            if (existente != null) {
                existente.setTotal(total);
                existente.setEstado(estado);
                existente.setIdCliente(cliente.getIdCliente());
                existente.setNombreCliente(cliente.getNombre());
                pedidoDAO.actualizar(existente);
                request.setAttribute("mensaje", "✅ Pedido actualizado correctamente.");
                request.setAttribute("tipoMensaje", "exito");
            } else {
                request.setAttribute("mensaje", "❌ Pedido no encontrado.");
                request.setAttribute("tipoMensaje", "error");
            }
        }

        cargarListaYForwardear(request, response);
    }

    // ──────────────────────────────────────────────────────────────────────
    // Método auxiliar: carga pedidos + clientes y hace forward
    // ──────────────────────────────────────────────────────────────────────
    private void cargarListaYForwardear(HttpServletRequest request,
                                        HttpServletResponse response)
            throws ServletException, IOException {
        List<Pedido>  listaPedidos  = pedidoDAO.listarTodos();
        List<Cliente> listaClientes = clienteDAO.listarTodos();
        request.setAttribute("listaPedidos", listaPedidos);
        request.setAttribute("listaClientes", listaClientes);
        forward(request, response, "/vistas/pedidos.jsp");
    }

}
