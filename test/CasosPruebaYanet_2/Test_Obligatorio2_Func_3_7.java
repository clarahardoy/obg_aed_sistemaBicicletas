/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CasosPruebaYanet_2;

import sistemaAutogestion.IObligatorio;
import sistemaAutogestion.Retorno;
import sistemaAutogestion.Sistema;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author yanete
 */
public class Test_Obligatorio2_Func_3_7 {
    private IObligatorio s;


    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion();
    }

    @Test
    public void sinEstaciones_retornaVacio() {
        Retorno r = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals("", r.getValorString());
    }

    @Test
    public void estacionesConCeroAncladas_porcentajeCero() {
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E1", "Aguada", 10).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E2", "Aguada", 5).getResultado());
        // No anclamos nada = porcentaje 0
        Retorno r = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals("Aguada#0", r.getValorString());
    }

    @Test
    public void unBarrio_calculoConRedondeo() {
        // Cap total barrio Centro = 6; ancladas = 2  2/6 = 33.33.. = 33
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E1", "Centro", 3).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E2", "Centro", 3).getResultado());

        // Anclar 1 en E1
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("A00001", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("A00001", "E1").getResultado());

        // Anclar 1 en E2
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("B00001", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("B00001", "E2").getResultado());

        Retorno r = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals("Centro#33", r.getValorString());
    }

    @Test
    public void variosBarrios_ordenAlfabetico_yRedondeo() {
        // Pocitos (cap 10, ancladas 7) = 70
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("P1", "Pocitos", 6).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("P2", "Pocitos", 4).getResultado());
        // Anclar 4 en P1
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("P00001", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("P00001", "P1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("P00002", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("P00002", "P1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("P00003", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("P00003", "P1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("P00004", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("P00004", "P1").getResultado());
        // Anclar 3 en P2
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("Q00001", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("Q00001", "P2").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("Q00002", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("Q00002", "P2").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("Q00003", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("Q00003", "P2").getResultado());

        // Aguada (cap 5, ancladas 2) = 40
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("A1", "Aguada", 2).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("A2", "Aguada", 3).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("A00001", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("A00001", "A1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("B00001", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("B00001", "A2").getResultado());

        // Cordón (cap 3, ancladas 2) = 67
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("C1", "Cordón", 3).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("C00001", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("C00001", "C1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("C00002", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("C00002", "C1").getResultado());

        Retorno r = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        // Orden alfabético por barrio: Aguada | Cordón | Pocitos
        assertEquals("Aguada#40|Cordón#67|Pocitos#70", r.getValorString());
    }

    @Test
    public void mantenimiento_noCuentaComoAnclada() {
        // Barrio Centro, cap 4; anclamos 3, luego pasamos 1 a mantenimiento.
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E1", "Centro", 4).getResultado());

        // Anclar 3
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("M00001", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("M00001", "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("M00002", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("M00002", "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("M00003", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("M00003", "E1").getResultado());

        // Pasar 1 a mantenimiento: la quita y la manda a depósito
        assertEquals(Retorno.Resultado.OK, s.marcarEnMantenimiento("M00001", "rueda").getResultado());

        // Quedan 2 ancladas / cap 4 = 50%
        Retorno r = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals("Centro#50", r.getValorString());
    }

    @Test
    public void redondeo_haciaArriba_y_haciaAbajo() {
        // Barrio A: cap 3, ancladas 2 = 66.66.. = 67
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("EA", "A", 3).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("A00011", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("A00011", "EA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("A00012", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("A00012", "EA").getResultado());

        // Barrio B: cap 3, ancladas 1 = 33.33.. = 33
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("EB", "B", 3).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("B00011", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("B00011", "EB").getResultado());

        Retorno r = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals("A#67|B#33", r.getValorString()); // orden alfabético por barrio
    }
}