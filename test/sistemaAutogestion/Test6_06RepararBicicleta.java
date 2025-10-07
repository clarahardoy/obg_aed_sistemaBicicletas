
package sistemaAutogestion;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class Test6_06RepararBicicleta {
    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }
    
    @Test
    public void repararBicicletaOk() {
        retorno = s.registrarBicicleta("AAA123", "URBANA");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.marcarEnMantenimiento("AAA123", "revisión general");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.repararBicicleta("AAA123");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }
    
    @Test
    public void repararBicicletaError01() {
        retorno = s.repararBicicleta(null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.repararBicicleta("");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.repararBicicleta("   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }
    
    @Test
    public void repararBicicletaError02() {
        retorno = s.repararBicicleta("ZZZ999");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }
    
    @Test
    public void repararBicicletaError03() {
        retorno = s.registrarBicicleta("AAA123", "URBANA");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.repararBicicleta("AAA123");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
}
