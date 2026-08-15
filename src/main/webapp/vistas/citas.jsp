<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.petservices.modelo.Cita" %>
<%@ page import="com.petservices.modelo.Cliente" %>
<%@ page import="com.petservices.modelo.Mascota" %>
<%--
  Vista: citas.jsp — Agenda de citas (CRUD).
  Usa el layout compartido: sidebar, header, CSS y JS centralizados.
  Incluye el filtrado dinámico de mascotas según el dueño seleccionado.

  Proyecto: PetServices - SENA GA7-220501096
--%>
<%
    request.setAttribute("moduloActivo", "citas");
    request.setAttribute("tituloPagina", "📅 Agenda de Citas");
    request.setAttribute("subtituloPagina", "Programa y da seguimiento a las citas veterinarias");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agenda de Citas - PetServices</title>
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
      Si el Servlet puso una citaEditar en el request, precargamos los campos
      y cambiamos la acción a "actualizar". Si no, es "crear".
    --%>
    <%
        Cita citaEditar = (Cita) request.getAttribute("citaEditar");
        boolean editando = (citaEditar != null);

        List<Cliente> listaClientes =
                (List<Cliente>) request.getAttribute("listaClientes");
        List<Mascota> listaMascotas =
                (List<Mascota>) request.getAttribute("listaMascotas");

        // Cliente preseleccionado: el de la cita en edición, o el primero de la lista
        int clienteSeleccionado = editando ? citaEditar.getIdCliente()
                : (listaClientes != null && !listaClientes.isEmpty()
                   ? listaClientes.get(0).getIdCliente() : -1);
    %>

    <div class="card">
        <div class="card-titulo">
            <div>
                <h2><%= editando ? "✏️ Editar Cita" : "📅 Nueva Cita" %></h2>
                <p><%= editando ? "Modifica los datos de la cita seleccionada" : "Agenda una nueva cita para una mascota" %></p>
            </div>
        </div>

        <form action="<%= request.getContextPath() %>/citas" method="post">
            <input type="hidden" name="accion"
                   value="<%= editando ? "actualizar" : "crear" %>">

            <% if (editando) { %>
                <input type="hidden" name="id" value="<%= citaEditar.getIdCita() %>">
            <% } %>

            <div class="form-grid">
                <div class="form-campo">
                    <label for="idCliente">Cliente (Dueño)</label>
                    <select id="idCliente" name="idCliente" required
                            onchange="cargarMascotas(this.value)">
                        <%
                            if (listaClientes == null || listaClientes.isEmpty()) {
                        %>
                            <option value="">No hay clientes registrados</option>
                        <%  } else {
                                for (Cliente c : listaClientes) {
                        %>
                            <option value="<%= c.getIdCliente() %>"
                                <%= c.getIdCliente() == clienteSeleccionado ? "selected" : "" %>>
                                <%= c.getNombre() %>
                            </option>
                        <%      }
                            } %>
                    </select>
                </div>

                <div class="form-campo">
                    <label for="idMascota">Mascota</label>
                    <select id="idMascota" name="idMascota" required
                            onchange="seleccionarMascota()">
                        <%
                            int mascotaSeleccionada = editando ? citaEditar.getIdMascota() : -1;
                            boolean hayMascotas = false;
                            if (listaMascotas != null) {
                                for (Mascota m : listaMascotas) {
                                    if (m.getIdCliente() == clienteSeleccionado) {
                                        hayMascotas = true;
                        %>
                            <option value="<%= m.getIdMascota() %>"
                                <%= m.getIdMascota() == mascotaSeleccionada ? "selected" : "" %>>
                                <%= m.getNombre() %> (<%= m.getRaza() != null ? m.getRaza() : "sin raza" %>)
                            </option>
                        <%          }
                                }
                            }
                            if (!hayMascotas) { %>
                            <option value="">Este cliente no tiene mascotas</option>
                        <%  } %>
                    </select>
                </div>

                <div class="form-campo">
                    <label for="fechaHora">Fecha y hora</label>
                    <input type="datetime-local" id="fechaHora" name="fechaHora"
                           value="<%= editando ? citaEditar.getFechaHora() : "" %>" required>
                </div>

                <div class="form-campo">
                    <label for="servicio">Servicio</label>
                    <select id="servicio" name="servicio" required>
                        <%
                            String servicioSel = editando ? citaEditar.getServicio() : "";
                            String[] servicios = {"Consulta", "Vacunación", "Baño/Peluquería", "Cirugía"};
                            for (String srv : servicios) {
                        %>
                            <option value="<%= srv %>" <%= srv.equals(servicioSel) ? "selected" : "" %>><%= srv %></option>
                        <%  } %>
                    </select>
                </div>

                <div class="form-campo">
                    <label for="estado">Estado</label>
                    <select id="estado" name="estado">
                        <%
                            String estadoSel = editando ? citaEditar.getEstado() : "pendiente";
                            if (estadoSel == null) estadoSel = "pendiente";
                            String[] estados = {"pendiente", "confirmada", "cancelada"};
                            for (String est : estados) {
                        %>
                            <option value="<%= est %>" <%= est.equals(estadoSel) ? "selected" : "" %>>
                                <%= est.substring(0, 1).toUpperCase() + est.substring(1) %>
                            </option>
                        <%  } %>
                    </select>
                </div>
            </div>

            <div class="form-acciones">
                <button type="submit" class="btn btn-primario">
                    <%= editando ? "💾 Actualizar" : "➕ Agendar" %>
                </button>
                <% if (editando) { %>
                    <a href="<%= request.getContextPath() %>/citas">
                        <button type="button" class="btn btn-secundario">✖ Cancelar</button>
                    </a>
                <% } %>
            </div>
        </form>
    </div>

    <!-- ── Tabla de citas ── -->
    <div class="card">
        <div class="card-titulo">
            <div>
                <h2>📋 Citas Programadas</h2>
                <p>Agenda general del consultorio</p>
            </div>
            <div class="busqueda">
                <span>🔍</span>
                <input type="search" data-filtro="tabla-citas" placeholder="Buscar cita...">
            </div>
        </div>

        <%
            List<Cita> listaCitas = (List<Cita>) request.getAttribute("listaCitas");
        %>

        <div class="tabla-envoltura">
            <table class="tabla" id="tabla-citas">
                <thead>
                    <tr>
                        <th>#ID</th>
                        <th>Mascota</th>
                        <th>Dueño</th>
                        <th>Fecha y hora</th>
                        <th>Servicio</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    if (listaCitas == null || listaCitas.isEmpty()) {
                %>
                    <tr>
                        <td colspan="7" class="vacio fila-vacia">No hay citas programadas aún.</td>
                    </tr>
                <%
                    } else {
                        for (Cita c : listaCitas) {
                            String estadoCita = (c.getEstado() != null) ? c.getEstado() : "pendiente";
                            String textoEstado = estadoCita.substring(0, 1).toUpperCase()
                                               + estadoCita.substring(1);
                            String horaCita = (c.getHora() != null && c.getHora().length() >= 5)
                                    ? c.getHora().substring(0, 5) : "—";
                %>
                    <tr>
                        <td><span class="badge">#<%= c.getIdCita() %></span></td>
                        <td><strong><%= c.getNombreMascota() != null ? c.getNombreMascota() : "—" %></strong></td>
                        <td><%= c.getNombreCliente() != null ? c.getNombreCliente() : "—" %></td>
                        <td><%= c.getFecha() != null ? c.getFecha() : "—" %> · <%= horaCita %></td>
                        <td><%= c.getServicio() != null ? c.getServicio() : "—" %></td>
                        <td><span class="badge-estado estado-<%= estadoCita %>"><%= textoEstado %></span></td>
                        <td>
                            <a class="btn-tabla btn-editar" href="<%= request.getContextPath() %>/citas?accion=editar&id=<%= c.getIdCita() %>">✏️ Editar</a>
                            <button class="btn-tabla btn-eliminar"
                                onclick="eliminarRegistro('<%= request.getContextPath() %>/citas?accion=eliminar&id=<%= c.getIdCita() %>',
                                        '<%= (c.getNombreMascota() != null ? c.getNombreMascota() : "la cita").replace("'", "\\'") %>')">
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
                <%= (listaCitas != null) ? listaCitas.size() : 0 %>
            </strong> citas
        </p>
    </div>

</main>

<script>
    // Datos de todas las mascotas para filtrar por dueño en el formulario
    var mascotas = [
    <%
        if (listaMascotas != null) {
            for (Mascota m : listaMascotas) {
    %>
        { id: <%= m.getIdMascota() %>, nombre: "<%= m.getNombre() %>", idCliente: <%= m.getIdCliente() %> },
    <%
            }
        }
    %>
    ];

    /**
     * Filtra el select de mascotas según el cliente (dueño) seleccionado.
     */
    function cargarMascotas(idCliente) {
        var select = document.getElementById("idMascota");
        select.innerHTML = "";
        var encontradas = false;
        for (var i = 0; i < mascotas.length; i++) {
            if (mascotas[i].idCliente == idCliente) {
                var opt = document.createElement("option");
                opt.value = mascotas[i].id;
                opt.text = mascotas[i].nombre;
                select.appendChild(opt);
                encontradas = true;
            }
        }
        if (!encontradas) {
            var opt = document.createElement("option");
            opt.value = "";
            opt.text = "Este cliente no tiene mascotas";
            select.appendChild(opt);
        }
    }

    /**
     * Al elegir una mascota, ajusta automáticamente el cliente (dueño).
     */
    function seleccionarMascota() {
        var idMascota = document.getElementById("idMascota").value;
        for (var i = 0; i < mascotas.length; i++) {
            if (mascotas[i].id == idMascota) {
                document.getElementById("idCliente").value = mascotas[i].idCliente;
                break;
            }
        }
    }
</script>

<script src="<%= request.getContextPath() %>/vistas/js/app.js"></script>
</body>
</html>
