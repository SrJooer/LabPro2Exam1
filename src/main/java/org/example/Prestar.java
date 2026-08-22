
package org.example;

public interface Prestar {
    void prestarMaterial() throws MaterialPrestado;
    void devolverMaterial();
    boolean disponible();
    
    
}
