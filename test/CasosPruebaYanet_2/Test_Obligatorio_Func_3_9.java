/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CasosPruebaYanet_2;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import sistemaAutogestion.IObligatorio;
import sistemaAutogestion.Retorno;
import sistemaAutogestion.Sistema;

/**
 *
 * @author yanete
 */
public class Test_Obligatorio_Func_3_9 {
    private IObligatorio s;

    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion();
    }

    @Test
       public void error1_NombreNullOVacio() {
           Retorno r;

           r = s.usuariosEnEspera(null);
           assertEquals(Retorno.Resultado.ERROR_1, r.getResultado());

           r = s.usuariosEnEspera("");
           assertEquals(Retorno.Resultado.ERROR_1, r.getResultado());

           r = s.usuariosEnEspera("   ");
           assertEquals(Retorno.Resultado.ERROR_1, r.getResultado());
       }

       @Test
       public void error2_EstacionInexistente() {
           Retorno r = s.usuariosEnEspera("NoExiste");
           assertEquals(Retorno.Resultado.ERROR_2, r.getResultado());
       }

       @Test
       public void ok_EstacionSinCola_RetornaVacio() {
           assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E1", "Centro", 3).getResultado());

           Retorno r = s.usuariosEnEspera("E1");
           assertEquals(Retorno.Resultado.OK, r.getResultado());
           assertEquals("", r.getValorString());
       }

       @Test
       public void ok_OrdenFIFO_y_TrimNombre() {
           // Estación sin bicis -> todos los alquileres irán a la cola de espera
           assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E1", "Centro", 5).getResultado());

           String c1 = String.format("%08d", 10000000 + 1);
           String c2 = String.format("%08d", 10000000 + 2);
           String c3 = String.format("%08d", 10000000 + 3);

           assertEquals(Retorno.Resultado.OK, s.registrarUsuario(c1, "Ana").getResultado());
           assertEquals(Retorno.Resultado.OK, s.registrarUsuario(c2, "Beto").getResultado());
           assertEquals(Retorno.Resultado.OK, s.registrarUsuario(c3, "Caro").getResultado());

           // Todos piden bici en E1 sin stock -> quedan encolados en ese orden
           assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta(c1, "E1").getResultado());
           assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta(c2, "E1").getResultado());
           assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta(c3, "E1").getResultado());

           // Llamamos con espacios para verificar normalización
           Retorno r = s.usuariosEnEspera("  E1  ");
           assertEquals(Retorno.Resultado.OK, r.getResultado());
           assertEquals(c1 + "|" + c2 + "|" + c3, r.getValorString());
       }

       @Test
       public void ok_ColaMixta_SeListaSoloQuienesEsperan() {
           // Preparamos escenario: dos usuarios en cola
           assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E1", "Centro", 2).getResultado());

           String c1 = String.format("%08d", 10000000 + 21);
           String c2 = String.format("%08d", 10000000 + 22);
           String c3 = String.format("%08d", 10000000 + 23);

           assertEquals(Retorno.Resultado.OK, s.registrarUsuario(c1, "U1").getResultado());
           assertEquals(Retorno.Resultado.OK, s.registrarUsuario(c2, "U2").getResultado());
           assertEquals(Retorno.Resultado.OK, s.registrarUsuario(c3, "U3").getResultado());

           // Encolamos 2 usuarios (no hay bicis)
           assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta(c1, "E1").getResultado());
           assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta(c2, "E1").getResultado());

           // Listado debe contener sólo los que esperan
           Retorno r = s.usuariosEnEspera("E1");
           assertEquals(Retorno.Resultado.OK, r.getResultado());
           assertEquals(c1 + "|" + c2, r.getValorString());

           // Aun si hay un tercero que no pidió, no aparece
           Retorno rAgain = s.usuariosEnEspera("E1");
           assertEquals(c1 + "|" + c2, rAgain.getValorString());
       }
    }