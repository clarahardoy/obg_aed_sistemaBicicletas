
package CasosPruebaYanet_2;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import sistemaAutogestion.IObligatorio;
import sistemaAutogestion.Retorno;
import sistemaAutogestion.Sistema;

public class Test_Obligatorio2_Func_3_6 {
    
    private IObligatorio s;

    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion();
    }


    @Test
    public void error1_nIgual1() {
        Retorno r = s.estacionesConDisponibilidad(1);
        assertEquals(Retorno.Resultado.ERROR_1, r.getResultado());
    }

    @Test
    public void error1_nCeroONegativo() {
        Retorno r0 = s.estacionesConDisponibilidad(0);
        assertEquals(Retorno.Resultado.ERROR_1, r0.getResultado());
        Retorno rNeg = s.estacionesConDisponibilidad(-5);
        assertEquals(Retorno.Resultado.ERROR_1, rNeg.getResultado());
    }

    // ================== Casos OK (vacíos/borde) ==================

    @Test
    public void ok_sinEstaciones_retornaCero() {
        Retorno r = s.estacionesConDisponibilidad(2);
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals(0, r.getValorEntero());
    }

    @Test
    public void ok_nadieSuperaUmbral() {
        // n = 2; estaciones con 0, 1 y 2 disponibles (ninguna > 2)
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E1", "Centro", 10).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E2", "Centro", 10).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E3", "Centro", 10).getResultado());

        // E1: 0 disponibles (no anclamos)
        // E2: 1 disponible
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("A00001", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("A00001", "E2").getResultado());

        // E3: 2 disponibles
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("B00001", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("B00001", "E3").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("B00002", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("B00002", "E3").getResultado());

        Retorno r = s.estacionesConDisponibilidad(2);
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals(0, r.getValorEntero());
    }

    @Test
    public void ok_umbralExactoNoCuenta() {
        // n = 3; una estación con exactamente 3 disponibles NO cuenta
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E1", "Centro", 10).getResultado());

        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("A00001", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("A00001", "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("A00002", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("A00002", "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("A00003", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("A00003", "E1").getResultado());

        Retorno r = s.estacionesConDisponibilidad(3);
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals(0, r.getValorEntero());
    }

    // ================== Casos OK (mixtos y conteos) ==================

    @Test
    public void ok_variasSuperanUmbral() {
        // n = 2; E1=3 (>2) , E2=5 (>2), E3=2 (=2, no), E4=0 (no)
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E1", "Centro", 10).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E2", "Centro", 10).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E3", "Centro", 10).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E4", "Centro", 10).getResultado());

        // E1: 3 disponibles
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("A00001", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("A00001", "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("A00002", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("A00002", "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("A00003", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("A00003", "E1").getResultado());

        // E2: 5 disponibles
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("B00001", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("B00001", "E2").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("B00002", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("B00002", "E2").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("B00003", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("B00003", "E2").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("B00004", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("B00004", "E2").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("B00005", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("B00005", "E2").getResultado());

        // E3: 2 disponibles
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("C00001", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("C00001", "E3").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("C00002", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("C00002", "E3").getResultado());

        // E4: 0 disponibles (no se ancla nada)

        Retorno r = s.estacionesConDisponibilidad(2);
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals(2, r.getValorEntero());
    }

    @Test
    public void ok_ignoraNoDisponibles_NoAncladas() {
        // n = 2; E1 tendrá 3 registradas pero 1 pasa a MANTENIMIENTO (queda 2 disponibles -> no cuenta)
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E1", "Centro", 10).getResultado());

        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("A00001", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("A00001", "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("A00002", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("A00002", "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("A00003", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("A00003", "E1").getResultado());

        // Pasa una a mantenimiento → la quita de la estación
        assertEquals(Retorno.Resultado.OK, s.marcarEnMantenimiento("A00001", "rueda").getResultado());

        Retorno r = s.estacionesConDisponibilidad(2);
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals(0, r.getValorEntero());
    }

    @Test
    public void ok_escenarioMixtoVariasEstaciones() {
        // n = 3
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E1", "Centro", 10).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E2", "Pocitos", 10).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E3", "Cordón", 10).getResultado());

        // E1: 4 disponibles (cuenta)
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("A00001", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("A00001", "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("A00002", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("A00002", "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("A00003", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("A00003", "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("A00004", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("A00004", "E1").getResultado());

        // E2: 3 disponibles (=3, no cuenta)
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("B00001", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("B00001", "E2").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("B00002", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("B00002", "E2").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("B00003", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("B00003", "E2").getResultado());

        // E3: 7 disponibles, pero pasamos 2 a mantenimiento → quedan 5 disponibles (>3, cuenta)
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("C00001", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("C00001", "E3").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("C00002", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("C00002", "E3").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("C00003", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("C00003", "E3").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("C00004", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("C00004", "E3").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("C00005", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("C00005", "E3").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("C00006", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("C00006", "E3").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("C00007", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("C00007", "E3").getResultado());

        assertEquals(Retorno.Resultado.OK, s.marcarEnMantenimiento("C00001", "mantenimiento").getResultado());
        assertEquals(Retorno.Resultado.OK, s.marcarEnMantenimiento("C00002", "mantenimiento").getResultado());

        Retorno r = s.estacionesConDisponibilidad(3);
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals(2, r.getValorEntero());
    }

    @Test
    public void ok_capacidadesGrandes() {
        // n = 15; E1=20, E2=16, E3=15, E4=0  -> cuentan E1 y E2
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E1", "Centro", 50).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E2", "Centro", 50).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E3", "Centro", 50).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E4", "Centro", 50).getResultado());

        // E1: 20
        for (int i = 1; i <= 20; i++) {
            String code = String.format("A%05d", i);
            assertEquals(Retorno.Resultado.OK, s.registrarBicicleta(code, "URBANA").getResultado());
            assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion(code, "E1").getResultado());
        }
        // E2: 16
        for (int i = 1; i <= 16; i++) {
            String code = String.format("B%05d", i);
            assertEquals(Retorno.Resultado.OK, s.registrarBicicleta(code, "URBANA").getResultado());
            assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion(code, "E2").getResultado());
        }
        // E3: 15
        for (int i = 1; i <= 15; i++) {
            String code = String.format("C%05d", i);
            assertEquals(Retorno.Resultado.OK, s.registrarBicicleta(code, "URBANA").getResultado());
            assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion(code, "E3").getResultado());
        }
        // E4: 0 (sin anclar)

        Retorno r = s.estacionesConDisponibilidad(15);
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals(2, r.getValorEntero());
    }
}
