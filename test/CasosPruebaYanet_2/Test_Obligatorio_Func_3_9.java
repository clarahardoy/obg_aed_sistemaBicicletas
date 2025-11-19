
package CasosPruebaYanet_2;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import sistemaAutogestion.IObligatorio;
import sistemaAutogestion.Retorno;
import sistemaAutogestion.Sistema;

public class Test_Obligatorio_Func_3_9 {
    private IObligatorio s;

    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion();
    }

    @Test
    public void error1_parametroNullOVacio() {
        Retorno r;

        r = s.usuariosEnEspera(null);
        assertEquals(Retorno.Resultado.ERROR_1, r.getResultado());

        r = s.usuariosEnEspera("");
        assertEquals(Retorno.Resultado.ERROR_1, r.getResultado());

        r = s.usuariosEnEspera("   ");
        assertEquals(Retorno.Resultado.ERROR_1, r.getResultado());
    }

    @Test
    public void error2_estacionInexistente() {
        Retorno r = s.usuariosEnEspera("NoExiste");
        assertEquals(Retorno.Resultado.ERROR_2, r.getResultado());
    }

    // ---------- OK: SIN ESPERA ----------

    @Test
    public void ok_estacionSinCola_devuelveVacio() {
        // estación válida sin bicis ni usuarios esperando
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E1", "Centro", 3).getResultado());

        Retorno r = s.usuariosEnEspera("E1");
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals("", r.getValorString());
    }

    // ---------- OK: FIFO Y TRIM ----------

    @Test
    public void ok_fifo_yTrimNombre() {
        // Estación sin bicis -> todos los alquileres quedarán en cola
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E1", "Centro", 5).getResultado());

        String c1 = "10000001";
        String c2 = "10000002";
        String c3 = "10000003";

        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(c1, "Ana").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(c2, "Beto").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(c3, "Caro").getResultado());

        // Como no hay bicis ancladas, todos quedan esperando en ese orden
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta(c1, "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta(c2, "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta(c3, "E1").getResultado());

        // Llamamos con espacios para validar el trim
        Retorno r = s.usuariosEnEspera("   E1   ");
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals(c1 + "|" + c2 + "|" + c3, r.getValorString());
    }

    // ---------- OK: NO CONSUME COLA (idempotente) ----------

    @Test
    public void ok_noConsumeCola_llamadasRepetidas() {
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E2", "Centro", 2).getResultado());

        String c1 = "20000001";
        String c2 = "20000002";
        String c3 = "20000003";

        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(c1, "U1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(c2, "U2").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(c3, "U3").getResultado());

        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta(c1, "E2").getResultado());
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta(c2, "E2").getResultado());

        Retorno r1 = s.usuariosEnEspera("E2");
        assertEquals(Retorno.Resultado.OK, r1.getResultado());
        assertEquals(c1 + "|" + c2, r1.getValorString());

        // Segunda llamada debe dar exactamente lo mismo
        Retorno r2 = s.usuariosEnEspera("E2");
        assertEquals(Retorno.Resultado.OK, r2.getResultado());
        assertEquals(c1 + "|" + c2, r2.getValorString());
    }

    // ---------- OK: SOLO LOS QUE ESTÁN EN COLA ----------

    @Test
    public void ok_soloQuienesEsperan_noIncluyeNoEncolados() {
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E3", "Centro", 3).getResultado());

        String c1 = "30000001";
        String c2 = "30000002";
        String c3 = "30000003"; // no pedirá bici

        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(c1, "U1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(c2, "U2").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(c3, "U3").getResultado());

        // Solo dos piden bici (no hay bicis, así que quedan en cola)
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta(c1, "E3").getResultado());
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta(c2, "E3").getResultado());

        Retorno r = s.usuariosEnEspera("E3");
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals(c1 + "|" + c2, r.getValorString());
    }
}
    