package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PanelFiltrosOrdenamiento extends JPanel {

    private final Biblioteca biblioteca;
    private final JComboBox<String> comboFiltroTipo;
    private final JComboBox<String> comboCriterioOrden;
    private final JComboBox<String> comboSentidoOrden;
    private final DefaultTableModel modeloTabla;

    public PanelFiltrosOrdenamiento(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelControles = new JPanel(new GridBagLayout());
        panelControles.setBorder(BorderFactory.createTitledBorder("Filtros y Criterios de Ordenamiento"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelControles.add(new JLabel("Filtrar por Tipo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        comboFiltroTipo = new JComboBox<>(new String[]{"Todos", "Libro", "Revista", "Audiovisual"});
        comboFiltroTipo.addActionListener(e -> aplicarFiltroYOrden());
        panelControles.add(comboFiltroTipo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelControles.add(new JLabel("Ordenar por:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        comboCriterioOrden = new JComboBox<>(new String[]{"Código", "Título", "Días Préstamo Base"});
        comboCriterioOrden.addActionListener(e -> aplicarFiltroYOrden());
        panelControles.add(comboCriterioOrden, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelControles.add(new JLabel("Sentido:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        comboSentidoOrden = new JComboBox<>(new String[]{"Ascendente", "Descendente"});
        comboSentidoOrden.addActionListener(e -> aplicarFiltroYOrden());
        panelControles.add(comboSentidoOrden, gbc);

        add(panelControles, BorderLayout.NORTH);

        String[] columnas = {"Código", "Título", "Tipo", "Complejidad", "Días Préstamo Base", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        JTable tablaMateriales = new JTable(modeloTabla);
        add(new JScrollPane(tablaMateriales), BorderLayout.CENTER);

        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && isShowing()) {
                aplicarFiltroYOrden();
            }
        });

        aplicarFiltroYOrden();
    }

    public void aplicarFiltroYOrden() {
        modeloTabla.setRowCount(0);
        List<MaterialBibliografico> listaProcesada = new ArrayList<>(biblioteca.getCatalogo());

        String tipoSeleccionado = (String) comboFiltroTipo.getSelectedItem();
        if (tipoSeleccionado != null && !tipoSeleccionado.equals("Todos")) {
            listaProcesada.removeIf(m -> {
                if (tipoSeleccionado.equals("Libro")) return !(m instanceof Libros);
                if (tipoSeleccionado.equals("Revista")) return !(m instanceof Revistas);
                if (tipoSeleccionado.equals("Audiovisual")) return !(m instanceof Audiovisuales);
                return false;
            });
        }

        String criterio = (String) comboCriterioOrden.getSelectedItem();
        boolean ascendente = "Ascendente".equals(comboSentidoOrden.getSelectedItem());

        Comparator<MaterialBibliografico> comparador = switch (criterio != null ? criterio : "Código") {
            case "Título" -> Comparator.comparing(m -> m.getTitulo().toLowerCase());
            case "Días Préstamo Base" -> Comparator.comparingInt(MaterialBibliografico::getDiasMaximo);
            default -> Comparator.comparingInt(MaterialBibliografico::getCodigo);
        };

        if (!ascendente) {
            comparador = comparador.reversed();
        }

        listaProcesada.sort(comparador);

        for (MaterialBibliografico m : listaProcesada) {
            String tipo = "Desconocido";
            if (m instanceof Libros) tipo = "Libro";
            else if (m instanceof Revistas) tipo = "Revista";
            else if (m instanceof Audiovisuales) tipo = "Audiovisual";

            modeloTabla.addRow(new Object[]{
                    m.getCodigo(),
                    m.getTitulo(),
                    tipo,
                    m.getNivel().name(),
                    m.getDiasMaximo(),
                    m.disponible() ? "Disponible" : "Prestado"
            });
        }
    }
}