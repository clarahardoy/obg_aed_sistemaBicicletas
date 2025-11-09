
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
        retorno = s.informacionMapa(null);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("0#ambas|no existe", retorno.getValorString());
    }
    
    @Test
    public void informacionMapaOkSinFilas() {
        String[][] mapa = new String[0][0];
        retorno  = s.informacionMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("0#ambas|no existe", retorno.getValorString());
    }
    
    @Test
    public void informacionMapaOkFilasSinColumnas() {
        String[][] mapa = new String[][] { {}, {}, {} };
        retorno  = s.informacionMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("0#ambas|no existe", retorno.getValorString());
    }
    
    @Test
    public void informacionMapaOkAmbasNoExisteAsc() {
        String[][] mapa = new String[][] {
            { "E1",  null, " "  },
            { null, "E2",  "E3"  },
            { null, null, "E4"  }
        };
        retorno  = s.informacionMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("2#ambas|no existe", retorno.getValorString());
    }
    
    @Test
    public void informacionMapaOkAmbasSiExisteAsc() {
        String[][] mapa = new String[][] {
            { null, null, "E1" },
            { null, "E2",  "E4" },
            { null, null, null }
        };
        retorno  = s.informacionMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("2#ambas|existe", retorno.getValorString());
    }
    
    @Test
    public void informacionMapaOkMaxColumnaSinAsc() {
        String[][] mapa = new String[][] {
            { "E1",  null, "E2",  null },
            { null, "E5", "E4", null },
            { null, null, "E6", null }
        };
        retorno  = s.informacionMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("3#columna|no existe", retorno.getValorString());
    }
    
    @Test
    public void informacionMapaOkIrregularYEspacios() {
        String[][] mapa = new String[][] {
            null,
            { "E1", "   " },
            { "E2", null, "E3", "" }
        };

        retorno  = s.informacionMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("2#ambas|no existe", retorno.getValorString());
    }
    
    @Test
    public void informacionMapaOkMenosDeTresColumnas() {
        String[][] mapa = new String[][] {
            { "E1", null },
            { null, "E2" }
        };

        retorno  = s.informacionMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("1#ambas|no existe", retorno.getValorString());
    }
    
}
