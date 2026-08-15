<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.petservices.modelo.Cliente" %>
<%--
  Vista: clientes.jsp — Gestión de clientes (CRUD).
  Usa el layout compartido: sidebar, header, CSS y JS centralizados.

  Proyecto: PetServices - SENA GA7-220501096
--%>
<%
    request.setAttribute("moduloActivo", "clientes");
    request.setAttribute("tituloPagina", "👥 Gestión de Clientes");
    request.setAttribute("subtituloPagina", "Registra, edita y administra los clientes del sistema");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Clientes - PetServices</title>
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
      Si el Servlet puso un clienteEditar en el request, precargamos los
      campos y cambiamos la acción a "actualizar". Si no, es "crear".
    --%>
    <%
        Cliente clienteEditar = (Cliente) request.getAttribute("clienteEditar");
        boolean editando = (clienteEditar != null);
    %>

    <div class="card">
        <div class="card-titulo">
            <div>
                <h2><%= editando ? "✏️ Editar Cliente" : "➕ Nuevo Cliente" %></h2>
                <p><%= editando ? "Modifica los datos del cliente seleccionado" : "Registra un nuevo cliente en el sistema" %></p>
            </div>
        </div>

        <form action="<%= request.getContextPath() %>/clientes" method="post">
            <input type="hidden" name="accion"
                   value="<%= editando ? "actualizar" : "crear" %>">

            <% if (editando) { %>
                <input type="hidden" name="id" value="<%= clienteEditar.getIdCliente() %>">
            <% } %>

            <div class="form-grid">
                <div class="form-campo">
                    <label for="nombre">Nombre completo</label>
                    <input type="text" id="nombre" name="nombre"
                           value="<%= editando ? clienteEditar.getNombre() : "" %>"
                           placeholder="Ej: Alejandro Puerto" required maxlength="50">
                </div>

                <div class="form-campo">
                    <label for="correo">Correo electrónico</label>
                    <input type="email" id="correo" name="correo"
                           value="<%= editando ? clienteEditar.getCorreo() : "" %>"
                           placeholder="correo@email.com" required maxlength="50">
                </div>

                <div class="form-campo">
                    <label for="contrasena">
                        Contraseña <%= editando ? "(dejar vacío = no cambiar)" : "" %>
                    </label>
                    <input type="password" id="contrasena" name="contrasena"
                           placeholder="<%= editando ? "Nueva contraseña (opcional)" : "Mínimo 6 caracteres" %>"
                           <%= editando ? "" : "required minlength='6'" %>>
                </div>
            </div>

            <div class="form-acciones">
                <button type="submit" class="btn btn-primario">
                    <%= editando ? "💾 Actualizar" : "➕ Registrar" %>
                </button>
                <% if (editando) { %>
                    <a href="<%= request.getContextPath() %>/clientes">
                        <button type="button" class="btn btn-secundario">✖ Cancelar</button>
                    </a>
                <% } %>
            </div>
        </form>
    </div>

    <!-- ── Tabla de clientes ── -->
    <div class="card">
        <div class="card-titulo">
            <div>
                <h2>📋 Clientes Registrados</h2>
                <p>Todos los clientes del sistema</p>
            </div>
            <div class="busqueda">
                <span>🔍</span>
                <input type="search" data-filtro="tabla-clientes" placeholder="Buscar cliente...">
            </div>
        </div>

        <%
            List<Cliente> listaClientes =
                    (List<Cliente>) request.getAttribute("listaClientes");
        %>

        <div class="tabla-envoltura">
            <table class="tabla" id="tabla-clientes">
                <thead>
                    <tr>
                        <th>#ID</th>
                        <th>Nombre</th>
                        <th>Correo</th>
                        <th>Fecha Registro</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    if (listaClientes == null || listaClientes.isEmpty()) {
                %>
                    <tr>
                        <td colspan="5" class="vacio fila-vacia">No hay clientes registrados aún.</td>
                    </tr>
                <%
                    } else {
                        for (Cliente c : listaClientes) {
                %>
                    <tr>
                        <td><span class="badge">#<%= c.getIdCliente() %></span></td>
                        <td><strong><%= c.getNombre() %></strong></td>
                        <td><%= c.getCorreo() %></td>
                        <td><%= c.getFechaRegistro() != null ? c.getFechaRegistro() : "—" %></td>
                        <td>
                            <a class="btn-tabla btn-editar" href="<%= request.getContextPath() %>/clientes?accion=editar&id=<%= c.getIdCliente() %>">✏️ Editar</a>
                            <button class="btn-tabla btn-eliminar"
                                onclick="eliminarRegistro('<%= request.getContextPath() %>/clientes?accion=eliminar&id=<%= c.getIdCliente() %>',
                                        '<%= c.getNombre().replace("'", "\\'") %>')">
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
                <%= (listaClientes != null) ? listaClientes.size() : 0 %>
            </strong> clientes
        </p>
    </div>

</main>

<script src="<%= request.getContextPath() %>/vistas/js/app.js"></script>
</body>
</html>
