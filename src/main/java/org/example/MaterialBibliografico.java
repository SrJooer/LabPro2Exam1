package org.example;

import java.awt.Image;


public abstract class MaterialBibliografico {
   protected String titulo;
   protected int codigo;
   protected boolean estadoPrestamo;
   protected int diasMaximo;
   protected NivelComplejidad nivel;
   protected Image caratula;

    public MaterialBibliografico(String titulo, int codigo, int diasMaximo, NivelComplejidad nivel, Image caratula) {
        this.titulo = titulo;
        this.codigo = codigo;
        this.diasMaximo = diasMaximo;
        this.nivel = nivel;
        this.caratula = caratula;
        this.estadoPrestamo=false;
    }
  
   public abstract String obtenerDescripcion();
   
   public abstract int calcularDias();

    public String getTitulo() {
        return titulo;
    }

    public int getCodigo() {
        return codigo;
    }

    public boolean isEstadoPrestamo() {
        return estadoPrestamo;
    }

    public int getDiasMaximo() {
        return diasMaximo;
    }

    public NivelComplejidad getNivel() {
        return nivel;
    }

    public Image getCaratula() {
        return caratula;
    }

    public void setEstadoPrestamo(boolean estadoPrestamo) {
        this.estadoPrestamo = estadoPrestamo;
    }
   
   
   
    
}
