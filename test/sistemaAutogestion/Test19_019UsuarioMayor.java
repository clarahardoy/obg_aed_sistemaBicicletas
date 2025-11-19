
package sistemaAutogestion;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class Test19_019UsuarioMayor {
    

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }


    @Test
    public void usuarioMayorOk_SinUsuarios() {
        retorno = s.usuarioMayor();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }

    @Test
    public void usuarioMayorOk_SinAlquileres() {
        s.registrarUsuario("11111111", "Ana");
        s.registrarUsuario("22222222", "Pedro");

        retorno = s.usuarioMayor();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }

    @Test
    public void usuarioMayorOk_UnSoloUsuarioUnAlquiler() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarUsuario("11111111", "Ana");
        s.registrarBicicleta("A00001", "URBANA");
        s.asignarBicicletaAEstacion("A00001", "E1");
        s.alquilarBicicleta("11111111", "E1");

        retorno = s.usuarioMayor();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("11111111", retorno.getValorString());
    }

    @Test
    public void usuarioMayorOk_VariosUsuariosDistintaCantidad() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarUsuario("11111111", "U1");
        s.registrarUsuario("22222222", "U2");
        s.registrarUsuario("33333333", "U3");

        // 1 alquiler
        s.registrarBicicleta("A00001", "URBANA");
        s.asignarBicicletaAEstacion("A00001", "E1");
        s.alquilarBicicleta("11111111", "E1");

        // 3 alquileres (ganador)
        s.registrarBicicleta("A00002", "URBANA");
        s.asignarBicicletaAEstacion("A00002", "E1");
        s.alquilarBicicleta("22222222", "E1");
        s.devolverBicicleta("22222222", "E1");
        
        s.alquilarBicicleta("22222222", "E1");
        s.devolverBicicleta("22222222", "E1");
        
        s.alquilarBicicleta("22222222", "E1");

        // 2 alquileres
        s.registrarBicicleta("A00003", "URBANA");
        s.asignarBicicletaAEstacion("A00003", "E1");
        s.alquilarBicicleta("33333333", "E1");
        s.devolverBicicleta("33333333", "E1");
        
        s.alquilarBicicleta("33333333", "E1");

        retorno = s.usuarioMayor();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("22222222", retorno.getValorString()); // gana 3 alquilers
    }

    @Test
    public void usuarioMayorOk_Empate_CedulaMenor() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarUsuario("55555555", "UserA");
        s.registrarUsuario("22222222", "UserB");
        s.registrarUsuario("88888888", "UserC");

        // todos con 2 alquileres
        for (int i = 1; i <= 6; i++) {
            s.registrarBicicleta("A0000" + i, "URBANA");
            s.asignarBicicletaAEstacion("A0000" + i, "E1");
        }

        s.alquilarBicicleta("55555555", "E1");
        s.devolverBicicleta("55555555", "E1");
        s.alquilarBicicleta("55555555", "E1");
        s.devolverBicicleta("55555555", "E1");

        // cedula mas chica
        s.alquilarBicicleta("22222222", "E1");
        s.devolverBicicleta("22222222", "E1");
        s.alquilarBicicleta("22222222", "E1");
        s.devolverBicicleta("22222222", "E1");

        s.alquilarBicicleta("88888888", "E1");
        s.devolverBicicleta("88888888", "E1");
        s.alquilarBicicleta("88888888", "E1");

        retorno = s.usuarioMayor();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("22222222", retorno.getValorString());
    }

    @Test
    public void usuarioMayorOk_TodosConUnAlquiler_CedulaMenor() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarUsuario("99999999", "U1");
        s.registrarUsuario("11111111", "U2");
        s.registrarUsuario("55555555", "U3");

        // todos con 1 alquiler
        s.registrarBicicleta("A00001", "URBANA");
        s.registrarBicicleta("A00002", "URBANA");
        s.registrarBicicleta("A00003", "URBANA");
        s.asignarBicicletaAEstacion("A00001", "E1");
        s.asignarBicicletaAEstacion("A00002", "E1");
        s.asignarBicicletaAEstacion("A00003", "E1");

        s.alquilarBicicleta("99999999", "E1");
        s.alquilarBicicleta("11111111", "E1");
        s.alquilarBicicleta("55555555", "E1");

        retorno = s.usuarioMayor();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("11111111", retorno.getValorString()); 
    }

    @Test
    public void usuarioMayorOk_MuchosAlquileresUnUsuario() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarUsuario("12345678", "Ana");

        // 10 bicis para alquilar múltiples veces
        for (int i = 1; i <= 10; i++) {
            String codigo = "A000" + String.format("%02d", i);
            s.registrarBicicleta(codigo, "URBANA");
            s.asignarBicicletaAEstacion(codigo, "E1");
        }

        // 10 alquileres
        for (int i = 0; i < 5; i++) {
            s.alquilarBicicleta("12345678", "E1");
            s.devolverBicicleta("12345678", "E1");
        }

        retorno = s.usuarioMayor();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("12345678", retorno.getValorString());
    }

    @Test
    public void usuarioMayorOk_AlgunosUsuariosSinAlquileres() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarUsuario("11111111", "U1"); // sin alquileres
        s.registrarUsuario("22222222", "U2"); // con alquileres
        s.registrarUsuario("33333333", "U3"); // sin alquileres

        s.registrarBicicleta("A00001", "URBANA");
        s.asignarBicicletaAEstacion("A00001", "E1");
        s.alquilarBicicleta("22222222", "E1");

        retorno = s.usuarioMayor();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("22222222", retorno.getValorString());
    }

    @Test
    public void usuarioMayorOk_DeshacerRetiroReduceCuenta() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarUsuario("11111111", "U1");
        s.registrarUsuario("22222222", "U2");

        s.registrarBicicleta("A00001", "URBANA");
        s.registrarBicicleta("A00002", "URBANA");
        s.asignarBicicletaAEstacion("A00001", "E1");
        s.asignarBicicletaAEstacion("A00002", "E1");

        s.alquilarBicicleta("11111111", "E1");
        s.devolverBicicleta("11111111", "E1");
        s.alquilarBicicleta("11111111", "E1");

        s.alquilarBicicleta("22222222", "E1");

        // deshacer 1 retiro (el último de U2)
        s.deshacerUltimosRetiros(1);

        retorno = s.usuarioMayor();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("11111111", retorno.getValorString());
    }

    @Test
    public void usuarioMayorOk_EmpateTres_PrimeraCedulaAlfabeticamente() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarUsuario("50000000", "UserA");
        s.registrarUsuario("30000000", "UserB");
        s.registrarUsuario("40000000", "UserC");

        // todos con 1 alquiler
        for (int i = 1; i <= 3; i++) {
            s.registrarBicicleta("A0000" + i, "URBANA");
            s.asignarBicicletaAEstacion("A0000" + i, "E1");
        }

        s.alquilarBicicleta("50000000", "E1");
        s.alquilarBicicleta("30000000", "E1");
        s.alquilarBicicleta("40000000", "E1");

        retorno = s.usuarioMayor();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("30000000", retorno.getValorString()); // 30 < 40 < 50
    }

    @Test
    public void usuarioMayorOk_CedulasConCeros() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarUsuario("00000001", "U1");
        s.registrarUsuario("00000002", "U2");

        // ambos con 1 alquiler
        s.registrarBicicleta("A00001", "URBANA");
        s.registrarBicicleta("A00002", "URBANA");
        s.asignarBicicletaAEstacion("A00001", "E1");
        s.asignarBicicletaAEstacion("A00002", "E1");

        s.alquilarBicicleta("00000001", "E1");
        s.alquilarBicicleta("00000002", "E1");

        retorno = s.usuarioMayor();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("00000001", retorno.getValorString());
    }

    @Test
    public void usuarioMayorOk_UsuarioEnCola_NoCuenta() {
        s.registrarEstacion("E1", "Centro", 1);
        s.registrarUsuario("11111111", "U1");
        s.registrarUsuario("22222222", "U2");

        s.registrarBicicleta("A00001", "URBANA");
        s.asignarBicicletaAEstacion("A00001", "E1");

        // U1 alquila
        s.alquilarBicicleta("11111111", "E1");

        // U2 intenta alquilar (queda en cola de espera)
        s.alquilarBicicleta("22222222", "E1");

        // solo U1 tiene alquiler real
        retorno = s.usuarioMayor();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("11111111", retorno.getValorString());
    }
}