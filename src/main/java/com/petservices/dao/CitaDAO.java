package com.petservices.dao;

import com.petservices.modelo.Cita;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DAO: CitaDAO — simulación en memoria (misma estrategia que ClienteDAO).
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA5-EV03
 */
public class CitaDAO {

    private static final List<Cita>    baseDatos  = new ArrayList<>();
    private static final AtomicInteger contadorId = new AtomicInteger(1);

    static {
        baseDatos.add(new Cita(contadorId.getAndIncrement(), "2026-04-20", "10:00:00", "confirmada", 1, 1));
        baseDatos.add(new Cita(contadorId.getAndIncrement(), "2026-04-22", "14:30:00", "pendiente",  2, 1));
        baseDatos.add(new Cita(contadorId.getAndIncrement(), "2026-04-25", "09:00:00", "pendiente",  3, 2));
        baseDatos.add(new Cita(contadorId.getAndIncrement(), "2026-04-28", "11:00:00", "confirmada", 4, 3));
    }

    public boolean insertar(Cita c) {
        c.setIdCita(contadorId.getAndIncrement());
        if (c.getEstado() == null) c.setEstado("pendiente");
        baseDatos.add(c);
        return true;
    }

    public List<Cita> listarTodos() {
        return new ArrayList<>(baseDatos);
    }

    public Cita buscarPorId(int id) {
        for (Cita c : baseDatos) {
            if (c.getIdCita() == id) return c;
        }
        return null;
    }

    public boolean actualizar(Cita nueva) {
        for (int i = 0; i < baseDatos.size(); i++) {
            if (baseDatos.get(i).getIdCita() == nueva.getIdCita()) {
                baseDatos.set(i, nueva);
                return true;
            }
        }
        return false;
    }

    public boolean eliminar(int id) {
        return baseDatos.removeIf(c -> c.getIdCita() == id);
    }
}
