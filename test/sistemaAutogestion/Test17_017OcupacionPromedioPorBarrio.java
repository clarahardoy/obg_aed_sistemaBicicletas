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
public class Test17_017OcupacionPromedioPorBarrio {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    // OK +

    @Test
    public void ocupacionPromedioXBarrioOk_SinEstaciones() {
        retorno = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }

    @Test
    public void ocupacionPromedioXBarrioOk_UnBarrioUnaEstacion() {
        s.registrarEstacion("E1", "Centro", 10);
        
        // 5 bicis ancladas de 10 espacios = 50%
        for (int i = 1; i <= 5; i++) {
            s.registrarBicicleta("A0000" + i, "URBANA");
            s.asignarBicicletaAEstacion("A0000" + i, "E1");
        }

        retorno = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Centro#50", retorno.getValorString());
    }

    @Test
    public void ocupacionPromedioXBarrioOk_UnBarrioVariasEstaciones() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarEstacion("E2", "Centro", 20);
        
        // E1: 5 bicis de 10
        for (int i = 1; i <= 5; i++) {
            s.registrarBicicleta("A0000" + i, "URBANA");
            s.asignarBicicletaAEstacion("A0000" + i, "E1");
        }
        
        // E2: 10 bicis de 20
        for (int i = 1; i <= 10; i++) {
            s.registrarBicicleta("B000" + String.format("%02d", i), "MOUNTAIN");
            s.asignarBicicletaAEstacion("B000" + String.format("%02d", i), "E2");
        }

