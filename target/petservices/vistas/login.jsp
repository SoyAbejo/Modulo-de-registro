<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Vista: login.jsp
  Formulario de inicio de sesión de PetServices.
  Apunta al LoginServlet mediante method="post".

  Proyecto: PetServices - SENA GA7-220501096-AA2-EV02
--%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión - PetServices</title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body {
            font-family: Arial, sans-serif;
            background: linear-gradient(135deg, #6c4cff, #a88cff);
            min-height: 100vh;
            display: flex; justify-content: center; align-items: center;
        }
        .container {
            display: flex; width: 750px;
            background: white; border-radius: 15px;
            overflow: hidden; box-shadow: 0 10px 30px rgba(0,0,0,0.25);
        }
        .left {
            width: 45%; background: #6c4cff; color: white;
            padding: 50px 35px; display: flex; flex-direction: column; justify-content: center;
        }
        .left h1 { font-size: 2rem; margin-bottom: 15px; }
        .left p  { opacity: 0.85; line-height: 1.6; }

        .right { width: 55%; padding: 50px 40px; }
        .right h2 { color: #6c4cff; margin-bottom: 25px; font-size: 1.5rem; }

        .form-group { margin-bottom: 18px; }
        .form-group label {
            display: block; font-size: 0.85rem;
            color: #555; margin-bottom: 5px; font-weight: bold;
        }
        .form-group input {
            width: 100%; padding: 11px 14px;
            border: 1.5px solid #ddd; border-radius: 8px; font-size: 0.95rem;
        }
        .form-group input:focus { outline: none; border-color: #6c4cff; }

        .btn-primary {
            width: 100%; padding: 12px; background: #6c4cff;
            color: white; border: none; border-radius: 8px;
            font-size: 1rem; cursor: pointer; margin-top: 5px;
        }
        .btn-primary:hover { background: #5936e3; }

        .link { text-align: center; margin-top: 14px; font-size: 0.9rem; color: #666; }
        .link a { color: #6c4cff; text-decoration: none; font-weight: bold; }

        .alerta { padding: 10px 14px; border-radius: 8px; margin-bottom: 15px; font-size: 0.9rem; }
        .alerta-error { background: #fde8e8; color: #c0392b; border: 1px solid #f5c6cb; }
        .alerta-exito { background: #eafaf1; color: #1e8449; border: 1px solid #a9dfbf; }

        /* Hint de cuentas demo */
        .demo-hint {
            background: #f0edff; border-radius: 8px;
            padding: 10px 14px; margin-top: 15px; font-size: 0.82rem; color: #555;
        }
        .demo-hint strong { color: #6c4cff; }
    </style>
</head>
<body>

<div class="container">

    <div class="left">
        <h1>🐾 PetServices</h1>
        <p>Bienvenido de nuevo.<br>Cuida a tu mascota con los mejores servicios de Tunja.</p>
    </div>

    <div class="right">
        <h2>🔐 Iniciar Sesión</h2>

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
                <%= mensaje %>  <%-- EXPRESIÓN JSP: muestra el mensaje dinámico --%>
            </div>
        <%  } %>

        <form action="<%= request.getContextPath() %>/login" method="post">

            <div class="form-group">
                <label for="correo">Correo electrónico</label>
                <input type="email" id="correo" name="correo"
                       placeholder="tucorreo@email.com" required>
            </div>

            <div class="form-group">
                <label for="contrasena">Contraseña</label>
                <input type="password" id="contrasena" name="contrasena"
                       placeholder="Tu contraseña" required>
            </div>

            <button type="submit" class="btn-primary">Ingresar 🚀</button>
        </form>

        <div class="link">
            <p>¿No tienes cuenta? <a href="<%= request.getContextPath() %>/registro">Regístrate</a></p>
        </div>

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
