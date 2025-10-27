package com.espejodetinta.model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int id;
    private List<LineaPedido> lineas = new ArrayList<>();
    private double total;

    public Pedido(int id) {
        this.id = id;
    }

    public int getId() { return id; }
    public List<LineaPedido> getLineas() { return lineas; }
    public double getTotal() { return total; }

    public void agregarLinea(LineaPedido lp) {
        lineas.add(lp);
        recalcularTotal();
    }

    private void recalcularTotal() {
        total = 0;
        for (LineaPedido lp : lineas) {
            total += lp.getSubtotal();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido #").append(id)
          .append(" | Total: $").append(total).append("\n");
        for (LineaPedido lp : lineas) {
            sb.append("   - ").append(lp.toString()).append("\n");
        }
        return sb.toString();
    }
}
