package com.espejodetinta.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.espejodetinta.model.Libro;
import com.espejodetinta.model.Pedido;
import com.espejodetinta.repository.InMemoryLibroRepository;
import com.espejodetinta.repository.InMemoryPedidoRepository;
import com.espejodetinta.service.LibroService;
import com.espejodetinta.service.PedidoService;
import com.espejodetinta.ui.Ansi;

public class ConsoleUI {
    private final Scanner in = new Scanner(System.in);
    private final InMemoryLibroRepository repoLibros = new InMemoryLibroRepository();
    private final LibroService libroService = new LibroService(repoLibros);
    private final PedidoService pedidoService = new PedidoService(new InMemoryPedidoRepository(), repoLibros);

    public void run() {
        String header = Ansi.BG_BEIGE + Ansi.VERDE + Ansi.BOLD + "  ¡Bienvenidx a Espejo de Tinta!  " + Ansi.RESET;
        System.out.println(header);
        boolean salir = false;

        do {
            imprimirMenu();
            int op = leerEntero("Elegí una opción: ");
            switch (op) {
                case 1 -> crearLibro();
                case 2 -> listarTodos();
                case 3 -> verPorId();
                case 4 -> actualizarLibro();
                case 5 -> eliminarLibro();
                case 6 -> crearPedido();
                case 7 -> listarPedidos();
                case 0 -> {
                    System.out.println(Ansi.VERDE + "¡Hasta luego!" + Ansi.RESET);
                    salir = true;
                }
                default -> System.out.println("Opción inválida.");
            }
        } while (!salir);
    }

    private void imprimirMenu() {
        System.out.println();
        System.out.println(Ansi.BEIGE + Ansi.BOLD + "Menú principal" + Ansi.RESET);
        System.out.println(Ansi.VERDE + "[1]" + Ansi.RESET + " Alta de libro");
        System.out.println(Ansi.VERDE + "[2]" + Ansi.RESET + " Listar libros");
        System.out.println(Ansi.VERDE + "[3]" + Ansi.RESET + " Ver libro por ID");
        System.out.println(Ansi.VERDE + "[4]" + Ansi.RESET + " Modificar libro");
        System.out.println(Ansi.VERDE + "[5]" + Ansi.RESET + " Eliminar libro");
        System.out.println(Ansi.VERDE + "[6]" + Ansi.RESET + " Crear pedido");
        System.out.println(Ansi.VERDE + "[7]" + Ansi.RESET + " Listar pedidos");
        System.out.println(Ansi.VERDE + "[0]" + Ansi.RESET + " Salir");
    }

    // ==== CRUD de libros (igual que antes) ====

    private void crearLibro() {
        System.out.println(Ansi.BEIGE + Ansi.BOLD + "Alta de libro" + Ansi.RESET);
        String titulo = leerTexto("Título: ");
        String autor  = leerTexto("Autor: ");
        double precio = leerDouble("Precio: ");
        int stock     = leerEntero("Stock: ");

        try {
            Libro creado = libroService.crear(titulo, autor, precio, stock);
            System.out.println(Ansi.VERDE + "Creado con ID " + creado.getId() + Ansi.RESET);
        } catch (IllegalArgumentException ex) {
            System.out.println("Error de validación: " + ex.getMessage());
        }
    }

    private void listarTodos() {
        System.out.println(Ansi.BEIGE + Ansi.BOLD + "Listado de libros" + Ansi.RESET);
        List<Libro> lista = libroService.listar();
        if (lista.isEmpty()) {
            System.out.println("No hay libros cargados.");
        } else {
            lista.forEach(lib -> System.out.println(formatear(lib)));
        }
    }

    private void verPorId() {
        int id = leerEntero("ID a consultar: ");
        Libro l = libroService.buscarPorId(id);
        if (l == null) {
            System.out.println("No existe el ID " + id);
        } else {
            System.out.println(formatear(l));
        }
    }

