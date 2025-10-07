
package sistemaAutogestion;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class Test10_010InformacionMapa {
    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }
    
    @Test
    public void informacionMapaOkNull() {
        retorno = s.informaciónMapa(null);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("0#ambas|no existe", retorno.getValorString());
    }
    
    @Test
    public void informacionMapaOkSinFilas() {
        String[][] mapa = new String[0][0];
        retorno  = s.informaciónMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("0#ambas|no existe", retorno.getValorString());
    }
    
    @Test
    public void informacionMapaOkFilasSinColumnas() {
        String[][] mapa = new String[][] { {}, {}, {} };
        retorno  = s.informaciónMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("0#ambas|no existe", retorno.getValorString());
    }
    
    @Test
    public void informacionMapaOkAmbasNoExisteAsc() {
        String[][] mapa = new String[][] {
            { "A",  null, " "  },
            { null, "B",  "C"  },
            { null, null, "D"  }
        };
        retorno  = s.informaciónMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("2#ambas|no existe", retorno.getValorString());
    }
    
    @Test
    public void informacionMapaOkAmbasSiExisteAsc() {
        String[][] mapa = new String[][] {
            { null, null, "A" },
            { null, "B",  "C" },
            { null, null, null }
        };
        retorno  = s.informaciónMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("2#ambas|existe", retorno.getValorString());
    }
    
    @Test
    public void informacionMapaOkMaxColumnaSinAsc() {
        String[][] mapa = new String[][] {
            { "A",  null, "C",  null },
            { null, "B2", "C2", null },
            { null, null, "C3", null }
        };
        retorno  = s.informaciónMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("3#columna|no existe", retorno.getValorString());
    }
    
    @Test
    public void informacionMapaOkIrregularYEspacios() {
        String[][] mapa = new String[][] {
            null,
            { "X", "   " },
            { "A", null, "B", "" }
        };

        retorno  = s.informaciónMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("2#ambas|no existe", retorno.getValorString());
    }
    
    @Test
    public void informacionMapaOkMenosDeTresColumnas() {
        String[][] mapa = new String[][] {
            { "A", null },
            { null, "B" }
        };

        retorno  = s.informaciónMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("1#ambas|no existe", retorno.getValorString());
    }
    
}
