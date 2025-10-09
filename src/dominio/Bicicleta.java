
package dominio;

import java.util.Objects;

public class Bicicleta implements Comparable<Bicicleta> {
    
    private String codigo;
    private String tipo;//URBANA, MOUNTAIN, ELECTRICA
    private String estado = "DISPONIBLE"; //DISPONIBLE, ALQUILADA, MANTENIMIENTO
    private String ubicacion = "DEPOSITO"; // DEPOSITO, ESTACION, EN USO
    private String motivoMantenimiento;
    public Usuario usuario;
    public Estacion estacion;
    
    public Bicicleta(String codigo, String tipo){
        this.codigo = codigo;
        this.tipo = tipo;
       
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Estacion getEstacion() {
        return estacion;
    }

    public void setEstacion(Estacion estacion) {
        this.estacion = estacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getMotivoMantenimiento() {
        return motivoMantenimiento;
    }

    public void setMotivoMantenimiento(String motivoMantenimiento) {
        this.motivoMantenimiento = motivoMantenimiento;
    }

    @Override
    public int hashCode() {
        if (codigo == null) 
            return 0;
        else return codigo.hashCode();
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
        final Bicicleta other = (Bicicleta) obj;
        return Objects.equals(this.codigo, other.codigo);
    }
    
    @Override
    public int compareTo(Bicicleta obj) {
        if (obj == null) return 1;
        
        return this.codigo.strip().compareToIgnoreCase(obj.codigo.strip());
    }    
    
    public String formatearEstado(String estado){
        return estado.substring(0,1).toUpperCase() + estado.substring(1).toLowerCase();
    }
}
