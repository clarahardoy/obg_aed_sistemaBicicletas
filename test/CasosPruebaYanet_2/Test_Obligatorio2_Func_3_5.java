
package CasosPruebaYanet_2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;

import sistemaAutogestion.IObligatorio;
import sistemaAutogestion.Retorno;
import sistemaAutogestion.Sistema;

/**
 *
 * @author yanete
 */
public class Test_Obligatorio2_Func_3_5 {
     private IObligatorio s;

    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion();
    }

    // ERROR_1: nombre null
    @Test
    public void error1_NombreNull() {
        Retorno r = s.listarBicicletasDeEstacion(null);
        assertEquals(Retorno.Resultado.ERROR_1, r.getResultado());
    }

    // ERROR_1: nombre vacío o espacios
    @Test
    public void error1_NombreVacio() {
        assertEquals(Retorno.Resultado.ERROR_1, s.listarBicicletasDeEstacion("").getResultado());
        assertEquals(Retorno.Resultado.ERROR_1, s.listarBicicletasDeEstacion("   ").getResultado());
    }

    // ERROR_2: estación inexistente
    @Test
    public void error2_EstacionInexistente() {
        s.registrarEstacion("E1", "Centro", 3);
        Retorno r = s.listarBicicletasDeEstacion("E2");
        assertEquals(Retorno.Resultado.ERROR_2, r.getResultado());
    }

    // OK: estación existente pero sin bicicletas -> cadena vacía
    @Test
    public void ok_EstacionSinBicis_RetornaVacio() {
        s.registrarEstacion("E1", "Centro", 5);
        Retorno r = s.listarBicicletasDeEstacion("E1");
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals("", r.getValorString());
    }

    // OK: un solo código
    @Test
    public void ok_UnaBici_UnicoCodigo() {
        s.registrarEstacion("E1", "Centro", 3);
        s.registrarBicicleta("A00002", "URBANA");
        s.asignarBicicletaAEstacion("A00002", "E1");

        Retorno r = s.listarBicicletasDeEstacion("E1");
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals("A00002", r.getValorString());
    }

    // OK: varias bicis agregadas en desorden -> listado en orden creciente por código
    @Test
    public void ok_VariasBicis_OrdenadasPorCodigo() {
        s.registrarEstacion("E1", "Centro", 6);

        // Registrar en depósito
        s.registrarBicicleta("UYT123", "URBANA");
        s.registrarBicicleta("AER345", "URBANA");
        s.registrarBicicleta("UTR112", "MOUNTAIN");
        s.registrarBicicleta("B00001", "ELECTRICA");

        // Asignar en orden desordenado
        s.asignarBicicletaAEstacion("UYT123", "E1");
        s.asignarBicicletaAEstacion("AER345", "E1");
        s.asignarBicicletaAEstacion("UTR112", "E1");
        s.asignarBicicletaAEstacion("B00001", "E1");

        Retorno r = s.listarBicicletasDeEstacion("E1");
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        // Esperado lexicográficamente: AER345 | B00001 | UTR112 | UYT123
        assertEquals("AER345|B00001|UTR112|UYT123", r.getValorString());
    }

    // OK: el nombre con espacios a los lados se normaliza (trim)
    @Test
    public void ok_TrimEnNombreDeEstacion() {
        s.registrarEstacion("E1", "Centro", 1);
        s.registrarBicicleta("ABC999", "URBANA");
        s.asignarBicicletaAEstacion("ABC999", "E1");

        Retorno r = s.listarBicicletasDeEstacion("  E1  ");
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals("ABC999", r.getValorString());
    }

    // OK: capacidad al límite (n = capacidad), se listan todas en orden
    @Test
    public void ok_CapacidadLlena_ListadoCompleto() {
        s.registrarEstacion("E1", "Centro", 5);
        String[] cods = {"Z00005","A00001","M00003","B00002","K00004"}; // desordenados

        for (String c : cods) s.registrarBicicleta(c, "URBANA");
        for (String c : cods) s.asignarBicicletaAEstacion(c, "E1");

        Retorno r = s.listarBicicletasDeEstacion("E1");
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals("A00001|B00002|K00004|M00003|Z00005", r.getValorString());
        // Validar separadores: no termina ni empieza con '|'
        assertFalse(r.getValorString().startsWith("|"));
        assertFalse(r.getValorString().endsWith("|"));
    }
}