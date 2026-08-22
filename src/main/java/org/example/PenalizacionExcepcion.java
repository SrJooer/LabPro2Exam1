
package org.example;

import java.time.LocalDate;
import java.util.Calendar;
import org.example.usuarios.Usuario;

public class PenalizacionExcepcion extends Excepciones{

    public PenalizacionExcepcion(String nombreUsuario, LocalDate penalizadoHasta) {
        super("El usuario \"" + nombreUsuario + "\" esta penalizado hasta:  " 
                + penalizadoHasta
                + " no puede solicitar más préstamos.");
    }
    
}
