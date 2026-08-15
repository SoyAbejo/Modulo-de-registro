<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.petservices.modelo.Producto" %>
<%--
  Vista: productos.jsp — Inventario de productos (CRUD).
  Usa el layout compartido: sidebar, header, CSS y JS centralizados.

  Proyecto: PetServices - SENA GA7-220501096
--%>
<%
    request.setAttribute("moduloActivo", "productos");
    request.setAttribute("tituloPagina", "🛒 Inventario de Productos");
    request.setAttribute("subtituloPagina", "Controla el catálogo, precios y stock de la tienda");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inventario de Productos - PetServices</title>
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
      Si el Servlet puso un productoEditar en el request, precargamos los
      campos y cambiamos la acción a "actualizar". Si no, es "crear".
    --%>
    <%
        Producto productoEditar = (Producto) request.getAttribute("productoEditar");
        boolean editando = (productoEditar != null);
    %>

    <div class="card">
        <div class="card-titulo">
            <div>
                <h2><%= editando ? "✏️ Editar Producto" : "🛒 Nuevo Producto" %></h2>
                <p><%= editando ? "Modifica los datos del producto seleccionado" : "Agrega un producto al inventario" %></p>
            </div>
        </div>

        <form action="<%= request.getContextPath() %>/productos" method="post">
            <input type="hidden" name="accion"
                   value="<%= editando ? "actualizar" : "crear" %>">

            <% if (editando) { %>
                <input type="hidden" name="id" value="<%= productoEditar.getIdProducto() %>">
            <% } %>

            <div class="form-grid">
                <div class="form-campo">
                    <label for="nombre">Nombre del producto</label>
                    <input type="text" id="nombre" name="nombre"
                           value="<%= editando ? productoEditar.getNombre() : "" %>"
                           placeholder="Ej: Croquetas Pedigree 10kg" required maxlength="60">
                </div>

                <div class="form-campo">
                    <label for="categoria">Categoría</label>
                    <select id="categoria" name="categoria" required>
                        <%
                            String catSel = editando ? productoEditar.getCategoria() : "";
                            String[] categorias = {"Alimento", "Medicamento", "Accesorio", "Juguete"};
                            for (String cat : categorias) {
                        %>
                            <option value="<%= cat %>" <%= cat.equals(catSel) ? "selected" : "" %>><%= cat %></option>
                        <%  } %>
                    </select>
                </div>

                <div class="form-campo">
                    <label for="precio">Precio ($)</label>
                    <input type="number" id="precio" name="precio" min="0" step="0.01"
                           value="<%= editando ? productoEditar.getPrecio() : "" %>"
                           placeholder="0.00" required>
                </div>

                <div class="form-campo">
                    <label for="stock">Stock (unidades)</label>
                    <input type="number" id="stock" name="stock" min="0" step="1"
                           value="<%= editando ? productoEditar.getStock() : "" %>"
                           placeholder="0" required>
                </div>
            </div>

            <div class="form-acciones">
                <button type="submit" class="btn btn-primario">
                    <%= editando ? "💾 Actualizar" : "➕ Agregar" %>
                </button>
                <% if (editando) { %>
                    <a href="<%= request.getContextPath() %>/productos">
                        <button type="button" class="btn btn-secundario">✖ Cancelar</button>
                    </a>
                <% } %>
            </div>
        </form>
    </div>

    <!-- ── Tabla de productos ── -->
    <div class="card">
        <div class="card-titulo">
            <div>
                <h2>📋 Inventario de Productos</h2>
                <p>Productos disponibles en la tienda</p>
            </div>
            <div class="busqueda">
                <span>🔍</span>
                <input type="search" data-filtro="tabla-productos" placeholder="Buscar producto...">
            </div>
        </div>

        <%
            List<Producto> listaProductos =
                    (List<Producto>) request.getAttribute("listaProductos");
        %>

        <div class="tabla-envoltura">
            <table class="tabla" id="tabla-productos">
                <thead>
                    <tr>
                        <th>#ID</th>
                        <th>Nombre</th>
                        <th>Categoría</th>
                        <th>Precio</th>
                        <th>Stock</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    if (listaProductos == null || listaProductos.isEmpty()) {
                %>
                    <tr>
                        <td colspan="6" class="vacio fila-vacia">No hay productos en el inventario aún.</td>
                    </tr>
                <%
                    } else {
                        for (Producto p : listaProductos) {
                %>
                    <tr>
                        <td><span class="badge">#<%= p.getIdProducto() %></span></td>
                        <td><strong><%= p.getNombre() %></strong></td>
                        <td><span class="cat-badge"><%= p.getCategoria() != null ? p.getCategoria() : "—" %></span></td>
                        <td><%= String.format("$%,.0f", p.getPrecio()) %></td>
                        <td>
                            <% if (p.getStock() <= 5) { %>
                                <span class="badge-estado estado-agotado"><%= p.getStock() %> ⚠️</span>
                            <% } else { %>
                                <%= p.getStock() %>
                            <% } %>
                        </td>
                        <td>
                            <a class="btn-tabla btn-editar" href="<%= request.getContextPath() %>/productos?accion=editar&id=<%= p.getIdProducto() %>">✏️ Editar</a>
                            <button class="btn-tabla btn-eliminar"
                                onclick="eliminarRegistro('<%= request.getContextPath() %>/productos?accion=eliminar&id=<%= p.getIdProducto() %>',
                                        '<%= p.getNombre().replace("'", "\\'") %>')">
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
                <%= (listaProductos != null) ? listaProductos.size() : 0 %>
            </strong> productos
        </p>
    </div>

</main>

<script src="<%= request.getContextPath() %>/vistas/js/app.js"></script>
</body>
</html>