    private void actualizarLibro() {
        int id = leerEntero("ID a modificar: ");
        Libro existente = libroService.buscarPorId(id);
        if (existente == null) {
            System.out.println("No existe el ID " + id);
            return;
        }
        System.out.println("Dejá vacío para mantener el valor actual.");
        String titulo = leerTextoOpcional("Título (" + existente.getTitulo() + "): ");
        String autor  = leerTextoOpcional("Autor (" + existente.getAutor() + "): ");
        Double precio = leerDoubleOpcional("Precio (" + existente.getPrecio() + "): ");
        Integer stock = leerEnteroOpcional("Stock (" + existente.getStock() + "): ");

        try {
            Libro actualizado = libroService.actualizarParcial(
                id,
                titulo.isBlank() ? null : titulo,
                autor.isBlank()  ? null : autor,
                precio,
                stock
            );
            System.out.println(Ansi.VERDE + "Actualizado: " + formatear(actualizado) + Ansi.RESET);
        } catch (IllegalArgumentException ex) {
            System.out.println("Error de validación: " + ex.getMessage());
        }
    }

    private void eliminarLibro() {
        int id = leerEntero("ID a eliminar: ");
        boolean ok = libroService.eliminar(id);
        System.out.println(ok ? Ansi.VERDE + "Eliminado." + Ansi.RESET : "No existe el ID " + id);
    }

    private String formatear(Libro l) {
        return Ansi.VERDE + "#" + l.getId() + Ansi.RESET +
               " | " + l.getTitulo() + " — " + l.getAutor() +
               " | $" + l.getPrecio() + " | stock: " + l.getStock();
    }

    // ==== NUEVAS FUNCIONALIDADES: PEDIDOS ====

    private void crearPedido() {
        List<int[]> items = new ArrayList<>();
        System.out.println(Ansi.BEIGE + "Creación de pedido" + Ansi.RESET);
        while (true) {
            int id = leerEntero("ID del libro (0 para terminar): ");
            if (id == 0) break;
            int cantidad = leerEntero("Cantidad: ");
            items.add(new int[]{id, cantidad});
        }

        try {
            Pedido pedido = pedidoService.crearPedido(items);
            System.out.println(Ansi.VERDE + "Pedido creado correctamente:" + Ansi.RESET);
            System.out.println(pedido);
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void listarPedidos() {
        System.out.println(Ansi.BEIGE + Ansi.BOLD + "Listado de pedidos" + Ansi.RESET);
        List<Pedido> pedidos = pedidoService.listarPedidos();
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.");
        } else {
            pedidos.forEach(p -> System.out.println(p.toString()));
        }
    }

    // ===== util entrada =====
    private int leerEntero(String msg) {
        while (true) {
            System.out.print(msg);
            try {
                int val = Integer.parseInt(in.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Ingresá un entero válido.");
            }
        }
    }

    private Integer leerEnteroOpcional(String msg) {
        System.out.print(msg);
        String s = in.nextLine().trim();
        if (s.isBlank()) return null;
        try { return Integer.parseInt(s); }
        catch (NumberFormatException e) { System.out.println("Valor inválido; se ignora."); return null; }
    }

    private double leerDouble(String msg) {
        while (true) {
            System.out.print(msg);
            try {
                double val = Double.parseDouble(in.nextLine().trim().replace(",", "."));
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Ingresá un número válido (usa punto para decimales).");
            }
        }
    }

    private Double leerDoubleOpcional(String msg) {
        System.out.print(msg);
        String s = in.nextLine().trim().replace(",", ".");
        if (s.isBlank()) return null;
        try { return Double.parseDouble(s); }
        catch (NumberFormatException e) { System.out.println("Valor inválido; se ignora."); return null; }
    }

    private String leerTexto(String msg) {
        while (true) {
            System.out.print(msg);
            String s = in.nextLine().trim();
            if (!s.isBlank()) return s;
            System.out.println("No puede estar vacío.");
        }
    }

    private String leerTextoOpcional(String msg) {
        System.out.print(msg);
        return in.nextLine();
    }
}
