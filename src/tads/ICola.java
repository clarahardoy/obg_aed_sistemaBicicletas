
package tads;


public interface ICola<T> {

    public void encolar(T dato);

    public T desencolar();

    public boolean esVacia();
    
    public T getFondo();
    
    public T getFrente(); 

}
