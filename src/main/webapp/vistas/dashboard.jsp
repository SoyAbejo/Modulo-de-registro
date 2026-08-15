<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.petservices.modelo.Cita" %>
<%--
  Vista: dashboard.jsp — Pantalla principal posterior al login.
  Muestra KPIs del sistema, últimas citas y accesos rápidos.

  Proyecto: PetServices - SENA GA7-220501096
--%>
<%
    request.setAttribute("moduloActivo", "dashboard");
    request.setAttribute("tituloPagina", "📊 Dashboard");
    request.setAttribute("subtituloPagina", "Resumen general del sistema PetServices");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - PetServices</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/vistas/css/estilos.css">
</head>
<body>

<%@ include file="fragmentos/sidebar.jsp" %>

<main class="contenido">

    <%@ include file="fragmentos/header.jsp" %>

    <%-- ── KPIs del sistema ── --%>
    <%
        int totalClientes  = (Integer) request.getAttribute("totalClientes");
        int totalMascotas  = (Integer) request.getAttribute("totalMascotas");
        int totalCitas     = (Integer) request.getAttribute("totalCitas");
        int totalProductos = (Integer) request.getAttribute("totalProductos");
        int totalServicios = (Integer) request.getAttribute("totalServicios");
        int totalPedidos   = (Integer) request.getAttribute("totalPedidos");
    %>

    <section class="kpis">
        <a href="<%= request.getContextPath() %>/clientes" class="kpi">
            <div class="kpi-icono morado">👥</div>
            <div>
                <div class="kpi-numero"><%= totalClientes %></div>
                <div class="kpi-etiqueta">Clientes registrados</div>
            </div>
        </a>
        <a href="<%= request.getContextPath() %>/mascotas" class="kpi">
            <div class="kpi-icono verde">🐾</div>
            <div>
                <div class="kpi-numero"><%= totalMascotas %></div>
                <div class="kpi-etiqueta">Mascotas</div>
            </div>
        </a>
        <a href="<%= request.getContextPath() %>/citas" class="kpi">
            <div class="kpi-icono azul">📅</div>
            <div>
                <div class="kpi-numero"><%= totalCitas %></div>
                <div class="kpi-etiqueta">Citas agendadas</div>
            </div>
        </a>
        <a href="<%= request.getContextPath() %>/servicios" class="kpi">
            <div class="kpi-icono ambar">🩺</div>
            <div>
                <div class="kpi-numero"><%= totalServicios %></div>
                <div class="kpi-etiqueta">Servicios</div>
            </div>
        </a>
        <a href="<%= request.getContextPath() %>/productos" class="kpi">
            <div class="kpi-icono rosa">🛒</div>
            <div>
                <div class="kpi-numero"><%= totalProductos %></div>
                <div class="kpi-etiqueta">Productos</div>
            </div>
        </a>
        <a href="<%= request.getContextPath() %>/pedidos" class="kpi">
            <div class="kpi-icono morado">📦</div>
            <div>
                <div class="kpi-numero"><%= totalPedidos %></div>
                <div class="kpi-etiqueta">Pedidos</div>
            </div>
        </a>
    </section>

    <div class="panel-grid">

        <%-- ── Últimas citas ── --%>
        <div class="card">
            <div class="card-titulo">
                <div>
                    <h2>📅 Últimas citas agendadas</h2>
                    <p>Actividad reciente del consultorio</p>
                </div>
                <a class="panel-enlace" href="<%= request.getContextPath() %>/citas">Ver todas →</a>
            </div>

            <%
                List<Cita> ultimasCitas = (List<Cita>) request.getAttribute("ultimasCitas");
            %>
            <div class="tabla-envoltura">
                <table class="tabla">
                    <thead>
                        <tr>
                            <th>#ID</th>
                            <th>Mascota</th>
                            <th>Servicio</th>
                            <th>Fecha y hora</th>
                            <th>Estado</th>
                        </tr>
                    </thead>
                    <tbody>
                    <%
                        if (ultimasCitas == null || ultimasCitas.isEmpty()) {
                    %>
                        <tr>
                            <td colspan="5" class="vacio fila-vacia">No hay citas agendadas aún.</td>
                        </tr>
                    <%
                        } else {
                            for (Cita c : ultimasCitas) {
                                String estadoCita = (c.getEstado() != null) ? c.getEstado() : "pendiente";
                                String textoEstado = estadoCita.substring(0, 1).toUpperCase()
                                                   + estadoCita.substring(1);
                                String horaCita = (c.getHora() != null && c.getHora().length() >= 5)
                                        ? c.getHora().substring(0, 5) : "—";
                    %>
                        <tr>
                            <td><span class="badge">#<%= c.getIdCita() %></span></td>
                            <td><strong><%= c.getNombreMascota() != null ? c.getNombreMascota() : "—" %></strong></td>
                            <td><%= c.getServicio() != null ? c.getServicio() : "—" %></td>
                            <td><%= c.getFecha() != null ? c.getFecha() : "—" %> · <%= horaCita %></td>
                            <td><span class="badge-estado estado-<%= estadoCita %>"><%= textoEstado %></span></td>
                        </tr>
                    <%
                            }
                        }
                    %>
                    </tbody>
                </table>
            </div>
        </div>

        <%-- ── Accesos rápidos ── --%>
        <div class="card">
            <div class="card-titulo">
                <div>
                    <h2>⚡ Accesos rápidos</h2>
                    <p>Módulos del sistema</p>
                </div>
            </div>
            <ul class="lista-accesos">
                <li><a href="<%= request.getContextPath() %>/clientes"><span class="icono">👥</span> Gestionar clientes</a></li>
                <li><a href="<%= request.getContextPath() %>/mascotas"><span class="icono">🐾</span> Registrar mascota</a></li>
                <li><a href="<%= request.getContextPath() %>/citas"><span class="icono">📅</span> Agendar nueva cita</a></li>
                <li><a href="<%= request.getContextPath() %>/servicios"><span class="icono">🩺</span> Catálogo de servicios</a></li>
                <li><a href="<%= request.getContextPath() %>/productos"><span class="icono">🛒</span> Inventario de productos</a></li>
                <li><a href="<%= request.getContextPath() %>/pedidos"><span class="icono">📦</span> Historial de pedidos</a></li>
            </ul>
        </div>

    </div>

</main>

<script src="<%= request.getContextPath() %>/vistas/js/app.js"></script>
</body>
</html>
