package org.example;
import org.example.usuarios.Usuario;
/**
 *
 * @author vasqu
 */
public class Reservas {
    int codigoMaterial;
    Usuario usuario;

    public Reservas(int codigoMaterial, Usuario usuario) {
        this.codigoMaterial = codigoMaterial;
        this.usuario = usuario;
    }

    public int getCodigoMaterial() {
        return codigoMaterial;
    }

    public Usuario getUsuario() {
        return usuario;
    }
    
    
}
