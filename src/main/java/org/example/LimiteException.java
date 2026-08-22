/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example;

public class LimiteException extends Excepciones{

    public LimiteException(String usuario, int limite) {
        super("El usuario "+usuario+" ya alcanzón su limite de prestamos");
    }
    
}
