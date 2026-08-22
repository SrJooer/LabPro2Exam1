package org.example.prestamos;

import org.example.MaterialBibliografico;
import org.example.NivelComplejidad;
import org.example.usuarios.Usuario;
import org.example.usuarios.UsuarioEstandar;
import org.example.usuarios.UsuarioPremium;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Prueba por consola del inciso 6. Sirve para demostrar el funcionamiento sin
 * depender de la GUI ni del servicio central.
 */
public class PruebaInciso6 {

    /** Material concreto minimo, solo para poder probar. Las subclases reales son Libros, Revistas y Audiovisuales. */
    private static class MaterialDePrueba extends MaterialBibliografico {
        MaterialDePrueba(String titulo, int codigo, int diasMaximo, NivelComplejidad nivel) {
            super(titulo, codigo, diasMaximo, nivel, null);
        }

        @Override
        public String obtenerDescripcion() {
            return "Material \"" + titulo + "\" (" + nivel + ")";
        }

        @Override
        public int calcularDias() {
            return diasMaximo + nivel.getDiasAdicionales();
        }
    }

    public static void main(String[] args) {
        LocalDate hoy = LocalDate.of(2026, 8, 22);

        Usuario ana = new UsuarioEstandar("U-001", "Ana Lopez");
        Usuario beto = new UsuarioPremium("U-002", "Beto Cruz", 2);

        System.out.println("=== Perfiles ===");
        for (Usuario u : List.of(ana, beto)) {
            System.out.println("  " + u + " | reserva=" + u.puedeReservar()
                    + " | acceso ALTO=" + u.puedeAcceder(NivelComplejidad.Alto));
        }

        // --- La fecha prevista sale de calcularDias(), que ya suma la complejidad ---
        MaterialBibliografico redes = new MaterialDePrueba("Redes de Computadoras", 101, 14, NivelComplejidad.Medio);
        LocalDate inicio = LocalDate.of(2026, 8, 1);
        Prestamo p1 = new Prestamo(ana, redes, inicio);
        ana.registrarPrestamo(p1);

        System.out.println("\n=== Prestamo 1 ===");
        System.out.println("  dias base 14 + complejidad Medio (+3) = " + redes.calcularDias() + " dias");
        System.out.println("  prestado " + p1.getFechaPrestamo() + ", vence " + p1.getFechaPrevistaDevolucion());
        System.out.println("  vencido al " + hoy + "? " + p1.estaVencido(hoy)
                + " (lleva " + p1.getDiasRetraso(hoy) + " dias de retraso)");

        // --- Devolucion tardia: genera castigo ---
        LocalDate devolucion = LocalDate.of(2026, 8, 23);
        p1.registrarDevolucion(devolucion);
        ana.registrarDevolucion(p1);
        int castigo = p1.getDiasCastigo(hoy);
        ana.aplicarPenalizacion(devolucion, castigo);

        System.out.println("\n=== Devolucion tardia ===");
        System.out.println("  devuelto el " + devolucion + " -> tarde? " + p1.seDevolvioTarde());
        System.out.println("  " + p1.getDiasRetraso(hoy) + " dias de retraso x2 = " + castigo + " dias de castigo");
        System.out.println("  penalizado hasta " + ana.getPenalizadoHasta());
        System.out.println("  bloqueado el " + devolucion + "? " + ana.estaPenalizado(devolucion));
        System.out.println("  bloqueado el 2026-09-05? " + ana.estaPenalizado(LocalDate.of(2026, 9, 5)));

        // --- Segundo prestamo tambien tardio: la penalizacion se encadena ---
        MaterialBibliografico calculo = new MaterialDePrueba("Calculo I", 102, 10, NivelComplejidad.Bajo);
        Prestamo p2 = new Prestamo(ana, calculo, LocalDate.of(2026, 7, 1));
        ana.registrarPrestamo(p2);
        p2.registrarDevolucion(LocalDate.of(2026, 7, 14));
        ana.registrarDevolucion(p2);

        System.out.println("\n=== Calculo RECURSIVO sobre el historial ===");
        System.out.println("  prestamos en el historial: " + ana.getHistorial().size());
        for (Prestamo p : CalculadoraPenalizaciones.prestamosConRetraso(ana, hoy)) {
            System.out.println("    - " + p.getMaterial().getTitulo() + ": "
                    + p.getDiasRetraso(hoy) + " dias tarde -> " + p.getDiasCastigo(hoy) + " de castigo");
        }
        System.out.println("  TOTAL acumulado = " + CalculadoraPenalizaciones.totalDiasPenalizacion(ana, hoy) + " dias");

        // --- Orden natural por fecha de vencimiento (sirve al inciso 14) ---
        List<Prestamo> ordenados = new ArrayList<>(ana.getHistorial());
        Collections.sort(ordenados);
        System.out.println("\n=== Ordenados por fecha de vencimiento ===");
        for (Prestamo p : ordenados) {
            System.out.println("  " + p.getFechaPrevistaDevolucion() + "  " + p.getMaterial().getTitulo());
        }

        // --- La lista de prestamos es de solo lectura ---
        System.out.print("\nIntentar modificar la lista de prestamos por fuera: ");
        try {
            ana.getPrestamos().add(redes);
            System.out.println("SE PUDO (mal)");
        } catch (UnsupportedOperationException e) {
            System.out.println("bloqueado correctamente");
        }
    }
}
