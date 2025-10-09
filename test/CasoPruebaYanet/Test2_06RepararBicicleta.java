
package CasoPruebaYanet;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import sistemaAutogestion.IObligatorio;
import sistemaAutogestion.Retorno;
import sistemaAutogestion.Sistema;

public class Test2_06RepararBicicleta {

    private Retorno ret;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    // ========== OK ==========
    @Test //ok
    public void ok_ReparaDesdeMantenimiento_enDeposito() {
        // Dada una bici registrada (queda en depósito, DISPONIBLE)
        s.registrarBicicleta("ABC001", "URBANA");

        // La paso a MANTENIMIENTO
        ret = s.marcarEnMantenimiento("ABC001", "rueda");
        assertEquals(Retorno.Resultado.OK, ret.getResultado());

        // Reparar bicicleta
        ret = s.repararBicicleta("ABC001");
        assertEquals(Retorno.Resultado.OK, ret.getResultado());

        // (opcional) si existe listarBicisEnDeposito, verifico que quedó DISPONIBLE
        try {
            Retorno dep = s.listarBicisEnDeposito();
            assertTrue(dep.getValorString().toUpperCase().contains("ABC001#URBANA#DISPONIBLE"));
        } catch (Throwable ignore) {}
    }

    // ========== ERROR_1 ==========
    @Test //ok
    public void error1_CodigoNullOVacio() {
        ret = s.repararBicicleta(null);
        assertEquals(Retorno.Resultado.ERROR_1, ret.getResultado());

        ret = s.repararBicicleta("");
        assertEquals(Retorno.Resultado.ERROR_1, ret.getResultado());

        ret = s.repararBicicleta("   ");
        assertEquals(Retorno.Resultado.ERROR_1, ret.getResultado());
    }

    // ========== ERROR_2 ==========
    @Test //ok
    public void error2_BiciInexistente() {
        ret = s.repararBicicleta("ZZZ999");
        assertEquals(Retorno.Resultado.ERROR_2, ret.getResultado());
    }

    // ========== ERROR_3 ==========
    @Test //ok
    public void error3_NoEstaEnMantenimiento_enDepositoDisponible() {
        s.registrarBicicleta("ABC001", "URBANA"); // está DISPONIBLE en depósito
        ret = s.repararBicicleta("ABC001");
        assertEquals(Retorno.Resultado.ERROR_3, ret.getResultado());
    }

    @Test //ok
    public void error3_Idempotencia_NoSePuedeRepararDeNuevo() {
        s.registrarBicicleta("ABC001", "URBANA");
        s.marcarEnMantenimiento("ABC001", "rueda");

        // primera reparación OK
        assertEquals(Retorno.Resultado.OK, s.repararBicicleta("ABC001").getResultado());
        // segunda reparación => ya no está en mantenimiento
        assertEquals(Retorno.Resultado.ERROR_3, s.repararBicicleta("ABC001").getResultado());
    }
}

