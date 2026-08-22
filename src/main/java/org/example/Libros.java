package org.example;

import java.awt.Image;


public  class Libros extends MaterialBibliografico{
    private String autor;
    private int numeroPaginas;
    private int ISBN;

    public Libros(String autor, int numeroPaginas, int ISBN, String titulo, int codigo, int diasMaximo, NivelComplejidad nivel, Image caratula) {
        super(titulo, codigo, diasMaximo, nivel, caratula);
        this.autor = autor;
        this.numeroPaginas = numeroPaginas;
        this.ISBN = ISBN;
    }

    @Override
    public String obtenerDescripcion(){
         return "Libro: \"" + titulo + "\" de " + autor +
           " (" + numeroPaginas + " páginas, ISBN: " + ISBN + ")";
    }
    
    @Override
    public int calcularDias(){
        int diasBase=diasMaximo;
        return diasBase+nivel.getDiasAdicionales();
    
    }
    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getNumeroPaginas() {
        return numeroPaginas;
    }

    public void setNumeroPaginas(int numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }

    public int getISBN() {
        return ISBN;
    }

    public void setISBN(int ISBN) {
        this.ISBN = ISBN;
    }
    
    
    
    
}
