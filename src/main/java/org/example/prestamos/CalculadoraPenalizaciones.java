package org.example.prestamos;

import org.example.usuarios.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Inciso 6 - Calculo RECURSIVO de penalizaciones.
 *
 * Recorre el historial de prestamos de un usuario llamandose a si mismo con el
 * indice siguiente. No hay ningun bucle: el caso base es haber llegado al final
 * de la lista.
 */
public final class CalculadoraPenalizaciones {

    private CalculadoraPenalizaciones() {
        // clase de utilidad, no se instancia
    }

    /**
     * Suma todos los dias de castigo que ha acumulado el usuario a lo largo de
     * su historial: tanto los prestamos devueltos tarde como los que siguen
     * vencidos sin devolver.
     */
    public static int totalDiasPenalizacion(Usuario usuario, LocalDate fechaReferencia) {
        return sumarDesde(usuario.getHistorial(), 0, fechaReferencia);
    }

    private static int sumarDesde(List<Prestamo> historial, int indice, LocalDate fechaReferencia) {
        if (indice == historial.size()) {
            return 0;                                                  // caso base
        }
        int castigoDeEste = historial.get(indice).getDiasCastigo(fechaReferencia);
        return castigoDeEste + sumarDesde(historial, indice + 1, fechaReferencia);   // paso recursivo
    }

    /**
     * Lista de prestamos del usuario que acarrean castigo, acumulando resultados
     * a medida que la recursividad avanza.
     */
    public static List<Prestamo> prestamosConRetraso(Usuario usuario, LocalDate fechaReferencia) {
        List<Prestamo> acumulador = new ArrayList<>();
        recolectarDesde(usuario.getHistorial(), 0, fechaReferencia, acumulador);
        return acumulador;
    }

    private static void recolectarDesde(List<Prestamo> historial, int indice,
                                        LocalDate fechaReferencia, List<Prestamo> acumulador) {
        if (indice == historial.size()) {
            return;                                                    // caso base
        }
        Prestamo actual = historial.get(indice);
        if (actual.getDiasRetraso(fechaReferencia) > 0) {
            acumulador.add(actual);
        }
        recolectarDesde(historial, indice + 1, fechaReferencia, acumulador);   // paso recursivo
    }
}
