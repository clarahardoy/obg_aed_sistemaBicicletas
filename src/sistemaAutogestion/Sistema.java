package sistemaAutogestion;

//Agregar aquí nombres y números de estudiante de los integrantes del equipo

import dominio.*;
import tads.ListaNodos;

public class Sistema implements IObligatorio {
    
    private ListaNodos<Estacion> estaciones;
    private ListaNodos<Usuario> usuarios;
    private ListaNodos<Bicicleta> bicicletas;
    
    
    @Override
    public Retorno crearSistemaDeGestion() {
        estaciones = new ListaNodos();
        usuarios = new ListaNodos();
        bicicletas = new ListaNodos();

        return Retorno.ok();
    }
    
    private boolean tipoValido(String tipo) {
        
    if (tipo == null || tipo.isEmpty()) 
        return false;
    
    String t = tipo.toUpperCase();           
        return t.equals("URBANA") || t.equals("MOUNTAIN") || t.equals("ELECTRICA");
    }

    @Override
    public Retorno registrarEstacion(String nombre, String barrio, int capacidad) {
        
        if(nombre == null || nombre.isEmpty() || barrio == null || barrio.isEmpty())
            return Retorno.error1();
        
        if(capacidad <= 0)
            return Retorno.error2();
        
        Estacion e = new Estacion(nombre, barrio, capacidad);
        if(estaciones.existeElemento(e))
            return Retorno.error3();
        
        estaciones.agregarAlFinal(e);
        return Retorno.ok();
     
    }

    @Override
    public Retorno registrarUsuario(String cedula, String nombre) {
        
        if(cedula == null || cedula.isEmpty() || nombre == null || nombre.isEmpty())
            return Retorno.error1();
        
        if(!cedula.matches("\\d{8}"))//cedula debe ser exactamente de 8 digitos
                return Retorno.error2();
        
        Usuario u = new Usuario(cedula, nombre);
        if(usuarios.existeElemento(u))
            return Retorno.error3();
        
        usuarios.agregarAlFinal(u);
        return Retorno.ok();
        
    }

    @Override
    public Retorno registrarBicicleta(String codigo, String tipo) {
        
       if(codigo == null || codigo.isEmpty() || tipo == null || tipo.isEmpty())
           return Retorno.error1();
       
       if(codigo.length() != 6)
           return Retorno.error2();
       
       if(!tipoValido(tipo))
           return Retorno.error3();
       
       Bicicleta b = new Bicicleta(codigo, tipo);
       if(bicicletas.existeElemento(b))
           return Retorno.error4();
       
       //cuando se registra se registra en disponible en el deposito
       b.setEstado("DISPONIBLE");
       b.setUbicacion("DEPOSITO");
       bicicletas.agregarAlFinal(b);
       return Retorno.ok();
       
    }

    @Override //HACER
    public Retorno marcarEnMantenimiento(String codigo, String motivo) {
        return Retorno.noImplementada();
    }

    @Override //HACER
    public Retorno repararBicicleta(String codigo) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno eliminarEstacion(String nombre) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno asignarBicicletaAEstacion(String codigo, String nombreEstacion) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno alquilarBicicleta(String cedula, String nombreEstacion) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno devolverBicicleta(String cedula, String nombreEstacionDestino) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno deshacerUltimosRetiros(int n) {
        return Retorno.noImplementada();
    }

    @Override //HACER
    public Retorno obtenerUsuario(String cedula) {
        return Retorno.noImplementada();
    }

    @Override //HACER
    public Retorno listarUsuarios() {
        return Retorno.noImplementada();
    }

    @Override //HACER
    public Retorno listarBicisEnDeposito() {
        return Retorno.noImplementada();
    }

    @Override //HACER
    public Retorno informaciónMapa(String[][] mapa) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno listarBicicletasDeEstacion(String nombreEstacion) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno estacionesConDisponibilidad(int n) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno ocupacionPromedioXBarrio() {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno rankingTiposPorUso() {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno usuariosEnEspera(String nombreEstacion) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno usuarioMayor() {
        return Retorno.noImplementada();
    }

}
