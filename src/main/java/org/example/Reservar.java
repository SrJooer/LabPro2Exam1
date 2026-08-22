
package org.example;

public interface Reservar {
    boolean reservar(String usuario);
    void cancelarReserva();
    boolean hayReservasPendientes();
    
    
}
