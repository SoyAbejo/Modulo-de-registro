package com.petservices.dao;

import com.petservices.modelo.Establecimiento;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DAO: EstablecimientoDAO — simulación en memoria (misma estrategia que ClienteDAO).
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA5-EV03
 */
public class EstablecimientoDAO {

    private static final List<Establecimiento> baseDatos  = new CopyOnWriteArrayList<>();
    private static final AtomicInteger          contadorId = new AtomicInteger(1);

    static {
        baseDatos.add(new Establecimiento(contadorId.getAndIncrement(),
                "Veterinaria San Juan", "veterinaria", "Calle 15 #23-45 Tunja", "3204567890"));
        baseDatos.add(new Establecimiento(contadorId.getAndIncrement(),
                "Pet Shop Amigos", "petshop", "Carrera 10 #8-20 Tunja", "3109876543"));
        baseDatos.add(new Establecimiento(contadorId.getAndIncrement(),
                "Groomer Happy Pets", "mixto", "Av. Oriental #45-67 Tunja", "3187654321"));
    }

    public boolean insertar(Establecimiento e) {
        e.setIdEstablecimiento(contadorId.getAndIncrement());
        baseDatos.add(e);
        return true;
    }

    public List<Establecimiento> listarTodos() {
        return new ArrayList<>(baseDatos);
    }

    public Establecimiento buscarPorId(int id) {
        for (Establecimiento e : baseDatos) {
            if (e.getIdEstablecimiento() == id) return e;
        }
        return null;
    }

    public boolean actualizar(Establecimiento nuevo) {
        for (int i = 0; i < baseDatos.size(); i++) {
            if (baseDatos.get(i).getIdEstablecimiento() == nuevo.getIdEstablecimiento()) {
                baseDatos.set(i, nuevo);
                return true;
            }
        }
        return false;
    }

    public boolean eliminar(int id) {
        return baseDatos.removeIf(e -> e.getIdEstablecimiento() == id);
    }
}
