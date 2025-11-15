
package sistemaAutogestion;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test15_015DevolverBicicleta {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    // ===================== OK =====================

    @Test
    public void devolverBicicletaOk_ConEspacioDisponible() {
        // Contexto: crear estación con espacio, usuario con bici alquilada
        s.registrarEstacion("E1", "Centro", 5);
        s.registrarUsuario("12345678", "Ana");
        s.registrarBicicleta("ABC001", "URBANA");
        s.asignarBicicletaAEstacion("ABC001", "E1");
        s.alquilarBicicleta("12345678", "E1");

        // devolver en la misma estación
        retorno = s.devolverBicicleta("12345678", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // chequear que la bici está disponible en la estación
        Retorno listar = s.listarBicicletasDeEstacion("E1");
        assertTrue(listar.getValorString().contains("ABC001"));
    }

    @Test
    public void devolverBicicletaOk_EnEstacionDiferente() {
        // usuario alquila en E1 y devuelve en E2 (est diferentes)
        s.registrarEstacion("E1", "Centro", 2);
        s.registrarEstacion("E2", "Pocitos", 3);
        s.registrarUsuario("12345678", "Pedro");
        s.registrarBicicleta("ABC001", "URBANA");
        s.asignarBicicletaAEstacion("ABC001", "E1");
        s.alquilarBicicleta("12345678", "E1");

        retorno = s.devolverBicicleta("12345678", "E2");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // la bici deberia estar en E2 y no en E1
        Retorno listarE2 = s.listarBicicletasDeEstacion("E2");
        assertTrue(listarE2.getValorString().contains("ABC001"));

        Retorno listarE1 = s.listarBicicletasDeEstacion("E1");
        assertFalse(listarE1.getValorString().contains("ABC001"));
    }

    @Test
    public void devolverBicicletaOk_ReasignaAUsuarioEnEspera() {
        // hay una estacion con 1 lugar, usuario devuelve y hay otro esperando
        s.registrarEstacion("E1", "Centro", 2);
        s.registrarUsuario("11111111", "User1");
        s.registrarUsuario("22222222", "User2");
        s.registrarBicicleta("ABC001", "URBANA");
        s.asignarBicicletaAEstacion("ABC001", "E1");

        // usuario 1 alquila
        s.alquilarBicicleta("11111111", "E1");

        // usuareio 2 quiere alquilar pero no hay bicis entonces queda en espera
        s.alquilarBicicleta("22222222", "E1");

        // usuario 1 devuelve -> debeeria asignarse automáticamente a user 2
        retorno = s.devolverBicicleta("11111111", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // estación NO debe tener bicis disponibles (pasó directo al user 2)
        Retorno listar = s.listarBicicletasDeEstacion("E1");
        assertFalse(listar.getValorString().contains("ABC001"));

        // user 2 ya no deberia estar en cola de espera
        Retorno espera = s.usuariosEnEspera("E1");
        assertFalse(espera.getValorString().contains("22222222"));
    }

    @Test
    public void devolverBicicletaOk_EstacionLlena_QuedaEnColaAnclaje() {
        // estacion con capacidad 1, ya llena
        s.registrarEstacion("E1", "Centro", 1);
        s.registrarUsuario("11111111", "User1");
        s.registrarUsuario("22222222", "User2");
        s.registrarBicicleta("ABC001", "URBANA");
        s.registrarBicicleta("ABC002", "MOUNTAIN");
        
        // ABC001 se asigna a E1 = E1 se llena
        s.asignarBicicletaAEstacion("ABC001", "E1");
    
        // user 1 alquila ABC001 = E1 se vacia
        s.alquilarBicicleta("11111111", "E1");

        // ABC002 se asigna a E1 = E1 se llena
        s.asignarBicicletaAEstacion("ABC002", "E1");

        // user 2 alquila ABC002 = E1 se vacia
        s.alquilarBicicleta("22222222", "E1");

        // user 1 devuelve ABC001 en E1 = E1 se llena
        assertEquals(Retorno.Resultado.OK, s.devolverBicicleta("11111111", "E1").getResultado());

        // E1 está llena (tiene ABC001)
        // user 2 intenta devolver ABC002 en E1 que está llena entonces tiene que ir ir a cola de anclaje
        retorno = s.devolverBicicleta("22222222", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // ABC002 NO tendria que estar anclada (esta en cola de anclaje)
        Retorno listar = s.listarBicicletasDeEstacion("E1");
        assertTrue("E1 debe tener ABC001", listar.getValorString().contains("ABC001"));
        assertFalse("E1 NO debe tener ABC002 (está en cola)", listar.getValorString().contains("ABC002"));
    }

    @Test
    public void devolverBicicletaOk_ConTrim() {
        s.registrarEstacion("E1", "Centro", 2);
        s.registrarUsuario("12345678", "Ana");
        s.registrarBicicleta("ABC001", "URBANA");
        s.asignarBicicletaAEstacion("ABC001", "E1");
        s.alquilarBicicleta("12345678", "E1");

        // con espacios
        retorno = s.devolverBicicleta("  12345678  ", "  E1  ");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    //ERROR_1: parametros null o vacios
    @Test
    public void devolverBicicletaError01_CedulaNull() {
        retorno = s.devolverBicicleta(null, "E1");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void devolverBicicletaError01_EstacionNull() {
        retorno = s.devolverBicicleta("12345678", null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void devolverBicicletaError01_CedulaVaciaOBlancos() {
        retorno = s.devolverBicicleta("", "E1");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.devolverBicicleta("   ", "E1");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void devolverBicicletaError01_EstacionVaciaOBlancos() {
        retorno = s.devolverBicicleta("12345678", "");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.devolverBicicleta("12345678", "   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    // ERROR_2: usuario inexistente o no tiene bici alquilada

    @Test
    public void devolverBicicletaError02_UsuarioInexistente() {
        s.registrarEstacion("E1", "Centro", 2);

        retorno = s.devolverBicicleta("99999999", "E1");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void devolverBicicletaError02_UsuarioSinBiciAlquilada() {
        s.registrarEstacion("E1", "Centro", 2);
        s.registrarUsuario("12345678", "Ana");

        // el usuario existe pero no tiene bici alquilada
        retorno = s.devolverBicicleta("12345678", "E1");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void devolverBicicletaError02_UsuarioYaDevolvio() {
        s.registrarEstacion("E1", "Centro", 2);
        s.registrarUsuario("12345678", "Ana");
        s.registrarBicicleta("ABC001", "URBANA");
        s.asignarBicicletaAEstacion("ABC001", "E1");
        s.alquilarBicicleta("12345678", "E1");

        // primer devolucio esta ok
        retorno = s.devolverBicicleta("12345678", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // intenta denuevo pero da error xq ya no tiene bici
        retorno = s.devolverBicicleta("12345678", "E1");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    // ERROR_3:  estacion de destino no existe

    @Test
    public void devolverBicicletaError03_EstacionInexistente() {
        s.registrarUsuario("12345678", "Ana");
        s.registrarEstacion("E1", "Centro", 2);
        s.registrarBicicleta("ABC001", "URBANA");
        s.asignarBicicletaAEstacion("ABC001", "E1");
        s.alquilarBicicleta("12345678", "E1");

        retorno = s.devolverBicicleta("12345678", "NOEXISTE");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

    // casos borde

    @Test
    public void devolverBicicletaOk_VariosUsuariosEnEspera_RespetaOrdenFIFO() {
        s.registrarEstacion("E1", "Centro", 1);
        s.registrarUsuario("11111111", "U1");
        s.registrarUsuario("22222222", "U2");
        s.registrarUsuario("33333333", "U3");
        s.registrarBicicleta("ABC001", "URBANA");
        s.asignarBicicletaAEstacion("ABC001", "E1");

        // U1 alquila
        s.alquilarBicicleta("11111111", "E1");

        // U2 y U3 quedan en espera
        s.alquilarBicicleta("22222222", "E1");
        s.alquilarBicicleta("33333333", "E1");

        // U1 devuelve: entonces tiene que ir a U2 xq es el primero en la cola
        retorno = s.devolverBicicleta("11111111", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // U3 tiene que seguir en espera, U2 no
        Retorno espera = s.usuariosEnEspera("E1");
        assertTrue(espera.getValorString().contains("33333333"));
        assertFalse(espera.getValorString().contains("22222222"));
    }

    @Test
    public void devolverBicicletaOk_MultiplesDevoluciones() {
        s.registrarEstacion("E1", "Centro", 3);
        s.registrarUsuario("11111111", "U1");
        s.registrarUsuario("22222222", "U2");
        s.registrarBicicleta("ABC001", "URBANA");
        s.registrarBicicleta("ABC002", "MOUNTAIN");
        
        s.asignarBicicletaAEstacion("ABC001", "E1");
        s.asignarBicicletaAEstacion("ABC002", "E1");
        
        s.alquilarBicicleta("11111111", "E1");
        s.alquilarBicicleta("22222222", "E1");

        // los dos devuelven
        retorno = s.devolverBicicleta("11111111", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.devolverBicicleta("22222222", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // las dos bicis deberian estar en E1
        Retorno listar = s.listarBicicletasDeEstacion("E1");
        assertTrue(listar.getValorString().contains("ABC001"));
        assertTrue(listar.getValorString().contains("ABC002"));
    }
}
