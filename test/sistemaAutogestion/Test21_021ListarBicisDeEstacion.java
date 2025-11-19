
package sistemaAutogestion;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class Test21_021ListarBicisDeEstacion {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    // OK

    @Test
    public void listarBicicletasDeEstacionOk_EstacionVacia() {
        s.registrarEstacion("E1", "Centro", 10);

        retorno = s.listarBicicletasDeEstacion("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }

    @Test
    public void listarBicicletasDeEstacionOk_UnaBici() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarBicicleta("ABC123", "URBANA");
        s.asignarBicicletaAEstacion("ABC123", "E1");

        retorno = s.listarBicicletasDeEstacion("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("ABC123", retorno.getValorString());
    }

    @Test
    public void listarBicicletasDeEstacionOk_OrdenAlfabetico() {
        s.registrarEstacion("E1", "Centro", 10);
        
        // Registrar en orden aleatorio
        s.registrarBicicleta("AER345", "URBANA");
        s.registrarBicicleta("UTR112", "MOUNTAIN");
        s.registrarBicicleta("UYT123", "ELECTRICA");
        
        s.asignarBicicletaAEstacion("AER345", "E1");
        s.asignarBicicletaAEstacion("UTR112", "E1");
        s.asignarBicicletaAEstacion("UYT123", "E1");

        retorno = s.listarBicicletasDeEstacion("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("AER345|UTR112|UYT123", retorno.getValorString());
    }

    @Test
    public void listarBicicletasDeEstacionOk_CodigosNumericos() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarBicicleta("000001", "URBANA");
        s.registrarBicicleta("999999", "MOUNTAIN");
        s.registrarBicicleta("500000", "ELECTRICA");
        
        s.asignarBicicletaAEstacion("999999", "E1");
        s.asignarBicicletaAEstacion("000001", "E1");
        s.asignarBicicletaAEstacion("500000", "E1");

        retorno = s.listarBicicletasDeEstacion("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        // orden de numero
        assertEquals("000001|500000|999999", retorno.getValorString());
    }

    @Test
    public void listarBicicletasDeEstacionOk_ConTrimNombre() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarBicicleta("ABC123", "URBANA");
        s.asignarBicicletaAEstacion("ABC123", "E1");

        retorno = s.listarBicicletasDeEstacion("  E1  ");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("ABC123", retorno.getValorString());
    }

    @Test
    public void listarBicicletasDeEstacionOk_MuchasBicis() {
        s.registrarEstacion("E1", "Centro", 20);
        
        for (int i = 10; i >= 1; i--) {
            String codigo = String.format("BIC%03d", i); // codigo de 6 caracteres
            s.registrarBicicleta(codigo, "URBANA");
            s.asignarBicicletaAEstacion(codigo, "E1");
        }

        retorno = s.listarBicicletasDeEstacion("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        // tiene estar ordenado ascendente
        assertEquals("BIC001|BIC002|BIC003|BIC004|BIC005|BIC006|BIC007|BIC008|BIC009|BIC010", 
                     retorno.getValorString());
    }

    @Test
    public void listarBicicletasDeEstacionOk_DespuesDeAlquilar() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarUsuario("11111111", "Ana");
        s.registrarBicicleta("A00001", "URBANA");
        s.registrarBicicleta("A00002", "MOUNTAIN");
        
        s.asignarBicicletaAEstacion("A00001", "E1");
        s.asignarBicicletaAEstacion("A00002", "E1");

        s.alquilarBicicleta("11111111", "E1");

        // solo tiene que quedar 1 bici en E1
        retorno = s.listarBicicletasDeEstacion("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        // puede ser A00001 o A00002 dependiendo de cual se alquilo
        String resultado = retorno.getValorString();
        assertTrue(resultado.equals("A00001") || resultado.equals("A00002"));
    }

    @Test
    public void listarBicicletasDeEstacionOk_DespuesDeDevolver() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarEstacion("E2", "Pocitos", 10);
        s.registrarUsuario("11111111", "Ana");
        s.registrarBicicleta("A00001", "URBANA");
        
        s.asignarBicicletaAEstacion("A00001", "E1");
        s.alquilarBicicleta("11111111", "E1");
        
        s.devolverBicicleta("11111111", "E2");

        // E1 tiene q estar vacia 
        retorno = s.listarBicicletasDeEstacion("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());

        // E2 tiene que tener la bici
        retorno = s.listarBicicletasDeEstacion("E2");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("A00001", retorno.getValorString());
    }

   
    

    public void listarBicicletasDeEstacionOk_DespuesDeMantenimiento() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarBicicleta("A00001", "URBANA");
        s.registrarBicicleta("A00002", "MOUNTAIN");

        s.asignarBicicletaAEstacion("A00001", "E1");
        s.asignarBicicletaAEstacion("A00002", "E1");

        // marcar en mantenimiento (va a depostiro)
        s.marcarEnMantenimiento("A00001", "rueda");

        // E1 solo deberia tener A00002
        retorno = s.listarBicicletasDeEstacion("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("A00002", retorno.getValorString());
    }


   @Test
    public void listarBicicletasDeEstacionOk_TodasEnMantenimiento_VanADeposito() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarBicicleta("A00001", "URBANA");
        s.registrarBicicleta("A00002", "MOUNTAIN");

        s.asignarBicicletaAEstacion("A00001", "E1");
        s.asignarBicicletaAEstacion("A00002", "E1");

        // marcar en mantenimiento para que vayan al deposito
        s.marcarEnMantenimiento("A00001", "rueda");
        s.marcarEnMantenimiento("A00002", "frenos");

        // E1 deberia estar vacia (bicis fueron al deposito)
        retorno = s.listarBicicletasDeEstacion("E1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString()); 

        Retorno deposito = s.listarBicisEnDeposito();
        assertTrue(deposito.getValorString().contains("A00001"));
        assertTrue(deposito.getValorString().contains("A00002"));
        assertTrue(deposito.getValorString().toUpperCase().contains("MANTENIMIENTO"));
    }
}
