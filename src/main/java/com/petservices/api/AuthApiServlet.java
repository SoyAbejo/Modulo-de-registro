package com.petservices.api;

import com.petservices.dao.ClienteDAO;
import com.petservices.modelo.Cliente;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.petservices.api.util.ApiUtil.*;

/**
 * Servicio REST: /api/auth/login
 * ──────────────────────────────────────────────────────────────────────────
 * POST /api/auth/login -> valida credenciales (correo, contrasena) y
 *                         devuelve los datos del cliente autenticado.
 *
 * Complementa el LoginServlet (basado en sesión/JSP) con una vía de
 * autenticación por API para clientes externos (apps móviles, SPA, etc.).
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA5-EV03
 */
@WebServlet("/api/auth/login")
public class AuthApiServlet extends HttpServlet {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Credenciales credenciales = GSON.fromJson(leerBody(request), Credenciales.class);
        if (credenciales == null || credenciales.correo == null || credenciales.contrasena == null) {
            enviarError(response, HttpServletResponse.SC_BAD_REQUEST, "Los campos 'correo' y 'contrasena' son obligatorios");
            return;
        }
        Cliente cliente = clienteDAO.validarLogin(credenciales.correo, credenciales.contrasena);
        if (cliente == null) {
            enviarError(response, HttpServletResponse.SC_UNAUTHORIZED, "Correo o contraseña incorrectos");
        } else {
            enviarJson(response, HttpServletResponse.SC_OK, cliente);
        }
    }

    /** DTO de entrada para el login. */
    static class Credenciales {
        String correo;
        String contrasena;
    }
}
