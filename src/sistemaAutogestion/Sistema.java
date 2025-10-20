package sistemaAutogestion;

//Agregar aquí nombres y números de estudiante de los integrantes del equipo

import dominio.*;
import tads.ListaSE;
import tads.PilaSE;

public class Sistema implements IObligatorio {
    
    private ListaSE<Estacion> estaciones;
    private ListaSE<Usuario> usuarios;
    private ListaSE<Bicicleta> bicicletas;
    private PilaSE<Alquiler> pilaUltimosAlquileres; 
    
    // ------------- ADMINISTRACION DE BICICLETAS ------------------------
    
    @Override
    public Retorno crearSistemaDeGestion() {
        estaciones = new ListaSE();
        usuarios = new ListaSE();
        bicicletas = new ListaSE();

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
        
        if(nombre == null || nombre.trim().isEmpty() || barrio == null || barrio.trim().isEmpty())
           
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
        
        if(cedula == null || cedula.trim().isEmpty() || nombre == null || nombre.trim().isEmpty())
            return Retorno.error1();
        
        String cedulaLimpia = cedula.trim();
        String nombreLimpio = nombre.trim();
        
        if(!cedulaLimpia.matches("\\d{8}"))//cedula debe ser exactamente de 8 digitos
                return Retorno.error2();
        
        Usuario u = new Usuario(cedulaLimpia, nombreLimpio);
        if(usuarios.existeElemento(u))
            return Retorno.error3();
        
        usuarios.agregarAlFinal(u);
        return Retorno.ok();
        
    }

    @Override
    public Retorno registrarBicicleta(String codigo, String tipo) {
        
       if(codigo == null || codigo.trim().isEmpty() || tipo == null || tipo.trim().isEmpty())
           return Retorno.error1();
       
        String codLimpio = codigo.trim();
        String tipoLimpio = tipo.trim();
       
       if (!codLimpio.matches("^[A-Z0-9]{6}$")) {
           return Retorno.error2();
       }
       
       if(!tipoValido(tipoLimpio))
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
        if(codigo == null || codigo.trim().isEmpty() || motivo == null || motivo.trim().isEmpty())
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
        bicicleta.setUsuario(null);
        bicicleta.setEstado("MANTENIMIENTO");
        bicicleta.setMotivoMantenimiento(motivo.trim());
        
        return Retorno.ok();
    }

    @Override
    public Retorno repararBicicleta(String codigo) {
        
        if(codigo == null || codigo.trim().isEmpty()){
            return Retorno.error1();
        }
        
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

    @Override
    public Retorno eliminarEstacion(String nombre) {
        
        if (nombre == null || nombre.isEmpty()){
        return Retorno.error1();
        }
         
        Estacion estacionABuscar = new Estacion(nombre, "", 0);
        Estacion estacion = this.estaciones.obtenerElemento(estacionABuscar);
        if (estacion == null) return Retorno.error2(); 
        
        boolean tieneBicicletasAncladas = !estacion.getBicicletas().esVacia();
        boolean tieneColaAlquiler = !estacion.getColaEsperaAlquiler().esVacia();
        boolean tieneColaAnclaje = !estacion.getColaEsperaAnclaje().esVacia();

        if (tieneBicicletasAncladas || tieneColaAlquiler || tieneColaAnclaje) {
            return Retorno.error3(); 
        }
        
        // si todo ok, eliminar
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

    @Override
    public Retorno listarBicisEnDeposito() {

        if(this.bicicletas == null || this.bicicletas.esVacia())
            return Retorno.ok("");
        
        String resultado = "";
        int cantidadBicis = this.bicicletas.getCantidadElementos();
        
        for(int i = 0; i< cantidadBicis; i++){
            Bicicleta b = this.bicicletas.obtenerElementoPorIndice(i);
            if(b != null){
                String ubi = b.getUbicacion();
                if(ubi != null && "DEPOSITO".equalsIgnoreCase(ubi)){
                    
                    String item = b.getCodigo() + "#" + b.getTipo() + "#" + b.formatearEstado(b.getEstado());
                    
                    if(resultado.isEmpty()) resultado = item;
                    else resultado = resultado + "|" + item;
                }
            }
        }
        return Retorno.ok(resultado);
    }

    
        @Override
        public Retorno informaciónMapa(String[][] mapa) {

         // Caso vacío o nulo 
        if (mapa == null || mapa.length == 0) {
            return Retorno.ok("0#ambas|no existe");
        }

        int filas = mapa.length;//cantidad filas
        int cols = 0;//secalcula por fila porque puede no tener el mismo ancho

        //se recorren las filas y si es mayor al nro de cols, cols pasa a ser lo contado
        for (int i = 0; i < filas; i++) {
            if (mapa[i] != null && mapa[i].length > cols) {
                cols = mapa[i].length;
            }
        }
        // si es cero significa que son null o vacias
        if (cols == 0) {
            return Retorno.ok("0#ambas|no existe");
        }


        int[] filasCnt = new int[filas];//giarda estaciones de la filai
        int[] colsCnt  = new int[cols];//guarda estaciones de columna
        
        //la fila puede tener distinto largo o no tener nada,
        //se calcula el ancho y se recorre
        for (int i = 0; i < filas; i++) {
            int ancho;
                if (mapa[i] == null) {
                    ancho = 0;
                } else {
                    ancho = mapa[i].length;
                }
            for (int j = 0; j < ancho; j++) {
                 String raw = mapa[i][j];
                if (raw == null) continue;

                String celda = raw.trim();
                if (celda.isEmpty()) continue; 
                if (celda.matches("(?i)^e\\d*$")) {//se detecta si hay estacion en la celda (empieza con E) y se cuenta
                    filasCnt[i] = filasCnt[i] + 1;
                    colsCnt[j]  = colsCnt[j] + 1;
                }
            }
        }

        //maximo de fila se guarda
        int maxFila = 0;
        for (int i = 0; i < filas; i++) {
            if (filasCnt[i] > maxFila)
                maxFila = filasCnt[i];
        }
        //maximo columna se guarda
        int maxCol = 0;
        for (int j = 0; j < cols; j++) {
            if (colsCnt[j] > maxCol) 
                maxCol = colsCnt[j];
        }

        //para ver quien tiene los maximos y asignarle un resultado
        int max;
        String resultado;

        if (maxFila > maxCol) {
            max = maxFila;
            resultado = "fila";
        } else if (maxCol > maxFila) {
            max = maxCol;
            resultado = "columna";
        } else {
            max = maxFila;
            resultado = "ambas";
        }

        //existen 3 columnras ascendentes
        String existeAsc;
        if (cols < 3) {//si hay menos de 3 directamente no existe
            existeAsc = "no existe";
        } else {
            boolean existe = false;
            for (int j = 0; j <= cols - 3 && !existe; j++) {//es hasta -3 para que j+2 no se salga
                int a = colsCnt[j];
                int b = colsCnt[j + 1];
                int c = colsCnt[j + 2];
                if (a < b && b < c) {
                    existe = true;
                }
            }
        if (existe) {
            existeAsc = "existe";
        } else {
            existeAsc = "no existe";
            }        
        
        }

    String valor = max + "#" + resultado + "|" + existeAsc;
    return Retorno.ok(valor);
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