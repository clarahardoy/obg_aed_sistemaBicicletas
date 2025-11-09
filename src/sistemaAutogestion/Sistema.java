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
        pilaUltimosAlquileres = new PilaSE<>();

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
        if (codigo == null || nombreEstacion == null || codigo.isEmpty() 
                || nombreEstacion.isEmpty()) 
            return Retorno.error1();
        
        Bicicleta bici = null;
        if (this.bicicletas != null) {
            bici = this.bicicletas.obtenerElemento(new Bicicleta(codigo, ""));
        }
        if (bici == null) {
            return Retorno.error2(); // no existe
        }
        //si el estado es null "" sino agarra el estado y se lo pone
        String estado = (bici.getEstado() == null) ? "" : bici.getEstado().trim();
        if (!estado.equalsIgnoreCase("DISPONIBLE")) {
            return Retorno.error2(); // no disponible
        }
        
         Estacion destino = null;
        if (this.estaciones != null) {
            destino = this.estaciones.obtenerElemento(new Estacion(nombreEstacion, "", 0));
        }
        if (destino == null) {
            return Retorno.error3(); // estación no existe
        }
        
        //si ya esta en la misma estacion no se hace nada, esta ok
        if (bici.getEstacion() != null && bici.getEstacion().equals(destino)) {
        bici.setUbicacion("ESTACION"); 
        return Retorno.ok();
        }
        
        int ocupadas = 0;
        if (destino.getBicicletas() != null) {
            ocupadas = destino.getBicicletas().getCantidadElementos();
        }
        if (ocupadas >= destino.getCapacidad()) {
            return Retorno.error4(); // sin anclajes libres
        }
        
        //se elimina bici de la estacion de origen
        Estacion origen = bici.getEstacion();
        if (origen != null && origen.getBicicletas() != null) {
            origen.getBicicletas().eliminar(bici);
        }
        //se ancla la bicci en la estacion destino
        if (destino.getBicicletas() != null) {
        destino.getBicicletas().agregarAlFinal(bici);
        }
        bici.setEstacion(destino);
        bici.setUbicacion("ESTACION");
        // estado se mantiene "DISPONIBLE"

        return Retorno.ok();
        
    }
    
    private Usuario buscarUsuario(String ci) {
        if (usuarios == null || usuarios.esVacia()) return null;
        return usuarios.obtenerElemento(new Usuario(ci, ""));
        }
    
    private Estacion buscarEstacion(String nom) {
        if (estaciones == null || estaciones.esVacia()) return null;
        return estaciones.obtenerElemento(new Estacion(nom, "", 0));
        }
    
    private Bicicleta tomarBiciDisponibleDe(Estacion est) {
        if (est == null || est.getBicicletas() == null || est.getBicicletas().esVacia()) return null;

        int n = est.getBicicletas().getCantidadElementos();
        int i = 0;
        while (i < n) {
            Bicicleta b = est.getBicicletas().obtenerElementoPorIndice(i);

            if (b != null) {
                String estado;
                if (b.getEstado() == null) {
                    estado = "";
                } else {
                    estado = b.getEstado().trim();
                }

                if (estado.equalsIgnoreCase("DISPONIBLE")) {
                    // la saco de la lista de la estación para pasarla a EN USO
                    est.getBicicletas().eliminar(b);
                    return b;
                }
            }

            i = i + 1;
        }

        return null;
    }

    @Override
    public Retorno alquilarBicicleta(String cedula, String nombreEstacion) {
        //validar parámetros
        if (cedula == null || nombreEstacion == null) return Retorno.error1();
        
        String ci  = cedula.trim();
        String nom = nombreEstacion.trim();
        if (ci.isEmpty() || nom.isEmpty()) return Retorno.error1();

        // usuario inexistente
        Usuario usu = buscarUsuario(ci);
        if (usu == null) return Retorno.error2();

        // estación inexistente
        Estacion est = buscarEstacion(nom);
        if (est == null) return Retorno.error3();

        // consulta si hay bici disponible en el momento de alquilar
        Bicicleta bici = tomarBiciDisponibleDe(est);
        if (bici != null) {
            // si hay la marca alguilada y en uso y la saca de la estacion
            bici.setEstado("ALQUILADA");
            bici.setUbicacion("EN USO");
            bici.setUsuario(usu);
            bici.setEstacion(null); 

            // se registra en la pila de alquileres
            if (pilaUltimosAlquileres != null) {
                pilaUltimosAlquileres.apilar(new Alquiler(usu, bici, est));
            }

            return Retorno.ok();
        }

            // si no hay disponibles, el usuario queda en cola de espera
            if (est.getColaEsperaAlquiler() != null) {
                est.getColaEsperaAlquiler().encolar(usu);
            }
            return Retorno.ok();
        }

    @Override
    public Retorno devolverBicicleta(String cedula, String nombreEstacionDestino) {
        if (cedula == null || nombreEstacionDestino == null) return Retorno.error1();
            
        // buscar usuario
        Usuario usuarioBuscado = usuarios.obtenerElemento(new Usuario(cedula, ""));
        if (usuarioBuscado == null) return Retorno.error2(); //usuario inexistente
            
        Bicicleta bici = buscarBicicletaAlquiladaPor(usuarioBuscado);
        if (bici == null) return Retorno.error2(); // wl usuario no tiene bici alquilada
              
        // buscar estacion
        Estacion estacionBuscada = estaciones.obtenerElemento(new Estacion(nombreEstacionDestino, "", 0));
        if (estacionBuscada == null) return Retorno.error3(); //estacion inexistente
            
        // si todo lo anterior ok, marcar alquiler como finalizado           
        marcarAlquilerComoFinalizado(usuarioBuscado, bici);
            
        // chequear si hay usuarios esperando alquilar en esta estación
        if (estacionBuscada.tieneColaEsperaAlquiler()) {

            // buscar usuarios esperando para reasignar la bici
            Usuario usuarioEsperando = estacionBuscada.getColaEsperaAlquiler().desencolar();             
            bici.setUsuario(usuarioEsperando);
            bici.setEstado("ALQUILADA");
            bici.setUbicacion("EN USO");
            bici.setEstacion(null);
        
            // marcar nuevo alquiler en la pila
            if (pilaUltimosAlquileres != null) pilaUltimosAlquileres.apilar(new Alquiler(usuarioEsperando, bici, estacionBuscada));
            return Retorno.ok();
        }

        // si no hay usuarios esperando a alquilar: anclar la bici
        int bicisOcupadas = 0;
        if (estacionBuscada.getBicicletas() != null) {
            bicisOcupadas = estacionBuscada.getBicicletas().getCantidadElementos();
        }

        // anclar la bici si hay espacio dispomible
        if (bicisOcupadas < estacionBuscada.getCapacidad()) {
            bici.setEstado("DISPONIBLE");
            bici.setUbicacion("ESTACION");
            bici.setUsuario(null);
            bici.setEstacion(estacionBuscada);

            if (estacionBuscada.getBicicletas() != null) {
                    estacionBuscada.getBicicletas().agregarAlFinal(bici);
                }
                return Retorno.ok();
                
            } else {
                // si no hay espacio, poner la bici en cola de espera por anclaje
                if (estacionBuscada.getColaEsperaAnclaje() != null) {
                    estacionBuscada.getColaEsperaAnclaje().encolar(bici);
                }
                
                bici.setEstado("DISPONIBLE");
                bici.setUbicacion("ESTACION"); // la bici queda esperando ser anclada
                bici.setUsuario(null);
                bici.setEstacion(estacionBuscada);

                return Retorno.ok();
            }
    }
 
    private Bicicleta buscarBicicletaAlquiladaPor(Usuario usuario) {
        if (bicicletas == null || bicicletas.esVacia()) return null;

        int cant = bicicletas.getCantidadElementos();
        for (int i = 0; i < cant; i++) {
           Bicicleta bici = bicicletas.obtenerElementoPorIndice(i);

           if (bici != null && bici.getUsuario() != null && bici.getUsuario().equals(usuario)) {
               String estado = bici.getEstado();

               if (estado.equalsIgnoreCase("ALQUILADA")) return bici;

               }
           }

        return null;
     }
        
    private void marcarAlquilerComoFinalizado(Usuario usuario, Bicicleta bici) {
        if (pilaUltimosAlquileres == null || pilaUltimosAlquileres.esVacia()) {
            return;
        }

        // usar pila auxiliar para encontrar el alquiler y marcarlo como inactivo
        PilaSE<Alquiler> pilaAux = new PilaSE<>();
        boolean encontrado = false;

        while (!pilaUltimosAlquileres.esVacia() && !encontrado) {
            Alquiler alquiler = pilaUltimosAlquileres.desapilar();

            if (alquiler != null && alquiler.getUsuario() != null && alquiler.getBicicleta() != null) {
                if (alquiler.getUsuario().equals(usuario) && alquiler.getBicicleta().equals(bici) && alquiler.isActivo()) {
                    alquiler.setActivo(false); // finalizar
                    encontrado = true;
                }
            }

            pilaAux.apilar(alquiler); // guardar en la auxiliar
            
        }

        // restaurar la pila
        while (!pilaAux.esVacia()) {
            pilaUltimosAlquileres.apilar(pilaAux.desapilar());
        }
        }

    @Override
    public Retorno deshacerUltimosRetiros(int n) {
            
        if (n <= 0) {
            return Retorno.error1();
        }

        // Si no hay historial, no hay nada para deshacer
        if (pilaUltimosAlquileres == null || pilaUltimosAlquileres.esVacia()) {
            return Retorno.ok("");
        }

        String resultado = "";//va acumulando resultados
        int deshechos = 0;//cuenta los retiros deshechos
            
        //es con pila porque el ultimo que se hizo se deshace primero
        while (deshechos < n && !pilaUltimosAlquileres.esVacia()) {
            Alquiler alq = pilaUltimosAlquileres.desapilar();
            if (alq == null) {
                break;
            }

            Bicicleta b = alq.getBicicleta();
            Usuario  u = alq.getUsuario();
            Estacion origen = alq.getEstacionOrigen();

            // Validación por seguridad
            if (b == null || u == null || origen == null) {
                continue;
            }

           //se revierte alquiler de la bicicleta
            b.setUsuario(null);
            b.setEstado("DISPONIBLE");

                // Si la bici estuviera listada en otra estación, removerla
                Estacion actual = b.getEstacion();
                if (actual != null) {
                    if (!actual.equals(origen)) {
                        if (actual.getBicicletas() != null) {
                            actual.getBicicletas().eliminar(b);
                        }
                    }
                }

                // Devuelve a estación de origen: anclar si hay lugar, sino a la cola de anclaje e bicis
                int ocupadas;
                if (origen.getBicicletas() == null) {
                    ocupadas = 0;
                } else {
                    ocupadas = origen.getBicicletas().getCantidadElementos();
                }

                if (ocupadas < origen.getCapacidad()) {

                    if (origen.getBicicletas() != null) {
                        if (!origen.getBicicletas().existeElemento(b)) {
                            origen.getBicicletas().agregarAlFinal(b);
                        }
                    }
                    b.setUbicacion("ESTACION");
                    b.setEstacion(origen);
                } else {
                    // Si no hay lugar encolar la bici
                    if (origen.getColaEsperaAnclaje() != null) {
                        origen.getColaEsperaAnclaje().encolar(b);
                    }
                    b.setUbicacion("ESTACION"); // esperando anclaje en esa estación
                    b.setEstacion(origen);
                }

                // se marca el alquiler activo como false
                alq.setActivo(false);

                // armado del formato de salida
                String item = b.getCodigo() + "#" + u.getCedula() + "#" + origen.getNombre();
                if (deshechos == 0) {
                    resultado = item;
                } else {
                    resultado = resultado + "|" + item;
                }

                deshechos = deshechos + 1;
            }

            return Retorno.ok(resultado);
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
        if (this.bicicletas == null || this.bicicletas.esVacia()) {
            return Retorno.ok("");
        }
        String resultado = listarDepositoRec(0, this.bicicletas.getCantidadElementos());
        return Retorno.ok(resultado);
    }

    private String listarDepositoRec(int i, int total) {
        if (i >= total) {//cuando se pasa del ultimo indice muestra "" para que no muestre separadores extra
            return "";
        }

        Bicicleta b = this.bicicletas.obtenerElementoPorIndice(i);
        //si esta en deposito, muestra la concatenacion, sino ""
        //es donde esta parado
        String actual = "";
        if (b != null) {
            String ubi = b.getUbicacion();
            if (ubi != null && "DEPOSITO".equalsIgnoreCase(ubi)) {
                actual = b.getCodigo() + "#" + b.getTipo() + "#" + b.formatearEstado(b.getEstado());
            }
        }
        //procesa todo lo que viene despues, de i+1 hasta el final
        String resto = listarDepositoRec(i + 1, total);

        if (actual.isEmpty()) {//si no hay nada en actual se devuelve resto
            return resto;
        } else if (resto.isEmpty()) {//si lo que le sigue esta vacio se devuelve actual
            return actual;
        } else {
            return actual + "|" + resto;//si hay en ambos, se unen con |
        }
    }

    @Override
    public Retorno informacionMapa(String[][] mapa) {

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
        if (nombreEstacion == null) {
            return Retorno.ok("");
        }
        nombreEstacion = nombreEstacion.trim();
        if (nombreEstacion.isEmpty()) {
            return Retorno.ok("");
        }

        // Busca la estación por nombre
        Estacion est;
        if (estaciones == null || estaciones.esVacia()) {
            est = null;
        } else {
            est = estaciones.obtenerElemento(new Estacion(nombreEstacion, "", 0));
        }
        if (est == null) {
            return Retorno.ok("");
        }

        // Si no tiene bicis, salida vacía o sea se ejecuto bien pero no hay para mostrar
        if (est.getBicicletas() == null || est.getBicicletas().esVacia()) {
            return Retorno.ok("");
        }
        
        //lista auxiliar ordenada
        tads.ListaSE<String> codigosOrdenados = new tads.ListaSE<>();

        int cantBici = est.getBicicletas().getCantidadElementos();
        int i = 0;
        while (i < cantBici) {
            Bicicleta bici = est.getBicicletas().obtenerElementoPorIndice(i);
            if (bici != null && bici.getCodigo() != null) {
                String cod = bici.getCodigo().trim();
                if (!cod.isEmpty()) {
                    codigosOrdenados.adicionarOrdenado(cod); // se inserta en orden creciente
                }
            }
            i = i + 1; // adiciono a la variable
        }

        // se arma el resultado
        String resultado = "";
        int cantCodOrd = codigosOrdenados.getCantidadElementos();
        int j = 0;
        while (j < cantCodOrd) {
            String cod = codigosOrdenados.obtenerElementoPorIndice(j);
            if (cod != null && !cod.isEmpty()) {
                if (resultado.isEmpty()) {
                    resultado = cod;
                } else {
                    resultado = resultado + "|" + cod;
                }
            }
            j = j + 1;
        }

    return Retorno.ok(resultado);
}

    @Override
    public Retorno estacionesConDisponibilidad(int n) {
        if (n <= 1) return Retorno.error1(); 
        
        
    int contadorEstaciones = 0; 
    int cantEstaciones = estaciones.getCantidadElementos();
    
    for (int i = 0; i < cantEstaciones; i++) {
        Estacion estacion = estaciones.obtenerElementoPorIndice(i);
        
        if (estacion != null) {
            int bicisDisponibles = contarBicisDisponibles(estacion);
            if (bicisDisponibles > n) contadorEstaciones++;
            }
        }
    return Retorno.ok(contadorEstaciones);
    }
    
    private int contarBicisDisponibles(Estacion estacion) {
    if (estacion == null || estacion.getBicicletas() == null || estacion.getBicicletas().esVacia()) {
        return 0;
    }
    
    int contador = 0;
    int cantBicis = estacion.getBicicletas().getCantidadElementos();
    
    for (int i = 0; i < cantBicis; i++) {
        Bicicleta bici = estacion.getBicicletas().obtenerElementoPorIndice(i);
        
        if (bici != null) {
            String estado = bici.getEstado();
            
            // contar la bici si está disponible
            if (estado.equalsIgnoreCase("DISPONIBLE")) contador++;
        }
    }
    return contador;
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
        if (nombreEstacion == null) {
            return Retorno.ok("");
        }
        nombreEstacion = nombreEstacion.trim();
        if (nombreEstacion.isEmpty()) {
            return Retorno.ok("");
        }

        // Buscar estación por nombre
        Estacion est;
        if (estaciones == null || estaciones.esVacia()) {
            est = null;
        } else {
            est = estaciones.obtenerElemento(new Estacion(nombreEstacion, "", 0));
        }
        if (est == null) {
            return Retorno.ok("");
        }

        // agarrar la cola de espera por alquiler
        tads.ColaSE<Usuario> cola = est.getColaEsperaAlquiler();
        if (cola == null || cola.esVacia()) {
            return Retorno.ok("");
        }

        //se recorre y se agrega en la aux sin perder el orden
        //para listar hay que desencolar para leer los elementos y se guardan en la aux
        
        tads.ColaSE<Usuario> aux = new tads.ColaSE<>();
        String resultado = "";

        while (!cola.esVacia()) {
            Usuario u = cola.desencolar();
            if (u != null) {
                String ci;
                if (u.getCedula() == null) {
                    ci = "";
                } else {
                    ci = u.getCedula().trim();
                }

                if (!ci.isEmpty()) {
                    if (resultado.isEmpty()) {
                        resultado = ci;
                    } else {
                        resultado = resultado + "|" + ci;
                    }
                }

                aux.encolar(u); 
            }
        }

        //despues se retaura la aux y queda en el mismo orden que estaba
        while (!aux.esVacia()) {
            cola.encolar(aux.desencolar());
        }

        return Retorno.ok(resultado);
    }

    @Override
    public Retorno usuarioMayor() {
        PilaSE<Alquiler> pilaAux = new PilaSE<>();
       
        int cantUsuarios = usuarios.getCantidadElementos();
        int[] contadorDeUsuario = new int[cantUsuarios];
        
        while (!pilaUltimosAlquileres.esVacia()) {
            
            Alquiler alquiler = pilaUltimosAlquileres.desapilar();
            pilaAux.apilar(alquiler);

            if (alquiler != null && alquiler.getUsuario() != null) {
                
                // buscar indice del usuario en la lista
                for (int i = 0; i < cantUsuarios; i++) {
                    Usuario usuario = usuarios.obtenerElementoPorIndice(i);
                    
                    if (usuario != null && usuario.equals(alquiler.getUsuario())) {
                        contadorDeUsuario[i]++;
                        break;
                    }
                }
            }
        }
        
        // restaurar pila original
        while (!pilaAux.esVacia()) {
            pilaUltimosAlquileres.apilar(pilaAux.desapilar());
        }
        
        int mayorCantAlquileres = 0;
        Usuario usuarioGanador = null;
         
        for (int i = 0; i < cantUsuarios; i++) {
            if (contadorDeUsuario[i] > mayorCantAlquileres) {
                mayorCantAlquileres = contadorDeUsuario[i];
                usuarioGanador = usuarios.obtenerElementoPorIndice(i);
            } 
            
            //si la cantidad de alquileres del usuario pasando por el FOR es igual al que actualmente tiene mas alquileres
            //gana el que tiene la cedula mas chica
            else if (contadorDeUsuario[i] == mayorCantAlquileres && contadorDeUsuario[i] > 0) {
                
                Usuario actual = usuarios.obtenerElementoPorIndice(i);
                if (actual != null && usuarioGanador != null) {
                    if (actual.getCedula().compareTo(usuarioGanador.getCedula()) < 0) {
                        usuarioGanador = actual;
                    }
                }
            }
        }
        
         // si no se encontró ningún usuario con alquileres
          if (usuarioGanador == null || mayorCantAlquileres == 0)  return Retorno.ok("");
          
          // si no, retorno el ganador
          return Retorno.ok(usuarioGanador.getCedula());
}
    }