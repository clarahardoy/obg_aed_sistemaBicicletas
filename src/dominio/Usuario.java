
package dominio;

import java.util.Objects;

public class Usuario implements Comparable<Usuario> {
    private String cedula;
    private String nombre;
    
    public Usuario(String cedula, String nombre){
        this.cedula = cedula;
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        
    if (cedula == null) return 0;
    else return cedula.hashCode();
    
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
        final Usuario other = (Usuario) obj;
        return Objects.equals(this.cedula, other.cedula);
    }

   @Override
    public int compareTo(Usuario obj) {
        if (obj == null) return 1;                 
        return this.cedula.compareTo(obj.cedula);  
    }   
    
    public static boolean validarCedula(String cedula) {
        if (cedula.length() != 8) {
            return false;
        }

        for (int i = 0; i < cedula.length(); i++) {
            char c = cedula.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}
