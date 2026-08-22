package org.example;

import org.example.usuarios.Usuario;
import org.example.usuarios.UsuarioEstandar;
import org.example.usuarios.UsuarioPremium;

public final class DatosIniciales {

    private DatosIniciales() {
    }

    public static Biblioteca crearBiblioteca() {
        Biblioteca biblioteca = new Biblioteca();
        try {
            Usuario ana = new UsuarioEstandar("U-001", "Ana Lopez");
            Usuario beto = new UsuarioPremium("U-002", "Beto Cruz", 2);
            Usuario carla = new UsuarioPremium("U-003", "Carla Mejia");
            Usuario diego = new UsuarioEstandar("U-004", "Diego Ramos");

            biblioteca.registrarUsuario(ana);
            biblioteca.registrarUsuario(beto);
            biblioteca.registrarUsuario(carla);
            biblioteca.registrarUsuario(diego);

            Libros redes = new Libros("Andrew Tanenbaum", 620, 132126953,
                    "Redes de Computadoras", 101, 14, NivelComplejidad.Medio, null);
            Libros clean = new Libros("Robert C. Martin", 464, 132350882,
                    "Clean Code", 102, 14, NivelComplejidad.Bajo, null);
            Revistas nature = new Revistas(342, Periocidad.Mensual,
                    "Nature", 201, 10, NivelComplejidad.Alto, null);
            Audiovisuales origen = new Audiovisuales(148, Formato.BluRay,
                    "Origen", 301, 7, NivelComplejidad.Bajo, null);

            biblioteca.registrarMaterial(redes);
            biblioteca.registrarMaterial(clean);
            biblioteca.registrarMaterial(nature);
            biblioteca.registrarMaterial(origen);

            biblioteca.solicitarPrestamo(redes, ana);
            biblioteca.solicitarPrestamo(origen, beto);
            biblioteca.solicitarPrestamo(nature, carla);
        } catch (Excepciones e) {
            System.out.println("Error cargando los datos iniciales: " + e.getMessage());
        }
        return biblioteca;
    }
}
