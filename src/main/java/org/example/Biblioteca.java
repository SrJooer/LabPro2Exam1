package org.example;

import org.example.prestamos.Prestamo;
import org.example.usuarios.Usuario;
import java.time.LocalDate;
import java.util.*;

public class Biblioteca {

    private final List<MaterialBibliografico> catalogo = new ArrayList<>();
    private final List<Usuario> usuarios = new ArrayList<>();
    private final List<Historial> historialPrestamos = new ArrayList<>();
    private final List<Reservas> colaReservas = new ArrayList<>();

    public void registrarMaterial(MaterialBibliografico material) {
        catalogo.add(material);
    }

    public void registrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<MaterialBibliografico> getCatalogo() {
        return catalogo;
    }

    public List<Usuario> getColaDeReservas(MaterialBibliografico material) {
        List<Usuario> lista = new ArrayList<>();
        for (Reservas r : colaReservas) {
            if (r.getCodigoMaterial() == material.getCodigo()) {
                lista.add(r.getUsuario());
            }
        }
        return lista;
    }

    public void solicitarPrestamo(MaterialBibliografico material, Usuario usuario) throws Excepciones {
        if (!material.disponible()) {
            throw new MaterialPrestado(material.getTitulo());
        }
        if (usuario.haAlcanzadoLimite()) {
            throw new LimiteException(usuario.getNombre(), usuario.getLimitePrestamos());
        }
        if (usuario.estaPenalizado(LocalDate.now())) {
            throw new PenalizacionExcepcion(usuario.getNombre(), usuario.getPenalizadoHasta());
        }
        if (!usuario.puedeAcceder(material.getNivel())) {
            throw new AutorizacionExcepcion(material.getTitulo());
        }

        material.prestarMaterial();
        Prestamo prestamo = new Prestamo(usuario, material, LocalDate.now());
        usuario.registrarPrestamo(prestamo);
        registrarEnHistorial(material.getCodigo());
    }

    private void registrarEnHistorial(int codigo) {
        for (Historial registro : historialPrestamos) {
            if (registro.codigo == codigo) {
                registro.cantidad++;
                return;
            }
        }
        historialPrestamos.add(new Historial(codigo, 1));
    }

    public void devolverMaterial(MaterialBibliografico material, Usuario usuario) {
        Prestamo prestamo = usuario.buscarPrestamoActivo(material);
    if (prestamo == null) {
        System.out.println("Este usuario no tiene un préstamo activo de este material.");
        return;
    }

    prestamo.registrarDevolucion(LocalDate.now()); 
    material.devolverMaterial();
    usuario.registrarDevolucion(prestamo);

    
    if (prestamo.seDevolvioTarde()) {
        int diasRetraso = prestamo.getDiasRetraso(LocalDate.now());
        calcularPenalizacion(usuario, diasRetraso);
    }

    
    for (int i = 0; i < colaReservas.size(); i++) {
        Reservas reserva = colaReservas.get(i);
        if (reserva.getCodigoMaterial() == material.getCodigo()) {
            colaReservas.remove(i);
            try {
                solicitarPrestamo(material, reserva.getUsuario());
            } catch (Excepciones e) {
                System.out.println("No se pudo asignar la reserva automáticamente: " + e.getMessage());
            }
            break;
        }
    }
    }

    public void reservarMaterial(MaterialBibliografico material, Usuario usuario) throws ReservaExcepcion {
        if (!usuario.puedeReservar()) {
            throw new ReservaExcepcion("Este usuario no tiene permitido reservar materiales.");
        }
        colaReservas.add(new Reservas(material.getCodigo(), usuario));
        material.reservar(usuario.getNombre());
    }

    public boolean cancelarReserva(MaterialBibliografico material, Usuario usuario) {
        boolean eliminado = colaReservas.removeIf(r -> r.getCodigoMaterial() == material.getCodigo()
                && r.getUsuario().getId().equals(usuario.getId()));

        if (eliminado) {
            material.cancelarReserva();
        }
        return eliminado;
    }

    public MaterialBibliografico buscarPorCodigo(int codigo) {
        return buscarPorCodigoRecursivo(0, codigo);
    }

    private MaterialBibliografico buscarPorCodigoRecursivo(int indice, int codigo) {
        if (indice >= catalogo.size()) {
            return null;
        }
        MaterialBibliografico actual = catalogo.get(indice);
        if (actual.getCodigo() == codigo) {
            return actual;
        }
        return buscarPorCodigoRecursivo(indice + 1, codigo);
    }

    public List<MaterialBibliografico> buscarPorTitulo(String texto) {
        List<MaterialBibliografico> resultados = new ArrayList<>();
        buscarPorTituloRecursivo(0, texto.toLowerCase(), resultados);
        return resultados;
    }

    private void buscarPorTituloRecursivo(int indice, String texto, List<MaterialBibliografico> resultados) {
        if (indice >= catalogo.size()) {
            return;
        }

        MaterialBibliografico actual = catalogo.get(indice);
        if (actual.getTitulo().toLowerCase().contains(texto)) {
            resultados.add(actual);
        }
        buscarPorTituloRecursivo(indice + 1, texto, resultados);
    }

    public void calcularPenalizacion(Usuario usuario, int diasRetraso) {
        if (usuario == null || diasRetraso <= 0) {
            return;
        }
        usuario.aplicarPenalizacion(LocalDate.now(), diasRetraso * 2);
    }

    public List<Historial> materialesMasSolicitados() {
        List<Historial> copia = new ArrayList<>(historialPrestamos);
        copia.sort((a, b) -> b.cantidad - a.cantidad);
        return copia;
    }
}