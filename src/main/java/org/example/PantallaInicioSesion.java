package org.example;

import javax.swing.*;
import java.awt.*;

public class PantallaInicioSesion extends JFrame {

    private final JTextField labelIdEmpleado;
    private final GestionEmpleados gestionEmpleados;

    public PantallaInicioSesion(GestionEmpleados gestionEmpleados) {
        this.gestionEmpleados = gestionEmpleados;

        setTitle("Acceso de Personal");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelObjetos = new JPanel(new GridLayout(5, 1, 10, 10));
        panelObjetos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Sistema de Préstamos para Biblioteca", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        panelObjetos.add(titulo);

        JLabel inicioSesion = new JLabel("Inicio de Sesión de Empleados", SwingConstants.CENTER);
        inicioSesion.setFont(new Font("SansSerif", Font.BOLD, 18));
        panelObjetos.add(inicioSesion);

        JPanel panelInput = new JPanel(new BorderLayout(10, 0));
        panelInput.add(new JLabel("ID Empleado: "), BorderLayout.WEST);
        labelIdEmpleado = new JTextField();
        panelInput.add(labelIdEmpleado, BorderLayout.CENTER);
        panelObjetos.add(panelInput);

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 0));
        JButton btnIngresar = new JButton("Iniciar Sesión");
        btnIngresar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnIngresar.addActionListener(e -> intentarLogin());

        JButton btnRegistrar = new JButton("Nuevo Empleado");
        btnRegistrar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnRegistrar.addActionListener(e -> mostrarDialogoRegistro());

        panelBotones.add(btnIngresar);
        panelBotones.add(btnRegistrar);
        panelObjetos.add(panelBotones);

        add(panelObjetos);
        setVisible(true);
    }

    private void intentarLogin() {
        String id = labelIdEmpleado.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el ID de empleado.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!GestionEmpleados.IdValido(id)) {
            JOptionPane.showMessageDialog(this,
                    "El ID debe contener exactamente 6 dígitos numéricos.",
                    "Formato Inválido",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        EmpleadoAutorizado empleado = gestionEmpleados.verificar(id);

        if (empleado != null) {
            JOptionPane.showMessageDialog(this,
                    "Bienvenido(a), " + empleado.getNombre(),
                    "Ingreso Correcto",
                    JOptionPane.INFORMATION_MESSAGE);

            new MenuPrincipal(empleado);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Empleado no localizado.", "Error de Verificación", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoRegistro() {
        JTextField txtNuevoId = new JTextField();
        JTextField txtNuevoNombre = new JTextField();

        Object[] formulario = {
                "ID de Empleado:", txtNuevoId,
                "Nombre Completo:", txtNuevoNombre
        };

        int opcion = JOptionPane.showConfirmDialog(
                this,
                formulario,
                "Registrar Nuevo Empleado",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (opcion == JOptionPane.OK_OPTION) {
            String id = txtNuevoId.getText().trim();
            String nombre = txtNuevoNombre.getText().trim();

            if (id.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Todos los campos son obligatorios.",
                        "Error de Registro",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!GestionEmpleados.IdValido(id)) {
                JOptionPane.showMessageDialog(this,
                        "El ID debe ser de exactamente 6 dígitos numéricos.",
                        "Formato de ID Inválido",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            EmpleadoAutorizado nuevoEmpleado = new EmpleadoAutorizado(id, nombre);
            boolean exito = gestionEmpleados.registrarEmpleado(nuevoEmpleado);

            if (exito) {
                JOptionPane.showMessageDialog(this,
                        "Empleado registrado exitosamente.\nYa puede iniciar sesión con el ID: " + id,
                        "Registro Completado",
                        JOptionPane.INFORMATION_MESSAGE);
                labelIdEmpleado.setText(id);
            } else {
                JOptionPane.showMessageDialog(this,
                        "El ID de empleado ya existe.",
                        "ID Duplicado",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
