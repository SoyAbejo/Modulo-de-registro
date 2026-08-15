<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Vista: login.jsp — Formulario de inicio de sesión.
  Rediseñado con el design system central (estilos.css).

  Proyecto: PetServices - SENA GA7-220501096
--%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión - PetServices</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/vistas/css/estilos.css">
</head>
<body class="auth-body">

<div class="auth-card">

    <div class="auth-lado">
        <h1>🐾 PetServices</h1>
        <p>Plataforma de gestión para servicios de mascotas y veterinaria.</p>
        <ul>
            <li>📅 Agenda de citas en línea</li>
            <li>🩺 Consultas y vacunación</li>
            <li>🛒 Tienda y pedidos</li>
        </ul>
    </div>

    <div class="auth-form">
        <h2>🔐 Iniciar Sesión</h2>
        <p class="subtitulo">Accede al panel de administración</p>

        <%--
          ELEMENTO JSP: Scriptlet — Lee mensajes puestos por el Servlet
          (Ej: registro exitoso, credenciales incorrectas, cierre de sesión)
        --%>
        <%
            String mensaje     = (String) request.getAttribute("mensaje");
            String tipoMensaje = (String) request.getAttribute("tipoMensaje");

            // También puede llegar mensaje vía query param cuando hay logout
            String logout = request.getParameter("logout");
            if ("ok".equals(logout) && mensaje == null) {
                mensaje     = "👋 Sesión cerrada correctamente.";
                tipoMensaje = "exito";
            }

            if (mensaje != null && !mensaje.isEmpty()) {
        %>
            <div class="alerta alerta-<%= tipoMensaje %>">
                <span><%= mensaje %></span>
            </div>
        <%  } %>

        <form action="<%= request.getContextPath() %>/login" method="post">

            <div class="form-campo">
                <label for="correo">Correo electrónico</label>
                <input type="email" id="correo" name="correo"
                       placeholder="tucorreo@email.com" required>
            </div>

            <div class="form-campo">
                <label for="contrasena">Contraseña</label>
                <input type="password" id="contrasena" name="contrasena"
                       placeholder="Tu contraseña" required>
            </div>

            <button type="submit" class="btn btn-primario">Ingresar 🚀</button>
        </form>

        <p class="auth-enlace">
            ¿No tienes cuenta? <a href="<%= request.getContextPath() %>/registro">Regístrate</a>
        </p>

        <%-- Hint de cuentas preinsertadas para la demo --%>
        <div class="demo-hint">
            <strong>👤 Cuentas demo:</strong><br>
            alejandro@gmail.com / 123456<br>
            daniel@gmail.com / 123456
        </div>
    </div>

</div>

</body>
</html>
