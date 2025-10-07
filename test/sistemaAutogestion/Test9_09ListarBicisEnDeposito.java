
package sistemaAutogestion;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class Test9_09ListarBicisEnDeposito {
    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }
    
    @Test
    public void listarBiciEnDepositoVacio() {
        retorno = s.listarBicisEnDeposito();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }
    
    @Test
    public void listarBiciEnDepositoUnaDisponible() {
        s.registrarBicicleta("AAA123", "URBANA");

        retorno  = s.listarBicisEnDeposito();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("AAA123#URBANA#DISPONIBLE", retorno.getValorString());
    }
    
    @Test //SE LISTARON 2 BICIS Y UNA SE CAMBIO DE ESTADO
    public void listarBiciDepositoEnOrdenYEstado() {
        s.registrarBicicleta("BBB111", "ELECTRICA"); 
        s.registrarBicicleta("CCC222", "MOUNTAIN");  
      
 
        assertEquals(Retorno.Resultado.OK,
                     s.marcarEnMantenimiento("CCC222", "service").getResultado());

        Retorno r = s.listarBicisEnDeposito();
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals(
            "BBB111#ELECTRICA#DISPONIBLE|CCC222#MOUNTAIN#MANTENIMIENTO",
            r.getValorString()
        );
        
        r = s.listarBicisEnDeposito();
        System.out.println(r.getValorString());
    }       
}
