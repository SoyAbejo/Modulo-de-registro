<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Fragmento: sidebar.jsp — Menú lateral reutilizable.
  ─────────────────────────────────────────────────────────
  Se incluye de forma estática en cada vista del panel autenticado.
  Requiere que la vista defina el atributo de request "moduloActivo"
  (valores: dashboard, clientes, mascotas, citas, servicios, productos, pedidos).

  Proyecto: PetServices - SENA GA7-220501096
--%>
<%
    String moduloActivo = (String) request.getAttribute("moduloActivo");
    if (moduloActivo == null) moduloActivo = "dashboard";

    String nombreSesion = (String) session.getAttribute("nombreUsuario");
    if (nombreSesion == null) nombreSesion = "Usuario";
    String ctx = request.getContextPath();
%>
<aside class="sidebar" id="sidebar">

    <div class="sidebar-marca">
        <a href="<%= ctx %>/dashboard" class="marca">🐾 <strong>PetServices</strong></a>
    </div>

    <nav class="sidebar-nav">
        <span class="nav-etiqueta">General</span>
        <a href="<%= ctx %>/dashboard" class="nav-enlace <%= "dashboard".equals(moduloActivo) ? "activo" : "" %>">
            📊 Dashboard
        </a>

        <span class="nav-etiqueta">Gestión</span>
        <a href="<%= ctx %>/clientes" class="nav-enlace <%= "clientes".equals(moduloActivo) ? "activo" : "" %>">
            👥 Clientes
        </a>
        <a href="<%= ctx %>/mascotas" class="nav-enlace <%= "mascotas".equals(moduloActivo) ? "activo" : "" %>">
            🐾 Mascotas
        </a>
        <a href="<%= ctx %>/citas" class="nav-enlace <%= "citas".equals(moduloActivo) ? "activo" : "" %>">
            📅 Citas
        </a>
        <a href="<%= ctx %>/servicios" class="nav-enlace <%= "servicios".equals(moduloActivo) ? "activo" : "" %>">
            🩺 Servicios
        </a>
        <a href="<%= ctx %>/productos" class="nav-enlace <%= "productos".equals(moduloActivo) ? "activo" : "" %>">
            🛒 Productos
        </a>
        <a href="<%= ctx %>/pedidos" class="nav-enlace <%= "pedidos".equals(moduloActivo) ? "activo" : "" %>">
            📦 Pedidos
        </a>
    </nav>

    <div class="sidebar-usuario">
        <div class="avatar">👤</div>
        <div class="datos">
            <strong><%= nombreSesion %></strong>
            <span>Cuenta activa</span>
        </div>
        <a href="<%= ctx %>/logout" class="btn-cerrar" title="Cerrar sesión">⏻</a>
    </div>

</aside>
