
package org.example;
import java.awt.Image;

public class Revistas extends MaterialBibliografico{
    private int numeroEdicion;
    private Periocidad periocidad;

    public Revistas(int numeroEdicion, Periocidad periocidad, String titulo, int codigo,
                   int diasMaximo, NivelComplejidad nivel, Image caratula) {
        super(titulo, codigo, diasMaximo, nivel, caratula);
        this.numeroEdicion = numeroEdicion;
        this.periocidad = periocidad;
    }
    
    @Override
    public String obtenerDescripcion(){
        return "Revista: " + titulo + " (Edición #" 
                + numeroEdicion + ", " + periocidad + ")";
    }
    @Override
    public int calcularDias(){
        int diasBase=diasMaximo;
        return diasBase+nivel.getDiasAdicionales();
    
    }

    public int getNumeroEdicion() {
        return numeroEdicion;
    }

    public Periocidad getPeriocidad() {
        return periocidad;
    }

    public void setNumeroEdicion(int numeroEdicion) {
        this.numeroEdicion = numeroEdicion;
    }

    public void setPeriocidad(Periocidad periocidad) {
        this.periocidad = periocidad;
    }
    
    
    
}
