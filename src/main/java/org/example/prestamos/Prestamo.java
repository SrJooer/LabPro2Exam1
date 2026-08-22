package org.example.prestamos;

import org.example.MaterialBibliografico;
import org.example.usuarios.Usuario;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Prestamo implements Comparable<Prestamo> {

    public static final int DIAS_CASTIGO_POR_DIA_RETRASO = 2;

    private final Usuario usuario;
    private final MaterialBibliografico material;
    private final LocalDate fechaPrestamo;
    private final LocalDate fechaPrevistaDevolucion;
    private LocalDate fechaDevolucionReal;

    public Prestamo(Usuario usuario, MaterialBibliografico material, LocalDate fechaPrestamo) {
        this.usuario = Objects.requireNonNull(usuario, "El prestamo necesita un usuario");
        this.material = Objects.requireNonNull(material, "El prestamo necesita un material");
        this.fechaPrestamo = Objects.requireNonNull(fechaPrestamo, "El prestamo necesita una fecha");
        this.fechaPrevistaDevolucion = fechaPrestamo.plusDays(material.calcularDias());
    }

    public boolean estaDevuelto() {
        return fechaDevolucionReal != null;
    }

    public boolean estaActivo() {
        return fechaDevolucionReal == null;
    }

    public boolean estaVencido(LocalDate fechaReferencia) {
        return estaActivo() && fechaReferencia.isAfter(fechaPrevistaDevolucion);
    }

    public boolean seDevolvioTarde() {
        return estaDevuelto() && fechaDevolucionReal.isAfter(fechaPrevistaDevolucion);
    }

    public boolean venceEnLosProximos(LocalDate fechaReferencia, int diasAntelacion) {
        if (!estaActivo() || fechaReferencia.isAfter(fechaPrevistaDevolucion)) {
            return false;
        }
        return !fechaPrevistaDevolucion.isAfter(fechaReferencia.plusDays(diasAntelacion));
    }

    public int getDiasRetraso(LocalDate fechaReferencia) {
        LocalDate corte = estaDevuelto() ? fechaDevolucionReal : fechaReferencia;
        long dias = ChronoUnit.DAYS.between(fechaPrevistaDevolucion, corte);
        return dias > 0 ? (int) dias : 0;
    }

    public int getDiasCastigo(LocalDate fechaReferencia) {
        return getDiasRetraso(fechaReferencia) * DIAS_CASTIGO_POR_DIA_RETRASO;
    }

    public void registrarDevolucion(LocalDate fechaDevolucion) {
        Objects.requireNonNull(fechaDevolucion, "La fecha de devolucion no puede ser nula");
        if (estaDevuelto()) {
            throw new IllegalStateException(
                    "El prestamo de \"" + material.getTitulo() + "\" ya fue devuelto el " + fechaDevolucionReal);
        }
        if (fechaDevolucion.isBefore(fechaPrestamo)) {
            throw new IllegalArgumentException(
                    "No se puede devolver antes de la fecha del prestamo (" + fechaPrestamo + ")");
        }
        this.fechaDevolucionReal = fechaDevolucion;
    }

    @Override
    public int compareTo(Prestamo otro) {
        return this.fechaPrevistaDevolucion.compareTo(otro.fechaPrevistaDevolucion);
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public MaterialBibliografico getMaterial() {
        return material;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDate getFechaPrevistaDevolucion() {
        return fechaPrevistaDevolucion;
    }

    public LocalDate getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    @Override
    public String toString() {
        String estado = estaDevuelto() ? "devuelto el " + fechaDevolucionReal : "en prestamo";
        return usuario.getNombre() + " -> \"" + material.getTitulo() + "\""
                + " | prestado " + fechaPrestamo
                + " | vence " + fechaPrevistaDevolucion
                + " | " + estado;
    }
}
