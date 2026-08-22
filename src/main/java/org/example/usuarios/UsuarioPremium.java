package org.example.usuarios;

import org.example.DatosNulosExcepcion;
import org.example.NivelComplejidad;

public class UsuarioPremium extends Usuario {

    private static final String TIPO_PERFIL = "Premium";
    private static final int LIMITE_BASE = 6;
    private static final int LIMITE_MAXIMO = 10;

    private int aniosAntiguedad;

    public UsuarioPremium(String id, String nombre) throws DatosNulosExcepcion {
        this(id, nombre, 0);
    }

    public UsuarioPremium(String id, String nombre, int aniosAntiguedad) throws DatosNulosExcepcion {
        super(id, nombre);
        setAniosAntiguedad(aniosAntiguedad);
    }

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
