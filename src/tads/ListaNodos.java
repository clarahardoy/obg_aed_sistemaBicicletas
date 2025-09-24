
package tads;

public class ListaNodos<T extends Comparable> implements IListaSimple<T> {
    
    private Nodo<T> cabeza; //referencia al primer nodo
    private Nodo<T> fin; //final de la lista "cola"
    private int cantidad; //numero de elementos

    public ListaNodos() {
        this.cabeza = null;
        this.fin = null;
        this.cantidad = 0;
    }

    @Override
    public boolean esVacia() {
        return cabeza == null;
    }

    @Override
    public int largo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
        if(cantidad == 0){ // Es el primer nodo
            Nodo nuevo = new Nodo(dato);
            nuevo.setSiguiente(null);
            cabeza = nuevo;
            fin = nuevo;
        }else{
            Nodo nuevo = new Nodo(dato);
            nuevo.setSiguiente(cabeza);
            cabeza = nuevo;
        }
        cantidad++;
    }

    @Override
    public void agregarAlFinal(T dato) {
        if(esVacia()){
            this.agregarAlInicio(dato);
        }else{
            Nodo nuevo = new Nodo(dato);
            fin.setSiguiente(nuevo);
            fin = fin.getSiguiente();
            cantidad++;
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
}
