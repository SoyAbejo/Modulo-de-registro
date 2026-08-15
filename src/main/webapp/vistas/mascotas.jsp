<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.petservices.modelo.Mascota" %>
<%@ page import="com.petservices.modelo.Cliente" %>
<%--
  Vista: mascotas.jsp — Gestión de mascotas (CRUD).
  Usa el layout compartido: sidebar, header, CSS y JS centralizados.

  Proyecto: PetServices - SENA GA7-220501096
--%>
<%
    request.setAttribute("moduloActivo", "mascotas");
    request.setAttribute("tituloPagina", "🐾 Gestión de Mascotas");
    request.setAttribute("subtituloPagina", "Registra y administra las mascotas de los clientes");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Mascotas - PetServices</title>
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
      Si el Servlet puso una mascotaEditar en el request, precargamos los
      campos y cambiamos la acción a "actualizar". Si no, es "crear".
    --%>
    <%
        Mascota mascotaEditar = (Mascota) request.getAttribute("mascotaEditar");
        boolean editando = (mascotaEditar != null);
    %>

    <div class="card">
        <div class="card-titulo">
            <div>
                <h2><%= editando ? "✏️ Editar Mascota" : "🐾 Nueva Mascota" %></h2>
                <p><%= editando ? "Modifica los datos de la mascota seleccionada" : "Registra una nueva mascota y asígnala a su dueño" %></p>
            </div>
        </div>

        <form action="<%= request.getContextPath() %>/mascotas" method="post">
            <input type="hidden" name="accion"
                   value="<%= editando ? "actualizar" : "crear" %>">

            <% if (editando) { %>
                <input type="hidden" name="id" value="<%= mascotaEditar.getIdMascota() %>">
            <% } %>

            <div class="form-grid">
                <div class="form-campo">
                    <label for="nombre">Nombre de la mascota</label>
                    <input type="text" id="nombre" name="nombre"
                           value="<%= editando ? mascotaEditar.getNombre() : "" %>"
                           placeholder="Ej: Max" required maxlength="50">
                </div>

                <div class="form-campo">
                    <label for="especie">Especie</label>
                    <select id="especie" name="especie" required>
                        <%
                            String especieSel = editando ? mascotaEditar.getEspecie() : "";
                            String[] especies = {"Perro", "Gato", "Ave", "Conejo", "Otro"};
                            for (String esp : especies) {
                        %>
                            <option value="<%= esp %>" <%= esp.equals(especieSel) ? "selected" : "" %>><%= esp %></option>
                        <%  } %>
                    </select>
                </div>

                <div class="form-campo">
                    <label for="raza">Raza</label>
                    <input type="text" id="raza" name="raza"
                           value="<%= editando ? mascotaEditar.getRaza() : "" %>"
                           placeholder="Ej: Labrador" required maxlength="50">
                </div>

                <div class="form-campo">
                    <label for="edad">Edad (años)</label>
                    <input type="number" id="edad" name="edad" min="0" max="50"
                           value="<%= editando ? mascotaEditar.getEdad() : "" %>"
                           placeholder="0">
                </div>

                <div class="form-campo">
                    <label for="idCliente">Dueño (Cliente)</label>
                    <select id="idCliente" name="idCliente" required>
                        <%
                            int idClienteSel = editando ? mascotaEditar.getIdCliente() : -1;
                            List<Cliente> listaClientes =
                                    (List<Cliente>) request.getAttribute("listaClientes");
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
            </div>

            <div class="form-acciones">
                <button type="submit" class="btn btn-primario">
                    <%= editando ? "💾 Actualizar" : "➕ Registrar" %>
                </button>
                <% if (editando) { %>
                    <a href="<%= request.getContextPath() %>/mascotas">
                        <button type="button" class="btn btn-secundario">✖ Cancelar</button>
                    </a>
                <% } %>
            </div>
        </form>
    </div>

    <!-- ── Tabla de mascotas ── -->
    <div class="card">
        <div class="card-titulo">
            <div>
                <h2>📋 Mascotas Registradas</h2>
                <p>Todas las mascotas del sistema</p>
            </div>
            <div class="busqueda">
                <span>🔍</span>
                <input type="search" data-filtro="tabla-mascotas" placeholder="Buscar mascota...">
            </div>
        </div>

        <%
            List<Mascota> listaMascotas =
                    (List<Mascota>) request.getAttribute("listaMascotas");
        %>

        <div class="tabla-envoltura">
            <table class="tabla" id="tabla-mascotas">
                <thead>
                    <tr>
                        <th>#ID</th>
                        <th>Nombre</th>
                        <th>Especie</th>
                        <th>Raza</th>
                        <th>Edad</th>
                        <th>Dueño</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    if (listaMascotas == null || listaMascotas.isEmpty()) {
                %>
                    <tr>
                        <td colspan="7" class="vacio fila-vacia">No hay mascotas registradas aún.</td>
                    </tr>
                <%
                    } else {
                        for (Mascota m : listaMascotas) {
                %>
                    <tr>
                        <td><span class="badge">#<%= m.getIdMascota() %></span></td>
                        <td><strong><%= m.getNombre() %></strong></td>
                        <td><%= m.getEspecie() != null ? m.getEspecie() : "—" %></td>
                        <td><%= m.getRaza() != null ? m.getRaza() : "—" %></td>
                        <td><%= m.getEdad() %> años</td>
                        <td><%= m.getNombreCliente() != null ? m.getNombreCliente() : "—" %></td>
                        <td>
                            <a class="btn-tabla btn-editar" href="<%= request.getContextPath() %>/mascotas?accion=editar&id=<%= m.getIdMascota() %>">✏️ Editar</a>
                            <button class="btn-tabla btn-eliminar"
                                onclick="eliminarRegistro('<%= request.getContextPath() %>/mascotas?accion=eliminar&id=<%= m.getIdMascota() %>',
                                        '<%= m.getNombre().replace("'", "\\'") %>')">
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
                <%= (listaMascotas != null) ? listaMascotas.size() : 0 %>
            </strong> mascotas
        </p>
    </div>

</main>

<script src="<%= request.getContextPath() %>/vistas/js/app.js"></script>
</body>
</html>
