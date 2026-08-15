<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Fragmento: header.jsp — Barra superior reutilizable.
  ─────────────────────────────────────────────────────────
  Se incluye de forma estática en cada vista del panel autenticado.
  Requiere que la vista defina los atributos de request:
    - tituloPagina    (obligatorio)
    - subtituloPagina (opcional)

  Proyecto: PetServices - SENA GA7-220501096
--%>
<%
    String tituloPagina = (String) request.getAttribute("tituloPagina");
    if (tituloPagina == null) tituloPagina = "PetServices";
    String subtituloPagina = (String) request.getAttribute("subtituloPagina");

    // Nota: nombre distinto a sidebar.jsp porque ambos fragmentos se
    // incluyen de forma estática en la misma página (evita variables duplicadas).
    String usuarioHeader = (String) session.getAttribute("nombreUsuario");
    if (usuarioHeader == null) usuarioHeader = "Usuario";
%>
<header class="barra-superior">
    <button class="menu-toggle" onclick="alternarMenu()" aria-label="Abrir menú">☰</button>
    <div class="barra-titulo">
        <h1><%= tituloPagina %></h1>
        <% if (subtituloPagina != null && !subtituloPagina.isEmpty()) { %>
            <p><%= subtituloPagina %></p>
        <% } %>
    </div>
    <div class="chip-sesion">👤 <strong><%= usuarioHeader %></strong></div>
</header>
