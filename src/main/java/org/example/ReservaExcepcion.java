package org.example;

public class ReservaExcepcion extends Excepciones {

    public ReservaExcepcion(String motivo) {
        super("No se pudo registrar la reserva: " + motivo);
    }
}
