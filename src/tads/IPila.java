/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tads;

/**
 *
 * @author clarahardoy
 */
public interface IPila<T> {

    public boolean esVacia();

    public void apilar(T dato);

    public T getTope();

    public T desapilar();
   
}