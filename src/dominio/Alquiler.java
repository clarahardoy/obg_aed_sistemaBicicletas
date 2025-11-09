
package dominio;

import java.util.Objects;


public class Alquiler implements Comparable<Alquiler> {
    private static long SEQ = 1;

    private String id;
    public Usuario usuario;
    public Bicicleta bicicleta; 
    public Estacion estacionOrigen; 
    public boolean activo; 
    
     public Alquiler( Usuario usuario, Bicicleta bicicleta, Estacion estacionOrigen) {
        this.id = String.valueOf(SEQ++);
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
    
    public boolean isActivo(){
        return activo; 
    }
    

    @Override
    public int hashCode() {
        return (id == null) ? 0 : id.hashCode();
    }
    

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Alquiler)) return false;
        Alquiler other = (Alquiler) obj;
        return Objects.equals(this.id, other.id);
    }
    
  
    @Override
    public int compareTo(Alquiler obj) {
        if (obj == null) return 1;
     
        long a = (this.id == null || this.id.isBlank()) ? 0L : Long.parseLong(this.id.trim());
        long b = (obj.id == null || obj.id.isBlank()) ? 0L : Long.parseLong(obj.id.trim());
        return Long.compare(a, b);
    }
    
}
