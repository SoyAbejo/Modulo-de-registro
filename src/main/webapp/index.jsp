<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  index.jsp — Página raíz de PetServices
  ────────────────────────────────────────
  Redirige automáticamente al login si no hay sesión activa,
  o al panel de clientes si ya está autenticado.

  Proyecto: PetServices - SENA GA7-220501096-AA2-EV02
--%>
<%
    // SCRIPTLET: Verifica si ya hay sesión activa
    Object clienteSesion = session.getAttribute("clienteSesion");
    if (clienteSesion != null) {
        // Usuario ya autenticado → ir al panel
        response.sendRedirect(request.getContextPath() + "/clientes");
    } else {
        // Sin sesión → ir al login
        response.sendRedirect(request.getContextPath() + "/login");
    }
%>
