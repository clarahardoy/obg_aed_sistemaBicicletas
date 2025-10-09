
package CasoPruebaYanet;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import sistemaAutogestion.IObligatorio;
import sistemaAutogestion.Retorno;
import sistemaAutogestion.Sistema;

public class Test2_04RegistrarBicicleta {

    private Retorno ret;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    // =============== OK ===============

    @Test //ok
    public void ok_Alfanumerico6_Mayusculas() {
        ret = s.registrarBicicleta("BIC001", "URBANA");
        assertEquals(Retorno.Resultado.OK, ret.getResultado());
    }

    @Test //NO PASA
    public void ok_CodigoConTrim_TipoCaseInsensitive() {
        // El validador hace trim del código y el tipo se normaliza a mayúsculas por Locale.ROOT
        ret = s.registrarBicicleta("  ABC123  ", "mountain");
        assertEquals(Retorno.Resultado.OK, ret.getResultado());
    }

    @Test //ok
    public void ok_LimitesPermitidos_CerosYZetas() {
        ret = s.registrarBicicleta("000000", "ELECTRICA");
        assertEquals(Retorno.Resultado.OK, ret.getResultado());

        ret = s.registrarBicicleta("ZZZZZZ", "URBANA");
        assertEquals(Retorno.Resultado.OK, ret.getResultado());
    }

    // =============== ERROR_1 (parámetro null o vacío) ===============

    @Test //ok
    public void error1_CodigoNull() {
        ret = s.registrarBicicleta(null, "URBANA");
        assertEquals(Retorno.Resultado.ERROR_1, ret.getResultado());
    }

    @Test //ok
    public void error1_TipoNull() {
        ret = s.registrarBicicleta("ABC123", null);
        assertEquals(Retorno.Resultado.ERROR_1, ret.getResultado());
    }

    @Test //ok
    public void error1_CodigoVacioOBlancos() {
        ret = s.registrarBicicleta("", "URBANA");
        assertEquals(Retorno.Resultado.ERROR_1, ret.getResultado());

        ret = s.registrarBicicleta("   ", "URBANA");
        assertEquals(Retorno.Resultado.ERROR_1, ret.getResultado());
    }

    @Test //ok
    public void error1_TipoVacioOBlancos() {
        ret = s.registrarBicicleta("ABC123", "");
        assertEquals(Retorno.Resultado.ERROR_1, ret.getResultado());

        ret = s.registrarBicicleta("ABC123", "   ");
        assertEquals(Retorno.Resultado.ERROR_1, ret.getResultado());
    }

    // =============== ERROR_2 (formato incorrecto de código) ===============
    // Tu validador: ^[A-Z0-9]{6}$  -> SOLO 6 alfanuméricos ASCII en MAYÚSCULA.

    @Test //ok
    public void error2_LargoIncorrecto_5y7() {
        ret = s.registrarBicicleta("AB123", "URBANA");    // 5
        assertEquals(Retorno.Resultado.ERROR_2, ret.getResultado());

        ret = s.registrarBicicleta("AB12345", "URBANA");  // 7
        assertEquals(Retorno.Resultado.ERROR_2, ret.getResultado());
    }

    @Test //OK
    public void error2_MinusculasNoPermitidas() {
        // Como NO conviertes a mayúsculas antes de validar, debe fallar
        ret = s.registrarBicicleta("ab123c", "URBANA");
        assertEquals(Retorno.Resultado.ERROR_2, ret.getResultado());
    }

    @Test //OK
    public void error2_SimbolosOGuionesOEspacioInterno() {
        ret = s.registrarBicicleta("AB_123", "URBANA");
        assertEquals(Retorno.Resultado.ERROR_2, ret.getResultado());

        ret = s.registrarBicicleta("AB-123", "URBANA");
        assertEquals(Retorno.Resultado.ERROR_2, ret.getResultado());

        ret = s.registrarBicicleta("AB 123", "URBANA");
        assertEquals(Retorno.Resultado.ERROR_2, ret.getResultado());
    }

    @Test //OK
    public void error2_UnicodeNoASCII() {
        // Dígitos árabes (Unicode)
        ret = s.registrarBicicleta("AB١٢٣٤", "URBANA");
        assertEquals(Retorno.Resultado.ERROR_2, ret.getResultado());

        // “Fullwidth”
        ret = s.registrarBicicleta("ＡＢ１２３", "URBANA");
        assertEquals(Retorno.Resultado.ERROR_2, ret.getResultado());

        // Letra con tilde
        ret = s.registrarBicicleta("ÁB1234", "URBANA");
        assertEquals(Retorno.Resultado.ERROR_2, ret.getResultado());
    }

    // =============== ERROR_3 (tipo no permitido) ===============

    @Test //ok
    public void error3_TipoDesconocido() {
        ret = s.registrarBicicleta("ABC123", "HÍBRIDA");
        assertEquals(Retorno.Resultado.ERROR_3, ret.getResultado());

        ret = s.registrarBicicleta("ABC124", "E-BIKE");
        assertEquals(Retorno.Resultado.ERROR_3, ret.getResultado());

        ret = s.registrarBicicleta("ABC125", "URBANA!"); // símbolo
        assertEquals(Retorno.Resultado.ERROR_3, ret.getResultado());
    }

    // =============== ERROR_4 (duplicado por código) ===============
    @Test //ok
    public void error4_DuplicadoMismoCodigo() {
        ret = s.registrarBicicleta("ABC123", "URBANA");
        assertEquals(Retorno.Resultado.OK, ret.getResultado());

        ret = s.registrarBicicleta("ABC123", "MOUNTAIN");
        assertEquals(Retorno.Resultado.ERROR_4, ret.getResultado()); 
    }
}

