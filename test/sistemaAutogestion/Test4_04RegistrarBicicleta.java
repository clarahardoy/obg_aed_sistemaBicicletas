
package sistemaAutogestion;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class Test4_04RegistrarBicicleta {
    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }
    
    @Test
    public void registrarBicicletaOk() {
        retorno = s.registrarBicicleta("AAA123", "URBANA");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }
    
    @Test
    public void registrarBicicletaError01() {
        retorno = s.registrarBicicleta("", "URBANA");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        
        retorno = s.registrarBicicleta("AAA123", "");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        
        retorno = s.registrarBicicleta("   ", "URBANA");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        
        retorno = s.registrarBicicleta("AAA123", "   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        
        retorno = s.registrarBicicleta(null, "URBANA");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        
        retorno = s.registrarBicicleta("AAA123", null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }
    
    @Test
    public void registrarBicicletaError02() {
        retorno = s.registrarBicicleta("AAA12", "URBANA");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    
    }
    
    @Test
    public void registrarBicicletaError03() {
        retorno = s.registrarBicicleta("AAA123", "URBAN");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
    
    @Test
    public void registrarBicicletaError04() {
        retorno = s.registrarBicicleta("AAA123", "URBANA");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        
        retorno = s.registrarBicicleta("AAA123", "MOUNTAIN");
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
    }
}
