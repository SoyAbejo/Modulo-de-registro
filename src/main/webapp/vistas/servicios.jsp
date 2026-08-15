<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.petservices.modelo.Servicio" %>
<%--
  Vista: servicios.jsp — Catálogo de servicios (CRUD).
  Usa el layout compartido: sidebar, header y estilos centralizados.

  Proyecto: PetServices - SENA GA7-220501096
--%>
<%
    request.setAttribute("moduloActivo", "servicios");
    request.setAttribute("tituloPagina", "🩺 Catálogo de Servicios");
    request.setAttribute("subtituloPagina", "Servicios ofrecidos por el establecimiento");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Servicios - PetServices</title>
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
      Si el Servlet puso un servicioEditar en el request, precargamos los
      campos y cambiamos la acción a "actualizar". Si no, es "crear".
    --%>
    <%
        Servicio servicioEditar = (Servicio) request.getAttribute("servicioEditar");
        boolean editando = (servicioEditar != null);
    %>

    <div class="card">
        <div class="card-titulo">
            <div>
                <h2><%= editando ? "✏️ Editar Servicio" : "🩺 Nuevo Servicio" %></h2>
                <p><%= editando ? "Modifica los datos del servicio seleccionado" : "Registra un nuevo servicio en el catálogo" %></p>
            </div>
        </div>

        <form action="<%= request.getContextPath() %>/servicios" method="post">
            <input type="hidden" name="accion"
                   value="<%= editando ? "actualizar" : "crear" %>">

            <% if (editando) { %>
                <input type="hidden" name="id" value="<%= servicioEditar.getIdServicio() %>">
            <% } %>

            <div class="form-grid">
                <div class="form-campo">
                    <label for="tipo">Tipo de servicio</label>
                    <select id="tipo" name="tipo" required>
                        <%
                            String tipoSel = editando ? servicioEditar.getTipo() : "";
                            String[] tipos = {"Consulta", "Vacunación", "Baño y corte", "Cirugía"};
                            for (String t : tipos) {
                        %>
                            <option value="<%= t %>" <%= t.equals(tipoSel) ? "selected" : "" %>><%= t %></option>
                        <%  } %>
                    </select>
                </div>

                <div class="form-campo">
                    <label for="nombre">Nombre del servicio</label>
                    <input type="text" id="nombre" name="nombre"
                           value="<%= editando ? servicioEditar.getNombre() : "" %>"
                           placeholder="Ej: Vacunación antirrábica" required maxlength="80">
                </div>
            </div>

            <div class="form-acciones">
                <button type="submit" class="btn btn-primario">
                    <%= editando ? "💾 Actualizar" : "➕ Agregar Servicio" %>
                </button>
                <% if (editando) { %>
                    <a href="<%= request.getContextPath() %>/servicios">
                        <button type="button" class="btn btn-secundario">✖ Cancelar</button>
                    </a>
                <% } %>
            </div>
        </form>
    </div>

    <!-- ── Tabla de servicios ── -->
    <div class="card">
        <div class="card-titulo">
            <div>
                <h2>📋 Servicios Registrados</h2>
                <p>Catálogo disponible para las citas</p>
            </div>
            <div class="busqueda">
                <span>🔍</span>
                <input type="search" data-filtro="tabla-servicios" placeholder="Buscar servicio...">
            </div>
        </div>

        <%
            List<Servicio> listaServicios =
                    (List<Servicio>) request.getAttribute("listaServicios");
        %>

        <div class="tabla-envoltura">
            <table class="tabla" id="tabla-servicios">
                <thead>
                    <tr>
                        <th>#ID</th>
                        <th>Tipo</th>
                        <th>Nombre</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    if (listaServicios == null || listaServicios.isEmpty()) {
                %>
                    <tr>
                        <td colspan="4" class="vacio fila-vacia">No hay servicios registrados aún.</td>
                    </tr>
                <%
                    } else {
                        for (Servicio s : listaServicios) {
                %>
                    <tr>
                        <td><span class="badge">#<%= s.getIdServicio() %></span></td>
                        <td><span class="cat-badge"><%= s.getTipo() != null ? s.getTipo() : "—" %></span></td>
                        <td><strong><%= s.getNombre() %></strong></td>
                        <td>
                            <a class="btn-tabla btn-editar" href="<%= request.getContextPath() %>/servicios?accion=editar&id=<%= s.getIdServicio() %>">✏️ Editar</a>
                            <button class="btn-tabla btn-eliminar"
                                onclick="eliminarRegistro('<%= request.getContextPath() %>/servicios?accion=eliminar&id=<%= s.getIdServicio() %>',
                                        '<%= s.getNombre().replace("'", "\\'") %>')">
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
                <%= (listaServicios != null) ? listaServicios.size() : 0 %>
            </strong> servicios
        </p>
    </div>

</main>

<script src="<%= request.getContextPath() %>/vistas/js/app.js"></script>
</body>
</html>
