package com.petservices.dao;

import com.petservices.modelo.Producto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DAO: ProductoDAO — simulación en memoria (misma estrategia que ClienteDAO).
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA5-EV03
 */
public class ProductoDAO {

    private static final List<Producto> baseDatos  = new ArrayList<>();
    private static final AtomicInteger   contadorId = new AtomicInteger(1);

    static {
        baseDatos.add(new Producto(contadorId.getAndIncrement(), "Croquetas Pedigree Adulto 10kg",
                "Alimento premium", 45000.00, "disponible", 30, 2, null));
        baseDatos.add(new Producto(contadorId.getAndIncrement(), "Shampoo para perros",
                "Antipulgas", 18000.00, "disponible", 50, 3, null));
        baseDatos.add(new Producto(contadorId.getAndIncrement(), "Juguete Kong",
                "Resistente", 25000.00, "disponible", 15, 2, null));
        baseDatos.add(new Producto(contadorId.getAndIncrement(), "Arena para gatos",
                "10kg", 22000.00, "disponible", 40, 2, null));
    }

    public boolean insertar(Producto p) {
        p.setIdProducto(contadorId.getAndIncrement());
        if (p.getEstado() == null) p.setEstado("disponible");
        baseDatos.add(p);
        return true;
    }

    public List<Producto> listarTodos() {
        return new ArrayList<>(baseDatos);
    }

    public Producto buscarPorId(int id) {
        for (Producto p : baseDatos) {
            if (p.getIdProducto() == id) return p;
        }
        return null;
    }

    public boolean actualizar(Producto nuevo) {
        for (int i = 0; i < baseDatos.size(); i++) {
            if (baseDatos.get(i).getIdProducto() == nuevo.getIdProducto()) {
                baseDatos.set(i, nuevo);
                return true;
            }
        }
        return false;
    }

    public boolean eliminar(int id) {
        return baseDatos.removeIf(p -> p.getIdProducto() == id);
    }
}
