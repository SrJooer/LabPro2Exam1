package org.example.usuarios;

import org.example.DatosNulosExcepcion;
import org.example.NivelComplejidad;

import javax.xml.crypto.Data;

public class UsuarioEstandar extends Usuario {

    private static final String TIPO_PERFIL = "estandar";
    private static final int LIMITE_PRESTAMOS = 0;

    public UsuarioEstandar(String id, String nombre) throws DatosNulosExcepcion {
        super(id, nombre);
    }

    @Override
    public int getLimitePrestamos() {
        return LIMITE_PRESTAMOS;
    }

    @Override
    public boolean puedeReservar() {
        return true;
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
