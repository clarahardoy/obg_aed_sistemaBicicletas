
package CasoPruebaYanet;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import sistemaAutogestion.IObligatorio;
import sistemaAutogestion.Retorno;
import sistemaAutogestion.Sistema;

public class Test3_04InformacionMapa {

    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() { s.crearSistemaDeGestion(); }

    @Test //OK
    public void ejemplo1() {
        String o = "o";
        String[][] mapa = {
            {o   ,o,o   ,o   ,o   ,o   ,o},
            {o   ,o,o   ,"E3",o   ,o   ,o},
            {o   ,o,o   ,o   ,o   ,o   ,o},
            {"E1",o,o   ,o   ,o   ,"E5",o},
            {o   ,o,o   ,o   ,o   ,o   ,o},
            {o   ,o,"E2",o   ,"E6",o   ,o},
            {o   ,o,o   ,"E7",o   ,o   ,o},
            {o   ,o,o   ,"E4",o   ,o   ,o},
        };
        Retorno r = s.informaciónMapa(mapa);
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals("3#columna|existe", r.getValorString());
    }

    @Test //OK
    public void ejemplo2() {
        String o = "o";
        String[][] mapa = {
            {o   ,o,o   ,o   ,o,o   ,o},
            {o   ,o,o   ,"E3",o,o   ,o},
            {o   ,o,o   ,o   ,o,o   ,o},
            {"E1",o,o   ,o   ,o,"E5",o},
            {o   ,o,o   ,o   ,o,o   ,o},
            {o   ,o,"E2",o   ,o,o   ,o},
            {o   ,o,o   ,o   ,o,o   ,o},
            {o   ,o,o   ,"E4",o,o   ,o},
        };
        Retorno r = s.informaciónMapa(mapa);
        assertEquals("2#ambas|existe", r.getValorString());
    }

    @Test //OK
    public void ejemplo3() {
        String o = "o";
        String[][] mapa = {
            {o   ,o   ,o,o   ,o   ,o   ,o},
            {o   ,o   ,o,"E3",o   ,o   ,o},
            {o   ,o   ,o,o   ,o   ,o   ,o},
            {"E1",o   ,o,o   ,o   ,"E5",o},
            {o   ,o   ,o,o   ,o   ,o   ,o},
            {o   ,o   ,o,o   ,"E6",o   ,o},
            {o   ,"E7",o,o   ,o   ,o   ,o},
            {o   ,o   ,o,"E4",o   ,o   ,o},
        };
        Retorno r = s.informaciónMapa(mapa);
        assertEquals("2#ambas|no existe", r.getValorString());
    }
    
    @Test ///OK
    public void noHayTernasConsecutivasAscendentes_0based() {
        String o = "o";
        String[][] mapa = {
            {o,  "E1",  o,  "E2"},
            {o,  "E3",  o,  "E4"},
            {o,    o,  "E5", "E6"},
        };
        Retorno r = s.informaciónMapa(mapa);
        assertEquals("3#columna|no existe", r.getValorString());
    }
    
    @Test //ok
    public void mapaNull() {
        assertEquals("0#ambas|no existe", s.informaciónMapa(null).getValorString());
    }

    @Test //ok
    public void matrizVacia_0x0() {
        assertEquals("0#ambas|no existe", s.informaciónMapa(new String[0][0]).getValorString());
    }

    @Test //ok
    public void matrizConFilaVacia() {
        assertEquals("0#ambas|no existe", s.informaciónMapa(new String[][] { {} }).getValorString());
    }

    @Test //OK
    public void sinEstaciones() {
        String o="o";
        String[][] m = {
            {o,o,o},
            {o,o,o}
        };
        assertEquals("0#ambas|no existe", s.informaciónMapa(m).getValorString());
    }

    @Test //OK
    public void soloUnaColumna_noPuedeHaberTriple() {
        String[][] m = {
            {"E1"},
            {"o"},
            {"E2"}
        }; 
        assertEquals("2#columna|no existe", s.informaciónMapa(m).getValorString());
    }

    @Test //OK
    public void dosColumnas_noPuedeHaberTriple() {
        String o="o";
        String[][] m = {
            {"E1",o   },
            {o   ,"E2"},
            {o   ,o    }
        }; 
        assertEquals("1#ambas|no existe", s.informaciónMapa(m).getValorString());
    }

    @Test //OK
    public void exactamenteTresColumnas_tripleSi() {
        String o="o";
        String[][] m = {
            {o, "E1",  o},
            {o,   o,  "E2"},
            {o,   o,  "E3"}
        }; 
        assertEquals("2#columna|existe", s.informaciónMapa(m).getValorString());
    }

    @Test //OK
    public void exactamenteTresColumnas_noTriple() {
        String o="o";
        String[][] m = {
            {"E1", "E2",  o},
            {  o ,   o , "E3"},
            {  o ,   o ,  o }
        }; 
        assertEquals("2#fila|no existe", s.informaciónMapa(m).getValorString());
    }
    
    @Test //ok
    public void maximoPorFila_unico() {
        String o="o";
        String[][] m = {
            {"E1","E2","E3"},
            {  o ,  o ,  o  }
        }; 
        assertEquals("3#fila|no existe", s.informaciónMapa(m).getValorString());
    }

    @Test //ok
    public void maximoPorColumna_unico() {
        String o="o";
        String[][] m = {
            {"E1",o},
            {"E2",o},
            {"E3",o}
        }; 
        assertEquals("3#columna|no existe", s.informaciónMapa(m).getValorString());
    }

    @Test //OK
    public void maximoEmpatado_enFilaYColumna_ambas() {
        String o="o";
        String[][] m2 = {
            {"E1","E2",  o },
            {  o ,  o , "E3"},
            {  o ,  o , "E4"}
        }; 
        assertEquals("2#ambas|no existe", s.informaciónMapa(m2).getValorString());
    }

    @Test //OK
    public void reconoceMinusculasYEspacios() {
        String[][] m = {
            {" e1 ", "o", "o"},
            {" o ",  " e2 ", "o"},
            {" o ",   " o ", " e3 "}
        }; 
        assertEquals("1#ambas|no existe", s.informaciónMapa(m).getValorString());
    }

    @Test //OK
    public void variasTernasConsecutivasAscendentes() {
        String o="o";
        String[][] m = {
            { o , "E1", "E2", "E3"},
            { o ,   o , "E4", "E5"},
            { o ,   o ,   o , "E6"},
        };
        assertEquals("3#ambas|existe", s.informaciónMapa(m).getValorString());
    }
    
}

