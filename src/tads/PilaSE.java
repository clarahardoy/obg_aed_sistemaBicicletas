
package tads;

public class PilaSE<T extends Comparable<T>> implements IPila<T> {
    private Nodo<T> tope; 
    
    public PilaSE() {
        tope = null; 
    }
    
    @Override
    public void apilar(T dato){
        Nodo<T> nodo_nuevo = new Nodo<>(dato); 
       
            nodo_nuevo.setSiguiente(tope);
            tope = nodo_nuevo;        
    }
    
    @Override
    public T desapilar() {
        if (esVacia()) return null;  
        T dato = tope.getDato();
        tope = tope.getSiguiente();
        return dato;
    }
    
    @Override
    public boolean esVacia() {
        return tope == null; 
    }
    
    @Override
    public T getTope() {
        return tope != null ? tope.getDato() : null; 
    }  
    
    @Override
    public PilaSE copiarPila() {
        PilaSE<T> copia = new PilaSE<>();
        PilaSE<T> invertida = new PilaSE<>();

        while (!this.esVacia()) {
            invertida.apilar(this.desapilar());
        }

        while (!(invertida.esVacia())) {
            T dato = invertida.desapilar();
            copia.apilar(dato);
            this.apilar(dato);
        }

        return copia;
    }
}

