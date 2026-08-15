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

    // ── Campos del módulo web (AA2-EV02) ──
    private String especie;       // Perro, Gato, Ave, ...
    private int    edad;          // en años
    private String nombreCliente; // desnormalizado para mostrar el dueño sin JOIN

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

    // Constructor completo (módulo web): incluye especie, edad y nombre del dueño
    public Mascota(int idMascota, String raza, String nombre, String especie,
                   int edad, int idCliente, String nombreCliente) {
        this.idMascota    = idMascota;
        this.raza         = raza;
        this.nombre       = nombre;
        this.especie      = especie;
        this.edad         = edad;
        this.idCliente    = idCliente;
        this.nombreCliente = nombreCliente;
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

    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
}
