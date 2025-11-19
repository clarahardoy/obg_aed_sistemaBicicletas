
package dominio;

public class Usuario implements Comparable<Usuario> {
    private String cedula;
    private String nombre;
    private int cantAlquileres;
    
    public Usuario(String cedula, String nombre){
        this.cedula = cedula;
        this.nombre = nombre;
       
    }
    
    public int getCantAlquileres() {
        return cantAlquileres;
    }

    public void setCantAlquileres(int cantAlquileres) {
        this.cantAlquileres = cantAlquileres;
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

    private String ciNorm() {
        return (cedula == null) ? "" : cedula.trim();
    }

    @Override
    public int hashCode() {
        return ciNorm().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Usuario)) return false;
        Usuario other = (Usuario) obj;
        String a = this.ciNorm();
        String b = (other.cedula == null) ? "" : other.cedula.trim();
        return a.equals(b);
    }

    @Override
    public int compareTo(Usuario obj) {
        if (obj == null) return 1;
        String a = this.ciNorm();
        String b = (obj.cedula == null) ? "" : obj.cedula.trim();
        return a.compareTo(b); // numérico en texto, está bien para 8 dígitos
    }

    public static boolean validarCedula(String cedula) {
        if (cedula == null) return false;
        String ci = cedula.trim();
        if (ci.length() != 8) return false;
        for (int i = 0; i < ci.length(); i++) {
            if (!Character.isDigit(ci.charAt(i))) return false;
        }
        return true;
    }
}
