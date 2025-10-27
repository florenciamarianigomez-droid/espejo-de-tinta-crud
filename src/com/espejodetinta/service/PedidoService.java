package com.espejodetinta.service;

import java.util.List;
import com.espejodetinta.model.*;
import com.espejodetinta.repository.InMemoryLibroRepository;
import com.espejodetinta.repository.InMemoryPedidoRepository;

public class PedidoService {
    private final InMemoryPedidoRepository repoPedidos;
    private final InMemoryLibroRepository repoLibros;

    public PedidoService(InMemoryPedidoRepository repoPedidos, InMemoryLibroRepository repoLibros) {
        this.repoPedidos = repoPedidos;
        this.repoLibros = repoLibros;
    }

    public Pedido crearPedido(List<int[]> items) throws IllegalArgumentException {
        Pedido pedido = repoPedidos.nuevoPedido();

        for (int[] item : items) {
            int id = item[0];
            int cantidad = item[1];
            Libro libro = repoLibros.findById(id);
            if (libro == null) {
                throw new IllegalArgumentException("No existe el libro con ID " + id);
            }
            if (cantidad <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
            }
            if (libro.getStock() < cantidad) {
                throw new IllegalArgumentException("Stock insuficiente para '" + libro.getTitulo() + "'");
            }
            libro.setStock(libro.getStock() - cantidad);
            pedido.agregarLinea(new LineaPedido(libro, cantidad));
        }

        return repoPedidos.save(pedido);
    }

    public List<Pedido> listarPedidos() {
        return repoPedidos.findAll();
    }
}
