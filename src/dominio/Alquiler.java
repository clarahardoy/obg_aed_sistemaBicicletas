/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

/**
 *
 * @author clarahardoy
 */
public class Alquiler implements Comparable<Alquiler> {
    public Usuario usuario;
    public Bicicleta bicicleta; 
    public Estacion estacionOrigen; 
    public boolean activo; 
    
     public Alquiler(Usuario usuario, Bicicleta bicicleta, Estacion estacionOrigen) {
        this.usuario = usuario;
        this.bicicleta = bicicleta;
        this.estacionOrigen = estacionOrigen;
        this.activo = true;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public Bicicleta getBicicleta() {
        return bicicleta;
    }
    
    public Estacion getEstacionOrigen() {
        return estacionOrigen;
    }
    
    public boolean estaActivo() {
        return activo;
    }
  
   // @Override
   // public int compareTo(Alquiler obj) {
   // }
    
}
