
package CasoPruebaYanet;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import sistemaAutogestion.IObligatorio;
import sistemaAutogestion.Retorno;
import sistemaAutogestion.Sistema;

public class Test2_03RegistrarUsuario {

    private Retorno ret;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    // ===================== OK =====================

    @Test //ok
    public void ok_CiConCerosALaIzquierda() {
        ret = s.registrarUsuario("01234567", "Ana");
        assertEquals(Retorno.Resultado.OK, ret.getResultado());
    }

    @Test //ok
    public void ok_CiMinimaYMaxima() {
        // "00000000" y "99999999" deben ser válidas
        ret = s.registrarUsuario("00000000", "Cero");
        assertEquals(Retorno.Resultado.OK, ret.getResultado());

        ret = s.registrarUsuario("99999999", "Max");
        assertEquals(Retorno.Resultado.OK, ret.getResultado());
    }

    @Test //NO PASA
    public void ok_TrimEnAmbosCampos() {
        ret = s.registrarUsuario(" 12345678 ", "  Juan Perez  ");
        assertEquals(Retorno.Resultado.OK, ret.getResultado());
    }

    // ===================== ERROR_1 =====================
    // Alguno de los parámetros es null o vacío (tras trim)

    @Test //ok
    public void error1_CedulaNull() {
        ret = s.registrarUsuario(null, "User");
        assertEquals(Retorno.Resultado.ERROR_1, ret.getResultado());
    }

    @Test //ok
    public void error1_NombreNull() {
        ret = s.registrarUsuario("12345678", null);
        assertEquals(Retorno.Resultado.ERROR_1, ret.getResultado());
    }

    @Test //ok
    public void error1_CedulaVacia_o_Blancos() {
        ret = s.registrarUsuario("", "User");
        assertEquals(Retorno.Resultado.ERROR_1, ret.getResultado());

        ret = s.registrarUsuario("   ", "User");
        assertEquals(Retorno.Resultado.ERROR_1, ret.getResultado());
    }

    @Test //ok
    public void error1_NombreVacio_o_Blancos() {
        ret = s.registrarUsuario("12345678", "");
        assertEquals(Retorno.Resultado.ERROR_1, ret.getResultado());

        ret = s.registrarUsuario("12345678", "   ");
        assertEquals(Retorno.Resultado.ERROR_1, ret.getResultado());
    }

    // ===================== ERROR_2 =====================
    // Formato de cédula inválido (exactamente 8 dígitos ASCII 0-9)

    @Test //ok
    public void error2_LargoIncorrecto_7y9() {
        ret = s.registrarUsuario("1234567", "User");   // 7 dígitos
        assertEquals(Retorno.Resultado.ERROR_2, ret.getResultado());

        ret = s.registrarUsuario("123456789", "User"); // 9 dígitos
        assertEquals(Retorno.Resultado.ERROR_2, ret.getResultado());
    }

    @Test //ok
    public void error2_ContieneNoDigitos_Letra_Guion_EspacioInterno() {
        ret = s.registrarUsuario("12A45678", "User");
        assertEquals(Retorno.Resultado.ERROR_2, ret.getResultado());

        ret = s.registrarUsuario("12-45678", "User");
        assertEquals(Retorno.Resultado.ERROR_2, ret.getResultado());

        ret = s.registrarUsuario("1234 5678", "User");
        assertEquals(Retorno.Resultado.ERROR_2, ret.getResultado());
    }

    @Test //ok
    public void error2_DigitosUnicodeNoASCII() {
        // Dígitos arábigos (Unicode): tu validador los rechaza porque exige '0'..'9'
        ret = s.registrarUsuario("١٢٣٤٥٦٧٨", "User"); // U+0661..U+0668
        assertEquals(Retorno.Resultado.ERROR_2, ret.getResultado());

        // Dígitos "fullwidth" (Unicode)
        ret = s.registrarUsuario("１２３４５６７８", "User");
        assertEquals(Retorno.Resultado.ERROR_2, ret.getResultado());
    }

    // ===================== ERROR_3 =====================
    // Ya existe usuario con esa cédula

    @Test //ok
    public void error3_DuplicadoMismaCedula() {
        ret = s.registrarUsuario("12345678", "Uno");
        assertEquals(Retorno.Resultado.OK, ret.getResultado());

        // mismo número de cédula, aunque cambie el nombre -> duplicado
        ret = s.registrarUsuario("12345678", "OtroNombre");
        assertEquals(Retorno.Resultado.ERROR_3, ret.getResultado());
    }
}

