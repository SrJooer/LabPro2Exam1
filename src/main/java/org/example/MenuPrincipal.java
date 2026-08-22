package org.example;
import org.example.usuarios.PanelClientes;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal  extends JFrame {

    private final EmpleadoAutorizado empleadoActual;
    private final Biblioteca biblioteca;

    public MenuPrincipal(EmpleadoAutorizado empleado) {
        this.empleadoActual = empleado;
        this.biblioteca = new Biblioteca();

        setTitle("Menu Principal");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panelSuperior.setBackground(new Color(230, 235, 240));

        JLabel lblInfoEmpleado = new JLabel("Empleado Activo: " + empleadoActual.getNombre());
        lblInfoEmpleado.setFont(new Font("SansSerif", Font.BOLD, 15));
        panelSuperior.add(lblInfoEmpleado, BorderLayout.WEST);

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnCerrarSesion.addActionListener(e -> {
            new PantallaInicioSesion(new GestionEmpleados());
            this.dispose();
        });
        panelSuperior.add(btnCerrarSesion, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);

        JTabbedPane tabsMenu = new JTabbedPane();
        tabsMenu.setFont(new Font("SansSerif", Font.PLAIN, 14));

        tabsMenu.addTab("Catálogo de Materiales", new PanelCatalogoMateriales(biblioteca));
        tabsMenu.addTab("Usuarios", new PanelClientes());
        tabsMenu.addTab("Préstamos y Reservas", crearPanelModulo("Gestión de Préstamos, Devoluciones y Cola de Espera"));
        tabsMenu.addTab("Búsqueda de Material", new PanelBusquedasMaterial(biblioteca));
        tabsMenu.addTab("Ordenamiento por Filtros", crearPanelModulo("Ordenamiento por Filtros"));

        add(tabsMenu, BorderLayout.CENTER);

        setVisible(true);
    }
    private JPanel crearPanelModulo(String descripcion) {
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Módulo en Desarrollo");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(80, 80, 80));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDescripcion = new JLabel(descripcion);
        lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblDescripcion.setForeground(new Color(120, 120, 120));
        lblDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelContenido.add(lblTitulo);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 10)));
        panelContenido.add(lblDescripcion);

        panelPrincipal.add(panelContenido);
        return panelPrincipal;

    }
}
