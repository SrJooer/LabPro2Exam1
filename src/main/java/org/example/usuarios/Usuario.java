package org.example.usuarios;

import org.example.MaterialBibliografico;
import org.example.NivelComplejidad;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public abstract class Usuario {

    private final String id;
    private String nombre;

    private final List<MaterialBibliografico> prestamosActivos = new ArrayList<>();

    private Calendar penalizadoHasta;

    protected Usuario(String id, String nombre) {
        if (id == null || nombre == null) {
            throw new IllegalArgumentException("Los datos no pueden ser nulos");
        }

        if (id.isEmpty() || nombre.isEmpty()) {
            throw new IllegalArgumentException("Los datos no pueden estar vacios");
        }
        this.id = id;
        this.nombre = nombre;
    }

    public abstract int getLimitePrestamos();
    public abstract boolean puedeReservar();
    public abstract boolean puedeAcceder(NivelComplejidad nivel);
    public abstract String getTipoPerfil();

    public boolean haAlcanzadoLimite() { return prestamosActivos.size() >= getLimitePrestamos(); }

    public int getPrestamosDisponibles() {
        return Math.max(0, getLimitePrestamos() - prestamosActivos.size());
    }

    public boolean tienePrestado(MaterialBibliografico prestamo) {
        return prestamosActivos.contains(prestamo);
    }

    public void registrarPrestamo(MaterialBibliografico prestamo) { prestamosActivos.add(prestamo); }

    public boolean registrarDevolucion(MaterialBibliografico prestamo) {
        return prestamosActivos.remove(prestamo);
    }

    public List<MaterialBibliografico> getPrestamos() {
        return prestamosActivos;
    }

    // Penalizaciones

    public boolean estaPenalizado(Calendar fecha) {
        return penalizadoHasta != null && fecha.after(penalizadoHasta);
    }

    public void aplicarPenalizacion(int dias) {
        Calendar fechaPenalizacion = Calendar.getInstance();
        fechaPenalizacion.add(Calendar.DAY_OF_MONTH, dias);
        penalizadoHasta = fechaPenalizacion;
    }

    public Calendar getPenalizadoHasta() { return penalizadoHasta; }
    public void quitarPenalizacion() { this.penalizadoHasta = null; }

    public String getId() { return id; }
    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del usuario está vacío");
        }
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario otro)) return false;
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
