<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.petservices.modelo.Pedido" %>
<%@ page import="com.petservices.modelo.Cliente" %>
<%--
  Vista: pedidos.jsp — Historial de pedidos (CRUD).
  Usa el layout compartido: sidebar, header, CSS y JS centralizados.

  Proyecto: PetServices - SENA GA7-220501096
--%>
<%
    request.setAttribute("moduloActivo", "pedidos");
    request.setAttribute("tituloPagina", "📦 Historial de Pedidos");
    request.setAttribute("subtituloPagina", "Consulta y administra los pedidos de los clientes");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Historial de Pedidos - PetServices</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/vistas/css/estilos.css">
</head>
<body>

<%@ include file="fragmentos/sidebar.jsp" %>

<main class="contenido">

    <%@ include file="fragmentos/header.jsp" %>

    <%
        String mensaje     = (String) request.getAttribute("mensaje");
        String tipoMensaje = (String) request.getAttribute("tipoMensaje");
        if (mensaje != null && !mensaje.isEmpty()) {
    %>
        <div class="alerta alerta-<%= tipoMensaje %>">
            <span><%= mensaje %></span>
        </div>
    <%  } %>

    <%--
      ── FORMULARIO CREAR / EDITAR ──
      Si el Servlet puso un pedidoEditar en el request, precargamos los campos
      y cambiamos la acción a "actualizar". Si no, es "crear".
    --%>
    <%
        Pedido pedidoEditar = (Pedido) request.getAttribute("pedidoEditar");
        boolean editando = (pedidoEditar != null);

        List<Cliente> listaClientes =
                (List<Cliente>) request.getAttribute("listaClientes");
    %>

    <div class="card">
        <div class="card-titulo">
            <div>
                <h2><%= editando ? "✏️ Editar Pedido" : "📦 Nuevo Pedido" %></h2>
                <p><%= editando ? "Modifica los datos del pedido seleccionado" : "Registra un nuevo pedido para un cliente" %></p>
            </div>
        </div>

        <form action="<%= request.getContextPath() %>/pedidos" method="post">
            <input type="hidden" name="accion"
                   value="<%= editando ? "actualizar" : "crear" %>">

            <% if (editando) { %>
                <input type="hidden" name="id" value="<%= pedidoEditar.getIdPedido() %>">
            <% } %>

            <div class="form-grid">
                <div class="form-campo">
                    <label for="idCliente">Cliente</label>
                    <select id="idCliente" name="idCliente" required>
                        <%
                            int idClienteSel = editando ? pedidoEditar.getIdCliente() : -1;
                            if (listaClientes == null || listaClientes.isEmpty()) {
                        %>
                            <option value="">No hay clientes registrados</option>
                        <%  } else {
                                for (Cliente c : listaClientes) {
                        %>
                            <option value="<%= c.getIdCliente() %>"
                                <%= c.getIdCliente() == idClienteSel ? "selected" : "" %>>
                                <%= c.getNombre() %>
                            </option>
                        <%      }
                            } %>
                    </select>
                </div>

                <div class="form-campo">
                    <label for="total">Total ($)</label>
                    <input type="number" id="total" name="total" min="0.01" step="0.01"
                           value="<%= editando ? pedidoEditar.getTotal() : "" %>"
                           placeholder="0.00" required>
                </div>

                <div class="form-campo">
                    <label for="estado">Estado</label>
                    <select id="estado" name="estado">
                        <%
                            String estadoSel = editando ? pedidoEditar.getEstado() : "pendiente";
                            if (estadoSel == null) estadoSel = "pendiente";
                            String[] estados = {"pendiente", "pagado", "entregado"};
                            for (String est : estados) {
                        %>
                            <option value="<%= est %>" <%= est.equals(estadoSel) ? "selected" : "" %>>
                                <%= est.substring(0, 1).toUpperCase() + est.substring(1) %>
                            </option>
                        <%  } %>
                    </select>
                </div>
            </div>

            <div class="form-acciones">
                <button type="submit" class="btn btn-primario">
                    <%= editando ? "💾 Actualizar" : "➕ Crear Pedido" %>
                </button>
                <% if (editando) { %>
                    <a href="<%= request.getContextPath() %>/pedidos">
                        <button type="button" class="btn btn-secundario">✖ Cancelar</button>
                    </a>
                <% } %>
            </div>
        </form>
    </div>

    <!-- ── Tabla de pedidos ── -->
    <div class="card">
        <div class="card-titulo">
            <div>
                <h2>📋 Historial de Pedidos</h2>
                <p>Todos los pedidos registrados en el sistema</p>
            </div>
            <div class="busqueda">
                <span>🔍</span>
                <input type="search" data-filtro="tabla-pedidos" placeholder="Buscar pedido...">
            </div>
        </div>

        <%
            List<Pedido> listaPedidos = (List<Pedido>) request.getAttribute("listaPedidos");
        %>

        <div class="tabla-envoltura">
            <table class="tabla" id="tabla-pedidos">
                <thead>
                    <tr>
                        <th>#ID</th>
                        <th>Cliente</th>
                        <th>Fecha</th>
                        <th>Total</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    if (listaPedidos == null || listaPedidos.isEmpty()) {
                %>
                    <tr>
                        <td colspan="6" class="vacio fila-vacia">No hay pedidos registrados aún.</td>
                    </tr>
                <%
                    } else {
                        for (Pedido p : listaPedidos) {
                            String estadoPedido = (p.getEstado() != null) ? p.getEstado() : "pendiente";
                            String textoEstado = estadoPedido.substring(0, 1).toUpperCase()
                                               + estadoPedido.substring(1);
                %>
                    <tr>
                        <td><span class="badge">#<%= p.getIdPedido() %></span></td>
                        <td><strong><%= p.getNombreCliente() != null ? p.getNombreCliente() : "—" %></strong></td>
                        <td><%= p.getFecha() != null ? p.getFecha() : "—" %></td>
                        <td><%= String.format("$%,.0f", p.getTotal()) %></td>
                        <td><span class="badge-estado estado-<%= estadoPedido %>"><%= textoEstado %></span></td>
                        <td>
                            <a class="btn-tabla btn-editar" href="<%= request.getContextPath() %>/pedidos?accion=editar&id=<%= p.getIdPedido() %>">✏️ Editar</a>
                            <button class="btn-tabla btn-eliminar"
                                onclick="eliminarRegistro('<%= request.getContextPath() %>/pedidos?accion=eliminar&id=<%= p.getIdPedido() %>')">
                                🗑️ Eliminar
                            </button>
                        </td>
                    </tr>
                <%
                        }
                    }
                %>
                </tbody>
            </table>
        </div>

        <p class="total-registros">
            Total registrados: <strong>
                <%= (listaPedidos != null) ? listaPedidos.size() : 0 %>
            </strong> pedidos
        </p>
    </div>

</main>

<script src="<%= request.getContextPath() %>/vistas/js/app.js"></script>
</body>
</html>
