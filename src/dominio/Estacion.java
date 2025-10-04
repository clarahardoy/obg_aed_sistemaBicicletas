
package dominio;

import java.util.Objects;
import tads.ListaSE;


public class Estacion implements Comparable<Estacion> {
    private  String nombre;
    private String barrio;
    private int capacidad;
    public ListaSE<Bicicleta> bicicletas;
    
    public Estacion(String nombre, String barrio, int capacidad){
        this.nombre = nombre;
        this.barrio = barrio;
        this.capacidad = capacidad;
        this.bicicletas = new ListaSE<>();
    }

    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getBarrio() {
        return barrio;
    }
    
    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public int getCapacidad() {
        return capacidad;
    }
    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.nombre);
        return hash;
    }
    
    public ListaSE<Bicicleta> getBicicletas() {
        return bicicletas;
    }

    public void setBicicletas(ListaSE<Bicicleta> bicicletas) {
        this.bicicletas = bicicletas;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Estacion other = (Estacion) obj;
        return Objects.equals(this.nombre, other.nombre);
    }
    
    @Override
    public int compareTo(Estacion obj) {
        if (obj == null) return 1; 
       
        return this.nombre.trim().compareToIgnoreCase(obj.nombre.trim());
    }
}

