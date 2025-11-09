
package sistemaAutogestion;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class Test12_012AlquilarBicicleta {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    // hay bici disponible en la estación se alquila
    @Test
    public void alquilarBicicletaOk_alquilaDirecto() {
        retorno = s.registrarEstacion("E1", "Centro", 1);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarUsuario("11111111", "U1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarBicicleta("ABC123", "URBANA");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.asignarBicicletaAEstacion("ABC123", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.alquilarBicicleta("11111111", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // la bici ya no está anclada en E1
        retorno = s.listarBicicletasDeEstacion("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }

    //no hay bicis el usuario queda en cola de espera
    @Test
    public void alquilarBicicletaOk_quedaEnCola() {
        retorno = s.registrarEstacion("E1", "Centro", 1);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarUsuario("11111111", "U1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        retorno = s.registrarUsuario("22222222", "U2");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // sin bicicletas en E1 ambos quedan en cola
        retorno = s.alquilarBicicleta("11111111", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        retorno = s.alquilarBicicleta("22222222", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.usuariosEnEspera("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("11111111|22222222", retorno.getValorString());
    }

    //prioridad por orden de llegada cuando se libera una bici
    @Test
    public void alquilarBicicletaOk_prioridadFIFO() {

        retorno = s.registrarEstacion("E1", "Centro", 1);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarUsuario("00000000", "U0");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        retorno = s.registrarUsuario("11111111", "U1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        retorno = s.registrarUsuario("22222222", "U2");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarBicicleta("ABC123", "URBANA");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        retorno = s.asignarBicicletaAEstacion("ABC123", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());


        retorno = s.alquilarBicicleta("00000000", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // U1 y U2 intentan alquilar, quedan en cola de espera
        retorno = s.alquilarBicicleta("11111111", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        retorno = s.alquilarBicicleta("22222222", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.usuariosEnEspera("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("11111111|22222222", retorno.getValorString());

        // U0 devuelve en E1 la bici va directa a U1
        retorno = s.devolverBicicleta("00000000", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // ahora en cola queda solo U2
        retorno = s.usuariosEnEspera("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("22222222", retorno.getValorString());

        // y en la estación no hay ancladas 
        retorno = s.listarBicicletasDeEstacion("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }


    @Test
    public void alquilarBicicletaError01_parametrosInvalidos() {
        retorno = s.alquilarBicicleta(null, "E1");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.alquilarBicicleta("", "E1");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.alquilarBicicleta("11111111", null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.alquilarBicicleta("11111111", "");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }


    @Test
    public void alquilarBicicletaError02_usuarioInexistente() {
        retorno = s.registrarEstacion("E1", "Centro", 1);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.alquilarBicicleta("11111111", "E1");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }


    @Test
    public void alquilarBicicletaError03_estacionInexistente() {
        retorno = s.registrarUsuario("11111111", "U1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.alquilarBicicleta("11111111", "NOEXISTE");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
}