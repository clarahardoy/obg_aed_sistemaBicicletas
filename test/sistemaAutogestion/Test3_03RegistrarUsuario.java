
package sistemaAutogestion;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class Test3_03RegistrarUsuario {
    
    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }
    
    @Test
    public void registrarUsuarioOk() {
        retorno = s.registrarUsuario("12345678", "Centro");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }
    
    @Test
    public void registrarUsuarioError01() {
        retorno = s.registrarUsuario("", "Lucia");//vacio
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarUsuario("12345678", "");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarUsuario("   ", "Lucia");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarUsuario("12345678", "   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarUsuario(null, "Lucia");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarUsuario("123456789", null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }
    
    @Test
    public void registrarUsuarioError02() {
        retorno = s.registrarUsuario("1234567", "Lucia");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

    }
    
    @Test
    public void registrarUsuarioError03() {
        retorno = s.registrarUsuario("12345678", "Lucia");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarUsuario("12345678", "Lucía Pérez"); // nombre distinto no importa
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());

    }
    
}
