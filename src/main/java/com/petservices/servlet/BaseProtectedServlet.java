package com.petservices.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Utilidades compartidas para servlets que requieren una sesión activa.
 */
public abstract class BaseProtectedServlet extends HttpServlet {

    protected boolean requireLogin(HttpServletRequest request,
                                   HttpServletResponse response)
            throws IOException {
        if (hasActiveSession(request)) {
            return false;
        }
        response.sendRedirect(request.getContextPath() + "/login");
        return true;
    }

    protected void forward(HttpServletRequest request,
                           HttpServletResponse response,
                           String viewPath)
            throws ServletException, IOException {
        request.getRequestDispatcher(viewPath).forward(request, response);
    }

    protected boolean hasActiveSession(HttpServletRequest request) {
        HttpSession sesion = request.getSession(false);
        return sesion != null && sesion.getAttribute("clienteSesion") != null;
    }

    // ── Utilidades de validación compartidas ──────────────────────────────

    protected String limpiar(String valor) {
        return (valor == null) ? "" : valor.trim();
    }

    protected int parseInt(String valor, int porDefecto) {
        if (valor == null || valor.trim().isEmpty()) return porDefecto;
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            return porDefecto;
        }
    }

    protected double parseDouble(String valor, double porDefecto) {
        if (valor == null || valor.trim().isEmpty()) return porDefecto;
        try {
            return Double.parseDouble(valor.trim());
        } catch (NumberFormatException e) {
            return porDefecto;
        }
    }
}
