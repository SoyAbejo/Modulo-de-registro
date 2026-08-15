package com.petservices.servlet;

import com.petservices.dao.ClienteDAO;
import com.petservices.modelo.Cliente;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet: LoginServlet
 * URL de mapeo: /login
 * ──────────────────────────────────────────────────────────────────────────
 * Gestiona el INICIO DE SESIÓN de clientes en PetServices.
 *
 *  doGet  → Muestra el formulario de login (login.jsp)
 *           También recibe el parámetro ?registro=exitoso para mostrar
 *           un mensaje de confirmación tras un registro nuevo.
 *  doPost → Valida credenciales y crea la sesión HTTP del usuario.
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA2-EV02
 */
public class LoginServlet extends HttpServlet {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    // ──────────────────────────────────────────────────────────────────────
    // doGet: Carga el formulario de inicio de sesión
    // ──────────────────────────────────────────────────────────────────────
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Si viene de un registro exitoso, pasamos el aviso al JSP
        String desde = request.getParameter("registro");
        if ("exitoso".equals(desde)) {
            request.setAttribute("mensaje",
                    "✅ ¡Cuenta creada exitosamente! Ya puedes iniciar sesión.");
            request.setAttribute("tipoMensaje", "exito");
        }

        // Si viene de un logout exitoso, mostrar aviso
        String desdeLogout = request.getParameter("logout");
        if ("ok".equals(desdeLogout)) {
            request.setAttribute("mensaje",
                    "✅ Has cerrado sesión correctamente.");
            request.setAttribute("tipoMensaje", "exito");
        }

        request.getRequestDispatcher("/vistas/login.jsp")
               .forward(request, response);
    }

    // ──────────────────────────────────────────────────────────────────────
    // doPost: Procesa las credenciales enviadas por el formulario
    // ──────────────────────────────────────────────────────────────────────
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Codificación UTF-8
        request.setCharacterEncoding("UTF-8");

        // 2. Leer parámetros del formulario
        String correo    = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");

        // 3. Validación mínima
        if (correo == null || contrasena == null
                || correo.trim().isEmpty() || contrasena.trim().isEmpty()) {
            request.setAttribute("mensaje", "❌ Ingresa correo y contraseña.");
            request.setAttribute("tipoMensaje", "error");
            request.getRequestDispatcher("/vistas/login.jsp")
                   .forward(request, response);
            return;
        }

        // 4. Verificar credenciales en el DAO
        Cliente cliente = clienteDAO.validarLogin(
                correo.trim().toLowerCase(),
                contrasena
        );

        if (cliente != null) {
            // 5a. CREDENCIALES CORRECTAS → Crear sesión HTTP
            HttpSession sesion = request.getSession(true); // crea sesión nueva

            // Guardamos el objeto Cliente completo en la sesión
            // Así cualquier JSP o Servlet puede recuperarlo con:
            //   Cliente c = (Cliente) session.getAttribute("clienteSesion");
            sesion.setAttribute("clienteSesion", cliente);
            sesion.setAttribute("nombreUsuario",  cliente.getNombre());

            // Tiempo máximo de inactividad: 30 minutos
            sesion.setMaxInactiveInterval(30 * 60);

            // Redirigir al dashboard principal (PRG pattern)
            response.sendRedirect(request.getContextPath() + "/dashboard");

        } else {
            // 5b. CREDENCIALES INCORRECTAS → Volver al login con error
            request.setAttribute("mensaje",
                    "❌ Correo o contraseña incorrectos. Intenta de nuevo.");
            request.setAttribute("tipoMensaje", "error");
            request.getRequestDispatcher("/vistas/login.jsp")
                   .forward(request, response);
        }
    }
}
