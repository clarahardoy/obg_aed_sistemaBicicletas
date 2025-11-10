
package tads;


public interface IPila<T> {

    public boolean esVacia();

    public void apilar(T dato);

    public T getTope();

    public T desapilar();
    
    public PilaSE copiarPila();
   
}