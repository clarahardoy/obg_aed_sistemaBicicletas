
package tads;


public interface IListaSimple<T> {
    
    //check general
    boolean esVacia();
    
    //para validacion y repostes
    int largo();
    
    //para las altas
    void agregarAlInicio(T dato);
    void agregarAlFinal(T dato);
    
    //para sacar la bici de la estacion 2.5
    boolean eliminar(T dato);
    
    //para reinicializar
    void vaciar();
    
    //para validar si ya existe
    boolean existeElemento(T x);
}
