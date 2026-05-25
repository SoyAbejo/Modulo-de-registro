package com.petservices.servlet;

import com.petservices.dao.ClienteDAO;
import com.petservices.modelo.Cliente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet: RegistroServlet
 * URL de mapeo: /registro
 * ──────────────────────────────────────────────────────────────────────────
 * Gestiona el REGISTRO de nuevos clientes en PetServices.
 *
 *  doGet  → Muestra el formulario de registro (registro.jsp)
 *  doPost → Procesa los datos enviados, valida y guarda el cliente
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA2-EV02
 */
@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    // Instancia del DAO (en producción se inyecta o se usa un pool)
    private final ClienteDAO clienteDAO = new ClienteDAO();

    // ──────────────────────────────────────────────────────────────────────
    // doGet: Se invoca cuando el usuario navega a /registro
    //        Solo carga la página del formulario.
    // ──────────────────────────────────────────────────────────────────────
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Limpiar cualquier mensaje previo antes de mostrar el formulario vacío
        request.removeAttribute("mensaje");
        request.removeAttribute("tipoMensaje");

        // Reenviar al JSP de registro (en WEB-INF/vistas)
        request.getRequestDispatcher("/vistas/registro.jsp")
               .forward(request, response);
    }

    // ──────────────────────────────────────────────────────────────────────
    // doPost: Se invoca cuando el formulario hace submit (method="post")
    //         Recibe, valida y persiste los datos del nuevo cliente.
    // ──────────────────────────────────────────────────────────────────────
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Establecer codificación UTF-8 para caracteres especiales (ñ, tildes)
        request.setCharacterEncoding("UTF-8");

        // 2. Leer parámetros del formulario
        String nombre       = request.getParameter("nombre");
        String correo       = request.getParameter("correo");
        String contrasena   = request.getParameter("contrasena");
        String confirmar    = request.getParameter("confirmar");

        // 3. Validaciones básicas del lado servidor
        //    (Complementan el HTML5 required — nunca confíes solo en el cliente)
        if (nombre == null || nombre.trim().isEmpty()
                || correo == null || correo.trim().isEmpty()
                || contrasena == null || contrasena.trim().isEmpty()) {

            enviarMensaje(request, response,
                    "❌ Todos los campos son obligatorios.", "error");
            return;
        }

        if (!contrasena.equals(confirmar)) {
            enviarMensaje(request, response,
                    "❌ Las contraseñas no coinciden.", "error");
            return;
        }

        if (contrasena.length() < 6) {
            enviarMensaje(request, response,
                    "❌ La contraseña debe tener al menos 6 caracteres.", "error");
            return;
        }

        // 4. Crear el objeto Cliente con los datos recibidos
        Cliente nuevoCliente = new Cliente(
                nombre.trim(),
                correo.trim().toLowerCase(),
                contrasena   // En producción: aplicar BCrypt u otro hash
        );

        // 5. Intentar insertar en el DAO (retorna false si el correo ya existe)
        boolean exito = clienteDAO.insertar(nuevoCliente);

        if (exito) {
            // 6a. Registro exitoso → redirigir al login con mensaje de éxito
            //     Se usa sendRedirect para evitar el reenvío del formulario (PRG pattern)
            response.sendRedirect(request.getContextPath()
                    + "/login?registro=exitoso");
        } else {
            // 6b. Correo duplicado → volver al formulario con mensaje de error
            enviarMensaje(request, response,
                    "❌ El correo '" + correo + "' ya está registrado.", "error");
        }
    }

    /**
     * Método auxiliar: coloca un mensaje en el request y reenvía al JSP.
     * Evita repetir el forward en cada rama del doPost.
     */
    private void enviarMensaje(HttpServletRequest req,
                               HttpServletResponse res,
                               String texto, String tipo)
            throws ServletException, IOException {
        req.setAttribute("mensaje",     texto);
        req.setAttribute("tipoMensaje", tipo);
        req.getRequestDispatcher("/vistas/registro.jsp").forward(req, res);
    }
}
