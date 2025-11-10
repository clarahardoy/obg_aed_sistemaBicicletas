/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaAutogestion;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
/**
 *
 * @author clarahardoy
 */
public class Test20_020EliminarEstacion {
    private Retorno retorno;
    private final IObligatorio s = new Sistema();
    
    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }
    @Test
    public void eliminarEstacionOk_EstacionVacia() {
        s.registrarEstacion("E1", "Centro", 10);

        retorno = s.eliminarEstacion("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.eliminarEstacion("E1");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void eliminarEstacionOk_SinBicisNiColas() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarEstacion("E2", "Pocitos", 5);

        retorno = s.eliminarEstacion("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // E2 tiene q seguir existiendo
        retorno = s.eliminarEstacion("E2");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    public void eliminarEstacionOk_DespuesDeVaciarla() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarUsuario("11111111", "Ana");
        s.registrarBicicleta("A00001", "URBANA");
        s.asignarBicicletaAEstacion("A00001", "E1");

        // alquilar la bici  y E1 queda vacía
        s.alquilarBicicleta("11111111", "E1");

        // como ahora E1 queda vacía se puede borrar
        retorno = s.eliminarEstacion("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    public void eliminarEstacionOk_ConTrim() {
        s.registrarEstacion("E1", "Centro", 10);

        retorno = s.eliminarEstacion("  E1  ");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    public void eliminarEstacionOk_VariasEstacionesIndependientes() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarEstacion("E2", "Pocitos", 5);
        s.registrarEstacion("E3", "Cordón", 8);

        // borrar E2
        retorno = s.eliminarEstacion("E2");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // E1 y E3 siguen existiendo
        s.registrarBicicleta("A00001", "URBANA");
        retorno = s.asignarBicicletaAEstacion("A00001", "E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.asignarBicicletaAEstacion("A00001", "E3");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    // ERROR_1: parametros null o vacíos

    @Test
    public void eliminarEstacionError01_NombreNull() {
        retorno = s.eliminarEstacion(null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void eliminarEstacionError01_NombreVacio() {
        retorno = s.eliminarEstacion("");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void eliminarEstacionError01_NombreBlancos() {
        retorno = s.eliminarEstacion("   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    // ERROR_2: estacion no existe

    @Test
    public void eliminarEstacionError02_EstacionInexistente() {
        retorno = s.eliminarEstacion("NOEXISTE");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void eliminarEstacionError02_YaEliminada() {
        s.registrarEstacion("E1", "Centro", 10);
        s.eliminarEstacion("E1");

        retorno = s.eliminarEstacion("E1");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    // ERROR_3: tiene bicis ancladas o colas pendientes

    @Test
    public void eliminarEstacionError03_TieneBicisAncladas() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarBicicleta("A00001", "URBANA");
        s.asignarBicicletaAEstacion("A00001", "E1");

        retorno = s.eliminarEstacion("E1");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }


    @Test
    public void eliminarEstacionError03_TieneColaEsperaAlquiler() {
        s.registrarEstacion("E1", "Centro", 1);
        s.registrarUsuario("11111111", "U1");

        // usuario intenta alquilar pero no hay bicis entonces queda en cola
        s.alquilarBicicleta("11111111", "E1");

        retorno = s.eliminarEstacion("E1");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

    @Test
    public void eliminarEstacionError03_TieneColaEsperaAnclaje() {
        s.registrarEstacion("E1", "Centro", 1);
        s.registrarEstacion("E2", "Pocitos", 5);
        s.registrarUsuario("11111111", "U1");
        s.registrarUsuario("22222222", "U2");
        s.registrarBicicleta("A00001", "URBANA");
        s.registrarBicicleta("A00002", "URBANA");

        // llenar E1
        s.asignarBicicletaAEstacion("A00001", "E1");

        // alquilar desde E2
        s.asignarBicicletaAEstacion("A00002", "E2");
        s.alquilarBicicleta("11111111", "E2");
        s.alquilarBicicleta("22222222", "E2");

        // intentar devolver en E1 que está llena entonces va a cola de anclaje
        s.devolverBicicleta("11111111", "E1");

        // E1 deberia tener cola de anclaje pendiente
        retorno = s.eliminarEstacion("E1");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

    @Test
    public void eliminarEstacionError03_TieneBicisYColas() {
        s.registrarEstacion("E1", "Centro", 2);
        s.registrarUsuario("11111111", "U1");
        s.registrarUsuario("22222222", "U2");
        s.registrarBicicleta("A00001", "URBANA");
        s.registrarBicicleta("A00002", "URBANA");

        // 1 bici anclada
        s.asignarBicicletaAEstacion("A00001", "E1");

        // 1 usuario alquila
        s.alquilarBicicleta("11111111", "E1");

        // otro usuario queda en espera
        s.alquilarBicicleta("22222222", "E1");

        // tiene cola de espera
        retorno = s.eliminarEstacion("E1");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
}