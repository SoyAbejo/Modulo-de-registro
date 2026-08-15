<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Vista: error500.jsp — Página personalizada de error 500.
  Referenciada desde WEB-INF/web.xml → <error-page> para el código 500.

  Proyecto: PetServices - SENA GA7-220501096
--%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error 500 - PetServices</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/vistas/css/estilos.css">
</head>
<body class="error-body">

    <div class="card error-card">
        <div class="codigo peligro">🐾 500</div>
        <h2>Error interno del servidor</h2>
        <p>Ocurrió un error inesperado al procesar tu solicitud.<br>
           Inténtalo de nuevo más tarde.</p>
        <a class="btn btn-primario" href="<%= request.getContextPath() %>/dashboard">🏠 Volver al inicio</a>
    </div>

</body>
</html>
