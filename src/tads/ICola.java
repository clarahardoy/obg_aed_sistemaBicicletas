/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tads;

/**
 *
 * @author clarahardoy
 */
public interface ICola<T> {

    public void encolar(T dato);

    public T desencolar();

    public boolean esVacia();
    
    public T getFondo();
    
    public T getFrente(); 

}
