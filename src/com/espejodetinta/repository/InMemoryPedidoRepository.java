package com.espejodetinta.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.espejodetinta.model.Pedido;

public class InMemoryPedidoRepository {
    private final List<Pedido> pedidos = new ArrayList<>();
    private final AtomicInteger seq = new AtomicInteger(0);

    public Pedido save(Pedido pedido) {
        pedidos.add(pedido);
        return pedido;
    }

    public Pedido nuevoPedido() {
        int id = seq.incrementAndGet();
        Pedido p = new Pedido(id);
        pedidos.add(p);
        return p;
    }

    public List<Pedido> findAll() {
        return new ArrayList<>(pedidos);
    }
}
