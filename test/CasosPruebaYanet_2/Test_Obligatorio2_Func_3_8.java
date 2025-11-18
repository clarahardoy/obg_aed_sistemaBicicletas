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

public class Test_Obligatorio2_Func_3_8 {
    private IObligatorio s;

    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion();
    }

  @Test
    public void sinAlquileres_todosCero_enOrdenAlfabetico() {
        // Sin historial => 0-0-0. Empate => alfabético ELECTRICA|MOUNTAIN|URBANA
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E1", "Centro", 10).getResultado());

        Retorno r = s.rankingTiposPorUso();
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals("ELECTRICA#0|MOUNTAIN#0|URBANA#0", r.getValorString());
    }

    @Test
    public void unSoloAlquiler_deUnTipo() {
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E1", "Centro", 10).getResultado());

        // Usuario y bici URBANA
        String ci1 = "10000001";
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(ci1, "U1").getResultado());

        String codU1 = "U00001";
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta(codU1, "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion(codU1, "E1").getResultado());

        // Alquilar
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta(ci1, "E1").getResultado());

        // Ranking: URBANA primero por cantidad; resto empatado alfabético
        Retorno r = s.rankingTiposPorUso();
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals("URBANA#1|ELECTRICA#0|MOUNTAIN#0", r.getValorString());
    }

    @Test
    public void variosTipos_conEmpates_seOrdenaDescYLuegoAlfabetico() {
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E1", "Centro", 50).getResultado());

        // URBANA: 3 alquileres
        String ciU1 = "10000011"; String codU1 = "U00001";
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(ciU1, "Uu1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta(codU1, "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion(codU1, "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta(ciU1, "E1").getResultado());

        String ciU2 = "10000012"; String codU2 = "U00002";
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(ciU2, "Uu2").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta(codU2, "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion(codU2, "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta(ciU2, "E1").getResultado());

        String ciU3 = "10000013"; String codU3 = "U00003";
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(ciU3, "Uu3").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta(codU3, "URBANA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion(codU3, "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta(ciU3, "E1").getResultado());

        // MOUNTAIN: 3 alquileres (empata con URBANA; gana MOUNTAIN alfabéticamente)
        String ciM1 = "10000021"; String codM1 = "M00001";
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(ciM1, "Mm1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta(codM1, "MOUNTAIN").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion(codM1, "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta(ciM1, "E1").getResultado());

        String ciM2 = "10000022"; String codM2 = "M00002";
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(ciM2, "Mm2").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta(codM2, "MOUNTAIN").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion(codM2, "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta(ciM2, "E1").getResultado());

        String ciM3 = "10000023"; String codM3 = "M00003";
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(ciM3, "Mm3").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta(codM3, "MOUNTAIN").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion(codM3, "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta(ciM3, "E1").getResultado());

        // ELECTRICA: 1 alquiler
        String ciE1 = "10000031"; String codE1 = "E00001";
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(ciE1, "Ee1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta(codE1, "ELECTRICA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion(codE1, "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta(ciE1, "E1").getResultado());

        Retorno r = s.rankingTiposPorUso();
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals("MOUNTAIN#3|URBANA#3|ELECTRICA#1", r.getValorString());
    }

    @Test
    public void unTipoConConteoCero_QuedaAlFinalEmpatandoAlfabetico() {
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("E1", "Centro", 20).getResultado());

        // ELECTRICA: 2 alquileres. Los otros tipos quedan en 0 (orden alfabético al final)
        String ciE1 = "10000101"; String codE1 = "E00011";
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(ciE1, "Ae1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta(codE1, "ELECTRICA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion(codE1, "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta(ciE1, "E1").getResultado());

        String ciE2 = "10000102"; String codE2 = "E00012";
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(ciE2, "Ae2").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta(codE2, "ELECTRICA").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion(codE2, "E1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.alquilarBicicleta(ciE2, "E1").getResultado());

        Retorno r = s.rankingTiposPorUso();
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals("ELECTRICA#2|MOUNTAIN#0|URBANA#0", r.getValorString());
    }
}
