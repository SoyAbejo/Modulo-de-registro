package com.petservices.modelo;

/**
 * Clase Modelo: Producto
 * Representa la entidad "producto" ofrecido por un establecimiento
 * (petshop / veterinaria).
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA5-EV03
 */
public class Producto {

    private int    idProducto;
    private String nombre;
    private String descripcion;
    private double precio;
    private String estado;   // disponible | agotado, etc.
    private int    stock;
    private int    idEstablecimiento;
    private Integer idCliente; // puede ser null

    public Producto() {}

    public Producto(String nombre, String descripcion, double precio,
                     int stock, int idEstablecimiento) {
        this.nombre      = nombre;
        this.descripcion = descripcion;
        this.precio      = precio;
        this.estado      = "disponible";
        this.stock       = stock;
        this.idEstablecimiento = idEstablecimiento;
    }

    public Producto(int idProducto, String nombre, String descripcion, double precio,
                     String estado, int stock, int idEstablecimiento, Integer idCliente) {
        this.idProducto  = idProducto;
        this.nombre      = nombre;
        this.descripcion = descripcion;
        this.precio      = precio;
        this.estado      = estado;
        this.stock       = stock;
        this.idEstablecimiento = idEstablecimiento;
        this.idCliente   = idCliente;
    }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getIdEstablecimiento() { return idEstablecimiento; }
    public void setIdEstablecimiento(int idEstablecimiento) { this.idEstablecimiento = idEstablecimiento; }

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }
}
