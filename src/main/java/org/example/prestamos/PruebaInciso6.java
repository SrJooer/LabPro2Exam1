package org.example.prestamos;

import org.example.DatosNulosExcepcion;
import org.example.MaterialBibliografico;
import org.example.NivelComplejidad;
import org.example.usuarios.Usuario;
import org.example.usuarios.UsuarioEstandar;
import org.example.usuarios.UsuarioPremium;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PruebaInciso6 {

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

    public static void main(String[] args) throws DatosNulosExcepcion {
        LocalDate hoy = LocalDate.of(2026, 8, 22);

        Usuario ana = new UsuarioEstandar("U-001", "Ana Lopez");
        Usuario beto = new UsuarioPremium("U-002", "Beto Cruz", 2);

        System.out.println("=== Perfiles ===");
        for (Usuario u : List.of(ana, beto)) {
            System.out.println("  " + u + " | reserva=" + u.puedeReservar()
                    + " | acceso Alto=" + u.puedeAcceder(NivelComplejidad.Alto));
        }

        MaterialBibliografico redes = new MaterialDePrueba("Redes de Computadoras", 101, 14, NivelComplejidad.Medio);
        Prestamo p1 = new Prestamo(ana, redes, LocalDate.of(2026, 8, 1));
        ana.registrarPrestamo(p1);

        System.out.println("\n=== Prestamo 1 ===");
        System.out.println("  14 dias base + 3 por complejidad Medio = " + redes.calcularDias());
        System.out.println("  prestado " + p1.getFechaPrestamo() + ", vence " + p1.getFechaPrevistaDevolucion());
        System.out.println("  vencido al " + hoy + "? " + p1.estaVencido(hoy)
                + " (" + p1.getDiasRetraso(hoy) + " dias de retraso)");

        LocalDate devolucion = LocalDate.of(2026, 8, 23);
        p1.registrarDevolucion(devolucion);
        ana.registrarDevolucion(p1);
        int castigo = p1.getDiasCastigo(hoy);
        ana.aplicarPenalizacion(devolucion, castigo);

        System.out.println("\n=== Devolucion tardia ===");
        System.out.println("  devuelto el " + devolucion + ", tarde? " + p1.seDevolvioTarde());
        System.out.println("  " + p1.getDiasRetraso(hoy) + " dias x2 = " + castigo + " dias de castigo");
        System.out.println("  penalizado hasta " + ana.getPenalizadoHasta());
        System.out.println("  bloqueado el " + devolucion + "? " + ana.estaPenalizado(devolucion));
        System.out.println("  bloqueado el 2026-09-05? " + ana.estaPenalizado(LocalDate.of(2026, 9, 5)));

        MaterialBibliografico calculo = new MaterialDePrueba("Calculo I", 102, 10, NivelComplejidad.Bajo);
        Prestamo p2 = new Prestamo(ana, calculo, LocalDate.of(2026, 7, 1));
        ana.registrarPrestamo(p2);
        p2.registrarDevolucion(LocalDate.of(2026, 7, 14));
        ana.registrarDevolucion(p2);

        System.out.println("\n=== Calculo recursivo sobre el historial ===");
        System.out.println("  prestamos en el historial: " + ana.getHistorial().size());
        for (Prestamo p : CalculadoraPenalizaciones.prestamosConRetraso(ana, hoy)) {
            System.out.println("    - " + p.getMaterial().getTitulo() + ": "
                    + p.getDiasRetraso(hoy) + " dias tarde -> " + p.getDiasCastigo(hoy) + " de castigo");
        }
        System.out.println("  TOTAL = " + CalculadoraPenalizaciones.totalDiasPenalizacion(ana, hoy) + " dias");

        List<Prestamo> ordenados = new ArrayList<>(ana.getHistorial());
        Collections.sort(ordenados);
        System.out.println("\n=== Ordenados por fecha de vencimiento ===");
        for (Prestamo p : ordenados) {
            System.out.println("  " + p.getFechaPrevistaDevolucion() + "  " + p.getMaterial().getTitulo());
        }

        System.out.print("\nModificar la lista de prestamos desde fuera: ");
        try {
            ana.getPrestamos().add(redes);
            System.out.println("SE PUDO (mal)");
        } catch (UnsupportedOperationException e) {
            System.out.println("bloqueado correctamente");
        }
    }
}
