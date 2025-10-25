
package dominio;

import java.util.Objects;
import tads.ListaSE;
import tads.ColaSE;


public class Estacion implements Comparable<Estacion> {
    private  String nombre;
    private String barrio;
    private int capacidad;
    public ListaSE<Bicicleta> bicicletas; 
    public ColaSE<Usuario> colaEsperaAlquiler; //usuarios esperando bici
    public ColaSE<Bicicleta> colaEsperaAnclaje ;//bicis esperando anclaje
    
    public Estacion(String nombre, String barrio, int capacidad){
        this.nombre = nombre;
        this.barrio = barrio;
        this.capacidad = capacidad;
        this.bicicletas = new ListaSE<>();
        this.colaEsperaAlquiler = new ColaSE<>(); // usuarios esperando bici
        this.colaEsperaAnclaje = new ColaSE<>(); // bici esperando anclaje
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

    public ListaSE<Bicicleta> getBicicletas() { 
        return bicicletas; 
    }
    
    public void setBicicletas(ListaSE<Bicicleta> bicicletas) { 
        this.bicicletas = bicicletas; 
    }

    public ColaSE<Usuario> getColaEsperaAlquiler() { 
        return colaEsperaAlquiler; 
    }
    public void setColaEsperaAlquiler(ColaSE<Usuario> q) { 
        this.colaEsperaAlquiler = q; 
    }

    public ColaSE<Bicicleta> getColaEsperaAnclaje() { 
        return colaEsperaAnclaje; 
    }
    public void setColaEsperaAnclaje(ColaSE<Bicicleta> q) { 
        this.colaEsperaAnclaje = q; 
    }

    public boolean tieneEspacioDisponible() { 
        return bicicletas.getCantidadElementos() < capacidad; 
    }
    public int getCantidadBicicletasAncladas() { 
        return bicicletas.getCantidadElementos(); 
    }
    public int getEspaciosLibres() { 
        return capacidad - bicicletas.getCantidadElementos(); 
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Estacion)) return false;
        Estacion other = (Estacion) o;
        return Objects.equals(this.nombre, other.nombre);
    }

    @Override
    public int hashCode() { return Objects.hash(nombre); }

    @Override
    public int compareTo(Estacion obj) {
        if (obj == null) return 1;
        return this.nombre.trim().compareToIgnoreCase(obj.nombre.trim());
    }
}

