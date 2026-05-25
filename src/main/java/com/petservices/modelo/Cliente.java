package com.petservices.modelo;

/**
 * Clase Modelo: Cliente
 * Representa la entidad "cliente" de la base de datos petconnect.
 * Sigue el patrón JavaBean (atributos privados + getters/setters).
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA2-EV02
 */
public class Cliente {

    // ── Atributos (coinciden con las columnas de la tabla `cliente`) ──
    private int    idCliente;
    private String nombre;
    private String correo;
    private String contrasena;      // Se guarda el hash en producción; aquí texto plano para la demo
    private String fechaRegistro;   // Almacenado como String para simplicidad en la capa de vista

    // ── Constructor vacío (requerido por JavaBean) ──
    public Cliente() {}

    // ── Constructor completo (sin id, para inserción) ──
    public Cliente(String nombre, String correo, String contrasena) {
        this.nombre     = nombre;
        this.correo     = correo;
        this.contrasena = contrasena;
    }

    // ── Constructor completo (con id, para listado) ──
    public Cliente(int idCliente, String nombre, String correo,
                   String contrasena, String fechaRegistro) {
        this.idCliente    = idCliente;
        this.nombre       = nombre;
        this.correo       = correo;
        this.contrasena   = contrasena;
        this.fechaRegistro = fechaRegistro;
    }

    // ── Getters y Setters ──
    public int    getIdCliente()    { return idCliente; }
    public void   setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getNombre()       { return nombre; }
    public void   setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo()       { return correo; }
    public void   setCorreo(String correo) { this.correo = correo; }

    public String getContrasena()   { return contrasena; }
    public void   setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getFechaRegistro() { return fechaRegistro; }
    public void   setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    /** Útil para depuración y logs */
    @Override
    public String toString() {
        return "Cliente{id=" + idCliente + ", nombre='" + nombre
                + "', correo='" + correo + "'}";
    }
}
