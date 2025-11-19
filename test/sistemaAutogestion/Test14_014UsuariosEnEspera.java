package sistemaAutogestion;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class Test14_014UsuariosEnEspera {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }


    @Test
    public void usuariosEnEsperaOk_sinEspera() {
        retorno = s.registrarEstacion("E1", "Centro", 2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.usuariosEnEspera("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }

    //  un usuario queda en cola
    @Test
    public void usuariosEnEsperaOk_unUsuario() {
        retorno = s.registrarEstacion("E1", "Centro", 1);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarUsuario("11111111", "U1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // al no haber bicis ancladas, el usuario queda en espera
        retorno = s.alquilarBicicleta("11111111", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.usuariosEnEspera("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("11111111", retorno.getValorString());
    }

    // varios usuarios se listan en orden FIFO 
    @Test
    public void usuariosEnEsperaOk_varios_FIFO() {
        retorno = s.registrarEstacion("E1", "Centro", 1);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarUsuario("11111111", "U1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        retorno = s.registrarUsuario("22222222", "U2");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        retorno = s.registrarUsuario("33333333", "U3");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // sin bicis ancladas todos quedan esperando en ese orden
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta("11111111", "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta("22222222", "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta("33333333", "E1").getResultado());

        retorno = s.usuariosEnEspera("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("11111111|22222222|33333333", retorno.getValorString());
    }

    // listar NO consume la cola 
    @Test
    public void usuariosEnEsperaOk_noConsumeCola() {
        retorno = s.registrarEstacion("E1", "Centro", 1);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarUsuario("11111111", "U1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        retorno = s.registrarUsuario("22222222", "U2");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta("11111111", "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta("22222222", "E1").getResultado());

        retorno = s.usuariosEnEspera("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("11111111|22222222", retorno.getValorString());

        // segunda llamada, debe ser igual
        retorno = s.usuariosEnEspera("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("11111111|22222222", retorno.getValorString());
    }

    // parámetros nulos o vacíos la implementación devuelve error1
    @Test
    public void usuariosEnEsperaOk_parametrosInvalidos_devuelveVacio() {
        retorno = s.usuariosEnEspera(null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.usuariosEnEspera("");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.usuariosEnEspera("   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    // estación inexistente  devuelve error2 con cadena vacía
    @Test
    public void usuariosEnEsperaOk_estacionInexistente_devuelveVacio() {
        retorno = s.usuariosEnEspera("NOEXISTE");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }
}
