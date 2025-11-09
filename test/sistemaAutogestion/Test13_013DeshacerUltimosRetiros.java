package sistemaAutogestion;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class Test13_013DeshacerUltimosRetiros {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }


    @Test
    public void deshacerUltimosRetirosError01_parametroInvalido() {
        retorno = s.deshacerUltimosRetiros(0);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.deshacerUltimosRetiros(-3);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }


    @Test
    public void deshacerUltimosRetirosOk_sinHistorial() {
        retorno = s.deshacerUltimosRetiros(1);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }

    @Test
    public void deshacerUltimosRetirosOk_unAlquiler_vuelveAOrigen() {
        // E1 con capacidad 1, U1, A1 en E1
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E1", "Centro", 1).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario("11111111", "U1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("A1B1C1", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("A1B1C1", "E1").getResultado());

        // U1 alquila
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta("11111111", "E1").getResultado());

        // Deshacer 1 
        retorno = s.deshacerUltimosRetiros(1);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("A1B1C1#11111111#E1", retorno.getValorString());

        // Verificar que A1B1C1 quedó anclada en E1
        retorno = s.listarBicicletasDeEstacion("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("A1B1C1", retorno.getValorString());
    }


    @Test
    public void deshacerUltimosRetirosOk_varios_LIFO() {
        // E1 cap 2, U1 y U2, A1 y A2 en E1
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E1", "Centro", 2).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario("11111111", "U1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario("22222222", "U2").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("A1A1A1", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("A2A2A2", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("A1A1A1", "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("A2A2A2", "E1").getResultado());

        // U1 alquila primero A1 
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta("11111111", "E1").getResultado());
        // U2 alquila después 
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta("22222222", "E1").getResultado());

        // Deshacer 2 primero el último retiro (U2), luego el anterior (U1)
        retorno = s.deshacerUltimosRetiros(2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        // Puede que A1/A2 se asignen en distinto orden según cómo quedaron en la lista de la estación
        // Pero el orden del string debe ser LIFO, último retiro, retiro anterior
        // verificamos ambas posibilidades válidas
        String vs = retorno.getValorString();
        boolean opcion1 = vs.equals("A2A2A2#22222222#E1|A1A1A1#11111111#E1");
        boolean opcion2 = vs.equals("A1A1A1#22222222#E1|A2A2A2#11111111#E1");
        assertEquals(true, opcion1 || opcion2);

        // Ambas bicis deben volver a estar ancladas en E1
        retorno = s.listarBicicletasDeEstacion("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("A1A1A1|A2A2A2", retorno.getValorString());
    }

//sin lugar en origense encola en espera de anclaje
    @Test
    public void deshacerUltimosRetirosOk_sinLugar_vaAColaAnclaje() {
        // E1 cap 1, U1, A1 en E1
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E1", "Centro", 1).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario("11111111", "U1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("A1C1E1", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("A1C1E1", "E1").getResultado());

        // U1 alquila A1
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta("11111111", "E1").getResultado());

        // Ocupar el único anclaje con otra bici
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("B1D1F1", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("B1D1F1", "E1").getResultado());

        // Deshacer 1A1C1E1 no puede anclar, va a cola de anclaje
        retorno = s.deshacerUltimosRetiros(1);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("A1C1E1#11111111#E1", retorno.getValorString());

        // En la estación sólo está B1D1F1 anclada (A1C1E1 quedó esperando anclaje)
        retorno = s.listarBicicletasDeEstacion("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("B1D1F1", retorno.getValorString());
    }

    // n mayor que el historial, deshace lo que haya y retorna sólo esos
    @Test
    public void deshacerUltimosRetirosOk_nMayorQueHistorial() {
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E1", "Centro", 1).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario("11111111", "U1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("ZZZ111", "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("ZZZ111", "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta("11111111", "E1").getResultado());

        retorno = s.deshacerUltimosRetiros(3); // hay sólo 1 retiro
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("ZZZ111#11111111#E1", retorno.getValorString());
    }
}
