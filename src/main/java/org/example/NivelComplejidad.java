
package org.example;


public enum NivelComplejidad {
    Bajo(1,0,"Material apto para todo público"),
    Medio(2,3,"Material nivel 2, requiere bases previas"),
    Alto(3,7,"Material Requiere autorización");
    
   private final int orden;
   private final int diasAdicionales;
   private final String descripcion;

    private NivelComplejidad(int orden, int diasAdicionales, String descripcion) {
        this.orden = orden;
        this.diasAdicionales = diasAdicionales;
        this.descripcion = descripcion;
    }
    
    public boolean necesitaAutorizacion(){
        return this==Alto;
    }

    public int getOrden() {
        return orden;
    }

    public int getDiasAdicionales() {
        return diasAdicionales;
    }

    public String getDescripcion() {
        return descripcion;
    }
   
   
    
}
