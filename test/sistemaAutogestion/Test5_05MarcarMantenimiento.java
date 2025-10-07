
package sistemaAutogestion;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class Test5_05MarcarMantenimiento {
    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }
    
    @Test
    public void marcarMantenimientoOk() {
        retorno = s.registrarBicicleta("AAA123", "URBANA");
        
        retorno = s.marcarEnMantenimiento("AAA123", "Rueda Rota");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }
    
    @Test
    public void marcarMantenimientoError01() {
        retorno = s.marcarEnMantenimiento(null, "Rueda Rota");
    assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

    retorno = s.marcarEnMantenimiento("AAA123", null);
    assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

    retorno = s.marcarEnMantenimiento("", "Rueda Rota");
    assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

    retorno = s.marcarEnMantenimiento("AAA123", "");
    assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    
    retorno = s.marcarEnMantenimiento("   ", "Rueda Rota");
    assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

    retorno = s.marcarEnMantenimiento("AAA123", "   ");
    assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }
    
    @Test
    public void marcarMantenimientoError02() {
        retorno = s.marcarEnMantenimiento("ZZZ999", "Pinchada");
    assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }
    
    @Test
    public void marcarMantenimientoError03() {
     //NO SE IMPLEMENTA YA QUE NO TENEMOS UNA BICICLETA EN ALQUILER PARA PODER TESTEAR
    }
    
    @Test
    public void marcarMantenimientoError04() {
        retorno = s.registrarBicicleta("AAA123", "URBANA");
    assertEquals(Retorno.Resultado.OK, retorno.getResultado());

    retorno = s.registrarBicicleta("AAA123", "MOUNTAIN");
    assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
    }
    
}
