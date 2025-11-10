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
public class Test18_018RankingPorTipoDeUso {
    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    // OK

    @Test
    public void rankingTiposPorUsoOk_SinAlquileres() {
        retorno = s.rankingTiposPorUso();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }

    @Test
    public void rankingTiposPorUsoOk_UnSoloTipo() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarUsuario("11111111", "Ana");
        s.registrarBicicleta("A00001", "URBANA");
        s.asignarBicicletaAEstacion("A00001", "E1");
        s.alquilarBicicleta("11111111", "E1");

        retorno = s.rankingTiposPorUso();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("URBANA#1", retorno.getValorString());
    }

    @Test
    public void rankingTiposPorUsoOk_TodosTiposDistintaCantidad() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarUsuario("11111111", "U1");
        s.registrarUsuario("22222222", "U2");
        s.registrarUsuario("33333333", "U3");
        s.registrarUsuario("44444444", "U4");
        s.registrarUsuario("55555555", "U5");
        s.registrarUsuario("66666666", "U6");

        // 3 URBANA
        s.registrarBicicleta("U00001", "URBANA");
        s.registrarBicicleta("U00002", "URBANA");
        s.registrarBicicleta("U00003", "URBANA");
        s.asignarBicicletaAEstacion("U00001", "E1");
        s.asignarBicicletaAEstacion("U00002", "E1");
        s.asignarBicicletaAEstacion("U00003", "E1");
        s.alquilarBicicleta("11111111", "E1");
        s.alquilarBicicleta("22222222", "E1");
        s.alquilarBicicleta("33333333", "E1");

        // 2 MOUNTAIN
        s.registrarBicicleta("M00001", "MOUNTAIN");
        s.registrarBicicleta("M00002", "MOUNTAIN");
        s.asignarBicicletaAEstacion("M00001", "E1");
        s.asignarBicicletaAEstacion("M00002", "E1");
        s.alquilarBicicleta("44444444", "E1");
        s.alquilarBicicleta("55555555", "E1");

        // 1 ELECTRICA
        s.registrarBicicleta("E00001", "ELECTRICA");
        s.asignarBicicletaAEstacion("E00001", "E1");
        s.alquilarBicicleta("66666666", "E1");

        retorno = s.rankingTiposPorUso();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        // orden descendente: por cantidad
        assertEquals("URBANA#3|MOUNTAIN#2|ELECTRICA#1", retorno.getValorString());
    }

    @Test
    public void rankingTiposPorUsoOk_TodosConMismaCantidad_OrdenAlfabetico() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarUsuario("11111111", "U1");
        s.registrarUsuario("22222222", "U2");
        s.registrarUsuario("33333333", "U3");

        // 1 de cada tipo
        s.registrarBicicleta("U00001", "URBANA");
        s.registrarBicicleta("M00001", "MOUNTAIN");
        s.registrarBicicleta("E00001", "ELECTRICA");
        
        s.asignarBicicletaAEstacion("U00001", "E1");
        s.asignarBicicletaAEstacion("M00001", "E1");
        s.asignarBicicletaAEstacion("E00001", "E1");
        
        s.alquilarBicicleta("11111111", "E1");
        s.alquilarBicicleta("22222222", "E1");
        s.alquilarBicicleta("33333333", "E1");

        retorno = s.rankingTiposPorUso();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        // orden alfabético
        assertEquals("ELECTRICA#1|MOUNTAIN#1|URBANA#1", retorno.getValorString());
    }

    @Test
    public void rankingTiposPorUsoOk_EmpateParcial_OrdenaMixto() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarUsuario("11111111", "U1");
        s.registrarUsuario("22222222", "U2");
        s.registrarUsuario("33333333", "U3");
        s.registrarUsuario("44444444", "U4");
        s.registrarUsuario("55555555", "U5");

        // 3 URBANA
        for (int i = 1; i <= 3; i++) {
            s.registrarBicicleta("U0000" + i, "URBANA");
            s.asignarBicicletaAEstacion("U0000" + i, "E1");
        }
        s.alquilarBicicleta("11111111", "E1");
        s.alquilarBicicleta("22222222", "E1");
        s.alquilarBicicleta("33333333", "E1");

        // 1 MOUNTAIN
        s.registrarBicicleta("M00001", "MOUNTAIN");
        s.asignarBicicletaAEstacion("M00001", "E1");
        s.alquilarBicicleta("44444444", "E1");

        // 1 ELECTRICA
        s.registrarBicicleta("E00001", "ELECTRICA");
        s.asignarBicicletaAEstacion("E00001", "E1");
        s.alquilarBicicleta("55555555", "E1");

        retorno = s.rankingTiposPorUso();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        // 1. URBANA 
        // ELECTRICA y MOUNTAIN empatan entonces es por orden alfabetico
        assertEquals("URBANA#3|ELECTRICA#1|MOUNTAIN#1", retorno.getValorString());
    }

    @Test
    public void rankingTiposPorUsoOk_SoloDosTipos() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarUsuario("11111111", "U1");
        s.registrarUsuario("22222222", "U2");
        s.registrarUsuario("33333333", "U3");

        // 2 MOUNTAIN
        s.registrarBicicleta("M00001", "MOUNTAIN");
        s.registrarBicicleta("M00002", "MOUNTAIN");
        s.asignarBicicletaAEstacion("M00001", "E1");
        s.asignarBicicletaAEstacion("M00002", "E1");
        s.alquilarBicicleta("11111111", "E1");
        s.alquilarBicicleta("22222222", "E1");

        // 1 ELECTRICA
        s.registrarBicicleta("E00001", "ELECTRICA");
        s.asignarBicicletaAEstacion("E00001", "E1");
        s.alquilarBicicleta("33333333", "E1");

        retorno = s.rankingTiposPorUso();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        // solo mostrar los que tienen alquileres
        assertEquals("MOUNTAIN#2|ELECTRICA#1", retorno.getValorString());
    }

    @Test
    public void rankingTiposPorUsoOk_MuchosAlquileresUnTipo() {
        s.registrarEstacion("E1", "Centro", 10);
        
        // 10 usuarios
        for (int i = 1; i <= 10; i++) {
            String ci = String.format("%08d", i); // 00000001, 00000002...
            s.registrarUsuario(ci, "User" + i);
        }

        // 10 bicis URBANA
        for (int i = 1; i <= 10; i++) {
            String codigo = "U000" + String.format("%02d", i);
            s.registrarBicicleta(codigo, "URBANA");
            s.asignarBicicletaAEstacion(codigo, "E1");
        }

        // todos alquilan
        for (int i = 1; i <= 10; i++) {
            String ci = String.format("%08d", i);
            s.alquilarBicicleta(ci, "E1");
        }

        retorno = s.rankingTiposPorUso();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("URBANA#10", retorno.getValorString());
    }


    @Test
    public void rankingTiposPorUsoOk_MismaBiciMultiplesAlquileres() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarUsuario("11111111", "U1");
        s.registrarUsuario("22222222", "U2");
        s.registrarBicicleta("U00001", "URBANA");
        s.asignarBicicletaAEstacion("U00001", "E1");
        
        // Usuario 1 alquila
        s.alquilarBicicleta("11111111", "E1");
        s.devolverBicicleta("11111111", "E1");
        
        // Usuario 2 alquila la misma bici
        s.alquilarBicicleta("22222222", "E1");

        // Deben contar 2 alquileres
        retorno = s.rankingTiposPorUso();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("URBANA#2", retorno.getValorString());
    }

    @Test
    public void rankingTiposPorUsoOk_DeshacerRetirosMantieneCuenta() {
        s.registrarEstacion("E1", "Centro", 10);
        s.registrarUsuario("11111111", "U1");
        s.registrarUsuario("22222222", "U2");
        
        s.registrarBicicleta("U00001", "URBANA");
        s.registrarBicicleta("U00002", "URBANA");
        s.asignarBicicletaAEstacion("U00001", "E1");
        s.asignarBicicletaAEstacion("U00002", "E1");
        
        s.alquilarBicicleta("11111111", "E1");
        s.alquilarBicicleta("22222222", "E1");

        // deshacer 1 retiro
        s.deshacerUltimosRetiros(1);

        // tiene que contar solo 1 alquiler (el que no se deshizo)
        retorno = s.rankingTiposPorUso();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("URBANA#1", retorno.getValorString());
    }

    @Test
    public void rankingTiposPorUsoOk_EmpateDosYUnoSuelto() {
        s.registrarEstacion("E1", "Centro", 10);
        
        for (int i = 1; i <= 5; i++) {
            s.registrarUsuario(String.format("%08d", i), "User" + i);
        }

        // 2 MOUNTAIN
        s.registrarBicicleta("M00001", "MOUNTAIN");
        s.registrarBicicleta("M00002", "MOUNTAIN");
        s.asignarBicicletaAEstacion("M00001", "E1");
        s.asignarBicicletaAEstacion("M00002", "E1");
        s.alquilarBicicleta("00000001", "E1");
        s.alquilarBicicleta("00000002", "E1");

        // 2 URBANA
        s.registrarBicicleta("U00001", "URBANA");
        s.registrarBicicleta("U00002", "URBANA");
        s.asignarBicicletaAEstacion("U00001", "E1");
        s.asignarBicicletaAEstacion("U00002", "E1");
        s.alquilarBicicleta("00000003", "E1");
        s.alquilarBicicleta("00000004", "E1");

        // 1 ELECTRICA
        s.registrarBicicleta("E00001", "ELECTRICA");
        s.asignarBicicletaAEstacion("E00001", "E1");
        s.alquilarBicicleta("00000005", "E1");

        retorno = s.rankingTiposPorUso();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        // empatan MOUNTAIN y URBANA (entonces: orden alfabético), dps ELECTRICA
        assertEquals("MOUNTAIN#2|URBANA#2|ELECTRICA#1", retorno.getValorString());
    }
}