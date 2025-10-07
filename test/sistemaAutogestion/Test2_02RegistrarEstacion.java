package sistemaAutogestion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test2_02RegistrarEstacion {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    @Test
    public void registrarEstacionOk() {
        retorno = s.registrarEstacion("Estacion01", "Centro", 5);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    public void registrarEstacionError01() {
        retorno = s.registrarEstacion("", "Centro", 5);//vacio
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarEstacion("Estacion01", "", 5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarEstacion("   ", "Centro", 5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarEstacion("Estacion01", "   ", 5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarEstacion(null, "Centro", 5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarEstacion("Estacion01", null, 5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void registrarEstacionError02() {
        retorno = s.registrarEstacion("Estacion01", "Centro", 0);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarEstacion("Estacion01", "Centro", -10);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void registrarEstacionError03() {
        s.registrarEstacion("Estacion01", "Centro", 5);
        retorno = s.registrarEstacion("Estacion01", "Centro", 5);
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());

    }
}
