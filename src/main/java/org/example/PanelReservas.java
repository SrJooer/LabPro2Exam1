package org.example;

import org.example.usuarios.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.util.List;

public class PanelReservas extends JPanel {

    private final Biblioteca biblioteca;

    private final JComboBox<Usuario> comboUsuarios;
    private final JComboBox<MaterialBibliografico> comboMateriales;
    private final JLabel etiquetaEstadoMaterial;
    private final DefaultTableModel modeloCola;
    private final DefaultTableModel modeloReservasActivas;

    public PanelReservas(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Registrar Reserva"));
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.insets = new Insets(8, 8, 8, 8);
        restricciones.fill = GridBagConstraints.HORIZONTAL;

        restricciones.gridx = 0;
        restricciones.gridy = 0;
        panelFormulario.add(new JLabel("Usuario:"), restricciones);
        restricciones.gridx = 1;
        restricciones.gridy = 0;
        comboUsuarios = new JComboBox<>();
        comboUsuarios.setPreferredSize(new Dimension(220, 25));
        comboUsuarios.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice,
                                                          boolean seleccionado, boolean foco) {
                super.getListCellRendererComponent(lista, valor, indice, seleccionado, foco);
                if (valor instanceof Usuario usuario) {
                    setText(usuario.getNombre() + " (" + usuario.getTipoPerfil() + ")");
                }
                return this;
            }
        });
        panelFormulario.add(comboUsuarios, restricciones);

        restricciones.gridx = 0;
        restricciones.gridy = 1;
        panelFormulario.add(new JLabel("Material:"), restricciones);
        restricciones.gridx = 1;
        restricciones.gridy = 1;
        comboMateriales = new JComboBox<>();
        comboMateriales.setPreferredSize(new Dimension(220, 25));
        comboMateriales.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice,
                                                          boolean seleccionado, boolean foco) {
                super.getListCellRendererComponent(lista, valor, indice, seleccionado, foco);
                if (valor instanceof MaterialBibliografico material) {
                    setText(material.getCodigo() + " - " + material.getTitulo());
                }
                return this;
            }
        });
        comboMateriales.addActionListener(e -> actualizarDetalleMaterial());
        panelFormulario.add(comboMateriales, restricciones);

        restricciones.gridx = 0;
        restricciones.gridy = 2;
        restricciones.gridwidth = 2;
        etiquetaEstadoMaterial = new JLabel(" ");
        etiquetaEstadoMaterial.setFont(new Font("SansSerif", Font.BOLD, 13));
        panelFormulario.add(etiquetaEstadoMaterial, restricciones);

        restricciones.gridy = 3;
        JButton botonReservar = new JButton("Reservar Material");
        botonReservar.setFont(new Font("SansSerif", Font.BOLD, 12));
        botonReservar.addActionListener(e -> reservar());
        panelFormulario.add(botonReservar, restricciones);

        restricciones.gridy = 4;
        JButton botonCancelar = new JButton("Cancelar Reserva");
        botonCancelar.setFont(new Font("SansSerif", Font.PLAIN, 12));
        botonCancelar.addActionListener(e -> cancelar());
        panelFormulario.add(botonCancelar, restricciones);

        restricciones.gridy = 5;
        JButton botonActualizar = new JButton("Actualizar Listados");
        botonActualizar.setFont(new Font("SansSerif", Font.PLAIN, 12));
        botonActualizar.addActionListener(e -> refrescar());
        panelFormulario.add(botonActualizar, restricciones);

        add(panelFormulario, BorderLayout.WEST);

        JPanel panelCola = new JPanel(new BorderLayout());
        panelCola.setBorder(BorderFactory.createTitledBorder("Cola de Espera del Material Seleccionado"));
        String[] columnasCola = {"Turno", "ID Usuario", "Nombre", "Perfil"};
        modeloCola = new DefaultTableModel(columnasCola, 0);
        JTable tablaCola = new JTable(modeloCola);
        panelCola.add(new JScrollPane(tablaCola), BorderLayout.CENTER);
        add(panelCola, BorderLayout.CENTER);

        JPanel panelActivas = new JPanel(new BorderLayout());
        panelActivas.setBorder(BorderFactory.createTitledBorder("Materiales con Reservas Pendientes"));
        String[] columnasActivas = {"Código", "Material", "Estado", "En Cola", "Siguiente en la Fila"};
        modeloReservasActivas = new DefaultTableModel(columnasActivas, 0);
        JTable tablaActivas = new JTable(modeloReservasActivas);
        JScrollPane desplazamientoActivas = new JScrollPane(tablaActivas);
        desplazamientoActivas.setPreferredSize(new Dimension(800, 180));
        panelActivas.add(desplazamientoActivas, BorderLayout.CENTER);
        add(panelActivas, BorderLayout.SOUTH);

        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && isShowing()) {
                refrescar();
            }
        });

        refrescar();
    }

    public void cargarCombos() {
        Usuario usuarioSeleccionado = (Usuario) comboUsuarios.getSelectedItem();
        MaterialBibliografico materialSeleccionado = (MaterialBibliografico) comboMateriales.getSelectedItem();

        comboUsuarios.removeAllItems();
        for (Usuario usuario : biblioteca.getUsuarios()) {
            comboUsuarios.addItem(usuario);
        }
        if (usuarioSeleccionado != null) {
            comboUsuarios.setSelectedItem(usuarioSeleccionado);
        }

        comboMateriales.removeAllItems();
        for (MaterialBibliografico material : biblioteca.getCatalogo()) {
            comboMateriales.addItem(material);
        }
        if (materialSeleccionado != null) {
            comboMateriales.setSelectedItem(materialSeleccionado);
        }
    }

    private void reservar() {
        Usuario usuario = (Usuario) comboUsuarios.getSelectedItem();
        MaterialBibliografico material = (MaterialBibliografico) comboMateriales.getSelectedItem();

        if (usuario == null || material == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un usuario y un material.",
                    "Datos Incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            biblioteca.reservarMaterial(material, usuario);
            JOptionPane.showMessageDialog(this,
                    "Reserva registrada para " + usuario.getNombre() + ".",
                    "Reserva Confirmada",
                    JOptionPane.INFORMATION_MESSAGE);
            refrescar();
        } catch (ReservaExcepcion e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Reserva Rechazada", JOptionPane.ERROR_MESSAGE);
        } catch (Excepciones e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error de la Biblioteca", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelar() {
        Usuario usuario = (Usuario) comboUsuarios.getSelectedItem();
        MaterialBibliografico material = (MaterialBibliografico) comboMateriales.getSelectedItem();

        if (usuario == null || material == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un usuario y un material.",
                    "Datos Incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (biblioteca.cancelarReserva(material, usuario)) {
            JOptionPane.showMessageDialog(this,
                    "Reserva cancelada para " + usuario.getNombre() + ".",
                    "Reserva Cancelada",
                    JOptionPane.INFORMATION_MESSAGE);
            refrescar();
        } else {
            JOptionPane.showMessageDialog(this,
                    usuario.getNombre() + " no tiene ninguna reserva sobre \"" + material.getTitulo() + "\".",
                    "Sin Reserva",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public void refrescar() {
        cargarCombos();
        actualizarDetalleMaterial();
        actualizarTablaActivas();
    }

    private void actualizarDetalleMaterial() {
        modeloCola.setRowCount(0);

        MaterialBibliografico material = (MaterialBibliografico) comboMateriales.getSelectedItem();
        if (material == null) {
            etiquetaEstadoMaterial.setText(" ");
            return;
        }

        String estado = material.disponible() ? "DISPONIBLE" : "PRESTADO";
        List<Usuario> cola = biblioteca.getColaDeReservas(material);
        etiquetaEstadoMaterial.setText("Estado: " + estado + "   |   En cola: " + cola.size());

        int turno = 1;
        for (Usuario usuario : cola) {
            modeloCola.addRow(new Object[]{
                    turno,
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getTipoPerfil()
            });
            turno++;
        }
    }

    private void actualizarTablaActivas() {
        modeloReservasActivas.setRowCount(0);

        for (MaterialBibliografico material : biblioteca.getCatalogo()) {
            List<Usuario> cola = biblioteca.getColaDeReservas(material);
            if (cola.isEmpty()) {
                continue;
            }
            modeloReservasActivas.addRow(new Object[]{
                    material.getCodigo(),
                    material.getTitulo(),
                    material.disponible() ? "DISPONIBLE" : "PRESTADO",
                    cola.size(),
                    cola.get(0).getNombre()
            });
        }
    }
}