        // tot centro: 15 bicis / 30 posibles = 50%
        retorno = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Centro#50", retorno.getValorString());
    }

    @Test
    public void ocupacionPromedioXBarrioOk_VariosBarriosOrdenAlfabetico() {
        s.registrarEstacion("E1", "Pocitos", 10);
        s.registrarEstacion("E2", "Centro", 20);
        s.registrarEstacion("E3", "Aguada", 10);
        
        // Pocitos: 5/10: 50%
        for (int i = 1; i <= 5; i++) {
            s.registrarBicicleta("P0000" + i, "URBANA");
            s.asignarBicicletaAEstacion("P0000" + i, "E1");
        }
        
        // Centro: 10/20: 50%
        for (int i = 1; i <= 10; i++) {
            s.registrarBicicleta("C000" + String.format("%02d", i), "MOUNTAIN");
            s.asignarBicicletaAEstacion("C000" + String.format("%02d", i), "E2");
        }
        
        // Aguada: 3/10: 30%
        for (int i = 1; i <= 3; i++) {
            s.registrarBicicleta("A0000" + i, "ELECTRICA");
            s.asignarBicicletaAEstacion("A0000" + i, "E3");
        }

        retorno = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        // orden alfabetico
        assertEquals("Aguada#30|Centro#50|Pocitos#50", retorno.getValorString());
    }

    @Test
    public void ocupacionPromedioXBarrioOk_RedondeoCorrect() {
        s.registrarEstacion("E1", "Centro", 3);
        
        // 1 bici de 3 = redondea a 33%
        s.registrarBicicleta("A00001", "URBANA");
        s.asignarBicicletaAEstacion("A00001", "E1");

        retorno = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Centro#33", retorno.getValorString());
    }

    @Test
    public void ocupacionPromedioXBarrioOk_RedondeoHaciaArriba() {
        s.registrarEstacion("E1", "Pocitos", 3);
        
        // 2 de 3 = redondea a 67%
        s.registrarBicicleta("A00001", "URBANA");
        s.registrarBicicleta("A00002", "URBANA");
        s.asignarBicicletaAEstacion("A00001", "E1");
        s.asignarBicicletaAEstacion("A00002", "E1");

        retorno = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Pocitos#67", retorno.getValorString());
    }

    @Test
    public void ocupacionPromedioXBarrioOk_EstacionVacia() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarEstacion("E2", "Pocitos", 5);
          
        retorno = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Centro#0|Pocitos#0", retorno.getValorString());
    }

    @Test
    public void ocupacionPromedioXBarrioOk_EstacionLlena() {
        s.registrarEstacion("E1", "Centro", 5);
        
        // 5/5 = 100%
        for (int i = 1; i <= 5; i++) {
            s.registrarBicicleta("A0000" + i, "URBANA");
            s.asignarBicicletaAEstacion("A0000" + i, "E1");
        }

        retorno = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Centro#100", retorno.getValorString());
    }

    @Test
    public void ocupacionPromedioXBarrioOk_PromedioConVariasEstaciones() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarEstacion("E2", "Centro", 20);
        s.registrarEstacion("E3", "Centro", 10);
        
        // E1: 10/10 = llena
        for (int i = 1; i <= 10; i++) {
            s.registrarBicicleta("A000" + String.format("%02d", i), "URBANA");
            s.asignarBicicletaAEstacion("A000" + String.format("%02d", i), "E1");
        }
        
        // E2: 10/20 = media
        for (int i = 1; i <= 10; i++) {
            s.registrarBicicleta("B000" + String.format("%02d", i), "MOUNTAIN");
            s.asignarBicicletaAEstacion("B000" + String.format("%02d", i), "E2");
        }
        
        // E3: 0/10 = vacia
        
        // tot: (10+10+0) / (10+20+10) = 20/40 = 50%
        retorno = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Centro#50", retorno.getValorString());
    }

    @Test
    public void ocupacionPromedioXBarrioOk_BicisAlquiladasNoCuentan() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarUsuario("11111111", "Ana");
        
        // 5 bicis asignadas
        for (int i = 1; i <= 5; i++) {
            s.registrarBicicleta("A0000" + i, "URBANA");
            s.asignarBicicletaAEstacion("A0000" + i, "E1");
        }
        
        // usuario alquila 2 (quedan 3 ancladas)
        s.alquilarBicicleta("11111111", "E1");
        s.alquilarBicicleta("11111111", "E1");

        // solo 3 bicis ancladas / 10 = 30%
        retorno = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Centro#30", retorno.getValorString());
    }

    @Test
    public void ocupacionPromedioXBarrioOk_NombresBarrioConEspacios() {
        s.registrarEstacion("E1", "  Ciudad Vieja  ", 10);
        s.registrarEstacion("E2", "Tres Cruces", 5);
        
        // Ciudad vija: 5/10 = 50%
        for (int i = 1; i <= 5; i++) {
            s.registrarBicicleta("A0000" + i, "URBANA");
            s.asignarBicicletaAEstacion("A0000" + i, "E1");
        }
        
        // Tres cruces: 2/5 = 40%
        s.registrarBicicleta("B00001", "MOUNTAIN");
        s.registrarBicicleta("B00002", "MOUNTAIN");
        s.asignarBicicletaAEstacion("B00001", "E2");
        s.asignarBicicletaAEstacion("B00002", "E2");

        retorno = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        // orden alfabeitico 
        assertTrue(retorno.getValorString().contains("Ciudad Vieja#50"));
        assertTrue(retorno.getValorString().contains("Tres Cruces#40"));
    }

    @Test
    public void ocupacionPromedioXBarrioOk_MuchosBarrios() {
        s.registrarEstacion("E1", "Aguada", 10);
        s.registrarEstacion("E2", "Buceo", 10);
        s.registrarEstacion("E3", "Centro", 10);
        s.registrarEstacion("E4", "Cordón", 10);
        s.registrarEstacion("E5", "Pocitos", 10);
        
        // aguada: 1/10 = 10%
        s.registrarBicicleta("A00001", "URBANA");
        s.asignarBicicletaAEstacion("A00001", "E1");
        
        // buceo : 2/10 = 20%
        s.registrarBicicleta("B00001", "URBANA");
        s.registrarBicicleta("B00002", "URBANA");
        s.asignarBicicletaAEstacion("B00001", "E2");
        s.asignarBicicletaAEstacion("B00002", "E2");
        
        // centro : 3/10 = 30%
        s.registrarBicicleta("C00001", "URBANA");
        s.registrarBicicleta("C00002", "URBANA");
        s.registrarBicicleta("C00003", "URBANA");
        s.asignarBicicletaAEstacion("C00001", "E3");
        s.asignarBicicletaAEstacion("C00002", "E3");
        s.asignarBicicletaAEstacion("C00003", "E3");
        
        // cordon: 4/10 = 40%
        for (int i = 1; i <= 4; i++) {
            s.registrarBicicleta("D0000" + i, "URBANA");
            s.asignarBicicletaAEstacion("D0000" + i, "E4");
        }
        
        // Pocitos: 5/10 = 50%
        for (int i = 1; i <= 5; i++) {
            s.registrarBicicleta("P0000" + i, "URBANA");
            s.asignarBicicletaAEstacion("P0000" + i, "E5");
        }

        retorno = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Aguada#10|Buceo#20|Centro#30|Cordón#40|Pocitos#50", 
                     retorno.getValorString());
    }
}
