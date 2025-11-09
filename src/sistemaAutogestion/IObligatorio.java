package sistemaAutogestion;

public interface IObligatorio {

    /*
    **************** REGISTROS y ELIMINACIÓN **************************************
     */
    
    Retorno crearSistemaDeGestion();

    Retorno registrarEstacion(String nombre, String barrio, int capacidad);

    Retorno registrarUsuario(String cedula, String nombre);

    Retorno registrarBicicleta(String codigo, String tipo);

    Retorno marcarEnMantenimiento(String codigo, String motivo);  
    
    Retorno repararBicicleta(String codigo);
    
    Retorno eliminarEstacion(String nombre);
    
    Retorno asignarBicicletaAEstacion(String codigo, String nombreEstacion); 
    
    Retorno alquilarBicicleta(String cedula, String nombreEstacion);
   
    Retorno devolverBicicleta(String cedula, String nombreEstacionDestino);
    
    Retorno deshacerUltimosRetiros(int n);
    /*
    **************** REPORTES Y CONSULTAS **************************************
     */
    
    Retorno obtenerUsuario(String cedula);
    
    Retorno listarUsuarios();
    
    Retorno listarBicisEnDeposito();
    
    Retorno informacionMapa(String [][] mapa);
    
    Retorno listarBicicletasDeEstacion(String nombreEstacion);    
    
    Retorno estacionesConDisponibilidad(int n);
    
    Retorno ocupacionPromedioXBarrio();
    
    Retorno rankingTiposPorUso();
    
    Retorno usuariosEnEspera(String nombreEstacion);
    
    Retorno usuarioMayor();
    
    
}
