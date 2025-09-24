
package tads;

public class ListaNodos<T extends Comparable<T>> implements IListaSimple<T> {
    
    private Nodo<T> cabeza; //referencia al primer nodo
    private Nodo<T> fin; //final de la lista "cola"
    private int cantidadElementos; //numero de elementos

    public ListaNodos() {
        this.cabeza = null;
        this.fin = null;
        this.cantidadElementos = 0;
    }

    @Override
    public boolean esVacia() {
        return cabeza == null;
    }

    @Override
    public int getCantidadElementos() {
        return this.cantidadElementos;
    }
    
    @Override
    public boolean existeElemento(T dato) {
        boolean existe = false;
        
        if(!esVacia()){
            Nodo<T> aux = cabeza;
            while(aux != null && !existe){
                if(aux.getDato().equals(dato)){
                    existe = true;
                }else{
                    aux = aux.getSiguiente();
                }
            }
        }
        
        return existe;
    }
    
    @Override
    public void agregarAlInicio(T dato) {
        if(cantidadElementos == 0){ // Es el primer nodo
            Nodo nuevo = new Nodo(dato);
            nuevo.setSiguiente(null);
            cabeza = nuevo;
            fin = nuevo;
        }else{
            Nodo nuevo = new Nodo(dato);
            nuevo.setSiguiente(cabeza);
            cabeza = nuevo;
        }
        cantidadElementos++;
    }

    @Override
    public void agregarAlFinal(T dato) {
        if(esVacia()){
            this.agregarAlInicio(dato);
        }else{
            Nodo nuevo = new Nodo(dato);
            fin.setSiguiente(nuevo);
            fin = fin.getSiguiente();
            cantidadElementos++;
        }
    }

    @Override
    public boolean eliminar(T dato) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void vaciar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public T obtenerElemento(T x) {
        Nodo<T> aux = this.cabeza;
        T ret = null;
        boolean existe = false;

        while (aux != null && !(existe)) {
            if (aux.getDato().equals(x)) {
                existe = true;
                ret = (T) aux.getDato();
            }
            aux = aux.getSiguiente();
        }
        return ret;
    }
    
    @Override
     public T obtenerElementoPorIndice(int indice) {
        if (indice < 0 || indice >= cantidadElementos) {
            return null;
        }

        Nodo<T> aux = this.cabeza;
        int actual = 0;

        while (aux != null) {
            if (actual == indice) {
                return aux.getDato();
            }
            aux = aux.getSiguiente();
            actual++;
        }

        return null;
    }
       
}
