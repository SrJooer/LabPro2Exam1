
package org.example;
import org.example.usuarios.Usuario;
import org.example.prestamos.Prestamo;
import java.time.LocalDate;
import java.util.*;
public class Biblioteca {
    private final List<Prestar> catalogo=new ArrayList<>();
    private final List<Usuario> usuarios = new ArrayList<>();
    private final List<Historial> historialPrestamos = new ArrayList<>();
    private final List<Reservas> colaReservas = new ArrayList<>();
    public void registrarMaterial(MaterialBibliografico material) {
        catalogo.add(material);
    }
     public void registrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }
    public void solicitarPrestamo(MaterialBibliografico material, Usuario usuario) throws Excepciones {

        if (!material.disponible()) {
            throw new MaterialPrestado(material.getTitulo());
        }
        if (usuario.haAlcanzadoLimite()) {
            throw new LimiteException(usuario.getNombre(), usuario.getLimitePrestamos());
        }
        if (usuario.estaPenalizado(LocalDate.now())) {
            throw new PenalizacionExcepcion(usuario.getNombre(),usuario.getPenalizadoHasta());
        }
        if (!usuario.puedeAcceder(material.getNivel())) {
            throw new AutorizacionExcepcion(material.getTitulo());
        }

        material.prestarMaterial();
        usuario.registrarPrestamo(new Prestamo(usuario, material, LocalDate.now()));
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
        material.devolverMaterial();

        Prestamo prestamo = usuario.buscarPrestamoActivo(material);
        if (prestamo != null) {
            prestamo.registrarDevolucion(LocalDate.now());
            usuario.registrarDevolucion(prestamo);
            calcularPenalizacion(usuario, prestamo.getDiasRetraso(LocalDate.now()));
        }

        
        for (int i = 0; i < colaReservas.size(); i++) {
            Reservas reserva = colaReservas.get(i);
            if (reserva.codigoMaterial == material.getCodigo()) {
                colaReservas.remove(i);
                try {
                    solicitarPrestamo(material, reserva.usuario);
                } catch (Excepciones e) {
                    System.out.println("No se pudo asignar la reserva automáticamente: " + e.getMessage());
                }
                break;
            }
        }
    }
    
    public void reservarMaterial(MaterialBibliografico material, Usuario usuario) throws Excepciones {
        if (!usuario.puedeReservar()) {
            throw new ReservaExcepcion("el perfil " + usuario.getTipoPerfil() + " no tiene permitido reservar");
        }
        if (material.disponible()) {
            throw new ReservaExcepcion("\"" + material.getTitulo() + "\" esta disponible, se puede prestar sin reservar");
        }
        if (usuario.tienePrestado(material)) {
            throw new ReservaExcepcion(usuario.getNombre() + " ya tiene prestado \"" + material.getTitulo() + "\"");
        }
        if (yaReservo(material, usuario)) {
            throw new ReservaExcepcion(usuario.getNombre() + " ya esta en la cola de \"" + material.getTitulo() + "\"");
        }
        colaReservas.add(new Reservas(material.getCodigo(), usuario));
        material.reservar(usuario.getNombre());
    }

    public boolean yaReservo(MaterialBibliografico material, Usuario usuario) {
        for (Reservas reserva : colaReservas) {
            if (reserva.getCodigoMaterial() == material.getCodigo()
                    && reserva.getUsuario().equals(usuario)) {
                return true;
            }
        }
        return false;
    }

    public boolean cancelarReserva(MaterialBibliografico material, Usuario usuario) {
        for (int i = 0; i < colaReservas.size(); i++) {
            Reservas reserva = colaReservas.get(i);
            if (reserva.getCodigoMaterial() == material.getCodigo()
                    && reserva.getUsuario().equals(usuario)) {
                colaReservas.remove(i);
                material.cancelarReserva(usuario.getNombre());
                return true;
            }
        }
        return false;
    }

    public List<Usuario> getColaDeReservas(MaterialBibliografico material) {
        List<Usuario> cola = new ArrayList<>();
        for (Reservas reserva : colaReservas) {
            if (reserva.getCodigoMaterial() == material.getCodigo()) {
                cola.add(reserva.getUsuario());
            }
        }
        return cola;
    }

    public List<Usuario> getUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    public List<MaterialBibliografico> getCatalogo() {
        List<MaterialBibliografico> materiales = new ArrayList<>();
        for (Prestar prestable : catalogo) {
            materiales.add((MaterialBibliografico) prestable);
        }
        return materiales;
    }
    public MaterialBibliografico buscarPorCodigo(int codigo) {
        return buscarPorCodigoRecursivo(0, codigo);
    }
    
     private MaterialBibliografico buscarPorCodigoRecursivo(int indice, int codigo) {
        if (indice >= catalogo.size()) {
            return null;
        }
        MaterialBibliografico actual = (MaterialBibliografico) catalogo.get(indice);
        if (actual.getCodigo() == codigo){ 
            return actual;}
        return buscarPorCodigoRecursivo(indice + 1, codigo);
    }
     
     public List<MaterialBibliografico> buscarPorTitulo(String texto) {
        List<MaterialBibliografico> resultados = new ArrayList<>();
        buscarPorTituloRecursivo(0, texto.toLowerCase(), resultados);
        return resultados;
    }
     
     private void buscarPorTituloRecursivo(int indice, String texto, List<MaterialBibliografico> resultados) {
        if (indice >= catalogo.size()) {
            return;}
        
        MaterialBibliografico actual = (MaterialBibliografico) catalogo.get(indice);
        if (actual.getTitulo().toLowerCase().contains(texto)) {
            
      
            resultados.add(actual);
        }
        buscarPorTituloRecursivo(indice + 1, texto, resultados);
    }
     public void calcularPenalizacion(Usuario usuario, int diasRetraso) {
        if (diasRetraso <= 0){
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
