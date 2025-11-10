/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaAutogestion;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author clarahardoy
 */
public class Test16_016EstacionesConDisponibilidadMayor {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    // OK

    @Test
    public void estacionesConDisponibilidadOk_SinEstaciones() {
        retorno = s.estacionesConDisponibilidad(2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(0, retorno.getValorEntero());
    }

    @Test
    public void estacionesConDisponibilidadOk_NingunaCumple() {
        s.registrarEstacion("E1", "Centro", 5);
        s.registrarEstacion("E2", "Pocitos", 3);
        s.registrarBicicleta("ABC001", "URBANA");
        s.registrarBicicleta("ABC002", "MOUNTAIN");
        
        // E1: 1 bici disponible
        s.asignarBicicletaAEstacion("ABC001", "E1");
        
        // E2: 1 bici disponible
        s.asignarBicicletaAEstacion("ABC002", "E2");

        // Buscar estaciones con MÁS de 5 disponibles
        retorno = s.estacionesConDisponibilidad(5);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(0, retorno.getValorEntero());
    }

    @Test
    public void estacionesConDisponibilidadOk_UnaEstacionCumple() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarEstacion("E2", "Pocitos", 5);
        
        // E1: 5 bicis disponibles
        for (int i = 1; i <= 5; i++) {
            s.registrarBicicleta("ABC00" + i, "URBANA");
            s.asignarBicicletaAEstacion("ABC00" + i, "E1");
        }
        
        // E2: 2 bicis disponibles
        s.registrarBicicleta("XYZ001", "MOUNTAIN");
        s.registrarBicicleta("XYZ002", "ELECTRICA");
        s.asignarBicicletaAEstacion("XYZ001", "E2");
        s.asignarBicicletaAEstacion("XYZ002", "E2");

        // Buscar estaciones con MÁS de 3 disponibles
        retorno = s.estacionesConDisponibilidad(3);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(1, retorno.getValorEntero()); // Solo E1 cumple (5 > 3)
    }

    @Test
    public void estacionesConDisponibilidadOk_VariasEstacionesCumplen() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarEstacion("E2", "Pocitos", 10);
        s.registrarEstacion("E3", "Cordón", 10);
        
        // E1: 4 bicis
        for (int i = 1; i <= 4; i++) {
            String codigo = String.format("AAAA0%d", i); // AAAA01, AAAA02, AAAA03, AAAA04
            s.registrarBicicleta(codigo, "URBANA");
            s.asignarBicicletaAEstacion(codigo, "E1");
        }
    
        // E2: 5 bicis
        for (int i = 1; i <= 5; i++) {
            String codigo = String.format("BBBB0%d", i); // BBBB01, BBBB02, BBBB03, BBBB04, BBBB05
            s.registrarBicicleta(codigo, "MOUNTAIN");
            s.asignarBicicletaAEstacion(codigo, "E2");
        }
    
        
        // E3: 2 bicis
        s.registrarBicicleta("CCCC01", "ELECTRICA");
        s.registrarBicicleta("CCCC02", "URBANA");
        s.asignarBicicletaAEstacion("CCCC01", "E3");
        s.asignarBicicletaAEstacion("CCCC02", "E3");

        // solo estaciones con mas de 2 disponibles
        retorno = s.estacionesConDisponibilidad(2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(2, retorno.getValorEntero()); // E1(4) y E2(5) cumplen
    }

    @Test
    public void estacionesConDisponibilidadOk_ExactamenteIgual_NoCuenta() {
        s.registrarEstacion("E1", "Centro", 5);
        s.registrarEstacion("E2", "Pocitos", 5);
        
        // E1: 3 bicis disponibles
        for (int i = 1; i <= 3; i++) {
            s.registrarBicicleta("ABC00" + i, "URBANA");
            s.asignarBicicletaAEstacion("ABC00" + i, "E1");
        }
        
        // E2: 3 bicis disponibles
        for (int i = 1; i <= 3; i++) {
            s.registrarBicicleta("XYZ00" + i, "MOUNTAIN");
            s.asignarBicicletaAEstacion("XYZ00" + i, "E2");
        }

        // Buscar estaciones con MÁS de 3 (3 NO es mayor, debe dar 0)
        retorno = s.estacionesConDisponibilidad(3);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(0, retorno.getValorEntero());
    }

    @Test
    public void estacionesConDisponibilidadOk_BicisEnMantenimiento_NoCuentan() {
        s.registrarEstacion("E1", "Centro", 5);
        
        // 3 bicis en E1
        for (int i = 1; i <= 3; i++) {
            s.registrarBicicleta("ABC00" + i, "URBANA");
            s.asignarBicicletaAEstacion("ABC00" + i, "E1");
        }
        
        // 1 bici en mantenimiento (desde E1)
        s.marcarEnMantenimiento("ABC001", "rueda");

        // Ahora E1 tiene 2 disponibles (no 3)
        retorno = s.estacionesConDisponibilidad(2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(0, retorno.getValorEntero()); // 2 NO es mayor a 2
    }

    @Test
    public void estacionesConDisponibilidadOk_BuscarConN_Minimo() {
        s.registrarEstacion("E1", "Centro", 5);
        s.registrarBicicleta("ABC001", "URBANA");
        s.asignarBicicletaAEstacion("ABC001", "E1");

        // n = 2 (límite mínimo permitido)
        retorno = s.estacionesConDisponibilidad(2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(0, retorno.getValorEntero()); // 1 no es mayor a 2
    }

    @Test
    public void estacionesConDisponibilidadOk_TodasLasEstacionesCumplen() {
        s.registrarEstacion("E1", "Centro", 5);
        s.registrarEstacion("E2", "Pocitos", 5);
        s.registrarEstacion("E3", "Cordón", 5);
        
        // Todas con 5 bicis
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 5; j++) {
                String codigo = String.format("E%dB%03d", i, j); 
                s.registrarBicicleta(codigo, "URBANA");
                s.asignarBicicletaAEstacion(codigo, "E" + i);
            }
        }

        // Buscar estaciones con MÁS de 2
        retorno = s.estacionesConDisponibilidad(2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(3, retorno.getValorEntero());
    }

    // ERROR_1: n < 0 

    @Test
    public void estacionesConDisponibilidadError01_N_MenorQue1() {
        retorno = s.estacionesConDisponibilidad(0);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void estacionesConDisponibilidadError01_N_IgualA0() {
        retorno = s.estacionesConDisponibilidad(0);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void estacionesConDisponibilidadError01_N_Negativo() {
        retorno = s.estacionesConDisponibilidad(-5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.estacionesConDisponibilidad(-1);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    // casos borde

    @Test
    public void estacionesConDisponibilidadOk_EstacionSinBicis() {
        s.registrarEstacion("E1", "Centro", 5);
        s.registrarEstacion("E2", "Pocitos", 5);
        
        // solo E2 tiene bicis
        s.registrarBicicleta("ABC001", "URBANA");
        s.asignarBicicletaAEstacion("ABC001", "E2");

        retorno = s.estacionesConDisponibilidad(2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(0, retorno.getValorEntero()); // ninguna tiene más de 2
    }

    @Test
    public void estacionesConDisponibilidadOk_N_MuyGrande() {
        s.registrarEstacion("E1", "Centro", 5);
        s.registrarBicicleta("ABC001", "URBANA");
        s.asignarBicicletaAEstacion("ABC001", "E1");

        // buscar estaciones con MÁS de 1000 disponibles
        retorno = s.estacionesConDisponibilidad(1000);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(0, retorno.getValorEntero());
    }
}