package com.espejodetinta.model;

public class LineaPedido {
    private Libro libro;
    private int cantidad;

    public LineaPedido(Libro libro, int cantidad) {
        this.libro = libro;
        this.cantidad = cantidad;
    }

    public Libro getLibro() { return libro; }
    public int getCantidad() { return cantidad; }

    public double getSubtotal() {
        return libro.getPrecio() * cantidad;
    }

    @Override
    public String toString() {
        return libro.getTitulo() + " x" + cantidad + "  ($" + getSubtotal() + ")";
    }
}
