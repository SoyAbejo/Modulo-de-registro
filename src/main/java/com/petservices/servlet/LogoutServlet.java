package com.petservices.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet: LogoutServlet
 * URL de mapeo: /logout
 * ──────────────────────────────────────────────────────────────────────────
 * Destruye la sesión activa y redirige al formulario de login.
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA2-EV02
 */
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener sesión existente (sin crear una nueva)
        HttpSession sesion = request.getSession(false);

        if (sesion != null) {
            sesion.invalidate(); // Destruir todos los atributos de sesión
        }

        // Redirigir al login con mensaje
        response.sendRedirect(request.getContextPath() + "/login?logout=ok");
    }
}
