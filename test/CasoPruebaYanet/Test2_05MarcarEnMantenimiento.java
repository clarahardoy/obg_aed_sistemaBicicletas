
package CasoPruebaYanet;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import sistemaAutogestion.IObligatorio;
import sistemaAutogestion.Retorno;
import sistemaAutogestion.Sistema;

public class Test2_05MarcarEnMantenimiento {

    private Retorno ret;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    // ===================== OK =====================

    @Test //NO PASA
    public void ok_BiciEnEstacionPasaAMantenimientoYVaADeposito() {
        s.registrarEstacion("E1", "Centro", 2);
        s.registrarBicicleta("ABC001", "URBANA");
        s.asignarBicicletaAEstacion("ABC001", "E1");

        // Pre: está en estación
        Retorno rListEst = s.listarBicicletasDeEstacion("E1");
        assertTrue(rListEst.getValorString().contains("ABC001"));

        ret = s.marcarEnMantenimiento("ABC001", "rueda pinchada");
        assertEquals(Retorno.Resultado.OK, ret.getResultado());

        // Ya no está en estación y sí figura en depósito como MANTENIMIENTO
        rListEst = s.listarBicicletasDeEstacion("E1");
        assertFalse(rListEst.getValorString().contains("ABC001"));

        Retorno rDep = s.listarBicisEnDeposito();
        String dep = rDep.getValorString();
        assertTrue(dep.contains("ABC001"));
        assertTrue(dep.toUpperCase().contains("ABC001#URBANA#MANTENIMIENTO"));
    }

    @Test //NO PASA
    public void ok_BiciEnDepositoDisponiblePasaAMantenimientoYSeReubicaAlFinal() {
        // B1 y B2 quedan en depósito en ese orden
        s.registrarBicicleta("ABC001", "URBANA");
        s.registrarBicicleta("ABC002", "URBANA");

        // Pasar B1 a mantenimiento (debería moverse al final)
        ret = s.marcarEnMantenimiento("ABC001", "ajuste frenos");
        assertEquals(Retorno.Resultado.OK, ret.getResultado());

        Retorno rDep = s.listarBicisEnDeposito();
        String dep = rDep.getValorString();

        // Debe estar B2 antes que B1
        int idxB1 = dep.indexOf("ABC001");
        int idxB2 = dep.indexOf("ABC002");
        assertTrue("Se espera B2 antes que B1 en el depósito", idxB2 != -1 && idxB1 != -1 && idxB2 < idxB1);

        // Y B1 debe estar con estado MANTENIMIENTO
        assertTrue(dep.toUpperCase().contains("ABC001#URBANA#MANTENIMIENTO"));
    }

    // ===================== ERROR_1 =====================
    // Alguno de los parámetros es null o vacío

    @Test //ok
    public void error1_CodigoNullOMotivoNullOVacios() {
        s.registrarBicicleta("ABC001", "URBANA");

        ret = s.marcarEnMantenimiento(null, "motivo");
        assertEquals(Retorno.Resultado.ERROR_1, ret.getResultado());

        ret = s.marcarEnMantenimiento("ABC001", null);
        assertEquals(Retorno.Resultado.ERROR_1, ret.getResultado());

        ret = s.marcarEnMantenimiento("", "motivo");
        assertEquals(Retorno.Resultado.ERROR_1, ret.getResultado());

        ret = s.marcarEnMantenimiento("   ", "motivo");
        assertEquals(Retorno.Resultado.ERROR_1, ret.getResultado());

        ret = s.marcarEnMantenimiento("ABC001", "");
        assertEquals(Retorno.Resultado.ERROR_1, ret.getResultado());

        ret = s.marcarEnMantenimiento("ABC001", "   ");
        assertEquals(Retorno.Resultado.ERROR_1, ret.getResultado());
    }

    // ===================== ERROR_2 =====================
    // Bici inexistente

    @Test //ok
    public void error2_BiciInexistente() {
        ret = s.marcarEnMantenimiento("ZZZ999", "motivo");
        assertEquals(Retorno.Resultado.ERROR_2, ret.getResultado());
    }

    // ===================== ERROR_3 =====================
    // Bici actualmente alquilada

    @Test //NO PASA
    public void error3_BiciAlquilada() {
        s.registrarEstacion("E1", "Centro", 1);
        s.registrarUsuario("12345678", "Ana");
        s.registrarBicicleta("ABC001", "URBANA");
        s.asignarBicicletaAEstacion("ABC001", "E1");

        s.alquilarBicicleta("12345678", "E1"); // ahora está ALQUILADA

        ret = s.marcarEnMantenimiento("ABC001", "no frena");
        assertEquals(Retorno.Resultado.ERROR_3, ret.getResultado());
    }

    // ===================== ERROR_4 =====================
    // Bici ya en mantenimiento

    @Test //ok
    public void error4_BiciYaEnMantenimiento() {
        s.registrarBicicleta("ABC001", "URBANA");

        // primera vez: OK
        ret = s.marcarEnMantenimiento("ABC001", "ruido");
        assertEquals(Retorno.Resultado.OK, ret.getResultado());

        // segunda vez: debe dar ERROR_4
        ret = s.marcarEnMantenimiento("ABC001", "otro motivo");
        assertEquals(Retorno.Resultado.ERROR_4, ret.getResultado());
    }
}

