<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.petservices.modelo.Cliente" %>
<%--
  Vista: clientes.jsp
  Panel de GESTIÓN DE CLIENTES — Módulo CRUD completo.
  
  Esta página muestra:
   1. Formulario para CREAR o ACTUALIZAR clientes
   2. Tabla con TODOS los clientes registrados (con opciones Editar/Eliminar)
   3. Datos de sesión del usuario autenticado

  Elementos JSP utilizados:
   - Directivas import (<%@ page import %>)
   - Scriptlets (<% %>) para lógica de iteración y condicionales
   - Expresiones (<%= %>) para mostrar valores
   - Atributos de sesión (session.getAttribute)
   - Atributos de petición (request.getAttribute)

  Proyecto: PetServices - SENA GA7-220501096-AA2-EV02
--%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Clientes - PetServices</title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body { font-family: Arial, sans-serif; background: #f5f3ff; display: flex; }

        /* ── Sidebar ── */
        .sidebar {
            width: 240px; min-height: 100vh;
            background: #6c4cff; color: white; padding: 20px;
            position: fixed; top: 0; left: 0;
        }
        .sidebar h2 { margin-bottom: 30px; font-size: 1.2rem; }
        .sidebar a {
            display: block; color: white; text-decoration: none;
            margin: 10px 0; padding: 10px 12px; border-radius: 8px; font-size: 0.95rem;
        }
        .sidebar a:hover, .sidebar a.activo { background: rgba(255,255,255,0.2); }
        .sidebar .logout {
            position: absolute; bottom: 20px; left: 20px; right: 20px;
            background: rgba(255,0,0,0.3); text-align: center;
        }

        /* ── Contenido principal ── */
        .content { margin-left: 240px; flex: 1; padding: 25px; }

        /* ── Encabezado ── */
        .header {
            background: white; padding: 18px 22px; border-radius: 12px;
            margin-bottom: 22px; display: flex;
            justify-content: space-between; align-items: center;
            box-shadow: 0 2px 8px rgba(0,0,0,0.06);
        }
        .header h1 { color: #6c4cff; font-size: 1.4rem; }
        .header span { color: #666; font-size: 0.9rem; }

        /* ── Mensajes ── */
        .alerta { padding: 10px 14px; border-radius: 8px; margin-bottom: 18px; font-size: 0.9rem; }
        .alerta-error { background: #fde8e8; color: #c0392b; border: 1px solid #f5c6cb; }
        .alerta-exito { background: #eafaf1; color: #1e8449; border: 1px solid #a9dfbf; }

        /* ── Tarjeta de formulario ── */
        .card {
            background: white; border-radius: 12px; padding: 22px 26px;
            margin-bottom: 25px; box-shadow: 0 2px 8px rgba(0,0,0,0.06);
        }
        .card h2 { color: #6c4cff; margin-bottom: 16px; font-size: 1.1rem; }

        .form-row { display: flex; gap: 15px; flex-wrap: wrap; }
        .form-group { flex: 1; min-width: 180px; }
        .form-group label { display: block; font-size: 0.82rem; color: #555; margin-bottom: 4px; font-weight: bold; }
        .form-group input {
            width: 100%; padding: 9px 12px;
            border: 1.5px solid #ddd; border-radius: 7px; font-size: 0.9rem;
        }
        .form-group input:focus { outline: none; border-color: #6c4cff; }

        .btn { padding: 9px 18px; border: none; border-radius: 7px; cursor: pointer; font-size: 0.9rem; }
        .btn-primary { background: #6c4cff; color: white; }
        .btn-primary:hover { background: #5936e3; }
        .btn-secondary { background: #eee; color: #333; margin-left: 8px; }
        .btn-secondary:hover { background: #ddd; }

        /* ── Tabla de clientes ── */
        table {
            width: 100%; border-collapse: collapse;
            background: white; border-radius: 12px; overflow: hidden;
            box-shadow: 0 2px 8px rgba(0,0,0,0.06);
        }
        thead th {
            background: #6c4cff; color: white;
            padding: 12px 14px; text-align: left; font-size: 0.9rem;
        }
        tbody tr:nth-child(even) { background: #fafafa; }
        tbody tr:hover { background: #f0edff; }
        tbody td { padding: 11px 14px; border-bottom: 1px solid #eee; font-size: 0.9rem; }

        .btn-edit   { background: #3498db; color: white; padding: 5px 11px; border: none; border-radius: 5px; cursor: pointer; font-size: 0.82rem; }
        .btn-delete { background: #e74c3c; color: white; padding: 5px 11px; border: none; border-radius: 5px; cursor: pointer; font-size: 0.82rem; margin-left: 5px; }
        .btn-edit:hover   { background: #2980b9; }
        .btn-delete:hover { background: #c0392b; }

        .badge {
            background: #f0edff; color: #6c4cff;
            padding: 3px 9px; border-radius: 12px; font-size: 0.8rem; font-weight: bold;
        }
    </style>
</head>
<body>

<%--
  SCRIPTLET JSP: Recupera el objeto Cliente de la sesión para mostrar
  el nombre del usuario autenticado en la barra lateral y encabezado.
--%>
<%
    Cliente usuarioSesion = (Cliente) session.getAttribute("clienteSesion");
    String  nombreSesion  = (String)  session.getAttribute("nombreUsuario");
    if (nombreSesion == null) nombreSesion = "Usuario";
%>

<!-- ── Sidebar de navegación ── -->
<div class="sidebar">
    <h2>🐾 PetServices</h2>
    <a href="<%= request.getContextPath() %>/clientes" class="activo">👥 Clientes</a>
    <a href="#">🐾 Mascotas</a>
    <a href="#">📅 Citas</a>
    <a href="#">🛒 Productos</a>
    <a href="#">📦 Pedidos</a>
    <a href="<%= request.getContextPath() %>/logout" class="logout">🚪 Cerrar sesión</a>
</div>

<!-- ── Contenido principal ── -->
<div class="content">

    <!-- Encabezado con datos de sesión -->
    <div class="header">
        <h1>👥 Gestión de Clientes</h1>
        <%-- EXPRESIÓN JSP: muestra el nombre del usuario de la sesión --%>
        <span>👤 Sesión activa: <strong><%= nombreSesion %></strong></span>
    </div>

    <%--
      SCRIPTLET JSP: Muestra el mensaje de éxito o error si el Servlet lo envió.
      El Servlet coloca el mensaje como atributo del request antes del forward.
    --%>
    <%
        String mensaje     = (String) request.getAttribute("mensaje");
        String tipoMensaje = (String) request.getAttribute("tipoMensaje");
        if (mensaje != null && !mensaje.isEmpty()) {
    %>
        <div class="alerta alerta-<%= tipoMensaje %>">
            <%= mensaje %>
        </div>
    <%  } %>

    <%--
      ── FORMULARIO CREAR / EDITAR ──
      Si el Servlet puso un clienteEditar en el request, precargamos los campos
      y cambiamos la acción a "actualizar". Si no, el formulario es para "crear".
    --%>
    <%
        Cliente clienteEditar = (Cliente) request.getAttribute("clienteEditar");
        boolean editando = (clienteEditar != null);
    %>

    <div class="card">
        <h2>
            <%-- EXPRESIÓN JSP: Título dinámico según si se está editando o creando --%>
            <%= editando ? "✏️ Editar Cliente" : "➕ Nuevo Cliente" %>
        </h2>

        <form action="<%= request.getContextPath() %>/clientes" method="post">

            <%-- Campo oculto que define la acción (crear o actualizar) --%>
            <input type="hidden" name="accion"
                   value="<%= editando ? "actualizar" : "crear" %>">

            <%-- Si editamos, enviamos el ID del cliente --%>
            <% if (editando) { %>
                <input type="hidden" name="id"
                       value="<%= clienteEditar.getIdCliente() %>">
            <% } %>

            <div class="form-row">
                <div class="form-group">
                    <label for="nombre">Nombre completo</label>
                    <%-- EXPRESIÓN JSP: Precarga el valor si se está editando --%>
                    <input type="text" id="nombre" name="nombre"
                           value="<%= editando ? clienteEditar.getNombre() : "" %>"
                           placeholder="Ej: Alejandro Puerto" required maxlength="50">
                </div>

                <div class="form-group">
                    <label for="correo">Correo electrónico</label>
                    <input type="email" id="correo" name="correo"
                           value="<%= editando ? clienteEditar.getCorreo() : "" %>"
                           placeholder="correo@email.com" required maxlength="50">
                </div>

                <div class="form-group">
                    <label for="contrasena">
                        Contraseña <%= editando ? "(dejar vacío = no cambiar)" : "" %>
                    </label>
                    <input type="password" id="contrasena" name="contrasena"
                           placeholder="<%= editando ? "Nueva contraseña (opcional)" : "Mínimo 6 caracteres" %>"
                           <%= editando ? "" : "required minlength='6'" %>>
                </div>
            </div>

            <div style="margin-top: 14px;">
                <button type="submit" class="btn btn-primary">
                    <%= editando ? "💾 Actualizar" : "➕ Registrar" %>
                </button>

                <%-- Botón Cancelar solo aparece en modo edición --%>
                <% if (editando) { %>
                    <a href="<%= request.getContextPath() %>/clientes">
                        <button type="button" class="btn btn-secondary">✖ Cancelar</button>
                    </a>
                <% } %>
            </div>
        </form>
    </div>

    <!-- ── Tabla de clientes ── -->
    <div class="card">
        <h2>📋 Clientes Registrados</h2>

        <%--
          SCRIPTLET JSP: Recupera la lista de clientes puesta por el Servlet
          y la itera para construir las filas de la tabla dinámicamente.
        --%>
        <%
            List<Cliente> listaClientes =
                    (List<Cliente>) request.getAttribute("listaClientes");
        %>

        <table>
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
            <%--
              SCRIPTLET + EXPRESIÓN JSP: Itera la lista e imprime una <tr> por cliente.
              Equivale a un bucle for-each con datos dinámicos en cada celda.
            --%>
            <%
                if (listaClientes == null || listaClientes.isEmpty()) {
            %>
                <tr>
                    <td colspan="5" style="text-align:center; color:#999; padding: 20px;">
                        No hay clientes registrados aún.
                    </td>
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
                        <%-- Botón Editar: llama al Servlet con accion=editar e id --%>
                        <a href="<%= request.getContextPath() %>
/clientes?accion=editar&id=<%= c.getIdCliente() %>">
                            <button class="btn-edit">✏️ Editar</button>
                        </a>

                        <%-- Botón Eliminar: confirmación antes de borrar --%>
                        <button class="btn-delete"
                            onclick="confirmarEliminar(<%= c.getIdCliente() %>,
                                    '<%= c.getNombre().replace("'", "\\'") %>')">
                            🗑️ Eliminar
                        </button>
                    </td>
                </tr>
            <%
                    } // fin for
                } // fin else
            %>
            </tbody>
        </table>

        <%-- EXPRESIÓN JSP: Muestra cuántos clientes hay en total --%>
        <p style="margin-top:10px; font-size:0.85rem; color:#888;">
            Total registrados: <strong>
                <%= (listaClientes != null) ? listaClientes.size() : 0 %>
            </strong> clientes
        </p>
    </div>

</div>

<!-- Script de confirmación de eliminación -->
<script>
    /**
     * Muestra un diálogo de confirmación y redirige al Servlet con accion=eliminar.
     * El Servlet procesa la eliminación y retorna a la lista actualizada.
     */
    function confirmarEliminar(id, nombre) {
        if (confirm('⚠️ ¿Seguro que deseas eliminar al cliente "' + nombre + '"?\nEsta acción no se puede deshacer.')) {
            window.location.href = '<%= request.getContextPath() %>/clientes?accion=eliminar&id=' + id;
        }
    }
</script>

</body>
</html>
