package CasoPruebaYanet;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import sistemaAutogestion.IObligatorio;
import sistemaAutogestion.Retorno;
import sistemaAutogestion.Sistema;

public class Test3_02ListarUsuarios {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    @Test //ok
    public void listarUsuariosVacio() {
        retorno = s.listarUsuarios();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }

    @Test //ok
    public void listarUsuariosSoloUnUsuario() {
        s.registrarUsuario("12345678", "Usuario01");
        retorno = s.listarUsuarios();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Usuario01#12345678", retorno.getValorString());
    }

    @Test //ok
    public void listarUsuariosIngresoOrdenado() {
        s.registrarUsuario("11111111", "Usuario01");
        s.registrarUsuario("31221111", "Usuario02");
        s.registrarUsuario("11331111", "Usuario03");
        retorno = s.listarUsuarios();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Usuario01#11111111|Usuario02#31221111|Usuario03#11331111", retorno.getValorString());
    }

    @Test //ok
    public void listarUsuariosIngresoDesordenado() {
        s.registrarUsuario("11111111", "Usuario01");
        s.registrarUsuario("11331111", "Usuario03");
        s.registrarUsuario("31221111", "Usuario02");
        retorno = s.listarUsuarios();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Usuario01#11111111|Usuario02#31221111|Usuario03#11331111", retorno.getValorString());
    }

}
