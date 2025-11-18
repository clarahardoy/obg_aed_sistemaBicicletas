/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CasosPruebaYanet_2;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import sistemaAutogestion.IObligatorio;
import sistemaAutogestion.Retorno;
import sistemaAutogestion.Sistema;

public class Test_Docentes_correccion {

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
    @Test
    public void crearSistemaDeGestion_Ok() {

        retorno = s.crearSistemaDeGestion();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }
    
     /* ===========================
       2.2 registrarEstacion
       =========================== */

    @Test
    public void registrarEstacion_OK_Multiples() {
        // Capacidad mínima válida (=1) y distintas estaciones deben registrarse OK
        retorno = s.registrarEstacion("E1", "Centro", 1);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarEstacion("E2", "Cordón", 10);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        
         retorno = s.registrarEstacion("E3", "Ciudad Vieja", 10);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    public void registrarEstacion_OK_BarrioDiferente_MismoCapacidad_NombresDistintos() {
        retorno = s.registrarEstacion("EstacionA", "Centro", 5);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Mismo barrio/capacidad pero nombre distinto => OK
        retorno = s.registrarEstacion("EstacionB", "Centro", 5);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
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

    @Test
    public void registrarEstacion_Error02_CapacidadNoPositiva() {
        retorno = s.registrarEstacion("Estacion01", "Centro", 0);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarEstacion("Estacion02", "Centro", -3);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
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
    @Test
    public void registrarUsuario_Ok() {
        retorno = s.registrarUsuario("12345678", "Ana");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        retorno = s.registrarUsuario("12345671", "Pedro");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        retorno = s.registrarUsuario("42345678", "Martina");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    public void registrarUsuario_Error01_NulosOVacios() {
        retorno = s.registrarUsuario(null, "Ana");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarUsuario("12345678", null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarUsuario("", "Ana");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarUsuario("12345678", "");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarUsuario("   ", "Ana");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarUsuario("12345678", "   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void registrarUsuario_Error02_FormatoCedula() {
        retorno = s.registrarUsuario("1234567", "Ana");     // 7 dígitos
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
        retorno = s.registrarUsuario("123456789", "Ana");   // 9 dígitos
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
        retorno = s.registrarUsuario("12A45678", "Ana");    // no numérica
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
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
    @Test
    public void registrarBicicleta_Ok() {
        retorno = s.registrarBicicleta("A00001", "URBANA");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        retorno = s.registrarBicicleta("B00002", "MOUNTAIN");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        retorno = s.registrarBicicleta("C00003", "ELECTRICA");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    public void registrarBicicleta_Error01_NulosOVacios() {
        retorno = s.registrarBicicleta(null, "URBANA");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        retorno = s.registrarBicicleta("A00001", null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        retorno = s.registrarBicicleta("", "URBANA");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        retorno = s.registrarBicicleta("A00001", "");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        retorno = s.registrarBicicleta("   ", "URBANA");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        retorno = s.registrarBicicleta("A00001", "   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void registrarBicicleta_Error02_CodigoFormato() {
        retorno = s.registrarBicicleta("ABC", "URBANA");     // < 6
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
        retorno = s.registrarBicicleta("ABCDE12", "URBANA"); // > 6
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void registrarBicicleta_Error03_TipoInvalido() {
        retorno = s.registrarBicicleta("Z99999", "PASEO");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

    @Test
    public void registrarBicicleta_Error04_Duplicado() {
        s.registrarBicicleta("D00004", "URBANA");
        s.registrarBicicleta("D00005", "URBANA");
        s.registrarBicicleta("D00007", "URBANA");
        retorno = s.registrarBicicleta("D00004", "URBANA");
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
    }

    /* ===========================
       2.5 marcarEnMantenimiento
       =========================== */
    @Test
    public void marcarEnMantenimiento_Ok_y_RepeticionDaError04() {
        s.registrarBicicleta("M00001", "URBANA");
        retorno = s.marcarEnMantenimiento("M00001", "Rueda pinchada");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Marcar nuevamente -> ya en mantenimiento
        retorno = s.marcarEnMantenimiento("M00001", "otro motivo");
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
    }

    @Test
    public void marcarEnMantenimiento_Error01_NulosOVacios() {
        retorno = s.marcarEnMantenimiento(null, "motivo");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        retorno = s.marcarEnMantenimiento("M00002", null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        retorno = s.marcarEnMantenimiento("", "motivo");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        retorno = s.marcarEnMantenimiento("   ", "motivo");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        retorno = s.marcarEnMantenimiento("M00002", "   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void marcarEnMantenimiento_Error02_Inexistente() {
        s.registrarBicicleta("D00004", "URBANA");
        s.registrarBicicleta("D00005", "URBANA");
        s.registrarBicicleta("D00007", "URBANA");
        retorno = s.marcarEnMantenimiento("NOEXST", "motivo");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    /* Nota: ERROR_3 “alquilada” no se prueba en primera entrega, ya que alquilar (2.9) no está requerido. */

 /* ===========================
       2.6 repararBicicleta
       =========================== */
    @Test
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

    @Test
    public void repararBicicleta_Error01_NulosOVacios() {
        retorno = s.repararBicicleta(null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        retorno = s.repararBicicleta("");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        retorno = s.repararBicicleta("   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void repararBicicleta_Error02_Inexistente() {
        
        s.registrarBicicleta("R00001", "URBANA");
        s.registrarBicicleta("R00002", "URBANA");
        retorno = s.repararBicicleta("NOEXST");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    /* ===========================
       3.1 obtenerUsuario
       =========================== */
    @Test
    public void obtenerUsuario_Ok() {
        s.registrarUsuario("11112222", "Beatriz");
         s.registrarUsuario("11112223", "Carla");
        retorno = s.obtenerUsuario("11112222");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Beatriz#11112222", retorno.getValorString());
    }

    @Test
    public void obtenerUsuario_Errores() {
        retorno = s.obtenerUsuario(null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.obtenerUsuario("");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.obtenerUsuario("   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.obtenerUsuario("1234567");   // 7 dígitos
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.obtenerUsuario("123456789"); // 9 dígitos
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.obtenerUsuario("12A45678");  // no numérica
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.obtenerUsuario("99999999");  // no existe
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

    /* ===========================
       3.2 listarUsuarios (orden por nombre ascendente)
       =========================== */
    @Test
    public void listarUsuarios_Ok_OrdenPorNombre() {
        s.registrarUsuario("22223333", "Carlos");
        s.registrarUsuario("33334444", "Ana");
        s.registrarUsuario("44445555", "Beatriz");
        retorno = s.listarUsuarios();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        // Orden esperado: Ana, Beatriz, Carlos
        assertEquals("Ana#33334444|Beatriz#44445555|Carlos#22223333", retorno.getValorString());
    }

    @Test
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

    @Test
    public void listarBicisEnDeposito_Ok_SinBicis() {
        IObligatorio s2 = new Sistema();
        s2.crearSistemaDeGestion();
        Retorno r = s2.listarBicisEnDeposito();
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertTrue(r.getValorString() == null || r.getValorString().isEmpty());
    }

    /* ===========================
       3.4 informaciónMapa
       =========================== */
    @Test
    public void informacionMapa_Ok_MaxColumna_y_ExisteAscendente() {

        String[][] mapa = new String[][]{
            {"E", "", "E", ""},
            {"", "E", "", ""},
            {"E", "E", "E", ""}
        };

        String[][] mapa2 = new String[][]{
            {"E", "", "", ""}, 
            {"", "E", "", ""}, 
            {"", "", "E", ""} 
        };
        retorno = s.informacionMapa(mapa2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        assertTrue(retorno.getValorString().endsWith("|no existe"));
    }

    @Test
    public void informacionMapa_Ok_MaxAmbas_y_NoExisteAscendente() {
        String[][] mapa = new String[][]{
            {"E", "E", "", ""}, // fila 0: 2
            {"", "E", "E", ""}, // fila 1: 2
            {"", "", "", ""} // fila 2: 0
        };

        retorno = s.informacionMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertTrue(retorno.getValorString().startsWith("2#"));
        assertTrue(retorno.getValorString().endsWith("|no existe"));
    }

    @Test
    public void informacionMapa_Ok_MapaVacio() {
        String[][] mapa = new String[][]{
            {"", "", ""},
            {"", "", ""}
        };
        retorno = s.informacionMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        // La letra indica: 0#ambas|...
        assertTrue(retorno.getValorString().startsWith("0#"));
    }
    
     // Helper para evitar repetición
    private void assertMapa(String[][] mapa, String esperado) {
        // Ajustá el nombre del método si tu interfaz usa tilde: informaciónMapa
        retorno = s.informacionMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(esperado, retorno.getValorString());
    }
    
    @Test
    public void tripletasAscendentes_Multiples() {
        // Columnas: c0=1, c1=2, c2=3, c3=4 => existen 2 tripletas consecutivas ascendentes
        // Filas: r0=1, r1=2, r2=3, r3=4 -> máx 4 en ambas
        String[][] m = {
                {"",  "",  "",  "E"},  // r0
                {"",  "",  "E", "E"},  // r1
                {"",  "E", "E", "E"},  // r2
                {"E", "E", "E", "E"}   // r3
        };
        assertMapa(m, "4#ambas|existe");
    }

}
