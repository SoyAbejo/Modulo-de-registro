package com.petservices.dao;

import com.petservices.modelo.Servicio;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DAO: ServicioDAO — simulación en memoria (misma estrategia que ClienteDAO).
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA5-EV03
 */
public class ServicioDAO {

    private static final List<Servicio> baseDatos  = new ArrayList<>();
    private static final AtomicInteger   contadorId = new AtomicInteger(1);

    static {
        baseDatos.add(new Servicio(contadorId.getAndIncrement(), "Consulta", "Vacunación antirrábica", null));
        baseDatos.add(new Servicio(contadorId.getAndIncrement(), "Baño y corte", "Grooming completo", null));
        baseDatos.add(new Servicio(contadorId.getAndIncrement(), "Cirugía", "Esterilización", null));
    }

    public boolean insertar(Servicio s) {
        s.setIdServicio(contadorId.getAndIncrement());
        baseDatos.add(s);
        return true;
    }

    public List<Servicio> listarTodos() {
        return new ArrayList<>(baseDatos);
    }

    public Servicio buscarPorId(int id) {
        for (Servicio s : baseDatos) {
            if (s.getIdServicio() == id) return s;
        }
        return null;
    }

    public boolean actualizar(Servicio nuevo) {
        for (int i = 0; i < baseDatos.size(); i++) {
            if (baseDatos.get(i).getIdServicio() == nuevo.getIdServicio()) {
                baseDatos.set(i, nuevo);
                return true;
            }
        }
        return false;
    }

    public boolean eliminar(int id) {
        return baseDatos.removeIf(s -> s.getIdServicio() == id);
    }
}
