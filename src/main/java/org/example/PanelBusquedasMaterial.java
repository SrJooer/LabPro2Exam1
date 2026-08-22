package org.example;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelBusquedasMaterial extends JPanel {

    private final JTextField textoBusqueda;
    private final JComboBox<String> comboTipoBusqueda;
    private final JTable tablaResultados;
    private final DefaultTableModel modeloTabla;
    private final JLabel etiquetaEstado;

    public PanelBusquedasMaterial() {
        setLayout(new BorderLayout(15,15));
        setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        JPanel panelControles = new JPanel(new GridBagLayout());
        panelControles.setBorder(BorderFactory.createTitledBorder("Criterios de Búsqueda"));
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.insets = new Insets(8, 8, 8, 8);
        restricciones.fill = GridBagConstraints.HORIZONTAL;

        restricciones.gridx = 0; restricciones.gridy = 0;
        panelControles.add(new JLabel("Término a buscar:"), restricciones);
        restricciones.gridx = 1; restricciones.gridy = 0;
        textoBusqueda = new JTextField(20);
        panelControles.add(textoBusqueda, restricciones);

        restricciones.gridx = 0; restricciones.gridy = 1;
        panelControles.add(new JLabel("Tipo de Búsqueda:"), restricciones);
        restricciones.gridx = 1; restricciones.gridy = 1;
        comboTipoBusqueda = new JComboBox<>(new String[]{"Búsqueda Exacta (Código/Título)", "Búsqueda Flexible (Aproximada)"});
        panelControles.add(comboTipoBusqueda, restricciones);

        restricciones.gridx = 0; restricciones.gridy = 2; restricciones.gridwidth = 2;
        JButton botonBuscar = new JButton("Buscar Material");
        botonBuscar.setFont(new Font("SansSerif", Font.BOLD, 12));
        botonBuscar.addActionListener(e -> ejecutarBusqueda());
        panelControles.add(botonBuscar, restricciones);

        add(panelControles, BorderLayout.NORTH);

        JPanel panelResultados = new JPanel(new BorderLayout(5, 5));
        panelResultados.setBorder(BorderFactory.createTitledBorder("Resultados de la Búsqueda"));

        String[] columnas = {"Código", "Título", "Tipo", "Complejidad", "Coincidencia"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaResultados = new JTable(modeloTabla);
        JScrollPane panelDesplazamientoTabla = new JScrollPane(tablaResultados);

        etiquetaEstado = new JLabel(" Ingrese un término para realizar la búsqueda.", SwingConstants.LEFT);
        etiquetaEstado.setFont(new Font("SansSerif", Font.ITALIC, 12));

        panelResultados.add(panelDesplazamientoTabla, BorderLayout.CENTER);
        panelResultados.add(etiquetaEstado, BorderLayout.SOUTH);

        add(panelResultados, BorderLayout.CENTER);
    }
    private void ejecutarBusqueda() {
        String termino = textoBusqueda.getText().trim();
        String tipoBusqueda = (String) comboTipoBusqueda.getSelectedItem();

        if (termino.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un término de búsqueda.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        modeloTabla.setRowCount(0);
        etiquetaEstado.setText(" Búsqueda ejecutada para: '" + termino + "' usando " + tipoBusqueda);
    }
}
