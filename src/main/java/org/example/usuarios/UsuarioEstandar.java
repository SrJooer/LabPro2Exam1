package org.example.usuarios;

import org.example.NivelComplejidad;

/**
 * Inciso 5 - Perfil estandar: limite reducido, no puede reservar material que
 * otro tiene prestado y no accede al nivel de complejidad mas alto.
 */
public class UsuarioEstandar extends Usuario {

    private static final String TIPO_PERFIL = "Estandar";
    private static final int LIMITE_PRESTAMOS = 3;

    public UsuarioEstandar(String id, String nombre) {
        super(id, nombre);
    }

    @Override
    public int getLimitePrestamos() {
        return LIMITE_PRESTAMOS;
    }

    @Override
    public boolean puedeReservar() {
        return false;
    }

    @Override
    public boolean puedeAcceder(NivelComplejidad nivel) {
        return !nivel.necesitaAutorizacion();
    }

    @Override
    public String getTipoPerfil() {
        return TIPO_PERFIL;
    }
}
