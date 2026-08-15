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

    // ── Campos del módulo web (AA2-EV02) ──
    private int    idCliente;
    private String nombreCliente; // desnormalizado para mostrar el dueño sin JOIN
    private String nombreMascota; // desnormalizado para mostrar la mascota sin JOIN
    private String servicio;      // Consulta, Vacunación, Baño/Peluquería, Cirugía

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

    // Constructor completo (módulo web): incluye cliente, mascota y servicio
    public Cita(int idCita, String fecha, String hora, String estado,
                int idCliente, String nombreCliente, int idMascota,
                String nombreMascota, String servicio) {
        this.idCita         = idCita;
        this.fecha          = fecha;
        this.hora           = hora;
        this.estado         = estado;
        this.idCliente      = idCliente;
        this.nombreCliente  = nombreCliente;
        this.idMascota      = idMascota;
        this.nombreMascota  = nombreMascota;
        this.servicio       = servicio;
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

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getNombreMascota() { return nombreMascota; }
    public void setNombreMascota(String nombreMascota) { this.nombreMascota = nombreMascota; }

    public String getServicio() { return servicio; }
    public void setServicio(String servicio) { this.servicio = servicio; }

    /**
     * Devuelve fecha y hora combinadas en formato apto para <input type="datetime-local">
     * (yyyy-MM-ddTHH:mm). Compatible con la vista web del módulo de citas.
     */
    public String getFechaHora() {
        if (fecha == null || fecha.isEmpty()) return "";
        String h = (hora != null && hora.length() >= 5) ? hora.substring(0, 5) : "";
        return h.isEmpty() ? fecha : fecha + "T" + h;
    }

    /**
     * Recibe el valor del input datetime-local (yyyy-MM-ddTHH:mm) y lo separa
     * en los campos fecha (yyyy-MM-dd) y hora (HH:mm:ss) del modelo físico.
     */
    public void setFechaHora(String fechaHora) {
        if (fechaHora == null || fechaHora.isEmpty()) return;
        String[] partes = fechaHora.split("T");
        this.fecha = partes[0];
        if (partes.length > 1 && !partes[1].isEmpty()) {
            String h = partes[1];
            // Si viene solo HH:mm, lo completamos a HH:mm:ss (formato del modelo físico)
            if (h.length() == 5) h = h + ":00";
            this.hora = h;
        }
    }
}
