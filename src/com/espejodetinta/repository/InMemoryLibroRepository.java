package com.espejodetinta.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.espejodetinta.model.Libro;

public class InMemoryLibroRepository {
    private final List<Libro> data = new ArrayList<>();
    private final AtomicInteger seq = new AtomicInteger(0);

    public Libro save(String titulo, String autor, double precio, int stock) {
        int id = seq.incrementAndGet();
        Libro l = new Libro(id, titulo, autor, precio, stock);
        data.add(l);
        return l;
    }

    public List<Libro> findAll() {
        return new ArrayList<>(data);
    }

    public Libro findById(int id) {
        return data.stream().filter(l -> l.getId() == id).findFirst().orElse(null);
    }

    public boolean delete(int id) {
        return data.removeIf(l -> l.getId() == id);
    }
}
