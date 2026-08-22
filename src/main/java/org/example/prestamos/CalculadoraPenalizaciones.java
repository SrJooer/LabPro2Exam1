package org.example.prestamos;

import org.example.usuarios.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class CalculadoraPenalizaciones {

    private CalculadoraPenalizaciones() {
    }

    public static int totalDiasPenalizacion(Usuario usuario, LocalDate fechaReferencia) {
        return sumarDesde(usuario.getHistorial(), 0, fechaReferencia);
    }

    private static int sumarDesde(List<Prestamo> historial, int indice, LocalDate fechaReferencia) {
        if (indice == historial.size()) {
            return 0;
        }
        int castigoDeEste = historial.get(indice).getDiasCastigo(fechaReferencia);
        return castigoDeEste + sumarDesde(historial, indice + 1, fechaReferencia);
    }

    public static List<Prestamo> prestamosConRetraso(Usuario usuario, LocalDate fechaReferencia) {
        List<Prestamo> acumulador = new ArrayList<>();
        recolectarDesde(usuario.getHistorial(), 0, fechaReferencia, acumulador);
        return acumulador;
    }

    private static void recolectarDesde(List<Prestamo> historial, int indice,
                                        LocalDate fechaReferencia, List<Prestamo> acumulador) {
        if (indice == historial.size()) {
            return;
        }
        Prestamo actual = historial.get(indice);
        if (actual.getDiasRetraso(fechaReferencia) > 0) {
            acumulador.add(actual);
        }
        recolectarDesde(historial, indice + 1, fechaReferencia, acumulador);
    }
}
