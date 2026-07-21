package com.petservices.modelo;

/**
 * Clase Modelo: Cita
 * Representa la entidad "cita" (agenda de citas de mascotas en un
 * establecimiento). Estado: pendiente | confirmada | cancelada | completada.
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA5-EV03
 */
public class Cita {

    private int    idCita;
    private String fecha;   // formato yyyy-MM-dd
    private String hora;    // formato HH:mm:ss
    private String estado;
    private int    idMascota;
    private int    idEstablecimiento;

    public Cita() {}

    public Cita(String fecha, String hora, int idMascota, int idEstablecimiento) {
        this.fecha = fecha;
        this.hora  = hora;
        this.estado = "pendiente";
        this.idMascota = idMascota;
        this.idEstablecimiento = idEstablecimiento;
    }

    public Cita(int idCita, String fecha, String hora, String estado,
                int idMascota, int idEstablecimiento) {
        this.idCita = idCita;
        this.fecha  = fecha;
        this.hora   = hora;
        this.estado = estado;
        this.idMascota = idMascota;
        this.idEstablecimiento = idEstablecimiento;
    }

    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getIdMascota() { return idMascota; }
    public void setIdMascota(int idMascota) { this.idMascota = idMascota; }

    public int getIdEstablecimiento() { return idEstablecimiento; }
    public void setIdEstablecimiento(int idEstablecimiento) { this.idEstablecimiento = idEstablecimiento; }
}
