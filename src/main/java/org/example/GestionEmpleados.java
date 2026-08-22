package org.example;

import java.util.ArrayList;
import java.util.List;

public class GestionEmpleados {

    private final List<EmpleadoAutorizado> listaEmpleados;

    public GestionEmpleados(){
        listaEmpleados = new ArrayList<>();

    }

    public boolean registrarEmpleado(EmpleadoAutorizado nuevoEmpleado) {
        if (nuevoEmpleado == null || !IdValido(nuevoEmpleado.getIdEmpleado())) {
            return false; // Retorna falso si no cumple el formato de 6 dígitos
        }

        for (EmpleadoAutorizado emp : listaEmpleados) {
            if (emp.getIdEmpleado().equals(nuevoEmpleado.getIdEmpleado().trim())) {
                return false;
            }
        }

        listaEmpleados.add(nuevoEmpleado);
        return true;
    }
    public EmpleadoAutorizado verificar(String idEmpleado){
        for(EmpleadoAutorizado empleado: listaEmpleados){
            if(empleado.getIdEmpleado().equalsIgnoreCase(idEmpleado)){
                empleado.setEstadoLoggedIn(true);
                return empleado;
            }
        }
        return null;
    }

    public static boolean IdValido(String id) {
        return id != null && id.matches("\\d{6}");
    }

    public void cerrarSesion(EmpleadoAutorizado empleado) {
        if (empleado != null) {
            empleado.setEstadoLoggedIn(false);
        }
    }

    public List<EmpleadoAutorizado> getListaEmpleados() {
        return listaEmpleados;
    }
}
