
package tads;


public interface IListaSimple<T> {
    
    //check general
    boolean esVacia();
    
    //para validacion y repostes
    int getCantidadElementos();
    
    //para las altas
    void agregarAlInicio(T dato);
    void agregarAlFinal(T dato);
    
    //para sacar la bici de la estacion 2.5
    boolean eliminar(T dato);
    
    //para reinicializar
    void vaciar();
    
    //para validar si ya existe
    boolean existeElemento(T x);
    
    public T obtenerElemento(T x);
    
    public T obtenerElementoPorIndice(int indice);  
}
