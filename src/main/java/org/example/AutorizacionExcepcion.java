
package org.example;

public class AutorizacionExcepcion extends Excepciones{

    public AutorizacionExcepcion(String titulo) {
        super("El material: "+titulo+" requiere autorización");
    }
    
}
