package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;

public class PanelCatalogoMateriales extends JPanel {

    private final JTextField textoTitulo;
    private final JTextField textoCodigo;
    private final JTextField textoRutaImagen;
    private final JComboBox<String> comboTipoMaterial;
    private final JComboBox<NivelComplejidad> comboComplejidad;
    private final JLabel etiquetaPortada;
    private final JLabel etiquetaBadgeComplejidad;
    private final JTable tablaMateriales;
    private final DefaultTableModel modeloTabla;
    private final Biblioteca biblioteca;

    private final JPanel panelAtributosEspecificos;
    private final JTextField textoCampo1;
    private final JTextField textoCampo2;
    private final JLabel etiquetaCampo1;
    private final JLabel etiquetaCampo2;

    public PanelCatalogoMateriales(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelFormularioYVisor = new JPanel(new GridBagLayout());
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.insets = new Insets(5, 5, 5, 5);
        restricciones.fill = GridBagConstraints.HORIZONTAL;

        restricciones.gridx = 0; restricciones.gridy = 0;
        panelFormularioYVisor.add(new JLabel("Título:"), restricciones);
        restricciones.gridx = 1; restricciones.gridy = 0;
        textoTitulo = new JTextField(15);
        panelFormularioYVisor.add(textoTitulo, restricciones);

        restricciones.gridx = 0; restricciones.gridy = 1;
        panelFormularioYVisor.add(new JLabel("Código:"), restricciones);
        restricciones.gridx = 1; restricciones.gridy = 1;
        textoCodigo = new JTextField(15);
        panelFormularioYVisor.add(textoCodigo, restricciones);

        restricciones.gridx = 0; restricciones.gridy = 2;
        panelFormularioYVisor.add(new JLabel("Tipo:"), restricciones);
        restricciones.gridx = 1; restricciones.gridy = 2;
        comboTipoMaterial = new JComboBox<>(new String[]{"Libro", "Revista", "Audiovisual"});
        comboTipoMaterial.addActionListener(e -> cambiarCamposDinamicos());
        panelFormularioYVisor.add(comboTipoMaterial, restricciones);

        restricciones.gridx = 0; restricciones.gridy = 3;
        panelFormularioYVisor.add(new JLabel("Complejidad:"), restricciones);
        restricciones.gridx = 1; restricciones.gridy = 3;
        comboComplejidad = new JComboBox<>(NivelComplejidad.values());
        comboComplejidad.addActionListener(e -> actualizarComplejidadVisual());
        panelFormularioYVisor.add(comboComplejidad, restricciones);

        panelAtributosEspecificos = new JPanel(new GridLayout(2, 2, 5, 5));
        etiquetaCampo1 = new JLabel("Autor:");
        textoCampo1 = new JTextField(10);
        etiquetaCampo2 = new JLabel("Páginas:");
        textoCampo2 = new JTextField(10);

        panelAtributosEspecificos.add(etiquetaCampo1);
        panelAtributosEspecificos.add(textoCampo1);
        panelAtributosEspecificos.add(etiquetaCampo2);
        panelAtributosEspecificos.add(textoCampo2);

        restricciones.gridx = 0; restricciones.gridy = 4; restricciones.gridwidth = 2;
        panelFormularioYVisor.add(panelAtributosEspecificos, restricciones);

        restricciones.gridwidth = 1;
        restricciones.gridx = 0; restricciones.gridy = 5;
        panelFormularioYVisor.add(new JLabel("Portada:"), restricciones);
        restricciones.gridx = 1; restricciones.gridy = 5;
        JPanel panelArchivo = new JPanel(new BorderLayout(5, 0));
        textoRutaImagen = new JTextField();
        textoRutaImagen.setEditable(false);
        JButton botonBuscar = new JButton("Buscar...");
        botonBuscar.addActionListener(e -> seleccionarImagen());
        panelArchivo.add(textoRutaImagen, BorderLayout.CENTER);
        panelArchivo.add(botonBuscar, BorderLayout.EAST);
        panelFormularioYVisor.add(panelArchivo, restricciones);

        restricciones.gridx = 0; restricciones.gridy = 6; restricciones.gridwidth = 2;
        JButton botonGuardar = new JButton("Registrar Material");
        botonGuardar.setFont(new Font("SansSerif", Font.BOLD, 12));
        botonGuardar.addActionListener(e -> registrarMaterialEnTabla());
        panelFormularioYVisor.add(botonGuardar, restricciones);

        add(panelFormularioYVisor, BorderLayout.WEST);

        JPanel panelVisor = new JPanel(new BorderLayout(10, 10));
        panelVisor.setBorder(BorderFactory.createTitledBorder("Detalle del Material"));

        etiquetaBadgeComplejidad = new JLabel("Complejidad: BAJO", SwingConstants.CENTER);
        etiquetaBadgeComplejidad.setFont(new Font("SansSerif", Font.BOLD, 14));
        etiquetaBadgeComplejidad.setOpaque(true);
        actualizarComplejidadVisual();
        panelVisor.add(etiquetaBadgeComplejidad, BorderLayout.NORTH);

        etiquetaPortada = new JLabel("Sin Portada", SwingConstants.CENTER);
        etiquetaPortada.setPreferredSize(new Dimension(180, 240));
        etiquetaPortada.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panelVisor.add(etiquetaPortada, BorderLayout.CENTER);

        add(panelVisor, BorderLayout.CENTER);

        String[] columnas = {"Código", "Título", "Tipo", "Complejidad", "Detalles Propios"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaMateriales = new JTable(modeloTabla);
        JScrollPane panelDesplazamientoTabla = new JScrollPane(tablaMateriales);
        panelDesplazamientoTabla.setPreferredSize(new Dimension(800, 200));

        add(panelDesplazamientoTabla, BorderLayout.SOUTH);

        cambiarCamposDinamicos();
    }

    private void cambiarCamposDinamicos() {
        String tipo = (String) comboTipoMaterial.getSelectedItem();
        if (tipo == null) return;

        switch (tipo) {
            case "Libro" -> {
                etiquetaCampo1.setText("Autor:");
                etiquetaCampo2.setText("Páginas:");
            }
            case "Revista" -> {
                etiquetaCampo1.setText("No. Edición:");
                etiquetaCampo2.setText("Periodicidad:");
            }
            case "Audiovisual" -> {
                etiquetaCampo1.setText("Duración (min):");
                etiquetaCampo2.setText("Formato:");
            }
        }
        textoCampo1.setText("");
        textoCampo2.setText("");
        panelAtributosEspecificos.revalidate();
        panelAtributosEspecificos.repaint();
    }

    private void registrarMaterialEnTabla() {
        String titulo = textoTitulo.getText().trim();
        String codigoTexto = textoCodigo.getText().trim();
        String tipo = (String) comboTipoMaterial.getSelectedItem();
        NivelComplejidad nivel = (NivelComplejidad) comboComplejidad.getSelectedItem();
        String valor1 = textoCampo1.getText().trim();
        String valor2 = textoCampo2.getText().trim();

        if (titulo.isEmpty() || codigoTexto.isEmpty() || valor1.isEmpty() || valor2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos del material.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int codigo = Integer.parseInt(codigoTexto);
            Image caratula = null;
            String rutaImg = textoRutaImagen.getText().trim();
            if (!rutaImg.isEmpty()) {
                caratula = new ImageIcon(rutaImg).getImage();
            }

            int diasMaximoBase = 7;
            MaterialBibliografico nuevoMaterial;

            if (tipo.equals("Libro")) {
                int paginas = Integer.parseInt(valor2);
                int isbnSimulado = 123456789;
                nuevoMaterial = new Libros(valor1, paginas, isbnSimulado, titulo, codigo, diasMaximoBase, nivel, caratula);
            } else if (tipo.equals("Revista")) {
                int edicion = Integer.parseInt(valor1);
                nuevoMaterial = new Revistas(edicion, Periocidad.Mensual, titulo, codigo, diasMaximoBase, nivel, caratula);
            } else {
                int duracion = Integer.parseInt(valor1);
                Formato formatoAudiovisual = Formato.values()[0];
                nuevoMaterial = new Audiovisuales(duracion, formatoAudiovisual, titulo, codigo, diasMaximoBase, nivel, caratula);
            }

            biblioteca.registrarMaterial(nuevoMaterial);

            String detalle = etiquetaCampo1.getText() + " " + valor1 + " | " + etiquetaCampo2.getText() + " " + valor2;
            modeloTabla.addRow(new Object[]{codigo, titulo, tipo, nivel.name(), detalle});

            JOptionPane.showMessageDialog(this, "Material registrado exitosamente en la Biblioteca.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            textoTitulo.setText("");
            textoCodigo.setText("");
            textoCampo1.setText("");
            textoCampo2.setText("");
            textoRutaImagen.setText("");
            etiquetaPortada.setIcon(null);
            etiquetaPortada.setText("Sin Portada");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El código, número de páginas, edición o duración deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarImagen() {
        JFileChooser selectorPortada = new JFileChooser();
        int resultado = selectorPortada.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = selectorPortada.getSelectedFile();
            textoRutaImagen.setText(archivo.getAbsolutePath());
            cargarImagenVisor(archivo.getAbsolutePath());
        }
    }

    private void cargarImagenVisor(String ruta) {
        File archivo = new File(ruta);
        if (archivo.exists() && !archivo.isDirectory()) {
            ImageIcon icono = new ImageIcon(new ImageIcon(ruta).getImage()
                    .getScaledInstance(180, 240, Image.SCALE_SMOOTH));
            etiquetaPortada.setIcon(icono);
            etiquetaPortada.setText("");
        } else {
            etiquetaPortada.setIcon(null);
            etiquetaPortada.setText("Sin Portada Disponible");
        }
    }

    private void actualizarComplejidadVisual() {
        NivelComplejidad nivel = (NivelComplejidad) comboComplejidad.getSelectedItem();
        if (nivel != null) {
            etiquetaBadgeComplejidad.setText("Complejidad: " + nivel.name());
            switch (nivel.name().toUpperCase()) {
                case "BAJO" -> etiquetaBadgeComplejidad.setBackground(new Color(144, 238, 144));
                case "MEDIO" -> etiquetaBadgeComplejidad.setBackground(new Color(255, 222, 121));
                case "ALTO" -> etiquetaBadgeComplejidad.setBackground(new Color(255, 128, 128));
                default -> etiquetaBadgeComplejidad.setBackground(Color.LIGHT_GRAY);
            }
        }
    }
}