package CasoPruebaSebastian;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import sistemaAutogestion.IObligatorio;
import sistemaAutogestion.Retorno;
import sistemaAutogestion.Sistema;

public class Test_Estudiantes_parcial {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        // 2.1
        retorno = s.crearSistemaDeGestion();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    /* ===========================
       2.1 crearSistemaDeGestion
       =========================== */
    @Test //ok
    public void crearSistemaDeGestion_Ok() {

        retorno = s.crearSistemaDeGestion();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }
    
     /* ===========================
       2.2 registrarEstacion
       =========================== */

    @Test //ok
    public void registrarEstacion_OK_Multiples() {
        // Capacidad mínima válida (=1) y distintas estaciones deben registrarse OK
        retorno = s.registrarEstacion("E1", "Centro", 1);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarEstacion("E2", "Cordón", 10);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        
        retorno = s.registrarEstacion("E3", "Ciudad Vieja", 10);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

   
    @Test //ok
    public void registrarEstacion_Error01_ParametrosNulosOVacios() {
        // nombre vacío / nulo / en blanco
        retorno = s.registrarEstacion("", "Centro", 5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarEstacion("   ", "Centro", 5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarEstacion(null, "Centro", 5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        // barrio vacío / nulo / en blanco
        retorno = s.registrarEstacion("Estacion01", "", 5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarEstacion("Estacion01", "   ", 5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarEstacion("Estacion01", null, 5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test //ok
    public void registrarEstacion_Error02_CapacidadNoPositiva() {
        retorno = s.registrarEstacion("Estacion01", "Centro", 0);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarEstacion("Estacion02", "Centro", -3);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test //ok
    public void registrarEstacion_Error03_DuplicadoPorNombre() {
        // Nombre único: si ya existe "EstacionX", cualquier intento con el mismo nombre debe fallar,
        // aunque cambien barrio o capacidad.
        s.registrarEstacion("EstacionX", "Centro", 5);
        s.registrarEstacion("E1", "Centro", 1);

        retorno = s.registrarEstacion("EstacionX", "Centro", 5);
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());

        retorno = s.registrarEstacion("EstacionX", "Pocitos", 7);
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

    /* ===========================
       2.3 registrarUsuario
       =========================== */
    @Test //ok
    public void registrarUsuario_Ok() {
        retorno = s.registrarUsuario("12345678", "Ana");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        retorno = s.registrarUsuario("12345671", "Pedro");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        retorno = s.registrarUsuario("42345678", "Martina");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }


    @Test //ok
    public void registrarUsuario_Error02_FormatoCedula() {
        retorno = s.registrarUsuario("1234567", "Ana");     // 7 dígitos
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
        retorno = s.registrarUsuario("123456789", "Ana");   // 9 dígitos
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
        retorno = s.registrarUsuario("12A45678", "Ana");    // no numérica
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test //ok
    public void registrarUsuario_Error03_Duplicado() {
        s.registrarUsuario("87654321", "Luis");
        s.registrarUsuario("87654324", "Pedro");
        s.registrarUsuario("47654321", "Marcelo");
        retorno = s.registrarUsuario("87654321", "Luis");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

    /* ===========================
       2.4 registrarBicicleta
       =========================== */
    @Test //ok
    public void registrarBicicleta_Ok() {
        retorno = s.registrarBicicleta("A00001", "URBANA");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        retorno = s.registrarBicicleta("B00002", "MOUNTAIN");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        retorno = s.registrarBicicleta("C00003", "ELECTRICA");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }


    @Test //ok
    public void registrarBicicleta_Error02_CodigoFormato() {
        retorno = s.registrarBicicleta("ABC", "URBANA");     // < 6
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
        retorno = s.registrarBicicleta("ABCDE12", "URBANA"); // > 6
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }


    /* ===========================
       2.5 marcarEnMantenimiento
       =========================== */
    @Test //ok
    public void marcarEnMantenimiento_Ok_y_RepeticionDaError04() {
        s.registrarBicicleta("M00001", "URBANA");
        retorno = s.marcarEnMantenimiento("M00001", "Rueda pinchada");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Marcar nuevamente -> ya en mantenimiento
        retorno = s.marcarEnMantenimiento("M00001", "otro motivo");
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
    }

 
    /* Nota: ERROR_3 “alquilada” no se prueba en primera entrega, ya que alquilar (2.9) no está requerido. */

 /* ===========================
       2.6 repararBicicleta
       =========================== */
    @Test //ok
    public void repararBicicleta_Ok_DesdeMantenimiento() {
        s.registrarBicicleta("R00001", "URBANA");
        s.registrarBicicleta("R00002", "URBANA");
        s.marcarEnMantenimiento("R00001", "cadena");
        retorno = s.repararBicicleta("R00001");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Reparar otra vez -> ya no está en mantenimiento => ERROR_3
        retorno = s.repararBicicleta("R00001");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }



    /* ===========================
       3.1 obtenerUsuario
       =========================== */
    @Test //ok
    public void obtenerUsuario_Ok() {
        s.registrarUsuario("11112222", "Beatriz");
         s.registrarUsuario("11112223", "Carla");
        retorno = s.obtenerUsuario("11112222");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Beatriz#11112222", retorno.getValorString());
    }

  

    /* ===========================
       3.2 listarUsuarios (orden por nombre ascendente)
       =========================== */
    @Test //ok
    public void listarUsuarios_Ok_OrdenPorNombre() {
        s.registrarUsuario("22223333", "Carlos");
        s.registrarUsuario("33334444", "Ana");
        s.registrarUsuario("44445555", "Beatriz");
        retorno = s.listarUsuarios();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        // Orden esperado: Ana, Beatriz, Carlos
        assertEquals("Ana#33334444|Beatriz#44445555|Carlos#22223333", retorno.getValorString());
    }

    @Test //ok
    public void listarUsuarios_Ok_Vacio() {
        // Sistema recién creado, sin usuarios
        IObligatorio s2 = new Sistema();
        s2.crearSistemaDeGestion();
        Retorno r = s2.listarUsuarios();
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        // La letra no especifica texto; asumimos cadena vacía para “sin datos”.
        assertTrue(r.getValorString() == null || r.getValorString().isEmpty());
    }

    /* ===========================
       3.3 listarBicisEnDeposito (recursiva)
       =========================== */
    @Test
    public void listarBicisEnDeposito_Ok_OrdenIngresoYEstados() {
        s.registrarBicicleta("A00001", "URBANA");    // Disponible (depósito)
        s.registrarBicicleta("A00002", "ELECTRICA"); // Disponible (depósito)
        s.registrarBicicleta("A00003", "MOUNTAIN");  // Disponible (depósito)

        // Marcar una en mantenimiento (sigue en depósito)
        s.marcarEnMantenimiento("A00002", "batería");

        retorno = s.listarBicisEnDeposito();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("A00001#URBANA#Disponible|A00002#ELECTRICA#Mantenimiento|A00003#MOUNTAIN#Disponible",
                retorno.getValorString());
    }

    /* ===========================
       3.4 informaciónMapa
       =========================== */
   
    @Test //ok
    public void informacionMapa_Ok_MaxAmbas_y_NoExisteAscendente() {
        String[][] mapa = new String[][]{
            {"E", "E", "", ""}, // fila 0: 2
            {"", "E", "E", ""}, // fila 1: 2
            {"", "", "", ""} // fila 2: 0
        };

        retorno = s.informaciónMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertTrue(retorno.getValorString().startsWith("2#ambas"));
        assertTrue(retorno.getValorString().endsWith("|no existe"));
    }

   
}
