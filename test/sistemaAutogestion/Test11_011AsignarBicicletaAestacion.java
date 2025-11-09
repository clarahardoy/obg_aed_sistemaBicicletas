
package sistemaAutogestion;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class Test11_011AsignarBicicletaAestacion {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    // OK desde depósito a estación con lugar
    @Test
    public void asignarBicicletaAEstacionOk() {
        retorno = s.registrarEstacion("E1", "Centro", 2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarBicicleta("ABC123", "URBANA");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.asignarBicicletaAEstacion("ABC123", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // debe quedar anclada en E1
        retorno = s.listarBicicletasDeEstacion("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("ABC123", retorno.getValorString());

        // y no figurar en depósito
        retorno = s.listarBicisEnDeposito();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }


    @Test
    public void asignarBicicletaAEstacionError01() {
        retorno = s.asignarBicicletaAEstacion(null, "E1");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.asignarBicicletaAEstacion("", "E1");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.asignarBicicletaAEstacion("ABC123", null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.asignarBicicletaAEstacion("ABC123", "");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }


    @Test
    public void asignarBicicletaAEstacionError02_biciInexistente() {
        retorno = s.registrarEstacion("E1", "Centro", 2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.asignarBicicletaAEstacion("ZZZ999", "E1");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }


    @Test
    public void asignarBicicletaAEstacionError03_estacionInexistente() {
        retorno = s.registrarBicicleta("ABC123", "URBANA");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.asignarBicicletaAEstacion("ABC123", "NOEXISTE");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }


    @Test
    public void asignarBicicletaAEstacionError04_sinLugar() {
        retorno = s.registrarEstacion("E1", "Centro", 1);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarBicicleta("AAA111", "URBANA");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarBicicleta("BBB222", "URBANA");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // llena la estación con la primera
        retorno = s.asignarBicicletaAEstacion("AAA111", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // la segunda no entra
        retorno = s.asignarBicicletaAEstacion("BBB222", "E1");
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());

        // verificar que sólo está AAA111
        retorno = s.listarBicicletasDeEstacion("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("AAA111", retorno.getValorString());
    }


    @Test
    public void asignarBicicletaAEstacionError02_biciNoDisponible_mantenimiento() {
        retorno = s.registrarEstacion("E1", "Centro", 2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarBicicleta("ABC123", "URBANA");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.marcarEnMantenimiento("ABC123", "ruido");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.asignarBicicletaAEstacion("ABC123", "E1");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        // no debe aparecer anclada
        retorno = s.listarBicicletasDeEstacion("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }


    @Test
    public void asignarBicicletaAEstacionError02_biciNoDisponible_alquilada() {
        retorno = s.registrarEstacion("E1", "Centro", 1);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarEstacion("E2", "Centro", 1);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarUsuario("11111111", "U1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarBicicleta("ABC123", "URBANA");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.asignarBicicletaAEstacion("ABC123", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.alquilarBicicleta("11111111", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // intentar mover alquilada
        retorno = s.asignarBicicletaAEstacion("ABC123", "E2");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }


    @Test
    public void asignarBicicletaAEstacionOk_moverEntreEstaciones() {
        retorno = s.registrarEstacion("E1", "Centro", 2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarEstacion("E2", "Cordon", 2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarBicicleta("ABC123", "URBANA");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.asignarBicicletaAEstacion("ABC123", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.asignarBicicletaAEstacion("ABC123", "E2");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.listarBicicletasDeEstacion("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());

        retorno = s.listarBicicletasDeEstacion("E2");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("ABC123", retorno.getValorString());
    }


    @Test
    public void asignarBicicletaAEstacionOk_mismaEstacion_noDuplica() {
        retorno = s.registrarEstacion("E1", "Centro", 2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarBicicleta("ABC123", "URBANA");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.asignarBicicletaAEstacion("ABC123", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());


        retorno = s.asignarBicicletaAEstacion("ABC123", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.listarBicicletasDeEstacion("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("ABC123", retorno.getValorString());
    }
}