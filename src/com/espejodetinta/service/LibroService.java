package com.espejodetinta.service;

import java.util.List;

import com.espejodetinta.model.Libro;
import com.espejodetinta.repository.InMemoryLibroRepository;

public class LibroService {
    private final InMemoryLibroRepository repo;

    public LibroService(InMemoryLibroRepository repo) {
        this.repo = repo;
    }

    public Libro crear(String titulo, String autor, double precio, int stock) {
        validarTexto("título", titulo);
        validarTexto("autor", autor);
        validarPrecio(precio);
        validarStock(stock);
        return repo.save(titulo.trim(), autor.trim(), precio, stock);
    }

    public List<Libro> listar() {
        return repo.findAll();
    }

    public Libro buscarPorId(int id) {
        return repo.findById(id);
    }

    public Libro actualizarParcial(int id, String titulo, String autor, Double precio, Integer stock) {
        Libro l = repo.findById(id);
        if (l == null) return null;

        if (titulo != null && !titulo.isBlank()) {
            validarTexto("título", titulo);
            l.setTitulo(titulo.trim());
        }
        if (autor != null && !autor.isBlank()) {
            validarTexto("autor", autor);
            l.setAutor(autor.trim());
        }
        if (precio != null) {
            validarPrecio(precio);
            l.setPrecio(precio);
        }
        if (stock != null) {
            validarStock(stock);
            l.setStock(stock);
        }
        return l;
    }

    public boolean eliminar(int id) {
        return repo.delete(id);
    }

    // ===== validaciones =====
    private void validarTexto(String campo, String v) {
        if (v == null || v.trim().isEmpty()) {
            throw new IllegalArgumentException("El " + campo + " es obligatorio.");
        }
        if (v.trim().length() > 120) {
            throw new IllegalArgumentException("El " + campo + " es demasiado largo (máx. 120).");
        }
    }

    private void validarPrecio(double p) {
        if (p < 0) throw new IllegalArgumentException("El precio no puede ser negativo.");
    }

    private void validarStock(int s) {
        if (s < 0) throw new IllegalArgumentException("El stock no puede ser negativo.");
    }
}
