
package dominio;

import java.util.Objects;


public class Alquiler implements Comparable<Alquiler> {
    private String id;
    public Usuario usuario;
    public Bicicleta bicicleta; 
    public Estacion estacionOrigen; 
    public boolean activo; 
    
     public Alquiler( Usuario usuario, Bicicleta bicicleta, Estacion estacionOrigen) {
        this.id = "";
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

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    

    public String getId() {
        return id;
    }
    

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
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
        final Alquiler other = (Alquiler) obj;
        return Objects.equals(this.id, other.id);
    }
    
  
    @Override
    public int compareTo(Alquiler obj) {
        if (obj == null) return 1;
        String a = (this.id == null) ? "" : this.id.trim();
        String b = (obj.id == null) ? "" : obj.id.trim();
        return a.compareToIgnoreCase(b);
    }
    
}
