package com.petservices.servlet;

import com.petservices.dao.CitaDAO;
import com.petservices.dao.ClienteDAO;
import com.petservices.dao.MascotaDAO;
import com.petservices.dao.PedidoDAO;
import com.petservices.dao.ProductoDAO;
import com.petservices.dao.ServicioDAO;
import com.petservices.modelo.Cita;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet: DashboardServlet
 * URL de mapeo: /dashboard
 * ──────────────────────────────────────────────────────────────────────────
 * Pantalla principal posterior al login. Muestra indicadores (KPIs) del
 * sistema, las últimas citas agendadas y accesos rápidos a cada módulo.
 *
 *  doGet → cargar estadísticas y hacer forward a dashboard.jsp
 *
 * Proyecto: PetServices - SENA GA7-220501096
 */
public class DashboardServlet extends BaseProtectedServlet {

    private final ClienteDAO  clienteDAO  = new ClienteDAO();
    private final MascotaDAO  mascotaDAO  = new MascotaDAO();
    private final CitaDAO     citaDAO     = new CitaDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ServicioDAO servicioDAO = new ServicioDAO();
    private final PedidoDAO   pedidoDAO   = new PedidoDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Protección básica: sesión activa
        if (requireLogin(request, response)) {
            return;
        }

        // ── KPIs del sistema ────────────────────────────────────────────
        request.setAttribute("totalClientes",  clienteDAO.listarTodos().size());
        request.setAttribute("totalMascotas",  mascotaDAO.listarTodos().size());
        request.setAttribute("totalCitas",     citaDAO.listarTodos().size());
        request.setAttribute("totalProductos", productoDAO.listarTodos().size());
        request.setAttribute("totalServicios", servicioDAO.listarTodos().size());
        request.setAttribute("totalPedidos",   pedidoDAO.listarTodos().size());

        // ── Últimas 5 citas (para la vista rápida) ──────────────────────
        List<Cita> ultimasCitas = new ArrayList<>(citaDAO.listarTodos());
        if (ultimasCitas.size() > 5) {
            ultimasCitas = ultimasCitas.subList(ultimasCitas.size() - 5, ultimasCitas.size());
        }
        request.setAttribute("ultimasCitas", ultimasCitas);

        forward(request, response, "/vistas/dashboard.jsp");
    }
}
