<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Vista: error404.jsp — Página personalizada de error 404.
  Referenciada desde WEB-INF/web.xml → <error-page> para el código 404.

  Proyecto: PetServices - SENA GA7-220501096
--%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error 404 - PetServices</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/vistas/css/estilos.css">
</head>
<body class="error-body">

    <div class="card error-card">
        <div class="codigo">🐾 404</div>
        <h2>Página no encontrada</h2>
        <p>La página que buscas no existe o fue movida.<br>
           Verifica la URL o vuelve al panel principal.</p>
        <a class="btn btn-primario" href="<%= request.getContextPath() %>/dashboard">🏠 Volver al inicio</a>
    </div>

</body>
</html>
