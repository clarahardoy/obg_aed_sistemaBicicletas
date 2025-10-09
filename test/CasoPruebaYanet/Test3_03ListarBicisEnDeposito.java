
package CasoPruebaYanet;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import sistemaAutogestion.IObligatorio;
import sistemaAutogestion.Retorno;
import sistemaAutogestion.Sistema;

public class Test3_03ListarBicisEnDeposito {

    private Retorno ret;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    @Test //ok
    public void depositoVacio() {
        ret = s.listarBicisEnDeposito();
        assertEquals(Retorno.Resultado.OK, ret.getResultado());
        assertEquals("", ret.getValorString());
    }

    @Test //NO PASA
    public void unaBici_Disponible() {
        s.registrarBicicleta("ABC001", "URBANA");
        ret = s.listarBicisEnDeposito();
        assertEquals(Retorno.Resultado.OK, ret.getResultado());
        assertEquals("ABC001#URBANA#Disponible", ret.getValorString());
    }

    @Test //NO PASA
    public void variasBicis_OrdenIngreso() {
        s.registrarBicicleta("ABC001", "URBANA");
        s.registrarBicicleta("ABC002", "MOUNTAIN");
        s.registrarBicicleta("ABC003", "ELECTRICA");

        ret = s.listarBicisEnDeposito();
        assertEquals(Retorno.Resultado.OK, ret.getResultado());
        assertEquals(
            "ABC001#URBANA#Disponible|ABC002#MOUNTAIN#Disponible|ABC003#ELECTRICA#Disponible",
            ret.getValorString()
        );
    }

    @Test //NO PASA
    public void estadoMantenimiento_ApareceComoMantenimiento() {
        s.registrarBicicleta("ABC001", "URBANA");
        s.marcarEnMantenimiento("ABC001", "rueda");
        ret = s.listarBicisEnDeposito();
        assertEquals(Retorno.Resultado.OK, ret.getResultado());
        assertTrue(ret.getValorString().contains("ABC001#URBANA#Mantenimiento"));
    }

    @Test //ok
    public void recursividad_NoAgregaSeparadorFinal() {
        s.registrarBicicleta("A00001", "URBANA");
        s.registrarBicicleta("A00002", "URBANA");
        String sdep = s.listarBicisEnDeposito().getValorString();
        assertFalse("No debe terminar con '|'", sdep.endsWith("|"));
        assertTrue("Debe tener separador entre elementos", sdep.contains("|"));
    }
    
     /* Escenario:
       1) Registro 6 bicis en este orden (todas quedan DISPONIBLE en depósito):
          AB0001(URBANA), AB0002(MOUNTAIN), AB0003(ELECTRICA),
          AB0004(URBANA), AB0005(MOUNTAIN), AB0006(URBANA)
     
       2) Marco en mantenimiento (en este orden): AB0002, AB0005, AB0003.
          Regla: al pasar a MANTENIMIENTO, la bici se reubica al FINAL del depósito.
     
       Orden esperado tras cada paso:
        - Inicio:
          AB0001 D | AB0002 D | AB0003 D | AB0004 D | AB0005 D | AB0006 D
     
        - Mantenimiento(AB0002):
          AB0001 D | AB0003 D | AB0004 D | AB0005 D | AB0006 D | AB0002 M
     
        - Mantenimiento(AB0005):
          AB0001 D | AB0003 D | AB0004 D | AB0006 D | AB0002 M | AB0005 M
     
        - Mantenimiento(AB0003):
          AB0001 D | AB0004 D | AB0006 D | AB0002 M | AB0005 M | AB0003 M
     
       Donde D=Disponible, M=Mantenimiento.
    */
    @Test //NO PASA
    public void listadoFinal_conDisponiblesYEnMantenimiento_enOrdenCorrecto() {
        s.registrarBicicleta("AB0001", "URBANA");
        s.registrarBicicleta("AB0002", "MOUNTAIN");
        s.registrarBicicleta("AB0003", "ELECTRICA");
        s.registrarBicicleta("AB0004", "URBANA");
        s.registrarBicicleta("AB0005", "MOUNTAIN");
        s.registrarBicicleta("AB0006", "URBANA");

        s.listarBicisEnDeposito().getValorString();
        
        // Poner algunas en mantenimiento (en distintos momentos)
        assertEquals(Retorno.Resultado.OK, s.marcarEnMantenimiento("AB0002", "ajuste cambios").getResultado());
        assertEquals(Retorno.Resultado.OK, s.marcarEnMantenimiento("AB0005", "frenos").getResultado());
        assertEquals(Retorno.Resultado.OK, s.marcarEnMantenimiento("AB0003", "rueda").getResultado());

        // Listado final esperado
        String esperado =
            "AB0001#URBANA#Disponible"   + "|" +
            "AB0004#URBANA#Disponible"   + "|" +
            "AB0006#URBANA#Disponible"   + "|" +
            "AB0002#MOUNTAIN#Mantenimiento" + "|" +
            "AB0005#MOUNTAIN#Mantenimiento" + "|" +
            "AB0003#ELECTRICA#Mantenimiento";

        String actual = s.listarBicisEnDeposito().getValorString();
        assertEquals(actual, esperado);

    }
}

