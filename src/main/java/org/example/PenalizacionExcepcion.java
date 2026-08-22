
package org.example;

import java.util.Calendar;
import org.example.usuarios.Usuario;

public class PenalizacionExcepcion extends Excepciones{

    public PenalizacionExcepcion(String nombreUsuario, Calendar penalizadoHasta) {
        super("El usuario \"" + nombreUsuario + "\" esta penalizado hasta:  " 
                + penalizadoHasta.getTime() 
                + " no puede solicitar más préstamos.");
    }
    
}
