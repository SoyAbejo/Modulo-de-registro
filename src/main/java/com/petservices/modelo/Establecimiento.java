package com.petservices.modelo;

/**
 * Clase Modelo: Establecimiento
 * Representa la entidad "establecimiento" (petshop, veterinaria o mixto).
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA5-EV03
 */
public class Establecimiento {

    private int    idEstablecimiento;
    private String nombre;
    private String tipo;       // petshop | veterinaria | mixto
    private String direccion;
    private String telefono;

    public Establecimiento() {}

    public Establecimiento(String nombre, String tipo, String direccion, String telefono) {
        this.nombre    = nombre;
        this.tipo      = tipo;
        this.direccion = direccion;
        this.telefono  = telefono;
    }

    public Establecimiento(int idEstablecimiento, String nombre, String tipo,
                            String direccion, String telefono) {
        this.idEstablecimiento = idEstablecimiento;
        this.nombre    = nombre;
        this.tipo      = tipo;
        this.direccion = direccion;
        this.telefono  = telefono;
    }

    public int getIdEstablecimiento() { return idEstablecimiento; }
    public void setIdEstablecimiento(int idEstablecimiento) { this.idEstablecimiento = idEstablecimiento; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
