package sistemaAutogestion;

//Agregar aquí nombres y números de estudiante de los integrantes del equipo

import dominio.*;
import tads.ListaSE;

public class Sistema implements IObligatorio {
    
    private ListaSE<Estacion> estaciones;
    private ListaSE<Usuario> usuarios;
    private ListaSE<Bicicleta> bicicletas;
    //private ListaSE<Bicicleta> deposito;

    
    
    // ------------- ADMINISTRACION DE BICICLETAS ------------------------
    
    @Override
    public Retorno crearSistemaDeGestion() {
        estaciones = new ListaSE();
        usuarios = new ListaSE();
        bicicletas = new ListaSE();
        //deposito   = new ListaSE<>();

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
       //deposito.agregarAlFinal(b);

       return Retorno.ok();
       
    }

    @Override
        // Solo si la bici no está alquilada. Pasa a estado “Mantenimiento” y queda fuera de servicio, siendo retirada y dejada en
        // el depósito (en caso de no estarlo actualmente).
    public Retorno marcarEnMantenimiento(String codigo, String motivo) {
        
        // validar parametros
        if(codigo == null || codigo.isEmpty() || motivo == null || motivo.isEmpty())
           return Retorno.error1();
        
        Bicicleta aBuscar = new Bicicleta(codigo.trim(), "");
        Bicicleta bicicleta = this.bicicletas.obtenerElemento(aBuscar);
        
        if (bicicleta == null) {
            return Retorno.error2(); 
        }
        
        // validar que la bici no está alquilada
        String estado = bicicleta.getEstado();
        if ("ALQUILADA".equalsIgnoreCase(estado)) {
            return Retorno.error3();
        }
        
        if ("MANTENIMIENTO".equalsIgnoreCase(estado)) {
            return Retorno.error4();
        }
        

        // si estaba en una estación, liberarla de esa estación
        Estacion estacion = bicicleta.getEstacion();
        if (estacion != null) {
            estacion.getBicicletas().eliminar(bicicleta);
            bicicleta.setEstacion(null);
        }
        
        
        bicicleta.setUbicacion("DEPOSITO");
        
        //if (this.deposito != null && !this.deposito.existeElemento(bicicleta)) {
        //this.deposito.agregarAlFinal(bicicleta);
        //}
        
        bicicleta.setUsuario(null);
        bicicleta.setEstado("MANTENIMIENTO");
        bicicleta.setMotivoMantenimiento(motivo.trim());
       

        return Retorno.ok();
    }

    @Override //HACER
    public Retorno repararBicicleta(String codigo) {
        
        if(codigo == null || codigo.trim().isEmpty()){
            return Retorno.error1();
        }
        
        // buscar bici
        Bicicleta aBuscar = new Bicicleta(codigo.trim(), "");
        Bicicleta bicicleta = this.bicicletas.obtenerElemento(aBuscar);
        if (bicicleta == null){
            return Retorno.error2();
        }
        
        if (!"MANTENIMIENTO".equalsIgnoreCase(bicicleta.getEstado())){
        return Retorno.error3();
        }
        
        bicicleta.setEstado("DISPONIBLE");
        bicicleta.setUbicacion("DEPOSITO");
        
        return Retorno.ok();
    }

    @Override //NO VA
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

    @Override
    public Retorno obtenerUsuario(String cedula) {
        
        if (cedula == null || cedula.trim().isEmpty()){
            return Retorno.error1(); 
        }
        
        if(!Usuario.validarCedula(cedula)){
            return Retorno.error2(); 
        }
        
        String ci = cedula.trim();
    
        Usuario aBuscar = new Usuario(ci, "");
        Usuario usu = this.usuarios.obtenerElemento(aBuscar); 
        if (usu == null){
            return Retorno.error3(); 
        }
        
        String datosUsuario = usu.getNombre() + "#" + usu.getCedula();
        return Retorno.ok(datosUsuario);
}
    @Override
    public Retorno listarUsuarios() {
        if(this.usuarios == null || this.usuarios.esVacia())
            return Retorno.ok("");
            
        ListaSE<String> ordenados = new ListaSE<>();
        
        int cantidadUsuarios = this.usuarios.getCantidadElementos();
        for(int i = 0; i< cantidadUsuarios; i++){
            Usuario usu = this.usuarios.obtenerElementoPorIndice(i);
            if(usu != null){
                String item = usu.getNombre() + "#" + usu.getCedula();
                ordenados.adicionarOrdenado(item);
            }
        }
        
        String resultado = "";
        int usuariosOrdenados = ordenados.getCantidadElementos();
        for(int i = 0; i < usuariosOrdenados; i++){
            String item = ordenados.obtenerElementoPorIndice(i);
            if(i == 0)
                resultado = item;
            else resultado = resultado + "|" + item;
        }
        return Retorno.ok(resultado);
    }
    

    
    

    @Override //SIN TERMINAR
    public Retorno listarBicisEnDeposito() {
                return Retorno.noImplementada();

//        if(this.bicicletas == null || this.bicicletas.esVacia())
//            return Retorno.ok("");
//        
//        ListaSE<Bicicleta> enDeposito = new ListaSE<>();
//        int totalBicis = this.bicicletas.getCantidadElementos();
//        
//        for(int i = 0; i < totalBicis; i ++){
//            Bicicleta b = this.bicicletas.obtenerElementoPorIndice(i);
//            if(b != null && b.getUbicacion() != null && "DEPOSITO".equalsIgnoreCase(b.getUbicacion())){
//                enDeposito.agregarAlFinal(b);
//            }
//        }
//        if(enDeposito.esVacia())
//            return Retorno.ok("");
//        
//        Bicicleta B = enDeposito.obtenerElementoPorIndice(i);
//    }
    }

    @Override //SIN TERMINAR
    public Retorno informaciónMapa(String[][] mapa) {
                return Retorno.noImplementada();
//        if(mapa == null || mapa.length == 0){
//            return Retorno.ok("0#ambas|no existe");
//        }
//        
//        int filas = mapa.length;
//        int columnas = 0;
//        
//        for(int i = 0; i < filas; i++){
//            if(mapa[i] != null && mapa[i].length > columnas){
//                columnas = mapa[i].length;
//            }
//        }
//    }
        
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