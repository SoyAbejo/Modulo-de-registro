package com.petservices.modelo;

/**
 * Clase Modelo: Servicio
 * Representa la entidad "servicio" (tipo de servicio ofrecido: consulta,
 * baño y corte, cirugía, etc.).
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA5-EV03
 */
public class Servicio {

    private int    idServicio;
    private String tipo;
    private String nombre;
    private Integer idMascota; // puede ser null

    public Servicio() {}

    public Servicio(String tipo, String nombre) {
        this.tipo   = tipo;
        this.nombre = nombre;
    }

    public Servicio(int idServicio, String tipo, String nombre, Integer idMascota) {
        this.idServicio = idServicio;
        this.tipo       = tipo;
        this.nombre     = nombre;
        this.idMascota  = idMascota;
    }

    public int getIdServicio() { return idServicio; }
    public void setIdServicio(int idServicio) { this.idServicio = idServicio; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getIdMascota() { return idMascota; }
    public void setIdMascota(Integer idMascota) { this.idMascota = idMascota; }
}
