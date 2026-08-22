
package org.example;

import java.awt.Image;

public class Audiovisuales extends MaterialBibliografico {
   private int duracionMinutos;
   private Formato formato;

    public Audiovisuales(int duracionMinutos, Formato formato, String titulo, int codigo,int diasMaximo, NivelComplejidad nivel, Image caratula) {
        super(titulo, codigo, diasMaximo, nivel, caratula);
        this.duracionMinutos = duracionMinutos;
        this.formato = formato;
    }
    @Override
    public String obtenerDescripcion(){
        return "Material audiovisual: \"" + titulo + "\" (" + formato + ", " 
                + duracionMinutos + " min)";
    }
    @Override
    public int calcularDias() {
        int diasBase = diasMaximo;
        return diasBase + nivel.getDiasAdicionales();
    }
    
    
    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public Formato getFormato() {
        return formato;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public void setFormato(Formato formato) {
        this.formato = formato;
    }
   
   
    
}
