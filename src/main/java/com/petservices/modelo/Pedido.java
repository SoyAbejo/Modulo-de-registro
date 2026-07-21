package com.petservices.modelo;

/**
 * Clase Modelo: Pedido
 * Representa la entidad "pedido" realizado por un cliente en un
 * establecimiento.
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA5-EV03
 */
public class Pedido {

    private int    idPedido;
    private String fecha;
    private double total;
    private String estado;
    private int    cantidad;
    private int    idCliente;
    private int    idEstablecimiento;

    public Pedido() {}

    public Pedido(double total, int cantidad, int idCliente, int idEstablecimiento) {
        this.total = total;
        this.estado = "pendiente";
        this.cantidad = cantidad;
        this.idCliente = idCliente;
        this.idEstablecimiento = idEstablecimiento;
    }

    public Pedido(int idPedido, String fecha, double total, String estado,
                  int cantidad, int idCliente, int idEstablecimiento) {
        this.idPedido = idPedido;
        this.fecha    = fecha;
        this.total    = total;
        this.estado   = estado;
        this.cantidad = cantidad;
        this.idCliente = idCliente;
        this.idEstablecimiento = idEstablecimiento;
    }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getIdEstablecimiento() { return idEstablecimiento; }
    public void setIdEstablecimiento(int idEstablecimiento) { this.idEstablecimiento = idEstablecimiento; }
}
