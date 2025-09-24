package sistemaAutogestion;

//Agregar aquí nombres y números de estudiante de los integrantes del equipo

import dominio.*;
import tads.ListaNodos;

public class Sistema implements IObligatorio {
    
    private ListaNodos<Estacion> estaciones;
    private ListaNodos<Usuario> usuarios;
    private ListaNodos<Bicicleta> bicicletas;
    
    
    // ------------- ADMINISTRACION DE BICICLETAS ------------------------
    
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
    // Solo si la bici no está alquilada. Pasa a estado “Mantenimiento” y queda fuera de servicio, siendo retirada y dejada en
      // el depósito (en caso de no estarlo actualmente).
    public Retorno marcarEnMantenimiento(String codigo, String motivo) {
        
        // validar parametros
        if(codigo == null || codigo.isEmpty() || motivo == null || motivo.isEmpty())
           return Retorno.error1();
        
        Bicicleta aBuscar = new Bicicleta(codigo, "");
        Bicicleta bicicleta = this.bicicletas.obtenerElemento(aBuscar);
        
        if (bicicleta == null) {
            return Retorno.error2(); 
        }
        
        // validar que la bici no está alquilada
        if ("ALQUILADA".equals(bicicleta.getEstado())) {
            return Retorno.error3();
        }
        
        if ("MANTENIMIENTO".equals(bicicleta.getEstado())) {
            return Retorno.error4();
        }
        

// si estaba en una estación, liberarla de esa estación
        Estacion estacion = bicicleta.getEstacion();
        if (estacion != null) {
            estacion.getBicicletas().eliminar(bicicleta);
            bicicleta.setEstacion(null);
        }
        
        if (!"DEPOSITO".equals(bicicleta.getUbicacion())){
             bicicleta.setUbicacion("DEPOSITO");
        }
        
        bicicleta.setUsuario(null);
        bicicleta.setEstado("MANTENIMIENTO");
        bicicleta.setMotivoMantenimiento(motivo);
       

        return Retorno.ok();
    }

    @Override //HACER
    public Retorno repararBicicleta(String codigo) {
        
        if(codigo == null || codigo.isEmpty()){
            return Retorno.error1();
        }
        
        // buscar bici
        Bicicleta aBuscar = new Bicicleta(codigo, "");
        Bicicleta bicicleta = this.bicicletas.obtenerElemento(aBuscar);
        if (bicicleta == null){
            return Retorno.error2();
        }
        
        if (!"MANTENIMIENTO".equals(bicicleta.getEstado())){
        return Retorno.error3();
        }
        
        bicicleta.setUbicacion("DEPOSITO");
        bicicleta.setEstado("DISPONIBLE");
        return Retorno.ok();
    }

    @Override
    public Retorno eliminarEstacion(String nombre) {
        
        if (nombre == null || nombre.isEmpty()){
        return Retorno.error1();
        }
         
        Estacion estacionABuscar = new Estacion(nombre, "", 0);
        Estacion estacion = this.estaciones.obtenerElemento(estacionABuscar);
        if (estacion == null) return Retorno.error2(); 
        
        // la letra dice: ni reservas/colas pendientes. preguntarle
        // boolean tieneReservasPendientes = estacion.;
        boolean tieneBicicletasAncladas = !estacion.getBicicletas().esVacia();
        
        if (tieneBicicletasAncladas) {
            return Retorno.error3(); 
        }
        
        this.estaciones.eliminar(estacion);
        return Retorno.ok(); 
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
    
    // ---------------------- LISTADOS Y REPORTES ----------------------

    @Override //HACER 
    public Retorno obtenerUsuario(String cedula) {
        
        if (cedula == null || cedula.isEmpty()){
            return Retorno.error1(); 
        }
        
        if (!this.validarCedula(cedula)){
            return Retorno.error1(); 
        }
        
        Usuario aBuscar = new Usuario(cedula, "");
        Usuario usu = this.usuarios.obtenerElemento(aBuscar); 
        if (usu == null){
            return Retorno.error3(); 
        }
        
        String datosUsuario = usu.getCedula() + "#" + usu.getNombre() + "#";
        return Retorno.ok(datosUsuario);
}
    @Override //HACER
    //falta lo de que los usuarios estén ordenados por nobmre. será una precondicion? 
    public Retorno listarUsuarios() {
        String resultado = ""; 
        int cantidadUsuarios = usuarios.getCantidadElementos();
        
         for (int i = 0; i < cantidadUsuarios; i++) {
             Usuario usuarioActual = usuarios.obtenerElementoPorIndice(i);
           
            resultado += usuarioActual.getNombre() + "#" + usuarioActual.getCedula();

            if (i < cantidadUsuarios - 1) {
            resultado += "|";
         }   
        }
        return Retorno.ok(resultado);
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
    
    public boolean validarCedula(String cedula) {
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