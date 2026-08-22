package org.example.usuarios;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelClientes extends JPanel {
    private final JTextField textoIdUsuario;
    private final JTextField textoNombreUsuario;
    private final JComboBox<String> comboPerfilUsuario;
    private final JTable tablaUsuarios;
    private final DefaultTableModel modeloTablaUsuarios;

    public PanelClientes() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Registrar Nuevo Usuario"));
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.insets = new Insets(8, 8, 8, 8);
        restricciones.fill = GridBagConstraints.HORIZONTAL;

        restricciones.gridx = 0;
        restricciones.gridy = 0;
        panelFormulario.add(new JLabel("ID Usuario:"), restricciones);
        restricciones.gridx = 1;
        restricciones.gridy = 0;
        textoIdUsuario = new JTextField(15);
        panelFormulario.add(textoIdUsuario, restricciones);

        restricciones.gridx = 0;
        restricciones.gridy = 1;
        panelFormulario.add(new JLabel("Nombre Completo:"), restricciones);
        restricciones.gridx = 1;
        restricciones.gridy = 1;
        textoNombreUsuario = new JTextField(15);
        panelFormulario.add(textoNombreUsuario, restricciones);

        restricciones.gridx = 0;
        restricciones.gridy = 2;
        panelFormulario.add(new JLabel("Perfil:"), restricciones);
        restricciones.gridx = 1;
        restricciones.gridy = 2;
        comboPerfilUsuario = new JComboBox<>(new String[]{"Estándar", "Premium"});
        panelFormulario.add(comboPerfilUsuario, restricciones);

        restricciones.gridx = 0;
        restricciones.gridy = 3;
        restricciones.gridwidth = 2;
        JButton botonRegistrar = new JButton("Registrar Usuario");
            botonRegistrar.setFont(new Font("SansSerif", Font.BOLD, 12));
            botonRegistrar.addActionListener(e -> registrarUsuario());
            panelFormulario.add(botonRegistrar, restricciones);

        add(panelFormulario, BorderLayout.WEST);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Usuarios Registrados"));

        String[] columnas = {"ID", "Nombre", "Perfil", "Límite Préstamos", "Préstamos Activos", "Penalizado"};
            modeloTablaUsuarios = new DefaultTableModel(columnas, 0);
            tablaUsuarios = new JTable(modeloTablaUsuarios);
            JScrollPane panelDesplazamientoTabla = new JScrollPane(tablaUsuarios);

        panelTabla.add(panelDesplazamientoTabla, BorderLayout.CENTER);
        add(panelTabla, BorderLayout.CENTER);
    }

    private void registrarUsuario() {
        String id = textoIdUsuario.getText().trim();
        String nombre = textoNombreUsuario.getText().trim();
        String perfil = (String) comboPerfilUsuario.getSelectedItem();

        if (id.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos del usuario.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String limitePrestamos = perfil.equals("Estándar") ? "2" : "5";

        modeloTablaUsuarios.addRow(new Object[]{id, nombre, perfil, limitePrestamos, "0", "No"});

        JOptionPane.showMessageDialog(this, "Usuario " + perfil + " registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        textoIdUsuario.setText("");
        textoNombreUsuario.setText("");
    }
}
