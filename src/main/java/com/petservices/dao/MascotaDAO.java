package com.petservices.dao;

import com.petservices.modelo.Mascota;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DAO: MascotaDAO — simulación en memoria (misma estrategia que ClienteDAO).
 * Reemplazar por JDBC contra la tabla `mascota` de petconnect_completo.sql
 * cuando se conecte la base de datos real.
 *
 * Proyecto: PetServices - SENA GA7-220501096-AA5-EV03
 */
public class MascotaDAO {

    private static final List<Mascota> baseDatos  = new CopyOnWriteArrayList<>();
    private static final AtomicInteger contadorId = new AtomicInteger(1);

    static {
        baseDatos.add(new Mascota(contadorId.getAndIncrement(), "Labrador", "Max",
                "Perro", 3, 1, "Alejandro Puerto"));
        baseDatos.add(new Mascota(contadorId.getAndIncrement(), "Siamés", "Luna",
                "Gato", 2, 2, "Daniel Mejia"));
        baseDatos.add(new Mascota(contadorId.getAndIncrement(), "Golden Retriever", "Rocky",
                "Perro", 5, 1, "Alejandro Puerto"));
        baseDatos.add(new Mascota(contadorId.getAndIncrement(), "Pastor Alemán", "Thor",
                "Perro", 4, 3, "David Yaya"));
    }

    public boolean insertar(Mascota m) {
        m.setIdMascota(contadorId.getAndIncrement());
        baseDatos.add(m);
        return true;
    }

    public List<Mascota> listarTodos() {
        return new ArrayList<>(baseDatos);
    }

    public Mascota buscarPorId(int id) {
        for (Mascota m : baseDatos) {
            if (m.getIdMascota() == id) return m;
        }
        return null;
    }

    public List<Mascota> buscarPorCliente(int idCliente) {
        List<Mascota> resultado = new ArrayList<>();
        for (Mascota m : baseDatos) {
            if (m.getIdCliente() == idCliente) resultado.add(m);
        }
        return resultado;
    }

    public boolean actualizar(Mascota nueva) {
        for (int i = 0; i < baseDatos.size(); i++) {
            if (baseDatos.get(i).getIdMascota() == nueva.getIdMascota()) {
                baseDatos.set(i, nueva);
                return true;
            }
        }
        return false;
    }

    public boolean eliminar(int id) {
        return baseDatos.removeIf(m -> m.getIdMascota() == id);
    }
}
