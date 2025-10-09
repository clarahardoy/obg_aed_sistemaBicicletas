package CasoPruebaYanet;

import org.junit.Test;
import static org.junit.Assert.*;
import sistemaAutogestion.IObligatorio;
import sistemaAutogestion.Retorno;
import sistemaAutogestion.Sistema;

public class Test2_01CrearSistemaDeGestion {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Test //ok
    public void testCrearSistemaDeGestion() {
        retorno = s.crearSistemaDeGestion();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

}
