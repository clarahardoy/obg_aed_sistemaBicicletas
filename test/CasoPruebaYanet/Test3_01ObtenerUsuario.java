package CasoPruebaYanet;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import sistemaAutogestion.IObligatorio;
import sistemaAutogestion.Retorno;
import sistemaAutogestion.Sistema;

public class Test3_01ObtenerUsuario {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    @Test //ok
    public void obtenerUsuarioOk() {
        s.registrarUsuario("12345678", "Usuario01");
        retorno = s.obtenerUsuario("12345678");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Usuario01#12345678", retorno.getValorString());
    }

    @Test //ok
    public void obtenerUsuarioError01() {
        // null / vacío / espacios
        retorno = s.obtenerUsuario(null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.obtenerUsuario("");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.obtenerUsuario("   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }
    
    @Test //ok
    public void obtenerUsuarioError02() {
        // formato inválido: largo ≠ 8, letras, separadores
        retorno = s.obtenerUsuario("1234567");     // 7 dígitos
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.obtenerUsuario("123456789");   // 9 dígitos
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.obtenerUsuario("12A45678");    // letra
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.obtenerUsuario("12-45678");    // separador
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.obtenerUsuario("1234 5678");   // espacio interno
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }


    @Test //ok
    public void obtenerUsuarioError03() {
        // usuario inexistente con formato válido
        retorno = s.obtenerUsuario("01234567"); // válido pero no registrado
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

}
