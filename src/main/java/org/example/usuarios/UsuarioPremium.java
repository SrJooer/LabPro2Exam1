package org.example.usuarios;

import org.example.NivelComplejidad;

/**
 * Inciso 5 - Perfil premium: limite mayor (y que ademas crece con la antiguedad,
 * para que la sobrescritura no sea una simple constante distinta), puede reservar
 * y accede a cualquier nivel de complejidad.
 */
public class UsuarioPremium extends Usuario {

    private static final String TIPO_PERFIL = "Premium";
    private static final int LIMITE_BASE = 6;
    private static final int LIMITE_MAXIMO = 10;

    private int aniosAntiguedad;

    public UsuarioPremium(String id, String nombre) {
        this(id, nombre, 0);
    }

    public UsuarioPremium(String id, String nombre, int aniosAntiguedad) {
        super(id, nombre);
        setAniosAntiguedad(aniosAntiguedad);
    }

    /** Calculo propio: base mas un prestamo por año de antiguedad, con tope. */
    @Override
    public int getLimitePrestamos() {
        return Math.min(LIMITE_BASE + aniosAntiguedad, LIMITE_MAXIMO);
    }

    @Override
    public boolean puedeReservar() {
        return true;
    }

    @Override
    public boolean puedeAcceder(NivelComplejidad nivel) {
        return true;
    }

    @Override
    public String getTipoPerfil() {
        return TIPO_PERFIL;
    }

    public int getAniosAntiguedad() {
        return aniosAntiguedad;
    }

    public final void setAniosAntiguedad(int aniosAntiguedad) {
        if (aniosAntiguedad < 0) {
            throw new IllegalArgumentException("La antiguedad no puede ser negativa");
        }
        this.aniosAntiguedad = aniosAntiguedad;
    }
}
