<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Vista: registro.jsp — Formulario de registro de nuevos clientes.
  Rediseñado con el design system central (estilos.css).

  Proyecto: PetServices - SENA GA7-220501096
--%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro - PetServices</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/vistas/css/estilos.css">
</head>
<body class="auth-body">

<div class="auth-card">

    <div class="auth-lado">
        <h1>🐾 PetServices</h1>
        <p>Regístrate y cuida a tu mascota con los mejores servicios.</p>
        <ul>
            <li>🛁 Baño y peluquería</li>
            <li>🩺 Consultas veterinarias</li>
            <li>🛒 Productos premium</li>
            <li>📅 Citas en línea</li>
        </ul>
    </div>

    <div class="auth-form">
        <h2>✍️ Crear Cuenta</h2>
        <p class="subtitulo">Completa tus datos para registrarte</p>

        <%--
          ELEMENTO JSP: Scriptlet
          Recupera el atributo "mensaje" que el Servlet colocó en el request.
        --%>
        <%
            String mensaje     = (String) request.getAttribute("mensaje");
            String tipoMensaje = (String) request.getAttribute("tipoMensaje");

            if (mensaje != null && !mensaje.isEmpty()) {
        %>
            <div class="alerta alerta-<%= tipoMensaje %>">
                <span><%= mensaje %></span>
            </div>
        <%
            }
        %>

        <form action="<%= request.getContextPath() %>/registro" method="post">

            <div class="form-campo">
                <label for="nombre">Nombre completo</label>
                <input type="text" id="nombre" name="nombre"
                       placeholder="Ej: Alejandro Puerto"
                       required maxlength="50">
            </div>

            <div class="form-campo">
                <label for="correo">Correo electrónico</label>
                <input type="email" id="correo" name="correo"
                       placeholder="tucorreo@email.com"
                       required maxlength="50">
            </div>

            <div class="form-campo">
                <label for="contrasena">Contraseña</label>
                <input type="password" id="contrasena" name="contrasena"
                       placeholder="Mínimo 6 caracteres"
                       required minlength="6">
            </div>

            <div class="form-campo">
                <label for="confirmar">Confirmar contraseña</label>
                <input type="password" id="confirmar" name="confirmar"
                       placeholder="Repite tu contraseña"
                       required minlength="6">
            </div>

            <button type="submit" class="btn btn-primario">🐾 Registrarse</button>
        </form>

        <p class="auth-enlace">
            ¿Ya tienes cuenta? <a href="<%= request.getContextPath() %>/login">Inicia sesión</a>
        </p>
    </div>

</div>

</body>
</html>
