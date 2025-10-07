package sistemaAutogestion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test7_07ObtenerUsuario {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    @Test
    public void obtenerUsuarioOk() {
        s.registrarUsuario("12345678", "Usuario01");
        retorno = s.obtenerUsuario("12345678");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Usuario01#12345678", retorno.getValorString());
    }

    @Test
    public void obtenerUsuarioError01() {
        retorno = s.obtenerUsuario(null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.obtenerUsuario("");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.obtenerUsuario("   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void obtenerUsuarioError02() {
        retorno = s.obtenerUsuario("1234567"); 
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.obtenerUsuario("123456789"); 
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.obtenerUsuario("12A45678"); 
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.obtenerUsuario("1234 567"); 
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void obtenerUsuarioError03() {
        retorno = s.obtenerUsuario("12345678");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

}
