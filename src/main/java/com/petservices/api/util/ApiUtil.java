package com.petservices.api.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * ApiUtil
 * ──────────────────────────────────────────────────────────────────────────
 * Utilidades comunes para todos los servicios REST del proyecto PetServices.
 * Centraliza:
 *   - La instancia de Gson usada para (de)serializar JSON.
 *   - El envío de respuestas JSON con el código de estado HTTP correcto.
 *   - La lectura del cuerpo (body) de la petición como texto.
 *   - La extracción del identificador numérico de la URL (pathInfo),
 *     por ejemplo /api/clientes/3 -> 3
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA5-EV03
 */
public final class ApiUtil {

    public static final Gson GSON = new Gson();

    private ApiUtil() {}

    /** Escribe un objeto como JSON en la respuesta, con el status HTTP indicado. */
    public static void enviarJson(HttpServletResponse response, int status, Object cuerpo)
            throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(GSON.toJson(cuerpo));
        }
    }

    /** Envía un mensaje de error homogéneo: {"error": "mensaje"} */
    public static void enviarError(HttpServletResponse response, int status, String mensaje)
            throws IOException {
        enviarJson(response, status, new ErrorDTO(mensaje));
    }

    /** Lee el cuerpo completo de la petición como String (UTF-8). */
    public static String leerBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                sb.append(linea);
            }
        }
        return sb.toString();
    }

    /**
     * Lee el body de la petición y lo deserializa como JSON.
     * Retorna null si el body está vacío o el JSON es inválido.
     */
    public static <T> T parsearJson(HttpServletRequest request, Class<T> clase) throws IOException {
        String body = leerBody(request);
        if (body == null || body.trim().isEmpty()) return null;
        try {
            return GSON.fromJson(body, clase);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    /**
     * Extrae el id numérico del pathInfo de la petición.
     * Ejemplo: pathInfo = "/3" -> 3 ; pathInfo = null o "/" -> -1 (no hay id)
     */
    public static int extraerId(HttpServletRequest request) {
        String pathInfo = request.getPathInfo(); // ej: "/3"
        if (pathInfo == null || pathInfo.equals("/")) {
            return -1;
        }
        String valor = pathInfo.substring(1); // quita el "/"
        // Si viene con más segmentos (ej: /3/algo) solo tomamos el primero
        int barra = valor.indexOf('/');
        if (barra != -1) {
            valor = valor.substring(0, barra);
        }
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /** DTO simple para representar errores en formato JSON. */
    public static class ErrorDTO {
        public String error;
        public ErrorDTO(String error) { this.error = error; }
    }
}
