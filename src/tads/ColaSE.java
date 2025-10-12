/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tads;

/**
 *
 * @author clarahardoy
 */
public class ColaSE<T extends Comparable> implements ICola<T> {
    private Nodo<T>  frente;
    private Nodo<T> fondo; 
    
    public ColaSE() {
        frente = null;
        fondo = null; 
    }
    
    @Override
    public void encolar (T dato) {
        Nodo<T> nodo_nuevo = new Nodo<T>(dato);
        if(esVacia()) {
            frente = nodo_nuevo;
        }
        else {
            fondo.setSiguiente(nodo_nuevo); 
        }
        fondo = nodo_nuevo;
    }
    
    //extraer primer elemento
    @Override
    public T desencolar() {
        if (!esVacia()){
            T dato_frente = frente.getDato();
            frente = frente.getSiguiente(); 
            if (frente == null) {
                fondo = frente; 
            }
            return dato_frente;
        } else {
            return null; 
        }
    }
    
    @Override
    public boolean esVacia(){
         return frente == null && fondo == null;
    }
    
    @Override
    public T getFrente(){
        return frente != null ? frente.getDato() : null;
    }
    
    @Override
    public T getFondo(){
        return fondo != null ? fondo.getDato() : null;
    }
    
}