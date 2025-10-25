
package sistemaAutogestion;


import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;



public class Test11_011AsignarBicicletaAestacion {
     private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }
    
    @Test
    public void DatosFaltantes() {

        retorno = s.asignarBicicletaAEstacion(null, "Central");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.asignarBicicletaAEstacion("AAA123", null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.asignarBicicletaAEstacion("", "Central");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.asignarBicicletaAEstacion("AAA123", "   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }
    
    @Test
    public void BiciNoExiste() {
        s.registrarEstacion("Central", "Centro", 2);

        Retorno r = s.asignarBicicletaAEstacion("ZZZ999", "Central");
        assertEquals(Retorno.Resultado.ERROR_2, r.getResultado());
    }
    
    @Test
    public void BiciNoDisponible() {
        s.registrarEstacion("Central", "Centro", 2);
        s.registrarBicicleta("AAA123", "URBANA");
        // Dejarla NO disponible
        s.marcarEnMantenimiento("AAA123", "service");

        Retorno r = s.asignarBicicletaAEstacion("AAA123", "Central");
        assertEquals(Retorno.Resultado.ERROR_2, r.getResultado());
    }
    
    @Test
    public void EstacionNoExiste() {
        s.registrarBicicleta("AAA123", "URBANA");

        Retorno r = s.asignarBicicletaAEstacion("AAA123", "Fantasia");
        assertEquals(Retorno.Resultado.ERROR_3, r.getResultado());
    }
    
    @Test
    public void EstacionLlena() {
        s.registrarEstacion("Chica", "Centro", 1);
        s.registrarBicicleta("AAA123", "URBANA");
        s.registrarBicicleta("BBB111", "MOUNTAIN");

        // Lleno la estación con la primera
        assertEquals(Retorno.Resultado.OK,
                s.asignarBicicletaAEstacion("AAA123", "Chica").getResultado());

        // Intento asignar otra a la misma (sin lugar)
        Retorno r = s.asignarBicicletaAEstacion("BBB111", "Chica");
        assertEquals(Retorno.Resultado.ERROR_4, r.getResultado());
    }
    
//    @Test
//    public void asignar_Ok_DesdeDeposito() throws Exception {
//        s.registrarEstacion("Central", "Centro", 2);
//        s.registrarBicicleta("AAA123", "URBANA");
//
//        Retorno r = s.asignarBicicletaAEstacion("AAA123", "Central");
//        assertEquals(Retorno.Resultado.OK, r.getResultado());
//
//        // Verificaciones: la bici está en ESTACION "Central"
//        Bicicleta b = getBici("AAA123");
//        assertNotNull(b);
//        assertEquals("ESTACION", b.getUbicacion());
//        assertNotNull(b.getEstacion());
//        assertEquals("Central", b.getEstacion().getNombre());
//
//        // Y la estación la tiene anclada
//        ListaSE<Bicicleta> listaCentral = getListaBicisEstacion("Central");
//        assertEquals(1, listaCentral.getCantidadElementos());
//        assertSame(b, listaCentral.obtenerElementoPorIndice(0));
//    }
    


}
