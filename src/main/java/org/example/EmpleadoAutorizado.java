package org.example;

public class EmpleadoAutorizado {

    String idEmpleado;
    String nombre;
    Boolean estadoLoggedIn;
    public EmpleadoAutorizado(String idEmpleado, String nombre) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.estadoLoggedIn = false;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public Boolean isEstadoLoggedIn() {
        return estadoLoggedIn;
    }

    public void setEstadoLoggedIn(Boolean estadoLoggedIn) {
        this.estadoLoggedIn = estadoLoggedIn;
    }
}

