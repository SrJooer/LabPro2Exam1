package org.example.usuarios;

import org.example.DatosNulosExcepcion;
import org.example.MaterialBibliografico;
import org.example.NivelComplejidad;
import org.example.prestamos.Prestamo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Usuario {

    private final String id;
    private String nombre;
    private final List<MaterialBibliografico> prestamosActivos = new ArrayList<>();
    private final List<Prestamo> historial = new ArrayList<>();
    private LocalDate penalizadoHasta;

    protected Usuario(String id, String nombre) throws DatosNulosExcepcion {
        if (id == null || nombre == null) {
            throw new DatosNulosExcepcion();
        }
        if (id.isBlank() || nombre.isBlank()) {
            throw new IllegalArgumentException("Los datos no pueden estar vacios");
        }
        this.id = id;
        this.nombre = nombre;
    }

    public abstract int getLimitePrestamos();

    public abstract boolean puedeReservar();

    public abstract boolean puedeAcceder(NivelComplejidad nivel);

    public abstract String getTipoPerfil();

    public boolean haAlcanzadoLimite() {
        return prestamosActivos.size() >= getLimitePrestamos();
    }

    public int getPrestamosDisponibles() {
        return Math.max(0, getLimitePrestamos() - prestamosActivos.size());
    }

    public boolean tienePrestado(MaterialBibliografico material) {
        return prestamosActivos.contains(material);
    }

    public void registrarPrestamo(Prestamo prestamo) {
        Objects.requireNonNull(prestamo, "El prestamo no puede ser nulo");
        prestamosActivos.add(prestamo.getMaterial());
        historial.add(prestamo);
    }

    public boolean registrarDevolucion(Prestamo prestamo) {
        Objects.requireNonNull(prestamo, "El prestamo no puede ser nulo");
        return prestamosActivos.remove(prestamo.getMaterial());
    }

    public Prestamo buscarPrestamoActivo(MaterialBibliografico material) {
        for (Prestamo prestamo : historial) {
            if (prestamo.estaActivo() && prestamo.getMaterial().equals(material)) {
                return prestamo;
            }
        }
        return null;
    }

    public List<MaterialBibliografico> getPrestamos() {
        return Collections.unmodifiableList(prestamosActivos);
    }

    public List<Prestamo> getHistorial() {
        return Collections.unmodifiableList(historial);
    }

    public boolean estaPenalizado(LocalDate fecha) {
        return penalizadoHasta != null && !fecha.isAfter(penalizadoHasta);
    }

    public void aplicarPenalizacion(LocalDate desde, int dias) {
        if (dias <= 0) {
            return;
        }
        LocalDate inicio = (penalizadoHasta != null && penalizadoHasta.isAfter(desde))
                ? penalizadoHasta
                : desde;
        penalizadoHasta = inicio.plusDays(dias);
    }

    public int getDiasPenalizacionRestantes(LocalDate fecha) {
        if (!estaPenalizado(fecha)) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(fecha, penalizadoHasta) + 1;
    }

    public LocalDate getPenalizadoHasta() {
        return penalizadoHasta;
    }

    public void quitarPenalizacion() {
        this.penalizadoHasta = null;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del usuario esta vacio");
        }
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Usuario otro)) {
            return false;
        }
        return id.equals(otro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return nombre + " (" + id + ") - " + getTipoPerfil()
                + " [" + prestamosActivos.size() + "/" + getLimitePrestamos() + " prestamos]";
    }
}
