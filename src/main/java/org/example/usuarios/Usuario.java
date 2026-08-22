package org.example.usuarios;
import org.example.DatosNulosExcepcion;
import org.example.MaterialBibliografico;
import org.example.NivelComplejidad;
import org.example.prestamos.Prestamo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Inciso 5 - Clase base de la jerarquia de usuarios.
 *
 * El usuario guarda su estado y responde preguntas sobre si mismo.
 * No decide si un prestamo es valido ni lanza excepciones de negocio:
 * de eso se encarga el servicio central, que consulta estos metodos.
 */
public abstract class Usuario {

    private final String id;
    private String nombre;

    /** Materiales que el usuario tiene ahora mismo en su poder. */
    private final List<MaterialBibliografico> prestamosActivos = new ArrayList<>();

    /** Todos los prestamos que ha hecho, devueltos o no (inciso 6). */
    private final List<Prestamo> historial = new ArrayList<>();

    /** Fecha hasta la que esta penalizado, inclusive. null = sin penalizacion. */
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

    // ------------------------------------------------------------------
    // Prestamos
    // ------------------------------------------------------------------

    public boolean haAlcanzadoLimite() { return prestamosActivos.size() >= getLimitePrestamos(); }

    public int getPrestamosDisponibles() {
        return Math.max(0, getLimitePrestamos() - prestamosActivos.size());
    }

    public boolean tienePrestado(MaterialBibliografico material) {
        return prestamosActivos.contains(material);
    }

    /** Lo llama el servicio despues de validar el prestamo. */
    public void registrarPrestamo(Prestamo prestamo) {
        Objects.requireNonNull(prestamo, "El prestamo no puede ser nulo");
        prestamosActivos.add(prestamo.getMaterial());
        historial.add(prestamo);
    }

    /** Lo llama el servicio al procesar la devolucion. El prestamo queda en el historial. */
    public boolean registrarDevolucion(Prestamo prestamo) {
        Objects.requireNonNull(prestamo, "El prestamo no puede ser nulo");
        return prestamosActivos.remove(prestamo.getMaterial());
    }

    /** Solo lectura: nadie de fuera puede meter prestamos saltandose al servicio. */
    public List<MaterialBibliografico> getPrestamos() {
        return Collections.unmodifiableList(prestamosActivos);
    }

<<<<<<< HEAD
    

    public boolean estaPenalizado(Calendar fecha) {
        return penalizadoHasta != null && fecha.after(penalizadoHasta);
=======
    public List<Prestamo> getHistorial() {
        return Collections.unmodifiableList(historial);
>>>>>>> 0e1580c (Bugs y prestamo)
    }

    /** Prestamo activo de ese material, o null si no lo tiene. */
    public Prestamo buscarPrestamoActivo(MaterialBibliografico material) {
        for (Prestamo prestamo : historial) {
            if (prestamo.estaActivo() && prestamo.getMaterial().equals(material)) {
                return prestamo;
            }
        }
        return null;
    }

    // ------------------------------------------------------------------
    // Penalizaciones (inciso 6)
    // ------------------------------------------------------------------

    /** Esta penalizado si la fecha todavia no ha pasado el ultimo dia de castigo. */
    public boolean estaPenalizado(LocalDate fecha) {
        return penalizadoHasta != null && !fecha.isAfter(penalizadoHasta);
    }

    /**
     * Suma dias de castigo. Si ya arrastraba una penalizacion vigente, los dias
     * nuevos se encadenan al final de la anterior en vez de reemplazarla.
     */
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
        return (int) java.time.temporal.ChronoUnit.DAYS.between(fecha, penalizadoHasta) + 1;
    }

    public LocalDate getPenalizadoHasta() { return penalizadoHasta; }

    public void quitarPenalizacion() { this.penalizadoHasta = null; }

    // ------------------------------------------------------------------

    public String getId() { return id; }
    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del usuario esta vacio");
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
