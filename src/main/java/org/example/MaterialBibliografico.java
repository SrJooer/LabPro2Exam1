package org.example;

import java.awt.Image;
import java.util.List;
import java.util.ArrayList;


public abstract class MaterialBibliografico implements Prestar, Reservar{
   protected String titulo;
   protected int codigo;
   protected boolean estadoPrestamo;
   protected int diasMaximo;
   protected NivelComplejidad nivel;
   protected Image caratula;
   protected List<String> reservas = new ArrayList<>();

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
   
   @Override
   public void prestarMaterial() throws MaterialPrestado{
       if(!disponible()){
            throw new MaterialPrestado(titulo);
       }
       this.estadoPrestamo=true;
   }
   @Override
   public void devolverMaterial() {
        this.estadoPrestamo = false;
    }
   
   @Override
    public boolean disponible() {
        return !estadoPrestamo;
    }
    
    @Override
    public boolean reservar(String usuario) {
    if (reservas.contains(usuario)) {
        return false; 
    }
    reservas.add(usuario);
    return true;
}
    
    @Override
    public void cancelarReserva() {
        if (!reservas.isEmpty()) {
         reservas.remove(0); 
        }
    }
    
    @Override
    public boolean hayReservasPendientes() {
        return !reservas.isEmpty();
    }   
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
