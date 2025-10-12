/**
 *
 * @author clarahardoy
 */

package tads;

public class PilaSE<T extends Comparable> implements IPila<T> {
    private Nodo<T> tope; 
    
    public PilaSE() {
        tope = null; 
    }
    
    @Override
    public void apilar(T dato){
        Nodo<T> nodo_nuevo = new Nodo<T>(dato); 
        if (!esVacia()){
            nodo_nuevo.setSiguiente(tope);
            tope = nodo_nuevo;
        }     
    }
    
    @Override
    public T desapilar() {
        T dato_tope = tope.getDato();
        if (!esVacia()){
            tope = tope.getSiguiente();
            return dato_tope;
        }
        else {
            return null;
        }
    }
    
    @Override
    public boolean esVacia() {
        return tope == null; 
    }
    
    @Override
    public T getTope() {
        return tope != null ? tope.getDato() : null; 
    }   
}

