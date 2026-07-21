package com.petservices.modelo;

/**
 * Clase Modelo: Mascota
 * Representa la entidad "mascota" del modelo físico petconnect_completo.sql.
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA5-EV03
 */
public class Mascota {

    private int    idMascota;
    private String raza;
    private String nombre;
    private int    idCliente;
    private Integer idServicio; // puede ser null

    public Mascota() {}

    public Mascota(String raza, String nombre, int idCliente) {
        this.raza      = raza;
        this.nombre    = nombre;
        this.idCliente = idCliente;
    }

    public Mascota(int idMascota, String raza, String nombre, int idCliente, Integer idServicio) {
        this.idMascota  = idMascota;
        this.raza       = raza;
        this.nombre     = nombre;
        this.idCliente  = idCliente;
        this.idServicio = idServicio;
    }

    public int getIdMascota() { return idMascota; }
    public void setIdMascota(int idMascota) { this.idMascota = idMascota; }

    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public Integer getIdServicio() { return idServicio; }
    public void setIdServicio(Integer idServicio) { this.idServicio = idServicio; }
}
