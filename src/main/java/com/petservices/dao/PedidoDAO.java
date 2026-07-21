package com.petservices.dao;

import com.petservices.modelo.Pedido;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DAO: PedidoDAO — simulación en memoria (misma estrategia que ClienteDAO).
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA5-EV03
 */
public class PedidoDAO {

    private static final List<Pedido>  baseDatos  = new ArrayList<>();
    private static final AtomicInteger contadorId = new AtomicInteger(1);

    static {
        baseDatos.add(new Pedido(contadorId.getAndIncrement(), "2026-03-15",
                68000.00, "completado", 2, 1, 2));
    }

    public boolean insertar(Pedido p) {
        p.setIdPedido(contadorId.getAndIncrement());
        if (p.getFecha() == null) p.setFecha(java.time.LocalDate.now().toString());
        if (p.getEstado() == null) p.setEstado("pendiente");
        baseDatos.add(p);
        return true;
    }

    public List<Pedido> listarTodos() {
        return new ArrayList<>(baseDatos);
    }

    public Pedido buscarPorId(int id) {
        for (Pedido p : baseDatos) {
            if (p.getIdPedido() == id) return p;
        }
        return null;
    }

    public boolean actualizar(Pedido nuevo) {
        for (int i = 0; i < baseDatos.size(); i++) {
            if (baseDatos.get(i).getIdPedido() == nuevo.getIdPedido()) {
                baseDatos.set(i, nuevo);
                return true;
            }
        }
        return false;
    }

    public boolean eliminar(int id) {
        return baseDatos.removeIf(p -> p.getIdPedido() == id);
    }
}
